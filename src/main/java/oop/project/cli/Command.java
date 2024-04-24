package oop.project.cli;

import java.util.List;

public class Command {
    String name;
    List<Argument> positionalArguments;
    List<Argument> namedArguments;

    boolean requireConfirmation;
    String confirmationMessage;



    public Command(String name, List<Argument> positionalArguments, List<Argument> namedArguments) {
        this(name, positionalArguments, namedArguments, false, "");
    }

    public Command(String name, List<Argument> positionalArguments, List<Argument> namedArguments,
                   boolean requireConfirmation, String confirmationMessage) {
        this.name = name;
        this.positionalArguments = positionalArguments;
        this.namedArguments = namedArguments;
        this.requireConfirmation = requireConfirmation;
        this.confirmationMessage = confirmationMessage;
    }
    public String getName() {
        return name;
    }

    public List<Argument> getPositionalArguments() {
        return positionalArguments;
    }

    public List<Argument> getNamedArguments() {
        return namedArguments;
    }

    public boolean isRequireConfirmation() {
        return requireConfirmation;
    }

    public String getConfirmationMessage() {
        return confirmationMessage;
    }
}
