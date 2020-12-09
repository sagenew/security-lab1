package com.company.task1;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static java.lang.Math.abs;


//    'a': 8.2389258,    'b': 1.5051398,    'c': 2.8065007,    'd': 4.2904556,
//    'e': 12.813865,    'f': 2.2476217,    'g': 2.0327458,    'h': 6.1476691,
//    'i': 6.1476691,    'j': 0.1543474,    'k': 0.7787989,    'l': 4.0604477,
//    'm': 2.4271893,    'n': 6.8084376,    'o': 7.5731132,    'p': 1.9459884,
//    'q': 0.0958366,    'r': 6.0397268,    's': 6.3827211,    't': 9.1357551,
//    'u': 2.7822893,    'v': 0.9866131,    'w': 2.3807842,    'x': 0.1513210,
//    'y': 1.9913847,    'z': 0.0746517
public class Task1 {
    private static final String filename = "task1.txt";
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
        Task1 task = new Task1();
        String crypto = task.getInput();
        String decrypto = task.encrypt(crypto, task.decrypt(crypto));
        System.out.println(decrypto);
    }



    private String encrypt(String text, byte key) {
        byte [] textBytes = text.getBytes();
        byte [] cryptoBytes = new byte[textBytes.length];
        for(int i = 0 ; i < textBytes.length; i++) {
            cryptoBytes[i] = (byte) (textBytes[i] ^ key);
        }
        return new String(cryptoBytes);
    }

    private byte decrypt(String text) {
        byte key = 0;
        double fQuotient, minFQuotient = 100;
        String encryptedText;
        for(int i = 0; i <= 255; i++) {
            encryptedText = encrypt(text, (byte) i);
            fQuotient = calculateFittingQuotient(encryptedText);
            if (fQuotient < minFQuotient) {
                key = (byte) i;
                minFQuotient = fQuotient;
            }
        }
        return key;
    }

    private double calculateFittingQuotient(String text) {
        Map<Character, Double> occurence = getOccurenceInText(text);
        char ch;
        double frequencyInText, frequencyInEnglish, absoluteDifference = 0;
        for (Map.Entry<Character, Double> entry : occurence.entrySet()) {
            ch = entry.getKey();
            if(occurrenceEnglish.containsKey(ch)) {
                frequencyInText = entry.getValue();
                frequencyInEnglish = occurrenceEnglish.get(ch);
                absoluteDifference += abs(frequencyInText - frequencyInEnglish);
            }
        }
        return absoluteDifference / ENGLISH_ALPHABET_LENGTH;
    }

    private Map<Character, Double> getOccurenceInText(String text) {
        Map<Character, Double> occurence = initAlphabetMap();
        char ch;
        double count;
        for(int i = 0; i < text.length(); i++) {
            ch = text.charAt(i);
            if(occurence.containsKey(ch)) {
                count = occurence.get(ch);
                occurence.put(ch, count + 1.0);
            }
        }
        for(Map.Entry<Character, Double> entry : occurence.entrySet()) {
            entry.setValue(entry.getValue() / text.length() * 100);
        }
        return occurence;
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
