import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

    private static final ArrayList<Task> tasks = new ArrayList<>();
    private static final Path DATA_FILE = Path.of("data", "gepit.txt");

    public static void main(String[] args) {
        System.out.println(INTRO_MESSAGE);

        try {
            loadTasks();
        } catch (GepitException e) {
            System.out.println(BAR);
            System.out.println(e.getMessage());
            System.out.println(BAR);
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
        saveTasks();

        return BAR + "\n I've done it boss. Deleted this guy:"
                + "\n     " + task
                + getTaskCountMessage();
    }

    private static String markTask(Task task) throws GepitException {
        task.markDone();
        saveTasks();

        return BAR + "\n Gr8 job it's done mate"
                + "\n     " + task
                + "\n" + BAR;
    }

    private static String unmarkTask(Task task) throws GepitException {
        task.markNotDone();
        saveTasks();

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
        saveTasks();

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
        String by = parts[1];

        Task task = new Deadline(description, by);
        tasks.add(task);
        saveTasks();

        System.out.println(GOT_IT_MESSAGE + task + getTaskCountMessage());
    }

    private static void addEvent(String input) throws GepitException {
        String[] parts = input.split(" /from ", 2);

        if (parts.length < 2 || parts[0].isBlank()) {
            throw new GepitException(
                    "An event should look like:"
                            + "\nevent meeting /from Mon 2pm /to 4pm");
        }

        String[] eventDuration = parts[1].split(" /to ", 2);

        if (eventDuration.length < 2
                || eventDuration[0].isBlank()
                || eventDuration[1].isBlank()) {
            throw new GepitException(
                    "An event should look like:"
                            + "\nevent meeting /from Mon 2pm /to 4pm");
        }

        String description = parts[0];
        String start = eventDuration[0];
        String end = eventDuration[1];

        Task task = new Event(description, start, end);
        tasks.add(task);
        saveTasks();

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

    private static void saveTasks() throws GepitException {
        try {
            Files.createDirectories(DATA_FILE.getParent());

            List<String> lines = new ArrayList<>();

            for (Task task : tasks) {
                lines.add(task.toDataString());
            }

            Files.write(DATA_FILE, lines);

        } catch (IOException e) {
            throw new GepitException("I couldn't save your tasks, only God is able to");
        }
    }

    private static void loadTasks() throws GepitException {
        if (!Files.exists(DATA_FILE)) {
            return;
        }

        try {
            List<String> lines = Files.readAllLines(DATA_FILE);

            for (String line : lines) {
                String[] lineSplit = line.split(" \\| ");

                String type = lineSplit[0];
                boolean isDone = lineSplit[1].equals("1");
                String description = lineSplit[2];

                Task task;

                if (type.equals("T")) {
                    task = new Todo(description);
                } else if (type.equals("D")) {
                    task = new Deadline(description, lineSplit[3]);
                } else if (type.equals("E")) {
                    task = new Event(description, lineSplit[3], lineSplit[4]);
                } else {
                    throw new GepitException("The task file contains an unknown task type");
                }

                if (isDone) {
                    task.markDone();
                }
                tasks.add(task);
            }

        } catch (IOException e) {
            throw new GepitException("Yo! I couldn't load your tasks, check the task file?");
        }
    }
}
