package oop.project.cli;

import java.util.Optional;

public class Argument {
    private String name;
    private String type;
    private boolean isOptional;
    private Optional<Object> defaultValue;

    public Argument(String name, String type) {
        this(name, type, false, null);
    }

    public Argument(String name, String type, boolean isOptional, Object defaultValue) {
        this.name = name;
        this.type = type;
        this.isOptional = isOptional;
        this.defaultValue = Optional.ofNullable(defaultValue); // Wrap the default value in Optional
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isOptional() {
        return isOptional;
    }

    public Optional<Object> getDefaultValue() {
        return defaultValue;
    }
}
