package oop.project.cli;

import java.util.*;

import oop.project.cli.Argument;
import oop.project.cli.InputParsing.Lexer;
import oop.project.cli.InputParsing.Token;

public class Parser {
    public Lexer lexer;
    private Map<String, Command> commands;

    public Parser() {
        this.lexer = new Lexer();
        commands = new HashMap<>();
        setupCommands();
    }
    private void setupCommands() {
        // TODO NamedArguments is always empty because we never actually put anything into it. Need to
        // work around that
        List<Argument> addArgs = List.of(
                new Argument("left", "integer"),
                new Argument("right", "integer")
        );
        List<Argument> subArgs = List.of(
                new Argument("left", "float", true, 0.0),  // Default for left is 0.0
                new Argument("right", "float")
        );

        commands.put("add", new Command("add", addArgs, new ArrayList<>()));
        commands.put("sub", new Command("sub", subArgs, new ArrayList<>()));
    }

    public Map<String, Object> parse(List<Token> tokens) {
        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("No input provided.");
        }
        Token commandToken = tokens.get(0);
        Command command = commands.get(commandToken.getValue());
        if (command == null) {
            throw new IllegalArgumentException("Unknown Command: " + commandToken.getValue());
        }

        return handleCommand(command, tokens.subList(1, tokens.size()));
    }

    private Map<String, Object> handleCommand(Command command, List<Token> tokens) {
        Map<String, Object> parsedArguments = new HashMap<>();
        System.out.println("hello");
        System.out.println(command.getName());
        System.out.println(command.getNamedArguments());
        // Initialize with default values or mark as Optional.empty() if not provided
        for (Argument arg : command.getNamedArguments()) {
            System.out.println(arg.getName());
            if (arg.isOptional()) {
                System.out.println("this runs");
                parsedArguments.put(arg.getName(), arg.getDefaultValue().orElse(Optional.empty()));
            }
        }
        int positionalIndex = 0;
        for (Token token : tokens) {
            if (token.getType() == Token.Type.FLAGS) {
                String key = token.getValue().substring(2);
                if (tokens.indexOf(token) + 1 < tokens.size()) {
                    Token nextToken = tokens.get(tokens.indexOf(token) + 1);
                    Argument arg = findArgumentByName(command, key);
                    if (arg != null && validateType(arg, nextToken)) {
                        parsedArguments.put(key, parseValue(nextToken, arg.getType()));
                    } else {
                        throw new IllegalArgumentException("Invalid type for flag: " + key);
                    }
                }
            } else {
                if (positionalIndex < command.getPositionalArguments().size()) {
                    Argument arg = command.getPositionalArguments().get(positionalIndex++);
                    if (validateType(arg, token)) {
                        parsedArguments.put(arg.getName(), parseValue(token, arg.getType()));
                    } else {
                        throw new IllegalArgumentException("Invalid type for argument: " + arg.getName());
                    }
                } else {
                    throw new IllegalArgumentException("Extraneous argument provided: " + token.getValue());
                }
            }
        }

        verifyArguments(command, parsedArguments);
        return parsedArguments;
    }


    private boolean validateType(Argument arg, Token token) {
        try {
            if (arg.getType().equals("integer")) {
                Integer.parseInt(token.getValue());
            } else if (arg.getType().equals("float")) {
                Float.parseFloat(token.getValue());
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private Argument findArgumentByName(Command command, String name) {
        // Check both positional and named arguments for the command
        for (Argument arg : command.getPositionalArguments()) {
            if (arg.getName().equals(name)) {
                return arg;
            }
        }
        for (Argument arg : command.getNamedArguments()) {
            if (arg.getName().equals(name)) {
                return arg;
            }
        }
        return null; // No argument found with the given name
    }

    private Object parseValue(Token token, String type) {
        if (type.equals("integer")) {
            return Integer.parseInt(token.getValue());
        } else if (type.equals("float")) {
            return Float.parseFloat(token.getValue());
        }
        return token.getValue(); // For strings or unhandled types
    }

    private void verifyArguments(Command command, Map<String, Object> providedArgs) {
        for (Argument arg : command.getPositionalArguments()) {
            if (!providedArgs.containsKey(arg.getName()) && !arg.isOptional()) {
                throw new IllegalArgumentException("Missing required argument: " + arg.getName());
            }
        }
    }
}
