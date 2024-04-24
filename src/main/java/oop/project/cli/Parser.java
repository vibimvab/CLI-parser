package oop.project.cli;

import java.util.*;

import oop.project.cli.Argument;
import oop.project.cli.InputParsing.Lexer;
import oop.project.cli.InputParsing.Token;

public class Parser {
    public Lexer lexer;
    private Map<String, Command> commands;

    /**
     * Initializes the lexer and command map for the parser.
     */
    public Parser() {
        this.lexer = new Lexer();
        commands = new HashMap<>();
        setupCommands();
    }

    /**
     * Configures commands with their respective arguments.
     */
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

    /**
     * Parses a list of tokens into a structured command with arguments.
     *
     * @param tokens the list of tokens to parse
     * @return a map of argument names to their parsed values
     * @throws IllegalArgumentException if no tokens are provided or the command is unknown
     */
    public Map<String, Object> parse(List<Token> tokens) {
        //TODO NAMED ARGUMENTS NOT WORKING WITH THIS IMPLEMENTATION
        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("No input provided.");
        }
        Token commandToken = tokens.get(0);
        Command command = commands.get(commandToken.getValue());
        if (command == null) {
            throw new IllegalArgumentException("Unknown Command: " + commandToken.getValue());
        }
        int i = 1;

        return handleCommand(command, tokens.subList(1, tokens.size()));
    }

    /**
     * Handles the parsing of tokens into arguments based on the specified command.
     *
     * @param command the command configuration
     * @param tokens  the list of tokens representing arguments
     * @return a map of argument names to their parsed values
     */
    private Map<String, Object> handleCommand(Command command, List<Token> tokens) {
        Map<String, Object> parsedArguments = new HashMap<>();
        System.out.println("hello");
        System.out.println(command.getName());
        System.out.println(command.getNamedArguments());
        System.out.println(tokens.size());

        //TODO NAMED ARGUMENTS FOR SINGLE ONES. WORKS FOR TWO NAMED ARGUMENTS
        // Initialize with default values or mark as Optional.empty() if not provided
//        for (Argument arg : command.getNamedArguments()) {
//            System.out.println(arg.getName());
//            if (arg.isOptional()) {
//                System.out.println("this runs");
//                parsedArguments.put(arg.getName(), arg.getDefaultValue().orElse(Optional.empty()));
//            }
//        }
        int positionalIndex = 0; // Track the position of positional arguments.

        // Loop through all tokens, processing each based on its type and expected format.
        int i = 0;
        while (i < tokens.size()) {
            Token token = tokens.get(i);
            if (token.getType() == Token.Type.FLAGS) {
                String key = token.getValue().substring(2);
                // Ensure there is a next token and it is not another flag
                if (i + 1 < tokens.size() && tokens.get(i + 1).getType() != Token.Type.FLAGS) {
                    Token nextToken = tokens.get(i + 1);
                    Argument arg = findArgumentByName(command, key);

                    // Validate the type of the next token and ensure it matches the expected type of the argument.
                    if (arg != null && validateType(arg, nextToken)) {
                        parsedArguments.put(key, parseValue(nextToken, arg.getType()));
                        i++; // Skip the next token since it's the value for the current flag
                    } else {
                        throw new IllegalArgumentException("Invalid type for flag: " + key);
                    }
                } else {
                    // If no valid next token, throw exception or handle as error
                    throw new IllegalArgumentException("Expected value after flag: " + key);
                }
            } else {
                // Process positional arguments if the token is not a flag.
                if (positionalIndex < command.getPositionalArguments().size()) {
                    Argument arg = command.getPositionalArguments().get(positionalIndex++);
                    if (validateType(arg, token)) {
                        parsedArguments.put(arg.getName(), parseValue(token, arg.getType()));
                    } else {
                        throw new IllegalArgumentException("Invalid type for positional argument: " + arg.getName());
                    }
                } else {
                    throw new IllegalArgumentException("Extraneous argument provided: " + token.getValue());
                }
            }
            i++;
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
        return null;
    }

    private Object parseValue(Token token, String type) {
        if (type.equals("integer")) {
            return Integer.parseInt(token.getValue());
        } else if (type.equals("float")) {
            return Float.parseFloat(token.getValue());
        }
        return token.getValue();
    }

    private void verifyArguments(Command command, Map<String, Object> providedArgs) {
        for (Argument arg : command.getPositionalArguments()) {
            if (!providedArgs.containsKey(arg.getName()) && !arg.isOptional()) {
                throw new IllegalArgumentException("Missing required argument: " + arg.getName());
            }
        }
    }
}
