package oop.project.cli;

import java.util.List;

public class Command {
    String name;
    List<Argument> positionalArguments;
    List<Argument> namedArguments;

    boolean requireConfirmation;
    String confirmationMessage;


    Command(String name, List<Argument> positionalArguments, List<Argument> namedArguments) {
        this.name = name;
        this.positionalArguments = positionalArguments;
        this.namedArguments = namedArguments;
        requireConfirmation = false;
        confirmationMessage = "";
    }

    Command(String name, List<Argument> positionalArguments, List<Argument> namedArguments,
            boolean requireConfirmation, String confirmationMessage) {
        this.name = name;
        this.positionalArguments = positionalArguments;
        this.namedArguments = namedArguments;
        this.requireConfirmation = requireConfirmation;
        this.confirmationMessage = confirmationMessage;
    }
}
