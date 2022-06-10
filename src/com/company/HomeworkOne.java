package com.company;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomeworkOne {
    //Экспертный уровень
    //Задача №1
    //Создать метод маскирования персональных данных, который:
    //Телефон (до/после маскирования): 79991113344 / 7999***3344
    //Email (до/после маскирования): test@yandex.ru / tes*@******.ru, my_mail@gmail.com / my_mai*@*****.com
    //Фио (до/после маскирования): Иванов Иван Иванович / И****в Иван И.
    //
    //Входящие параметры: String text
    //Возвращаемый результат: String
    //
    //Примеры возможного текста:
    //<client>(Какие то данные)<data>79991113344;test@yandex.ru;Иванов Иван Иванович</data></client> - "<client>(Какие то данные)<data>7999***3344;tes*@******.ru;И****в Иван И.</data></client>"
    //<client>(Какие то данные)<data></data></client> - вернет тоже (никаких ошибок)
    //<client>(Какие то данные)<data>Иванов Иван Иванович;79991113344</data></client> - "<client>(Какие то данные)<data>И****в Иван И.;7999***3344</data></client>"

    //Используемые технологии: String.find, String.replaceAll, String.split, String.join, String.contains, String.substring
    //Регулярные выражения, класс StringBuilder

    public static void main(String[] args) {
        String correct = "<client>(Какие то данные)<data>79991113344;test@yandex.ru;Иванов Иван Иванович</data></client>";
        String empty = "<client>(Какие то данные)<data></data></client>";
        String differentOrder = "<client>(Какие то данные)<data>Иванов Иван Иванович;79991113344</data></client>";
        String wrong = "<client></client>";
        System.out.println(maskPersonalData(correct));
        System.out.println(maskPersonalData(empty));
        System.out.println(maskPersonalData(differentOrder));
        System.out.println(maskPersonalData(wrong));

    }

    public static String maskPersonalData(String clientInfo) {
        String clientInfoFormat = "^<client>.*<data>.*</data></client>$";
        Matcher clientInfoMatcher = getMatcherFromFormat(clientInfoFormat, clientInfo);
        if (clientInfoMatcher.matches()) {
            String clientDataFormat = "<data>.*</data>";
            Matcher clientDataMatcher = getMatcherFromFormat(clientDataFormat, clientInfo);
            clientDataMatcher.find();
            int startData = clientDataMatcher.start();
            int endData = clientDataMatcher.end();
            String clientData = clientInfo.substring(startData, endData);

            return clientInfo.substring(0, startData) + maskName(maskEmail(maskPhoneNumber(clientData))) + clientInfo.substring(endData);
        }
        return clientInfo;
    }

    private static Matcher getMatcherFromFormat(String format, String input) {
        return Pattern.compile(format).matcher(input);
    }

    private static String maskPhoneNumber(String data) {
        String phoneNumberFormat = "\\d{11}";
        Matcher phoneNumberMatcher = getMatcherFromFormat(phoneNumberFormat, data);
        if (phoneNumberMatcher.find()) {
            int start = phoneNumberMatcher.start();
            int end = phoneNumberMatcher.end();
            return data.substring(0, start + 4) + "***" + data.substring(end - 3);
        }
        return data;
    }

    private static String maskEmail(String data) {
        String emailFormat = "[A-Za-z0-9+_.-]+@[A-za-z]+.[A-za-z]{2,}";
        Matcher emailMatcher = getMatcherFromFormat(emailFormat, data);
        if (emailMatcher.find()) {
            int start = emailMatcher.start();
            int end = emailMatcher.end();
            String email = data.substring(start, end);
            int emailSignPosition = email.indexOf("@");
            int dotPosition = email.indexOf(".");
            StringBuilder newEmail = new StringBuilder(email);
            newEmail.setCharAt(emailSignPosition - 1, '*');
            for (int i = emailSignPosition + 1; i < dotPosition; i++) {
                newEmail.setCharAt(i, '*');
            }
            return data.substring(0, start) + newEmail.toString() + data.substring(end);
        }
        return data;
    }

    private static String maskName(String data) {
        String nameFormat = "[А-Я]{1}[а-я]+\\s[А-Я]{1}[а-я]+\\s[А-Я]{1}[а-я]+";
        Matcher nameMatcher = getMatcherFromFormat(nameFormat, data);
        if (nameMatcher.find()) {
            int start = nameMatcher.start();
            int end = nameMatcher.end();
            String[] name = data.substring(start, end).split(" ");
            StringBuilder newSurname = new StringBuilder(name[0]);
            for (int i = 1; i < newSurname.length()-1; i++) {
                newSurname.setCharAt(i, '*');
            }
            name[0] = newSurname.toString();
            name[2] = name[2].charAt(0) + ".";
            return data.substring(0, start) + String.join(" ", name) + data.substring(end);
        }
        return data;
    }
}
