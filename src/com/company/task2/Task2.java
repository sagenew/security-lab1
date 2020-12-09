package com.company.task2;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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
    public static void main(String[] args) throws UnsupportedEncodingException {
        Task2 task = new Task2();
        String crypto16 = task.getInput();
        String crypto = decode(crypto16);
        System.out.println(crypto);
        Map<Integer, Double> mapOfHammingDistance = new HashMap<>();
        System.out.println("--------HAMMING DISTANCES--------");
        for(int i = 2; i <= 30; i++) {
            double NAvgHD = task.getNAvgHD(crypto, i);
            System.out.println(i + "\t" + NAvgHD);
            mapOfHammingDistance.put(i, NAvgHD);
        }
        task.visualiseMapOfHammingDistance(mapOfHammingDistance);

        Map<Integer, Double> mapOfCoincidence = new HashMap<>();
        System.out.println("------INDEX OF COINCIDENCE-------");
        for (int i = 1; i <= 30; i++) {
            double indexOfCoincidence = task.getIndexOfCoincidence(crypto, i);
            System.out.println(i + "\t" + task.getIndexOfCoincidence(crypto, i));
            mapOfCoincidence.put(i, indexOfCoincidence);
        }
        task.visualiseMapOfCoincidence(mapOfCoincidence);

        Scanner scanner = new Scanner(System.in);
        byte [] key;
        String decrypto;
        int keyLength;
        while (true) {
            System.out.println("Insert the value of key length: ");
            keyLength = scanner.nextInt();
            key = task.decryptKey(crypto, keyLength);
            System.out.println(Arrays.toString(key));
            decrypto = task.encryptByKey(crypto, key);
            System.out.println(decrypto);
            System.out.println("Does it look like the desired result ? (y/n)");
            String answer = scanner.next();
            if (answer.equals("y")) break;
        }
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
            keyBytes[i] = decryptByte(subTextBuilder.toString());
        }
        return keyBytes;
    }

    private byte decryptByte(String text) {
        byte key = 0;
        double chiSS, minChiSS = 100000;
        String encryptedText;
        for(int i = 0; i <= 255; i++) {
            encryptedText = encryptByByte(text, (byte) i);
            chiSS = calculateChiSquaredStatistic(encryptedText);
            if (chiSS < minChiSS) {
                key = (byte) i;
                minChiSS = chiSS;
            }
        }
        return key;
    }

    private double calculateChiSquaredStatistic(String text) {
        Map<Character, Integer> occurrence = getOccurrenceInText(text);
        char ch;
        double countInText, countInEnglish, difference, absoluteDifference = 0;
        for (Map.Entry<Character, Integer> entry : occurrence.entrySet()) {
            ch = entry.getKey();
            if(occurrenceEnglish.containsKey(ch)) {
                countInText = entry.getValue();
                countInEnglish = occurrenceEnglish.get(ch) * text.length();
                difference = Math.pow((countInText - countInEnglish), 2) / countInEnglish;
                absoluteDifference += difference;
            }
        }
        return absoluteDifference;
    }

    private Map<Character, Integer> getOccurrenceInText(String text) {
        Map<Character, Integer> occurrence = initAlphabetMap();
        char ch;
        int count;
        for(int i = 0; i < text.length(); i++) {
            ch = text.charAt(i);
            if(occurrence.containsKey(ch)) {
                count = occurrence.get(ch);
                occurrence.put(ch, count + 1);
            }
        }
        return occurrence;
    }

    private Map<Character, Integer> initAlphabetMap() {
        return new HashMap<>() {
            {
                put('a', 0); put('b', 0); put('c', 0); put('d', 0); put('e', 0); put('f', 0);
                put('g', 0); put('h', 0); put('i', 0); put('j', 0); put('k', 0); put('l', 0);
                put('m', 0); put('n', 0); put('o', 0); put('p', 0); put('q', 0); put('r', 0);
                put('s', 0); put('t', 0); put('u', 0); put('v', 0); put('w', 0); put('x', 0);
                put('y', 0); put('z', 0);
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

    private static String decode(String text) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i += 2) {
            char decodedChar = (char) Integer.parseInt(text.substring(i, i + 2), 16);
            builder.append(decodedChar);
        }
        return builder.toString();
    }
}
