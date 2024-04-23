package oop.project.cli;

import java.util.List;
import oop.project.cli.InputParsing.Lexer;
import oop.project.cli.InputParsing.Token;

public class Parser {
    public void parse(List<Token> tokens) {
        if (tokens.isEmpty()) {
            System.out.println("No input provided.");
            return;
        }
        Token commandToken = tokens.get(0);
        if (commandToken.getType() != Token.Type.COMMAND) {
            System.out.println("Error: Expected a command, but got " + commandToken.getValue());
            return;
        }
        switch (commandToken.getValue()) {
            case "add":
                handleAdd(tokens);
                break;
            case "sub":
                handleSubtract(tokens);
                break;
            case "calc":
                handleCalc(tokens);
                break;
            case "date":
                handleDate(tokens);
                break;
            default:
                System.out.println("Invalid command: " + commandToken.getValue());
        }
    }
    private void handleAdd(List<Token> tokens) {
        if (tokens.size() < 3) {
            System.out.println("Error: 'add' command requires two integer arguments.");
            return;
        }
        try {
            int a = Integer.parseInt(tokens.get(1).getValue());
            int b = Integer.parseInt(tokens.get(2).getValue());
            System.out.println("Result of add: " + (a + b));
        } catch (NumberFormatException e) {
            System.out.println("Error: 'add' command requires integer arguments.");
        }
    }

    private void handleSubtract(List<Token> tokens) {
        float left = 0, right = 0;
        boolean leftSet = false, rightSet = false;

        for (int i = 1; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.getType() == Token.Type.FLAGS) {
                if (token.getValue().equals("--left") && i + 1 < tokens.size()) {
                    left = Float.parseFloat(tokens.get(i + 1).getValue());
                    leftSet = true;
                    i++; // move past the value
                } else if (token.getValue().equals("--right") && i + 1 < tokens.size()) {
                    right = Float.parseFloat(tokens.get(i + 1).getValue());
                    rightSet = true;
                    i++; // move past the value
                }
            }
        }

        if (!leftSet || !rightSet) {
            System.out.println("Error: 'sub' command requires --left and --right flags with values.");
            return;
        }

        System.out.println("Result of sub: " + (left - right));
    }
    private void handleCalc(List<Token> tokens) {
        if (tokens.size() < 2) {
            System.out.println("Error: 'calc' command requires a subcommand.");
            return;
        }
        System.out.println("Executing calc operation for: " + tokens.get(1).getValue());
    }

    private void handleDate(List<Token> tokens) {
        if (tokens.size() < 2) {
            System.out.println("Error: 'date' command requires a date argument.");
            return;
        }
        System.out.println("Date set to: " + tokens.get(1).getValue());
    }
}
