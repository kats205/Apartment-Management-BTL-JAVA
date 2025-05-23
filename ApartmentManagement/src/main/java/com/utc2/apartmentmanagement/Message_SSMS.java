package com.utc2.apartmentmanagement;

import com.twilio.Twilio;
//import com.twilio.rest.assistants.v1.assistant.Message;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class Message_SSMS {
    // Find your Account Sid and Token at twilio.com/console
//    public static final String ACCOUNT_SID = "AC569c766cc55c6de568ca0ce377456aaa";
//    public static final String AUTH_TOKEN = "f0bffaf18beee214ced40d59c7fa0b5f";
//
//    public static void main(String[] args) {
//        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//        Message message = Message.creator(
//                        new com.twilio.type.PhoneNumber("+84976739505"),
//                        new com.twilio.type.PhoneNumber("+15735333692"),
//                        "Hello Trung Le")
//                .create();
//
//        System.out.println(message.getSid());
//    }
    public static final String ACCOUNT_SID = "AC569c766cc55c6de568ca0ce377456aaa";
    public static final String AUTH_TOKEN = "f0bffaf18beee214ced40d59c7fa0b5f";

    public static void sendSms(String to, String body) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(
                new PhoneNumber(to),    // Số điện thoại nhận (vd: "+84901234567")
                new PhoneNumber("+15735333692"), // Số điện thoại gửi (được Twilio cấp)
                body
        ).create();

        System.out.println("Sent message SID: " + message.getSid());
    }

    public static void main(String[] args) {
        sendSms("+84976739505", "Xin chào! Đây là tin nhắn từ hệ thống.");
    }
}
