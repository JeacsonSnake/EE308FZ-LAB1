package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class lab1_2 {
    // String[] specialPatterArr = {"switch", "if", "else"};
    static boolean[] hasSpecialPattern = { false, false, false };
    static boolean[] layer = { false };

    public static void main(String[] args) throws IOException {
        BufferedReader bReader = new BufferedReader(new FileReader("test.c"));
        String readString;
        String patternStr = "switch if else abstract default goto boolean package nchronzed break double implements private this byte import protected throw throws case extends instanceof public transient catch int return char final interface short try class finally long static void const float native strictfp volatile continue for new super while assert enum";
        String[] patternArr = patternStr.split(" ");
        StringBuffer readStringBuffer = new StringBuffer();
        int count1 = 0;
        while ((readString = bReader.readLine()) != null) {
            readStringBuffer.append(readString);
        }
        bReader.close();
        for (int i = 0; i < patternArr.length; i++) {
            count1 += findByRegex(patternArr[i], readStringBuffer, i);
        }
        System.out.println(count1);
        findSwitch(readStringBuffer);

    }

    public static int findByRegex(String pattern, StringBuffer readStringBuffer, int index) {
        boolean active = false;
        int countNum = 0;
        if (index <= 2) {
            active = true;
        }
        Pattern patternObj = Pattern.compile(pattern);
        Matcher macherObj = patternObj.matcher(readStringBuffer);
        while (macherObj.find()) {
            if (active) {
                hasSpecialPattern[index] = true;
                active = false;
            }
            countNum++;
        }
        return countNum;
    }

    public static void findSwitch(StringBuffer readStringBuffer) {
        Pattern patternObj = Pattern.compile("switch.*?}");
        Matcher macherObj = patternObj.matcher(readStringBuffer);
        String subString = "";
        Pattern subPatternObj = Pattern.compile("case");
        Matcher subMacherObj = null;
        int switchNum = 0;
        int[] caseNumArr = { 0 };
        while (macherObj.find()) {
            switchNum++;
            caseNumArr = Arrays.copyOf(caseNumArr, switchNum);
            subString = macherObj.toString();
            subMacherObj = subPatternObj.matcher(subString);
            while (subMacherObj.find()) {
                caseNumArr[switchNum - 1]++;
            }

        }
        System.out.println("switch num: " + switchNum);
        System.out.print("case num:");
        for (int i = 0; i < caseNumArr.length; i++) {
            System.out.print(" " + caseNumArr[i]);
        }
        System.out.println();
    }

    public static int findIfElse(String pattern, StringBuffer readStringBuffer) {
        Pattern patternObj = Pattern.compile(pattern);
        Matcher macherObj = patternObj.matcher(readStringBuffer);
        int countNum = 0;
        while (macherObj.find()) {
            countNum++;
        }
        return countNum;
    }

}
