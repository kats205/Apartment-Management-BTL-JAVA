//package com.utc2.apartmentmanagement.Service;
//
////import com.google.zxing.BarcodeFormat;
////import com.google.zxing.WriterException;
////import com.google.zxing.common.BitMatrix;
////import com.google.zxing.qrcode.QRCodeWriter;
//
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.WriterException;
//import com.google.zxing.client.j2se.MatrixToImageWriter;
//import com.google.zxing.common.BitMatrix;
//import com.google.zxing.qrcode.QRCodeWriter;
//
//import java.io.IOException;
//import java.nio.file.FileSystems;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.time.LocalDate;
//
//public class QRCodeGenerate {
//    public static void generateBillHTML(int userId, int amount, String outputPath) throws IOException {
//        String template = Files.readString(Paths.get("src/main/resources/templates/billTemplate.html"));
//        String filled = template
//                .replace("{{userId}}", String.valueOf(userId))
//                .replace("{{amount}}", String.valueOf(amount))
//                .replace("{{date}}", LocalDate.now().toString());
//
//        Files.writeString(Paths.get(outputPath), filled);
//    }
//    public static void generateQRCodeImage(String text, int width, int height, String filePath)
//            throws WriterException, IOException {
//        QRCodeWriter qrCodeWriter = new QRCodeWriter();
//        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
//
//        Path path = FileSystems.getDefault().getPath(filePath);
//        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
//    }
//
//    public static void main(String[] args) {
//        String text = "https://qrcode-three-cyan.vercel.app/";
//        String filePath = "qr_code.png";
//
//        try {
//            generateQRCodeImage(text, 300, 300, filePath);
//            System.out.println("Tạo mã QR thành công: " + filePath);
//        } catch (WriterException | IOException e) {
//            System.err.println("Lỗi khi tạo QR: " + e.getMessage());
//        }
//    }
//}
