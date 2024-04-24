package oop.project.cli;

import oop.project.cli.InputParsing.Lexer;
import oop.project.cli.InputParsing.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ScenariosTests {

    @Nested
    class Add {

        @ParameterizedTest
        @MethodSource
        public void testAdd(String name, String command, Object expected) {
            try {
                Map<String, Object> result = Scenarios.parse(command);
                assertEquals(expected, result);
            } catch (Exception e) {
                if (expected instanceof Class<?>) {
                    assertTrue(((Class<?>) expected).isInstance(e));
                } else {
                    throw e;
                }
            }
        }


        public static Stream<Arguments> testAdd() {
            return Stream.of(
                Arguments.of("Add", "add 1 2", Map.of("left", 1, "right", 2)),
                Arguments.of("Missing Argument", "add 1", IllegalArgumentException.class),
                Arguments.of("Extraneous Argument", "add 1 2 3", IllegalArgumentException.class),
                Arguments.of("Not A Number", "add one two", IllegalArgumentException.class),
                Arguments.of("Not An Integer", "add 1.0 2.0", IllegalArgumentException.class)
            );
        }

    }

    @Nested
    class Div {

        @ParameterizedTest
        @MethodSource
        public void testSub(String name, String command, Object expected){
            try {
                Map<String, Object> result = Scenarios.parse(command);
                assertEquals(expected, result);
            } catch (Exception e) {
                if (expected instanceof Class<?>) {
                    assertTrue(((Class<?>) expected).isInstance(e));
                } else {
                    throw e;
                }
            }
        }

        public static Stream<Arguments> testSub() {
            return Stream.of(
                Arguments.of("Sub", "sub --left 1.0 --right 2.0", Map.of("right", 2.0, "left", 1.0)),
                Arguments.of("Left Only", "sub --left 1.0", IllegalArgumentException.class),
                Arguments.of("Right Only", "sub --right 2.0", Map.of("left", Optional.empty(), "right", 2.0)),
                Arguments.of("Missing Value", "sub --right", IllegalArgumentException.class),
                Arguments.of("Extraneous Argument", "sub --right 2.0 extraneous", IllegalArgumentException.class),
                Arguments.of("Misspelled Flag", "sub --write 2.0", IllegalArgumentException.class),
                Arguments.of("Not A Number", "sub --right two", IllegalArgumentException.class)
            );
        }

    }

    @Nested
    class Sqrt {

        @ParameterizedTest
        @MethodSource
        public void testSqrt(String name, String command, Object expected) {
            test(command, expected);
        }

        public static Stream<Arguments> testSqrt() {
            return Stream.of(
                Arguments.of("Valid", "sqrt 4", Map.of("number", 4)),
                Arguments.of("Imperfect Square", "sqrt 3", Map.of("number", 3)),
                Arguments.of("Zero", "sqrt 0", Map.of("number", 0)),
                Arguments.of("Negative", "sqrt -1", null)
            );
        }

    }

    @Nested
    class Calc {

        @ParameterizedTest
        @MethodSource
        public void testCalc(String name, String command, Object expected) {
            test(command, expected);
        }

        public static Stream<Arguments> testCalc() {
            return Stream.of(
                Arguments.of("Add", "calc add", Map.of("subcommand", "add")),
                Arguments.of("Sub", "calc sub", Map.of("subcommand", "sub")),
                Arguments.of("Sqrt", "calc sqrt", Map.of("subcommand", "sqrt")),
                Arguments.of("Missing", "calc", null),
                Arguments.of("Invalid", "calc unknown", null)
            );
        }

    }

    @Nested
    class Date {

        @ParameterizedTest
        @MethodSource
        public void testDate(String name, String command, Object expected) {
            test(command, expected);
        }

        public static Stream<Arguments> testDate() {
            return Stream.of(
                Arguments.of("Date", "date 2024-01-01", Map.of("date", LocalDate.of(2024, 1, 1))),
                Arguments.of("Invalid", "date 20240401", null)
            );
        }

    }
    @ParameterizedTest
    @MethodSource("Lexing")
    public void testLexing(String input, List<Token> expectedTokens) {
        Lexer lexer = new Lexer();
        List<Token> resultTokens = lexer.lex(input);
        assertEquals(expectedTokens.size(), resultTokens.size(), "Number of tokens should match.");

        for (int i = 0; i < expectedTokens.size(); i++) {
            assertEquals(expectedTokens.get(i).getType(), resultTokens.get(i).getType(), "Token types should match.");
            assertEquals(expectedTokens.get(i).getValue(), resultTokens.get(i).getValue(), "Token values should match.");
        }
    }

    public static Stream<Arguments> Lexing() {
        return Stream.of(
                Arguments.of("add", List.of(new Token(Token.Type.COMMAND, "add"))),
                Arguments.of("--flag 123", List.of(new Token(Token.Type.FLAGS, "--flag"), new Token(Token.Type.INTEGER, "123"))),
                Arguments.of("--opt 45.67", List.of(new Token(Token.Type.FLAGS, "--opt"), new Token(Token.Type.FLOAT, "45.67"))),
                Arguments.of("xyz", List.of(new Token(Token.Type.INVALID, "xyz"))),
                Arguments.of("2022-01-01", List.of(new Token(Token.Type.DATE, "2022-01-01")))
        );
    }

    private static void test(String command, Object expected) {
        if (expected != null) {
            var result = Scenarios.parse(command);
            assertEquals(expected, result);
        } else {
            //TODO: Update with your specific Exception class or whatever other
            //error handling model you use to check for specific library issues.
            assertThrows(Exception.class, () -> {
                Scenarios.parse(command);
            });
        }
    }

}
