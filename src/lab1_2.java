package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class lab1_2 {
    String[] specialPatterArr = {"switch", "if", "else"};
    boolean[] hasSpecialPattern = { false };
    boolean[] layer = { false };
    
    public static void main(String[] args) throws IOException {
        BufferedReader bReader = new BufferedReader(new FileReader("test.c"));
        String readString;
        String patternStr = "abstract default goto switch boolean if package nchronzed break double implements private this byte else import protected throw throws case extends instanceof public transient catch int return char final interface short try class finally long static void const float native strictfp volatile continue for new super while assert enum";
        String[] patternArr = patternStr.split(" ");
        StringBuffer readStringBuffer = new StringBuffer();
        int count1 = 0;
        while ((readString = bReader.readLine()) != null) {
            readStringBuffer.append(readString);
        }
        bReader.close();
        for (int i = 0; i < patternArr.length; i++) {
            count1 += findByRegex(patternArr[i], readStringBuffer);
        }
        System.out.println(count1);

    }


    public static int findByRegex(String pattern, StringBuffer readStringBuffer) {
        Pattern patternObj = Pattern.compile(pattern);
        Matcher macherObj = patternObj.matcher(readStringBuffer);
        int countNum = 0;
        while (macherObj.find()) {
            countNum++;
        }
        return countNum;
    }

    public static int findSwitch(String pattern, StringBuffer readStringBuffer) {
        Pattern patternObj = Pattern.compile(pattern);
        Matcher macherObj = patternObj.matcher(readStringBuffer);
        int countNum = 0;
        while (macherObj.find()) {
            countNum++;
        }
        return countNum;
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
