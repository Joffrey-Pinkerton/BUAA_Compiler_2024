import lexicon.Token;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class Handler {
    private static final Stack<String> outputStack = new Stack<>();
    private static final ArrayList<String> errorList = new ArrayList<>();

    private static final Stack<String> saveOutputStack = new Stack<>();
    private static final ArrayList<String> saveErrorList = new ArrayList<>();

    public static void pushOutput(String str) {
        outputStack.push(str);
    }

    public static void popOutput() {
        outputStack.pop();
    }

    public static void addErrorInfo(String error) {
        errorList.add(error);
    }

    public static void print() {
        if (!errorList.isEmpty()) {
            printErrors();
        } else {
            printInfos();
        }
    }


    private static void printInfos() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("parser.txt"));
            for (String str : outputStack) {
                bw.write(str + "\n");
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void printErrors() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("error.txt"));
            for (String error : errorList) {
                bw.write(error + "\n");
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void save() {
        saveOutputStack.clear();
        saveErrorList.clear();

        saveOutputStack.addAll(outputStack);
        saveErrorList.addAll(errorList);
    }

    public static void restore() {
        outputStack.clear();
        errorList.clear();

        outputStack.addAll(saveOutputStack);
        errorList.addAll(saveErrorList);
    }
}
