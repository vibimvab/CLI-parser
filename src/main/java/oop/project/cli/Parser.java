package oop.project.cli;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import oop.project.cli.InputParsing.Lexer;
import oop.project.cli.InputParsing.Token;

public class Parser {
    public Lexer lexer;
    private List<Command> commands;

    public Parser() {
        this.lexer = new Lexer();
        commands = new ArrayList<>();
    }

    public void addCommand(String name, List<Argument> positionalArguments, List<Argument> namedArguments) {
        commands.add(new Command(name, positionalArguments, namedArguments));
    }

    public void addCommand(String name, List<Argument> positionalArguments, List<Argument> namedArguments,
                           boolean requireConfirmation, String confirmationMessage) {
        commands.add(new Command(name, positionalArguments, namedArguments, requireConfirmation, confirmationMessage));
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
//        switch (commandToken.getValue()) {
//            case "add":
//                return handleAdd(tokens);
//            case "sub":
//                return handleSubtract(tokens);
//            case "calc":
//                return handleCalc(tokens);
//            case "date":
//                return handleDate(tokens);
//            default:
//                System.out.println("Invalid command: " + commandToken.getValue());
//                throw new IllegalArgumentException("Invalid Command");
//        }

        for (Command command: commands) {
            if (command.name.equals(commandToken.getValue())) {
                return handleCommand(tokens);
            }
        }

        // command not found in command list
        System.out.println("Invalid command: " + commandToken.getValue());
        throw new IllegalArgumentException("Invalid Command");
    }

    /**
    * General use function that parses arguments according to
    * */
    private Map<String, Object> handleCommand(List<Token> tokens) {
        // Todo
        throw new UnsupportedOperationException();
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