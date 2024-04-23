package oop.project.cli.InputParsing;

import java.util.List;
import java.util.ArrayList;

public class Lexer {
    public List<Token> lex(String input) {
        List<Token> tokens = new ArrayList<>();
        String[] parts = input.split(" ");

        for (String part : parts) {
            if (part.isEmpty()) continue;

            Token.Type type = determineType(part);
            tokens.add(new Token(type, part));
        }

        return tokens;
    }

    private Token.Type determineType(String part) {
        if (part.matches("^\\d+$")) {
            return Token.Type.INTEGER;
        } else if (part.matches("^--\\w+$")) {
            return Token.Type.FLAGS;
        } else if (part.matches("^\\d+\\.\\d+$")) {
            return Token.Type.FLOAT; // Assume decimal is float
        } else if (part.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            return Token.Type.DATE;
        } else if (isCommand(part)) {
            return Token.Type.COMMAND;
        } else {
            return Token.Type.INVALID;
        }
    }

    private boolean isCommand(String part) {
        return part.equals("add") || part.equals("sub") || part.equals("calc") || part.equals("date");
    }
}