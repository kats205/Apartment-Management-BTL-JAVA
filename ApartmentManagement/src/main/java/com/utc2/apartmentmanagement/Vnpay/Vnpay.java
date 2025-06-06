package com.utc2.apartmentmanagement.Vnpay;

import com.google.gson.JsonObject;
import com.utc2.apartmentmanagement.Payment.vnpay.VnpayRedirectUrlBuilder;
import com.utc2.apartmentmanagement.Payment.vnpay.VnpayReturnServer;
import javafx.application.Platform;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.utc2.apartmentmanagement.Utils.AlertBox.showAlert;

public class Vnpay {
    // Payment processing methods
    private static final AtomicBoolean serverStarted = new AtomicBoolean(false);
    private ExecutorService executorService;

    public JsonObject handlePayVnpay(long priceService) {
        try {
            if (!serverStarted.getAndSet(true)) {
                executorService = Executors.newSingleThreadExecutor();
                executorService.submit(() -> {
                    try {
                        VnpayReturnServer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Platform.runLater(() -> showAlert("Lỗi: ", e.getMessage()));
                    }
                });
            }

            JsonObject jb = VnpayRedirectUrlBuilder.createVnpayUrl(priceService);
            String paymentUrl = jb.get("data").getAsString();

            System.out.println("Mở trình duyệt tới: " + paymentUrl);
            Desktop.getDesktop().browse(new URI(paymentUrl));
            return jb;
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi: ", e.getMessage());
        }
        return null;
    }

}
