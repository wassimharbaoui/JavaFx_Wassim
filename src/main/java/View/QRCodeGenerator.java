package View;

import Entities.Evenement;
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
import java.util.Map;
import java.util.List;

    public class QRCodeGenerator {
        public static Image generateQRCodeImage(List<Evenement> evenements) throws WriterException, IOException {
            // Créer une chaîne contenant les informations des événements
            StringBuilder eventInfo = new StringBuilder();
            for (Evenement evenement : evenements) {
                eventInfo.append("Titre: ").append(evenement.getTitre()).append("\n");
                eventInfo.append("Date début: ").append(evenement.getDate_debut()).append("\n");
                eventInfo.append("Date fin: ").append(evenement.getDate_fin()).append("\n");
                eventInfo.append("Thème: ").append(evenement.getTheme()).append("\n");
                eventInfo.append("Localisation: ").append(evenement.getLocalisation()).append("\n");
                eventInfo.append("Description: ").append(evenement.getDescription()).append("\n\n");
            }

            // Générer le QR code à partir de la chaîne d'informations des événements
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 1); // set the margin to control the white border size

            BitMatrix bitMatrix = qrCodeWriter.encode(eventInfo.toString(), BarcodeFormat.QR_CODE, 200, 200, hints);

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();

            return SwingFXUtils.toFXImage(javax.imageio.ImageIO.read(new java.io.ByteArrayInputStream(pngData)), null);
        }
    }
