package View;

import Entities.Ticket;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QRCodeGenerator {
    public static Image generateQRCodeImage(List<Ticket> tickets) throws WriterException, IOException {
        // Créer une chaîne contenant les informations des tickets
        StringBuilder ticketInfo = new StringBuilder();
        for (Ticket ticket : tickets) {
            ticketInfo.append("ID: ").append(ticket.getId()).append("\n");
            ticketInfo.append("User ID: ").append(ticket.getUser_id()).append("\n");
            ticketInfo.append("Event ID: ").append(ticket.getEvent_id()).append("\n");
            ticketInfo.append("Prix: ").append(ticket.getPrix()).append("\n");
            ticketInfo.append("Type de ticket: ").append(ticket.getType_ticket()).append("\n\n");
        }

        // Générer le QR code à partir de la chaîne d'informations des tickets
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1); // set the margin to control the white border size

        BitMatrix bitMatrix = qrCodeWriter.encode(ticketInfo.toString(), BarcodeFormat.QR_CODE, 200, 200, hints);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();

        return SwingFXUtils.toFXImage(javax.imageio.ImageIO.read(new java.io.ByteArrayInputStream(pngData)), null);
    }
}
