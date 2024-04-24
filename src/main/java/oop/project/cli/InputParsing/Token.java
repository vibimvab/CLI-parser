package oop.project.cli.InputParsing;

/**
 * Represents a token that is produced by the Lexer.
 * This token can be of various types defined in the Type enum,
 */
public class Token {
    public enum Type{
        COMMAND, INTEGER, INVALID, FLAGS, DATE, FLOAT
    }
    private final Type type;
    private final String value;

    public Token(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return type + ": " + value;
    }

}