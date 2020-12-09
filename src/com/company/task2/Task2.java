package com.company.task2;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static java.lang.Math.abs;

public class Task2 {
    private static final String filename = "task2.txt";
    private static final int ENGLISH_ALPHABET_LENGTH = 26;
    private static final Map<Character, Double> occurrenceEnglish = new HashMap<>() {
        {
            put('a', 8.2389258); put('b', 1.5051398); put('c', 2.8065007); put('d', 4.2904556);
            put('e', 12.813865); put('f', 2.2476217); put('g', 2.0327458); put('h', 6.1476691);
            put('i', 6.1476691); put('j', 0.1543474); put('k', 0.7787989); put('l', 4.0604477);
            put('m', 2.4271893); put('n', 6.8084376); put('o', 7.5731132); put('p', 1.9459884);
            put('q', 0.0958366); put('r', 6.0397268); put('s', 6.3827211); put('t', 9.1357551);
            put('u', 2.7822893); put('v', 0.9866131); put('w', 2.3807842); put('x', 0.1513210);
            put('y', 1.9913847); put('z', 0.0746517);
        }
    };
    public static void main(String[] args) {
        Task2 task = new Task2();
        String crypto = task.getInput();
//        Map<Integer, Double> mapOfHammingDistance = new HashMap<>();
//        System.out.println("--------HAMMING DISTANCES--------");
//        for(int i = 2; i <= 30; i++) {
//            double NAvgHD = task.getNAvgHD(crypto, i);
//            System.out.println(i + "\t" + NAvgHD);
//            mapOfHammingDistance.put(i, NAvgHD);
//        }
//        task.visualiseMapOfHammingDistance(mapOfHammingDistance);
//
//        Map<Integer, Double> mapOfCoincidence = new HashMap<>();
//        System.out.println("------INDEX OF COINCIDENCE-------");
//        for (int i = 1; i <= 30; i++) {
//            double indexOfCoincidence = task.getIndexOfCoincidence(crypto, i);
//            System.out.println(i + "\t" + task.getIndexOfCoincidence(crypto, i));
//            mapOfCoincidence.put(i, indexOfCoincidence);
//        }
//        task.visualiseMapOfCoincidence(mapOfCoincidence);

//        Scanner scanner = new Scanner(System.in);
//        String key;
//        String decrypto;
//        int keyLength;
//        boolean keyFound = false;
//        System.out.println("Insert the value of key length: ");
//        while (!keyFound) {
//            keyLength = scanner.nextInt();
//            key = task.decryptKey(crypto, keyLength);
//            System.out.println(key);
//            decrypto = task.encryptByKey(crypto, key);
//            System.out.println(decrypto);
//        }
        String text = "ABCABC";
        byte [] key = new byte[] {0,7,22};
//        System.out.println(task.encryptByKey(text, key));
//        System.out.println(task.encryptByKey(task.encryptByKey(text, key), key));
//        System.out.println(Arrays.toString(task.decryptKey(crypto, 3)));
        System.out.println(task.encryptByKey(crypto, task.decryptKey(crypto, 3)));

    }

    private void visualiseMapOfCoincidence(Map<Integer, Double> map) {
        XYSeries series = new XYSeries("Dependency");
        map.forEach(series::add);
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        LineChart lineChart = new LineChart(dataset,
                "Index of coincidence", "Offset", "Map of indexes of coincidence", Color.GREEN);
        lineChart.setVisible(true);
    }

    private void visualiseMapOfHammingDistance(Map<Integer, Double> map) {
        XYSeries series = new XYSeries("Dependency");
        map.forEach(series::add);
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        LineChart lineChart = new LineChart(dataset,
                "Normalised Average Hamming Distance", "Offset", "Map of Hamming Distances", Color.RED);
        lineChart.setVisible(true);
    }

    private double getIndexOfCoincidence(String text, int offset) {
        char ch, chOffset;
        int coincidences = 0;
        for (int i = offset; i < text.length(); i++) {
            ch = text.charAt(i);
            chOffset = text.charAt(i - offset);
            if(ch == chOffset) coincidences ++;
        }
        return (double) coincidences / (text.length() - offset);
    }

    //Normalised Average Hamming Distance
    private double getNAvgHD(String text, int n) {
        int numOfChunks = (int) Math.ceil((double) text.length() / n);
        String [] chunks = new String[numOfChunks];
        for(int i = 0; i < numOfChunks; i ++) {
            chunks [i] = text.substring(i * n, Math.min(i * n + n, text.length()));
        }
        int sumHDs = 0;
        for (int i = 0; i < numOfChunks - 1; i++) {
            sumHDs += getHammingDistance(chunks[i].getBytes(), chunks[i + 1].getBytes());
        }
        double AvgHD = (double) sumHDs / numOfChunks;
        return AvgHD / n;

    }

