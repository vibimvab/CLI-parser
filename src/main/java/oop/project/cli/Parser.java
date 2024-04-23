package oop.project.cli.InputParsing;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import oop.project.cli.InputParsing.Lexer;
import oop.project.cli.InputParsing.Token;

public class Parser {
    public Lexer lexer;

    public Parser() {
        this.lexer = new Lexer();
    }
    public Map<String, Object> parse(List<Token> tokens) {
        if (tokens.isEmpty()) {
            System.out.println("No input provided.");
            throw new IllegalArgumentException("No input provided.");
        }
        Token commandToken = tokens.get(0);
        if (commandToken.getType() != Token.Type.COMMAND) {
            throw new IllegalArgumentException("Expected a command, got " + commandToken.getValue());
        }
        switch (commandToken.getValue()) {
            case "add":
                return handleAdd(tokens);
            case "sub":
                return handleSubtract(tokens);
            case "calc":
                return handleCalc(tokens);
            case "date":
                return handleDate(tokens);
            default:
                System.out.println("Invalid command: " + commandToken.getValue());
                throw new IllegalArgumentException("Invalid Command");
        }
    }
    private Map<String, Object> handleAdd(List<Token> tokens) {
        List<Integer> returnValue = new ArrayList<>();
        if (tokens.size() != 3) {
            throw new IllegalArgumentException("Incorrect Number of arguments");
        }
        try {
            int a = Integer.parseInt(tokens.get(1).getValue());
            int b = Integer.parseInt(tokens.get(2).getValue());
            return Map.of("left", a, "right", b);
        } catch (NumberFormatException e) {
            System.out.println("Error: 'add' command requires integer arguments.");
            throw new IllegalArgumentException("No input provided.");
        }
    }

    private Map<String, Object> handleSubtract(List<Token> tokens) {
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
            throw new IllegalArgumentException("No input provided.");
        }

        System.out.println("Result of sub: " + (left - right));
        throw new IllegalArgumentException("No input provided.");
    }
    private Map<String, Object> handleCalc(List<Token> tokens) {
        if (tokens.size() < 2) {
            System.out.println("Error: 'calc' command requires a subcommand.");
            throw new IllegalArgumentException("No input provided.");
        }
        System.out.println("Executing calc operation for: " + tokens.get(1).getValue());
        throw new IllegalArgumentException("No input provided.");
    }

    private Map<String, Object> handleDate(List<Token> tokens) {
        if (tokens.size() < 2) {
            System.out.println("Error: 'date' command requires a date argument.");
            throw new IllegalArgumentException("No input provided.");
        }
        System.out.println("Date set to: " + tokens.get(1).getValue());
        throw new IllegalArgumentException("No input provided.");
    }
}