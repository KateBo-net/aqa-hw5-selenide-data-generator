package ru.netology.delivery.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DataGenerator {
    static final String dateFormat = "dd.MM.yyyy";
    static List<String> cities = new ArrayList<>();

    private DataGenerator() {
    }

    public static String generateDate(int shift) {
        // логика генерации строки с датой
        return LocalDate.now().plusDays(shift).format(DateTimeFormatter.ofPattern(dateFormat));
    }

    public static void generateListCities() throws IOException {
        try (BufferedReader fileReader = new BufferedReader(new FileReader("src/test/resources/city.csv", StandardCharsets.UTF_8))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                cities.add(line);
            }
        }
    }

    public static String generateCity() throws IOException {
        // генерация города их списка
        generateListCities();
        Random random = new Random();
        return cities.get(random.nextInt(cities.size()));
    }

    public static String generateName(String locale) {
        // генерация фамилия + имя
        Faker faker = new Faker(new Locale(locale));
        return faker.name().lastName() + " " + faker.name().firstName();
    }

    public static String generatePhone(String locale) {
        // генерация номера телефона
        Faker faker = new Faker(new Locale(locale));
        return faker.phoneNumber().phoneNumber();
    }

    public static class Registration {
        private Registration() {
        }

        public static UserInfo generateUser(String locale) throws IOException {
            return new UserInfo(generateCity(), generateName(locale), generatePhone(locale));
        }
    }

    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;
    }

}