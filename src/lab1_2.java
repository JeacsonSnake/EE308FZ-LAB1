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
    static String[][] layers = new String[8][3];
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
            String patternStr = "void signed unsigned short long int float double char enum struct union typedef Bool Imaginary Complex const volatile restrict inline auto static extern register sizeof goto return break continue if else switch case default do\\s while for";

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
                    findIfSeries(0, readStringBuffer, 0);
                    System.out.println("if-else num: " + ifElseNum);
                    if (level > 3) {
                        System.out.println("if-elseif-else num: " + ifElseifElseNum);
                    }
                }
            }

        } catch (Exception e) {
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
        Matcher matcherObj = patternObj.matcher(readStringBuffer);
        while (matcherObj.find()) {
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
        Matcher matcherObj = patternObj.matcher(readStringBuffer);
        String subString = "";
        Pattern subPatternObj = Pattern.compile("case");
        Matcher submatcherObj = null;
        int switchNum = 0;
        int[] caseNumArr = { 0 };
        while (matcherObj.find()) {
            switchNum++;
            caseNumArr = Arrays.copyOf(caseNumArr, switchNum);
            subString = matcherObj.toString();
            submatcherObj = subPatternObj.matcher(subString);
            while (submatcherObj.find()) {
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

    private static int findIfSeries(int index, StringBuffer readStringBuffer, int layerNum) {
        StringBuffer tempBuffer = readStringBuffer;
        int temp = 0;
        int tempElseIfPosition = tempBuffer.length();
        int tempElsePosition = tempBuffer.length();

        int findCloseBracket = readStringBuffer.length();
        Pattern patternIfObj = Pattern.compile("\\s[^else]\\sif.*?\\{");
        Matcher matcherIfObj = patternIfObj.matcher(readStringBuffer);

        Pattern patternElseIfObj = Pattern.compile("\\s?else\\sif.*?\\{");
        Matcher matcherElseIfObj = patternElseIfObj.matcher(readStringBuffer);

        Pattern patternElseObj = Pattern.compile("\\s?else\\s?\\{");
        Matcher matcherElseObj = patternElseObj.matcher(readStringBuffer);

        Pattern patternOpenBracketObj = Pattern.compile("\\{");
        Matcher matcherOpenBracketObj = patternOpenBracketObj.matcher(readStringBuffer);

        Pattern patternCloseBracketObj = Pattern.compile("\\}");
        Matcher matcherCloseBracketObj = patternCloseBracketObj.matcher(readStringBuffer);
        if (layerNum > 0) {
            if (layers[layerNum - 1][0] == "if(){}") {
                if (matcherCloseBracketObj.find(index - 1)) {
                    findCloseBracket = matcherCloseBracketObj.end();
                } else {
                    findCloseBracket = readStringBuffer.length();
                }
            }
        } else {
            if (layers[layerNum][0] == "if(){}") {
                if (matcherCloseBracketObj.find(index - 1)) {
                    findCloseBracket = matcherCloseBracketObj.end();
                } else {
                    findCloseBracket = readStringBuffer.length();
                }
            }
        }

        // if (matcherCloseBracketObj.find(index)) {
        // tempCloseBracketPosition = matcherCloseBracketObj.start();
        // }

        if (matcherIfObj.find(index)) {
            if (matcherIfObj.end() < findCloseBracket) {
                for (int i = 0; i < layers[layerNum].length; i++) {
                    layers[layerNum][i] = "";
                }
                layers[layerNum][0] = "if(){}";
                // System.out.println("_________if_____________");
                // System.out.println(matcherIfObj.toString());
                // System.out.println("___________layer: " + layerNum + "________");
                // System.out.println();
                temp = findIfSeries(matcherIfObj.end(), tempBuffer, layerNum + 1);
                if (matcherElseObj.find(temp)) {
                    tempElsePosition = matcherElseObj.start();
                }
                if (matcherElseIfObj.find(temp)) {
                    tempElseIfPosition = matcherElseIfObj.start();
                }

                if (tempElseIfPosition < tempElsePosition) {
                    layers[layerNum][1] = "else if(){}";
                    // System.out.println("_________else if___________");
                    // System.out.println(matcherElseIfObj.toString());
                    // System.out.println("________layer: " + layerNum + "____________");
                    // System.out.println();
                    temp = findIfSeries(matcherElseIfObj.end(), tempBuffer, layerNum + 1);
                    temp = findElseIf(temp, tempBuffer, layerNum);
                } else {
                    layers[layerNum][2] = "else{}";
                    // System.out.println("_________else_____________");
                    // System.out.println(matcherElseObj.toString());
                    // System.out.println("______layer: " + layerNum + "____________");
                    // System.out.println();
                    if (layers[layerNum][1] == "else if(){}") {
                        ifElseifElseNum++;
                    } else {
                        ifElseNum++;
                    }
                    temp = findIfSeries(matcherElseObj.end(), tempBuffer, layerNum + 1);
                }

            } else {
                return findCloseBracket;
            }

        } else if (matcherElseObj.find(index)) {
            return findCloseBracket;
        } else if (matcherOpenBracketObj.find(index)) {
            temp = findIfSeries(matcherOpenBracketObj.end(), tempBuffer, layerNum + 1);
        } else if (matcherCloseBracketObj.find(index)) {
            return matcherCloseBracketObj.end();
        }
        return temp;
    }

    private static int findElseIf(int index, StringBuffer tempBuffer, int layerNum) {
        int temp = index;
        int elsePosition = tempBuffer.length();
        int elseIfPosition = tempBuffer.length();

        Pattern patternElseIfObj = Pattern.compile("\\s?else\\sif.*?\\{");
        Matcher matcherElseIfObj = patternElseIfObj.matcher(tempBuffer);

        Pattern patternElseObj = Pattern.compile("\\s?else\\s?\\{");
        Matcher matcherElseObj = patternElseObj.matcher(tempBuffer);
        if (matcherElseObj.find(index)) {
            elsePosition = matcherElseObj.start();
        }
        if (matcherElseIfObj.find(index)) {
            elseIfPosition = matcherElseIfObj.start();
        }

        if (elseIfPosition < elsePosition) {
            // System.out.println("_________else if___________");
            // System.out.println(matcherElseIfObj.toString());
            // System.out.println("_________layer: " + layerNum + "___________");
            // System.out.println();
            temp = findIfSeries(matcherElseIfObj.end() - 1, tempBuffer, layerNum + 1);
            temp = findElseIf(temp, tempBuffer, layerNum);
        } else {
            layers[layerNum][2] = "else{}";
            // System.out.println("_________else____*________");
            // System.out.println(matcherElseObj.toString());
            // System.out.println("_________layer: " + layerNum + "_________");
            ifElseifElseNum++;
            temp = findIfSeries(matcherElseObj.end(), tempBuffer, layerNum + 1);
        }

        return temp;
    }
}
