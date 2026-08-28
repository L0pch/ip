import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Gepit {
    private static final String BAR = "____________________________________________________________";
    private static final String INTRO_MESSAGE = """
            ____________________________________________________________
            Hey man I'm Gepit
            What can I do for you?
            ____________________________________________________________
            """;

    private static final String GOODBYE_MESSAGE = """
            ____________________________________________________________
            Bye Friendo
            See you again next time :)
            ____________________________________________________________
            """;

    private static TaskList tasks = new TaskList();
    private static final Storage storage =
            new Storage("data/gepit.txt");

    public static void main(String[] args) {
        System.out.println(INTRO_MESSAGE);

        try {
            tasks = new TaskList(storage.load());
        } catch (GepitException e) {
            System.out.println(BAR);
            System.out.println(e.getMessage());
            System.out.println(BAR);
            tasks = new TaskList();
        }

        runChatBot();
        System.out.println(GOODBYE_MESSAGE);
    }

    private static void runChatBot() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();

            try {
                // exit
                if (input.equals("bye")) {
                    return;
                }

                //print list
                if (input.equals("list")) {
                    String taskOutput = "";
                    for (int i = 0; i < tasks.size(); i++) {
                        Task task = tasks.get(i);
                        taskOutput = taskOutput + "\n" + (i + 1) + ". " + task;
                    }
                    System.out.println(BAR + taskOutput + "\n" + BAR);
                    continue;
                }

                //split input
                String[] parts = input.split(" ", 2);
                String cmd = parts[0];

                if (cmd.equals("delete")) {
                    if (parts.length < 2) {
                        throw new GepitException("You want me to delete everything?? or would you rather specify which");
                    }

                    int index = getTaskIndex(parts[1]);
                    System.out.println(deleteTask(index));
                    continue;
                }

                if (cmd.equals("mark")) {
                    if (parts.length < 2) {
                        throw new GepitException("Bruv tell me which index to mark??");
                    }

                    int index = getTaskIndex(parts[1]);
                    System.out.println(markTask(tasks.get(index)));
                    continue;
                }

                if (cmd.equals("unmark")) {
                    if (parts.length < 2) {
                        throw new GepitException("Bruv tell me which index to unmark??");
                    }

                    int index = getTaskIndex(parts[1]);
                    System.out.println(unmarkTask(tasks.get(index)));
                    continue;
                }

                if (cmd.equals("todo")) {
                    if (parts.length < 2 || parts[1].isBlank()) {
                        throw new GepitException("todo needs a description mate");
                    }

                    addTodo(parts[1]);
                    continue;
                }

                if (cmd.equals("deadline")) {
                    if (parts.length < 2 || parts[1].isBlank()) {
                        throw new GepitException("a deadline needs a description and /by date and(or) time");
                    }

                    addDeadline(parts[1]);
                    continue;
                }

                if (cmd.equals("event")) {
                    if (parts.length < 2 || parts[1].isBlank()) {
                        throw new GepitException("an event needs a description, /from and /to date and(or) time");
                    }

                    addEvent(parts[1]);
                    continue;
                }

                throw new GepitException(
                        "Sorry m8, IDK what " + cmd + " is supposed to mean"
                );

            } catch (GepitException e) {
                System.out.println(BAR);
                System.out.println(e.getMessage());
                System.out.println(BAR);
            }
        }
    }

    private static String deleteTask(int index) throws GepitException {
        Task task = tasks.remove(index);
        storage.save(tasks);

        return BAR + "\n I've done it boss. Deleted this guy:"
                + "\n     " + task
                + getTaskCountMessage();
    }

    private static String markTask(Task task) throws GepitException {
        task.markDone();
        storage.save(tasks);

        return BAR + "\n Gr8 job it's done mate"
                + "\n     " + task
                + "\n" + BAR;
    }

    private static String unmarkTask(Task task) throws GepitException {
        task.markNotDone();
        storage.save(tasks);

        return BAR + "\n Get to it soon bruv"
                + "\n     " + task
                + "\n" + BAR;
    }


    private static final String GOT_IT_MESSAGE = BAR + "\n Got it. task added" + "\n     ";
    private static String getTaskCountMessage() {
        return "\n " + "Now you have " + tasks.size() + " tasks in the list\n" + BAR;
    }

    private static void addTodo(String description) throws GepitException {
        Task task = new Todo(description);
        tasks.add(task);
        storage.save(tasks);

        System.out.println(GOT_IT_MESSAGE + task + getTaskCountMessage());
    }

    private static void addDeadline(String input) throws GepitException {
        String[] parts = input.split(" /by ", 2);

        if (parts.length < 2
                || parts[0].isBlank()
                || parts[1].isBlank()) {
            throw new GepitException(
                    "A deadline should look like:"
                            + "\ndeadline return book /by Sunday");
        }

        String description = parts[0];
        LocalDate by;

        try {
            by = LocalDate.parse(parts[1]);
        } catch (DateTimeParseException e) {
            throw new GepitException(
                    "Deadline date should use YYYY-MM-DD, e.g. 2026-09-10");
        }

        Task task = new Deadline(description, by);
        tasks.add(task);
        storage.save(tasks);

        System.out.println(GOT_IT_MESSAGE + task + getTaskCountMessage());
    }

    private static void addEvent(String input) throws GepitException {
        String[] parts = input.split(" /from ", 2);

        if (parts.length < 2 || parts[0].isBlank()) {
            throw new GepitException(
                    "An event should look like:"
                            + "\nevent meeting /from 2026-09-10 /to 2026-09-11");
        }

        String[] eventDuration = parts[1].split(" /to ", 2);

        if (eventDuration.length < 2
                || eventDuration[0].isBlank()
                || eventDuration[1].isBlank()) {
            throw new GepitException(
                    "An event should look like:"
                            + "\nevent meeting /from 2026-09-10 /to 2026-09-11");
        }

        String description = parts[0];
        LocalDate start;
        LocalDate end;

        try {
            start = LocalDate.parse(eventDuration[0]);
            end = LocalDate.parse(eventDuration[1]);
        } catch (DateTimeParseException e) {
            throw new GepitException(
                    "Event dates should use yyyy-MM-dd, "
                            + "e.g. event meeting /from 2026-09-10 /to 2026-09-11");
        }

        if (end.isBefore(start)) {
            throw new GepitException(
                    "An event cannot end before it starts.");
        }

        Task task = new Event(description, start, end);
        tasks.add(task);
        storage.save(tasks);

        System.out.println(GOT_IT_MESSAGE + task + getTaskCountMessage());
    }

    private static int getTaskIndex(String input) throws GepitException {
        int taskNum;

        try {
            taskNum = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new GepitException(
                    input + " isn't a valid task number"
            );
        }

        int index = taskNum - 1;
        if (index < 0 || index >= tasks.size()) {
            throw new GepitException(
                    "Task " + taskNum + " doesn't exist"
            );
        }

        return index;
    }
}
