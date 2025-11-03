package uz.pdp.shopapplication.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import uz.pdp.shopapplication.dto.UserDto;
import uz.pdp.shopapplication.service.DocxService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class DocxServiceImpl implements DocxService {


    public byte[] generateUserCertificate(UserDto user) {

        try {
            InputStream templateStream = new ClassPathResource("templates/Справка.docx").getInputStream();
            XWPFDocument document = new XWPFDocument(templateStream);
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            for (XWPFParagraph paragraph : document.getParagraphs()) {
                replacePlaceholdersInRuns(paragraph.getRuns(), user);
            }

            insertQrCode(document, "QR", "https://www.youtube.com/shorts/SnhHFzaSrnE");

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

        String issuedAtStr = user.getIssuedAt() != null
                ? user.getIssuedAt().format(df)
                : "";
        System.out.println("DEBUG user: issuedAt=" + user.getIssuedAt() + ", bankAccount=" + user.getBankAccount());

        for (XWPFRun run : runs) {
            String text = run.getText(0);
            if (text == null) continue;

            text = text.replace("{FULL_NAME}", safe(user.getFullName()));
            text = text.replace("{PASSPORT}", safe(user.getPassportNumber()));
            text = text.replace("{ISSUED_AT}", safe(issuedAtStr));
            text = text.replace("{BANK_ACCOUNT}", safe(user.getBankAccount()));
            text = text.replace("{BALANCE}", user.getBalance() != null ? String.format("%.2f", user.getBalance()) : "0.00");
            text = text.replace("{DATE}", safe(date));

            run.setText(text, 0);
        }

    }

    private String safe(String value) {
        return value != null ? value : "";
    }

    private void insertQrCode(XWPFDocument document, String placeholder, String url) {
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            for (XWPFRun run : paragraph.getRuns()) {
                String text = run.getText(0);
                if (text == null) continue;
                if (text != null && text.toUpperCase().contains(placeholder.toUpperCase())) {
                    run.setText("", 0);

                    try {
                        QRCodeWriter qrCodeWriter = new QRCodeWriter();
                        BitMatrix matrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 200, 200);
                        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(matrix);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write(qrImage, "png", baos);
                        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

                        run.addPicture(bais, XWPFDocument.PICTURE_TYPE_PNG, "qr.png", Units.toEMU(100), Units.toEMU(100));
                    } catch (IOException | WriterException |
                             org.apache.poi.openxml4j.exceptions.InvalidFormatException e) {
                        throw new RuntimeException("Ошибка вставки QR-кода: " + e.getMessage(), e);
                    }
                    return;
                }
            }
        }
    }

    public byte[] convertDocxToPdf(byte[] docxBytes) {
        try (ByteArrayInputStream in = new ByteArrayInputStream(docxBytes);
             XWPFDocument document = new XWPFDocument(in);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            PdfOptions options = PdfOptions.create();
            PdfConverter.getInstance().convert(document, out, options);

            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при конвертации DOCX в PDF: " + e.getMessage(), e);
        }
    }

    public byte[] generateUserCertificatePdf(UserDto user) {
        byte[] docxBytes = generateUserCertificate(user);
        return convertDocxToPdf(docxBytes);
    }
}
