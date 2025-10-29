package uz.pdp.shopapplication.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.shopapplication.dto.ProductDto;
import uz.pdp.shopapplication.service.PdfService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class PdfServiceImpl implements PdfService {

    @Override
    public byte[] generateProductPdf(ProductDto product) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22);
            document.add(new Paragraph(product.getName(), titleFont));
            document.add(new Paragraph(" "));


            if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                try {
                    String encodedUrl = product.getImageUrl().replace(" ", "%20");
                    BufferedImage bufferedImage = ImageIO.read(new URL(encodedUrl));
                    if (bufferedImage != null) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write(bufferedImage, "png", baos);
                        Image image = Image.getInstance(baos.toByteArray());
                        image.scaleToFit(300, 300);
                        image.setAlignment(Element.ALIGN_CENTER);
                        document.add(image);
                    } else {
                        document.add(new Paragraph("[Изображение не найдено или не поддерживается]"));
                    }
                } catch (Exception e) {
                    document.add(new Paragraph("[Ошибка при загрузке изображения]"));
                }
            }

            document.add(new Paragraph(" "));


            Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 14);
            document.add(new Paragraph("Описание: " + product.getDescription(), textFont));
            document.add(new Paragraph("Цена: " + product.getPrice() + " $", textFont));
            if (product.getCategoryName() != null)
                document.add(new Paragraph("Категория: " + product.getCategoryName(), textFont));

            document.close();
            return out.toByteArray();
        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Ошибка при генерации PDF: " + e.getMessage(), e);
        }
    }
}
