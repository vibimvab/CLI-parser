package oop.project.cli;

import oop.project.cli.InputParsing.Lexer;
import oop.project.cli.InputParsing.Token;

import java.awt.print.Paper;
import java.util.List;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

public class Scenarios {
    private static final Parser parser = new Parser();

    public static Map<String, Object> parse(String command) {
        List<Token> tokens = parser.lexer.lex(command);
        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("No input provided.");
        }
        for(int i = 0; i < tokens.size(); i++) {
            System.out.println(tokens.toString());
        }
        return parser.parse(tokens);
    }
}
