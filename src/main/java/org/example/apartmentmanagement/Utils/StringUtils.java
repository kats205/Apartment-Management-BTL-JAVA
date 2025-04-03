package org.example.apartmentmanagement.Utils;

public class StringUtils {
    // kiểm tra rỗng của một chuỗi
    public static boolean isEmpty(String str){
        return str == null || str.trim().isEmpty();
    }
    //viết hoa chữ cái đầu cho tên
    public static String capitalizeName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "";
        }
        String[] words = name.trim().toLowerCase().split("\\s+");
        StringBuilder capitalized = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                capitalized.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }
        return capitalized.toString().trim();
    }
}
