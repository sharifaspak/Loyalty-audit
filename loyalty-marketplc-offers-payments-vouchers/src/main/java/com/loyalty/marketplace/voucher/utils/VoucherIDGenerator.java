package com.loyalty.marketplace.voucher.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class VoucherIDGenerator {

    private static final char[] EXCLUDES = {'J', 'I', 'E', 'L'};
    private String ip;
    private String hexTime;
    private int encodingBase;
    private int timeLength;
    private long ipReset;
    private long counterReset;
    private long counter = 0;

    public VoucherIDGenerator(int encodingBase, int timeLength) {
        this.encodingBase = encodingBase;
        this.timeLength = timeLength;
        this.ipReset = getPower(encodingBase, 2);
        this.counterReset = getPower(encodingBase, 2);
        this.ip = getIP();
        this.hexTime = getHexTime();
    }

    public String generateOid() {
        String oid = ip + hexTime + Long.toString(counter + counterReset, encodingBase);
        counter = (counter + 1) % counterReset;
        if (counter == 0) {
            String tempTime = getHexTime();
            while (hexTime.equalsIgnoreCase(tempTime)) {
                tempTime = getHexTime();
            }
            hexTime = tempTime;
        }
        return shuffleString(excludeChars(oid.toUpperCase()).substring(2));
    }

    private long getPower(int n, int p) {
        long result = 1;
        for (int i = 0; i < p; i++) {
            result *= n;
        }
        return result;
    }

    private String getIP() {
        long ipValue = 0;
        try {
            byte[] b = InetAddress.getLocalHost().getAddress();
            ipValue = ((b[3] & 0xFF) << 0) & 0xFFFFFFFFL;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return Long.toString(ipValue + ipReset, encodingBase).substring(1);
    }

    private  String getHexTime() {
        String s = Long.toString(System.currentTimeMillis(), encodingBase);
        int l = s.length();
        if (l > timeLength) {
            return (s.substring(l - timeLength));
        } else {
            return s;
        }
    }

    private String excludeChars(String str) {
        for (char c : EXCLUDES) {
            str = str.replace(c, getNewCharForReplacement());
        }
        return str;
    }

    private char getNewCharForReplacement() {
        char newChar = randomAlphabetic(1).toUpperCase().charAt(0);
        if (isValidChar(newChar))
            return newChar;
        else
            return getNewCharForReplacement();
    }

    private boolean isValidChar(char newChar) {
        for (char c : EXCLUDES) {
            if (c == newChar) return false;
        }
        return true;
    }

    private String shuffleString(String string) {
        List<String> letters = Arrays.asList(string.split(""));
        Collections.shuffle(letters);
        StringBuilder shuffled = new StringBuilder();
        for (String letter : letters) {
            shuffled.append(letter);
        }
        return shuffled.toString();
    }
}
