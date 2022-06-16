package com.company;

import com.company.util.GeneratorExpertHomework;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomeworkTwo {
    // Предыстория: Мы находимся в статистическом институте. Хочется понять уровень миграции между регионами за месяц.
    // Для этого было решено произвести анализ передвижения автомобилей: на границе каждого региона стоят камеры,
    // фиксирующие въезд и выезд автомобилей. Формат автомобильного номера: (буква)(3 цифры)(2 буквы)(2-3 цифры)
    // К474СЕ178 - где 178 регион

    // Задача №1: составить топ-5 популярных регионов (куда больше всего въехали).
    // Сделать аналитическую раскладку: машины какого региона больше всего въезжали в этот топ-5.
    // Ожидаемый результат:
    // ТОП-5: 98, 178, 01, 22, 33
    // 98 - больше всего въехало с номерами 178: 23 машины
    // 178 - больше всего въехало с номера 98: 50 машин
    // 01 - больше всего въехало с номера 178: 11 машин
    // 22 - больше всего въехало с номера 01: 30 машин
    // 33 - больше всего въехало с номера 12: 100 машин

    // Задача №2: узнать сколько всего машин со спец номерами: начинаются на M и заканчиваются на АВ.
    // Не повторяющиеся

    //Входящие данные
    // Map<Integer, Map<String, String[]>> - где ключ первого уровня - номер региона,
    // out, input - ключи второго уровня (выезд, въезд), и String[] - массивы номеров.
    // { 1 : {
    //      "out" : ["К474СЕ178"],
    //      "input": ["А222РТ178"]
    //    },
    //   2 : {
    //        "out" : ["К722АВ12", "А222РТ178"],
    //        "input" : ["М001АВ01", "А023РВ73"],
    //   }
    // ..
    //  }

    //Список технологий:
    // Set (HashSet) - узнать что это, set.contains(), set.put()
    // Map (HashMap) - узнать что это, map.get(), map.put(), map.entrySet() - для итерации, entry.getValue(), entry.getKey()
    // <Integer> - обозначает тип который хранится в этой структуре данных (Generics)
    // Регулярные выражения - вытащить регион авто

    public static void main(String[] args) {
        Map<Integer, Map<String, String[]>> testData = GeneratorExpertHomework.getData();
        getTopFiveRegions(testData);
        Map<Integer, Map<String, String[]>> testDataTwo = getDataForCountCars();
        System.out.println(getCountOfSpecialNumbers(testDataTwo));
    }

    private static Map<Integer, Map<String, String[]>> getDataForCountCars() {
        Map<Integer, Map<String, String[]>> data = new HashMap<>();

        String[] inputRegion10 = {"М777АВ10"};
        String[] outputRegion10 = {};

        Map<String, String[]> region10 = new HashMap<>();

        region10.put("out", outputRegion10);
        region10.put("input", inputRegion10);

        data.put(10, region10);

        String[] inputRegion20 = {"М777АВ10", "А111АА777"};
        String[] outputRegion20 = {"А777БВ77", "М222АВ10"};

        Map<String, String[]> region20 = new HashMap<>();

        region20.put("out", outputRegion20);
        region20.put("input", inputRegion20);

        data.put(20, region20);

        String[] inputRegion30 = {"М222АВ10", "Б777БВ13", "В777БВ13", "Г777БВ13", "Д777БВ99", "Е777БВ99", "Е777БВ13"};
        String[] outputRegion30 = {"А777БВ77", "Б777БВ77"};

        Map<String, String[]> region30 = new HashMap<>();

        region30.put("out", outputRegion30);
        region30.put("input", inputRegion30);

        data.put(30, region30);

        String[] inputRegion40 = {"А777БВ14", "Б777БВ14", "В777БВ14", "М333АВ55", "Д777БВ99", "Е777БВ99", "Е777БВ14", "О111ОО14"};
        String[] outputRegion40 = {"А777БВ77", "Б777БВ77"};

        Map<String, String[]> region40 = new HashMap<>();

        region40.put("out", outputRegion40);
        region40.put("input", inputRegion40);

        data.put(40, region40);
        return data;
    }

    private static Map<Integer, String[]> getMapInputCars(Map<Integer, Map<String, String[]>> data) {
        Map<Integer, String[]> inputCars = new HashMap<>();
        for (Map.Entry<Integer, Map<String, String[]>> region: data.entrySet()) {
            String[] inputNumbers = region.getValue().get("input");
            inputCars.put(region.getKey(), inputNumbers);
        }
        return inputCars;
    }

    private static Map<String, Integer> getCarsStatisticsByRegion(String[] numbers) {
        Map<String, Integer> carsStatistics = new HashMap<>();
        String registrationEndFormat = "[а-яА-Я]{2}\\d{2,3}";
        for (int i = 0; i < numbers.length; i++) {
            String currentNumber = numbers[i];
            Matcher registrationEndMatcher = Pattern.compile(registrationEndFormat).matcher(currentNumber);
            if (registrationEndMatcher.find()) {
                String region = currentNumber.substring(registrationEndMatcher.start()+2);
                int regionCount = carsStatistics.containsKey(region) ? carsStatistics.get(region) + 1 : 1;
                carsStatistics.put(region, regionCount);
            }
        }
        return carsStatistics;
    }

    public static void getTopFiveRegions(Map<Integer, Map<String, String[]>> data) {
        Comparator<Map.Entry<Integer, String[]>> carRegionComparator = new Comparator<>() {
            @Override
            public int compare(Map.Entry<Integer, String[]> o1, Map.Entry<Integer, String[]> o2) {
                return Integer.compare(o2.getValue().length, o1.getValue().length);
            }
        };
        Map<Integer, String[]> inputCars = getMapInputCars(data);
        List<Map.Entry<Integer, String[]>> carList = new LinkedList<>(inputCars.entrySet());
        carList.sort(carRegionComparator);
        Map<Integer, Map<String, Integer>> regionStatistics = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            Map.Entry<Integer, String[]> currentRegion = carList.get(i);
            regionStatistics.put(currentRegion.getKey(), getCarsStatisticsByRegion(currentRegion.getValue()));

        }
        System.out.println("ТОП-5: " + Arrays.toString(regionStatistics.keySet().toArray()));
        for (Map.Entry<Integer, Map<String, Integer>> region: regionStatistics.entrySet()) {
            System.out.print(region.getKey() + " - ");
            Map.Entry<String, Integer> maxEntry = Collections.max(region.getValue().entrySet(), (Map.Entry.comparingByValue()));

            System.out.println("больше всего въехало с номерами " + maxEntry.getKey() + ": " + maxEntry.getValue() + " машин");
        }
    }

    public static int getCountOfSpecialNumbers(Map<Integer, Map<String, String[]>> data) {
        Set<String> knapsack = new HashSet<>();
        String format = "[мМ]\\d{3}[аА][вВ]\\d{2,3}";
        for (Map.Entry<Integer, Map<String, String[]>> region: data.entrySet()) {
            Map<String, String[]> regionMoving = region.getValue();
            for (Map.Entry<String, String[]> numbersArray: regionMoving.entrySet()) {
                String[] currentArray = numbersArray.getValue();
                getUniqueNumbersFromArrayByFormat(currentArray, format, knapsack);
            }
        }
        return knapsack.size();
    }

    private static void getUniqueNumbersFromArrayByFormat(String[] array, String format, Set<String> knapsack) {
        for (int i = 0; i < array.length; i++) {
            String currentNumber = array[i];
            Matcher matcher = Pattern.compile(format).matcher(currentNumber);
            if (matcher.matches()) {
                knapsack.add(currentNumber);
            }
        }
    }


}
