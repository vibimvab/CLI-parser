package oop.project.cli;

import java.util.Scanner;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your command:");
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("exit")) {
                break;
            }
            try {
                Map<String, Object> result = Scenarios.parse(input);
                System.out.println("Result: " + result);
            } catch (Exception e) {
                System.out.println("Error processing command: " + e.getMessage());
            }
        }
        scanner.close();
        System.out.println("Program exited.");
    }
}
