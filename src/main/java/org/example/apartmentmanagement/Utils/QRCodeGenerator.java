package org.example.apartmentmanagement.Utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class QRCodeGenerator {

    public static void generateQRCode(String url, String filePath) throws WriterException, IOException {
        int width = 300;
        int height = 300;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        // Cấu hình mã hóa
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        // Tạo QR Code
        BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, width, height, hints);

        // Lưu QR Code vào file ảnh
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

        System.out.println("✅ QR Code đã tạo thành công: " + filePath);
    }

    public static void main(String[] args) {
        try {
            // Tạo URL chứa thông tin hóa đơn
            String billURL = "https://yourwebsite.com/bill?id=12345";
            generateQRCode(billURL, "QRCode_Bill.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
