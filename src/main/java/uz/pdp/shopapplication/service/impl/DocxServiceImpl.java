package uz.pdp.shopapplication.service.impl;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import uz.pdp.shopapplication.dto.UserDto;
import uz.pdp.shopapplication.service.DocxService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class DocxServiceImpl implements DocxService {

    @Override
    public byte[] generateUserCertificate(UserDto user) {

        try {
            InputStream templateStream = new ClassPathResource("templates/Справка.docx").getInputStream();
            XWPFDocument document = new XWPFDocument(templateStream);
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            for (XWPFParagraph paragraph : document.getParagraphs()) {
                replacePlaceholdersInRuns(paragraph.getRuns(), user);
            }

            document.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при генерации DOCX: " + e.getMessage(), e);
        }
    }

    private void replacePlaceholdersInRuns(List<XWPFRun> runs, UserDto user) {
        if (runs == null) return;

        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String date = LocalDate.now().format(df);

        for (XWPFRun run : runs) {
            String text = run.getText(0);
            if (text == null) continue;

            text = text.replace("{FULL_NAME}", user.getFullName());
            text = text.replace("{PASSPORT}", user.getPassportNumber());
            text = text.replace("{BALANCE}", String.format("%.2f", user.getBalance()));
            text = text.replace("{DATE}", LocalDate.now().toString());

            run.setText(text, 0);
        }
    }
}
