package oop.project.cli;

import oop.project.cli.InputParsing.Lexer;
import oop.project.cli.InputParsing.Parser;
import oop.project.cli.InputParsing.Token;

import java.awt.print.Paper;
import java.util.List;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

public class Scenarios {
    private static final Parser parser = new Parser();

    /**
     * Parses and returns the arguments of a command (one of the scenarios
     * below) into a Map of names to values. This method is provided as a
     * starting point that works for most groups, but depending on your command
     * structure and requirements you may need to make changes to adapt it to
     * your needs - use whatever is convenient for your design.
     */
    public static Map<String, Object> parse(String command) {
        List<Token> tokens = parser.lexer.lex(command);

        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("No input provided.");
        }

        Token commandToken = tokens.get(0);
        if (commandToken.getType() != Token.Type.COMMAND) {
            throw new IllegalArgumentException("Expected a command, got " + commandToken.getValue());
        }

        return switch (commandToken.getValue()) {
            case "add" -> add(tokens);
            case "sub" -> sub(tokens);
            case "sqrt" -> sqrt(tokens);
            case "calc" -> calc(tokens);
            case "date" -> date(tokens);
            default -> throw new IllegalArgumentException("Unknown command.");
        };
    }

    /**
     * Takes two positional arguments:
     *  - {@code left: <your integer type>}
     *  - {@code right: <your integer type>}
     */
    private static Map<String, Object> add(List<Token> tokens) {
        if (tokens.size() < 3) {
            throw new IllegalArgumentException("add command requires two integer arguments.");
        }
        Token left = tokens.get(1);
        Token right = tokens.get(2);
        if (left.getType() != Token.Type.INTEGER || right.getType() != Token.Type.INTEGER) {
            throw new IllegalArgumentException("add command requires two integers.");
        }
        return parser.parse(tokens);
    }

    /**
     * Takes two <em>named</em> arguments:
     *  - {@code left: <your decimal type>} (optional)
     *     - If your project supports default arguments, you could also parse
     *       this as a non-optional decimal value using a default of 0.0.
     *  - {@code right: <your decimal type>} (required)
     */
    static Map<String, Object> sub(List<Token> tokens) {
        //TODO: Parse arguments and extract values.
        Optional<Double> left = Optional.empty();
        double right = 0.0;
        return Map.of("left", left, "right", right);
    }

    /**
     * Takes one positional argument:
     *  - {@code number: <your integer type>} where {@code number >= 0}
     */
    static Map<String, Object> sqrt(List<Token> tokens) {
        //TODO: Parse arguments and extract values.
        int number = 0;
        return Map.of("number", number);
    }

    /**
     * Takes one positional argument:
     *  - {@code subcommand: "add" | "div" | "sqrt" }, aka one of these values.
     *     - Note: Not all projects support subcommands, but if yours does you
     *       may want to take advantage of this scenario for that.
     */
    static Map<String, Object> calc(List<Token> tokens) {
        //TODO: Parse arguments and extract values.
        String subcommand = "";
        return Map.of("subcommand", subcommand);
    }

    /**
     * Takes one positional argument:
     *  - {@code date: Date}, a custom type representing a {@code LocalDate}
     *    object (say at least yyyy-mm-dd, or whatever you prefer).
     *     - Note: Consider this a type that CANNOT be supported by your library
     *       out of the box and requires a custom type to be defined.
     */
    static Map<String, Object> date(List<Token> tokens) {
        //TODO: Parse arguments and extract values.
        LocalDate date = LocalDate.EPOCH;
        return Map.of("date", date);
    }

    //TODO: Add your own scenarios based on your software design writeup. You
    //should have a couple from pain points at least, and likely some others
    //for notable features. This doesn't need to be exhaustive, but this is a
    //good place to test/showcase your functionality in context.

}