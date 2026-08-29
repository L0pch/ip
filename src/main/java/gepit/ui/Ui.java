package gepit.ui;

import gepit.task.Task;

import java.util.List;
import java.util.Scanner;

/**
 * Handles interactions with the user.
 */
public class Ui {
    private static final String BAR =
            "____________________________________________________________";

    private final Scanner scanner;

    /**
     * Creates a user interface that reads from standard input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Returns whether another command can be read.
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads and returns the next user command.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Shows the chatbot greeting.
     */
    public void showWelcome() {
        System.out.println("""
                ____________________________________________________________
                Hey man I'm Gepit
                What can I do for you?
                ____________________________________________________________
                """);
    }

    /**
     * Shows the chatbot farewell.
     */
    public void showGoodbye() {
        System.out.println("""
                ____________________________________________________________
                Bye Friendo
                See you again next time :)
                ____________________________________________________________
                """);
    }

    /**
     * Shows an error to the user.
     */
    public void showError(String message) {
        System.out.println(BAR);
        System.out.println(message);
        System.out.println(BAR);
    }

    /**
     * Shows an arbitrary chatbot response.
     */
    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showTaskMatches(List<Task> tasks) {
        StringBuilder output = new StringBuilder();
        output.append(BAR)
                .append("\nHere are the matching tasks in your list:");

        for (int i = 0; i < tasks.size(); i++) {
            output.append("\n")
                    .append(i + 1)
                    .append(".")
                    .append(tasks.get(i));
        }

        output.append("\n").append(BAR);

        System.out.println(output);
    }

    /**
     * Returns the separator used by chatbot messages.
     */
    public String getBar() {
        return BAR;
    }
}