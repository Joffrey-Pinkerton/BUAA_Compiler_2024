package top;

import exception.ErrorInfo;
import lexicon.Token;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class Handler {
    private static final Stack<Token> tokenStack = new Stack<>();
    private static final ArrayList<ErrorInfo> errorList = new ArrayList<>();
    private static final Stack<String> outputList = new Stack<>();

    private static final Stack<Token> saveTokenStack = new Stack<>();
    private static final ArrayList<ErrorInfo> saveErrorList = new ArrayList<>();
    private static final ArrayList<String> saveOutputList = new ArrayList<>();

    public static void addToken(Token token) {
        tokenStack.push(token);
        outputList.add(token.getType() + " " + token);
    }

    public static void popToken() {
        outputList.pop();
        tokenStack.pop();
    }

    public static void addErrorInfo(ErrorInfo error) {
        errorList.add(error);
    }

    public static void addOutputInfo(String output) {
        outputList.add(output);
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
            for (String str : outputList) {
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
            for (ErrorInfo error : errorList) {
                bw.write(error + "\n");
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void save() {
        saveTokenStack.clear();
        saveErrorList.clear();
        saveOutputList.clear();

        saveTokenStack.addAll(tokenStack);
        saveErrorList.addAll(errorList);
        saveOutputList.addAll(outputList);
    }

    public static void restore() {
        tokenStack.clear();
        errorList.clear();
        outputList.clear();

        tokenStack.addAll(saveTokenStack);
        errorList.addAll(saveErrorList);
        outputList.addAll(saveOutputList);
    }
}
