package core;

import exception.ErrorInfo;
import lexicon.Token;
import syntax.Unit;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class Handler {
    private static final Stack<Unit> unitStack = new Stack<>();
    private static final ArrayList<ErrorInfo> errorList = new ArrayList<>();

    private static final Stack<Unit> saveUnitStack = new Stack<>();
    private static final ArrayList<ErrorInfo> saveErrorList = new ArrayList<>();

    public static void addSyntacticUnit(Unit unit) {
        unitStack.push(unit);
    }

    public static void popSyntacticUnit() {
        unitStack.pop();
    }

    public static void addErrorInfo(ErrorInfo error) {
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

            for (Unit unit : unitStack) {
                String output = unit instanceof Token ? (((Token) unit).tokenType() + " " + unit) : unit.getType().toString();
                bw.write(output + "\n");
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
        saveUnitStack.clear();
        saveErrorList.clear();

        saveUnitStack.addAll(unitStack);
        saveErrorList.addAll(errorList);
    }

    public static void restore() {
        unitStack.clear();
        errorList.clear();

        unitStack.addAll(saveUnitStack);
        errorList.addAll(saveErrorList);
    }
}
