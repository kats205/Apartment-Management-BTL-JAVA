package org.example.apartmentmanagement.Utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class QRCodeScanner {

    public static String readQRCode(String filePath) throws IOException, NotFoundException {
        // Đọc ảnh QR Code
        BufferedImage bufferedImage = ImageIO.read(new File(filePath));

        // Chuyển ảnh thành binary bitmap
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        // Giải mã QR Code
        Result result = new MultiFormatReader().decode(bitmap);
        return result.getText();
    }

    public static void main(String[] args) {
        try {
            String result = readQRCode("QRCode_Bill.png");
            System.out.println("✅ Dữ liệu từ QR Code:\n" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}