    private int getHammingDistance(byte [] bytes1, byte [] bytes2) {
        if(bytes1.length != bytes2.length)
            bytes2 = Arrays.copyOf(bytes2, bytes1.length);
        int hammingDistance = 0;
        byte xor;
        for (int i = 0; i < bytes1.length; i++) {
            xor = (byte) (bytes1[i] ^ bytes2[i]);
            while (xor > 0) {
                hammingDistance += xor & 1;
                xor >>= 1;
            }
        }
        return hammingDistance;
    }

    private String encryptByKey(String text, byte [] key) {
        byte [] textBytes = text.getBytes();
        byte [] cryptoBytes = new byte[textBytes.length];
        for (int i = 0; i < textBytes.length; i++) {
            cryptoBytes[i] = (byte) (textBytes[i] ^ key[i % key.length]);
//            System.out.println(textBytes[i] + " ^ " + key[i % key.length] + " = " + cryptoBytes[i]);
        }
        return new String(cryptoBytes);
    }

    private String encryptByByte(String text, byte key) {
        byte [] textBytes = text.getBytes();
        byte [] cryptoBytes = new byte[textBytes.length];
        for(int i = 0 ; i < textBytes.length; i++) {
            cryptoBytes[i] = (byte) (textBytes[i] ^ key);
        }
        return new String(cryptoBytes);
    }

    private byte [] decryptKey(String text, int keyLength) {
        byte [] keyBytes = new byte[keyLength];
        StringBuilder subTextBuilder;
        for (int i = 0; i < keyLength; i++) {
            subTextBuilder = new StringBuilder();
            int index = i;
            while(index < text.length()) {
                String symbol = text.substring(index, index + 1);
                subTextBuilder.append(symbol);
                index += keyLength;
            }
            if(i == 0)
            System.out.println(subTextBuilder.toString());
            keyBytes[i] = decryptByte(subTextBuilder.toString());
        }
        return keyBytes;
    }

    private byte decryptByte(String text) {
        byte key = 0;
        double fQuotient, minFQuotient = 100;
        String encryptedText;
        for(int i = 0; i <= 255; i++) {
            encryptedText = encryptByByte(text, (byte) i);
            fQuotient = calculateFittingQuotient(encryptedText);
            if (fQuotient < minFQuotient) {
                key = (byte) i;
                minFQuotient = fQuotient;
            }
        }
        return key;
    }

    private double calculateFittingQuotient(String text) {
        Map<Character, Double> occurrence = getOccurrenceInText(text);
        char ch;
        double frequencyInText, frequencyInEnglish, absoluteDifference = 0;
        for (Map.Entry<Character, Double> entry : occurrence.entrySet()) {
            ch = entry.getKey();
            if(occurrenceEnglish.containsKey(ch)) {
                frequencyInText = entry.getValue();
                frequencyInEnglish = occurrenceEnglish.get(ch);
                absoluteDifference += abs(frequencyInText - frequencyInEnglish);
            }
        }
        return absoluteDifference / ENGLISH_ALPHABET_LENGTH;
    }

    private Map<Character, Double> getOccurrenceInText(String text) {
        Map<Character, Double> occurrence = initAlphabetMap();
        char ch;
        double count;
        for(int i = 0; i < text.length(); i++) {
            ch = text.charAt(i);
            if(occurrence.containsKey(ch)) {
                count = occurrence.get(ch);
                occurrence.put(ch, count + 1.0);
            }
        }
        for(Map.Entry<Character, Double> entry : occurrence.entrySet()) {
            entry.setValue(entry.getValue() / text.length() * 100);
        }
        return occurrence;
    }

    private Map<Character, Double> initAlphabetMap() {
        return new HashMap<>() {
            {
                put('a', 0.0); put('b', 0.0); put('c', 0.0); put('d', 0.0); put('e', 0.0); put('f', 0.0);
                put('g', 0.0); put('h', 0.0); put('i', 0.0); put('j', 0.0); put('k', 0.0); put('l', 0.0);
                put('m', 0.0); put('n', 0.0); put('o', 0.0); put('p', 0.0); put('q', 0.0); put('r', 0.0);
                put('s', 0.0); put('t', 0.0); put('u', 0.0); put('v', 0.0); put('w', 0.0); put('x', 0.0);
                put('y', 0.0); put('z', 0.0);
            }
        };
    }

    private String getInput() {
        StringBuilder sb = new StringBuilder();
        try {
            Scanner scanner = new Scanner(new File(filename), StandardCharsets.UTF_8);
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine()).append(System.lineSeparator());
            }
            scanner.close();
            return sb.toString().trim();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }
}
