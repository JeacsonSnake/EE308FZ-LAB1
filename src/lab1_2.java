package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class lab1_2 {
    // String[] specialPatterArr = {"switch", "if", "else"};
    static boolean[] hasSpecialPattern = { false, false, false };
    static boolean[] layer = { false };
    static int ifElseNum = 0;
    static int ifElseifElseNum = 0;

    public static void main(String[] args) throws IOException {
        System.out.println("Please enter the file name and the level of requirement: (seprated by space)");
        Scanner input = new Scanner(System.in);
        String fileLevelStrings = input.nextLine();
        String[] fileLevelStringsArr = fileLevelStrings.split(" ");
        int level = Integer.parseInt(fileLevelStringsArr[1]);
        try {
            BufferedReader bReader = new BufferedReader(new FileReader(fileLevelStringsArr[0]));
            String readString;
            String patternStr = "void signed unsigned short long int float double char enum struct union typedef Bool Imaginary Complex const volatile restrict inline auto static extern register sizeof goto return break continue if else switch case default do while for";
            
            String[] patternArr = patternStr.split(" ");
            StringBuffer readStringBuffer = new StringBuffer();
            int count1 = 0;
            while ((readString = bReader.readLine()) != null) {
                readStringBuffer.append(readString);
            }
            bReader.close();
            input.close();
            for (int i = 0; i < patternArr.length; i++) {
                count1 += findByRegex(patternArr[i], readStringBuffer, i);
            }
            System.out.println("total num: " + count1);
            if (level > 1) {
                findSwitch(readStringBuffer);
                if (level > 2) {
                    findIE(readStringBuffer, level);
                }
            }

        } catch (Exception e) {
            System.out.println("Can't find the file!");
            System.out.println(e);
        }
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

    public static void findIE(StringBuffer readStringBuffer, int level) {
        findIfElse(readStringBuffer, level);
        System.out.println("if-else num: " + ifElseNum);
        if (level == 4) {
            System.out.println("if-elseif-else num: " + ifElseifElseNum);
        }
    }

    public static void findIfElse(StringBuffer readStringBuffer, int level) {
        Pattern patternObj = Pattern.compile("if.*?{");
        Pattern subElsePatternObj = Pattern.compile("else.*?{");

        Matcher macherObj = patternObj.matcher(readStringBuffer);
        Matcher subElseMacherObj = subElsePatternObj.matcher(readStringBuffer);

        String subString = "";
        StringBuffer subStringBuffer = new StringBuffer();
        while (macherObj.find()) {
            subString = macherObj.toString();
            subStringBuffer.append(subString);
            findIfElse(readStringBuffer, level);

            if (subElseMacherObj.find(macherObj.end())) {
                subString = subElseMacherObj.toString();
                subStringBuffer.append(subString);
                findIfElse(readStringBuffer, level);
                if (subElseMacherObj.start() == macherObj.end()) {
                    ifElseNum++;
                }

            }

            if (level == 4) {
                findIfElseIf(readStringBuffer, macherObj);
            }
        }
    }

    public static void findIfElseIf(StringBuffer readStringBuffer, Matcher macherObj) {
        Pattern subElseIfPatternObj = Pattern.compile("else if.*?}");
        Pattern subElsePatternObj = Pattern.compile("else.*?}");

        Matcher subElseIfMacherObj = subElseIfPatternObj.matcher(readStringBuffer);
        Matcher subElseMacherObj = subElsePatternObj.matcher(readStringBuffer);

        String subString = "";
        StringBuffer subStringBuffer = new StringBuffer();

        while (subElseIfMacherObj.find(macherObj.end())) {
            subString = subElseIfMacherObj.toString();
            subStringBuffer.append(subString);
            findIfElse(readStringBuffer, 4);
        }
        if (subElseMacherObj.find(macherObj.end())) {
            if (subElseMacherObj.start() == subElseIfMacherObj.end()) {
                ifElseifElseNum++;
            }
        }
    }
}
