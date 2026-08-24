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

    private static final Task[] tasks = new Task[100];
    private static int taskCount = 0;

    public static void main(String[] args) {
        System.out.println(INTRO_MESSAGE);
        runChatBot();
        System.out.println(GOODBYE_MESSAGE);
    }

    //handles where the input gets sent to for further handling
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
                    for (int i = 0; i < taskCount; i++) {
                        Task task = tasks[i];
                        taskOutput = taskOutput + "\n" + (i + 1) + ". " + task;
                    }
                    System.out.println(BAR + taskOutput + "\n" + BAR);
                    continue;
                }

                //split input
                String[] parts = input.split(" ", 2);
                String cmd = parts[0];

                if (cmd.equals("mark")) {
                    if (parts.length < 2) {
                        throw new GepitException("Bruv tell me which index to mark??");
                    }

                    int index = getTaskIndex(parts[1]);
                    System.out.println(markTask(tasks[index]));
                    continue;
                }

                if (cmd.equals("unmark")) {
                    if (parts.length < 2) {
                        throw new GepitException("Bruv tell me which index to unmark??");
                    }

                    int index = getTaskIndex(parts[1]);;
                    System.out.println(unmarkTask(tasks[index]));
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

    private static String markTask(Task task) {
        task.markDone();
        return BAR + "\n Gr8 job it's done mate" +
                "\n     " + task.toString() +
                "\n" + BAR;
    }

    private static String unmarkTask(Task task) {
        task.markNotDone();
        return BAR + "\n Get to it soon bruv" +
                "\n     " + task.toString() +
                "\n" + BAR;
    }


    private static final String GOTIT = BAR + "\n Got it. task added" + "\n     ";
    private static String getTaskCountMessage() {
        return "\n " + "Now you have " + taskCount + " tasks in the list\n" + BAR;
    }

    private static void addTodo(String input) {
        tasks[taskCount] = new Todo(input);
        taskCount++;
        System.out.println(GOTIT + tasks[taskCount - 1].toString() + getTaskCountMessage());
    }

    private static void addDeadline(String input) throws GepitException {
        String[] parts = input.split(" /by ", 2);

        if (parts.length < 2
                || parts[0].isBlank()
                || parts[1].isBlank()) {
            throw new GepitException(
                    "A deadline should look like:"
                    + "\ndeadline return book /by Sunday"
            );
        }

        String descr = parts[0];
        String by = parts[1];
        tasks[taskCount] = new Deadline(descr, by);
        taskCount++;
        System.out.println(GOTIT + tasks[taskCount - 1].toString() + getTaskCountMessage());
    }

    private static void addEvent(String input) throws GepitException {
        String[] parts = input.split(" /from ", 2);

        if (parts.length < 2
                || parts[0].isBlank()) {
            throw new GepitException(
                    "An event must have descr and /from"
            );
        }

        String descr = parts[0];
        String[] eventDuration = parts[1].split(" /to ", 2);

        if (eventDuration.length < 2
                || eventDuration[0].isBlank()
                || eventDuration[1].isBlank()) {
            throw new GepitException(
                    "An event should look like:"
                            + "\nevent meeting /from Mon 2pm /to 4pm"
            );
        }

        String start = eventDuration[0];
        String end = eventDuration[1];
        tasks[taskCount] = new Event(descr, start, end);
        taskCount++;
        System.out.println(GOTIT + tasks[taskCount - 1].toString() + getTaskCountMessage());
    }

    //to validate index Strings and return correct task array index
    private static int getTaskIndex(String input) throws GepitException {
        int taskNum;

        try {
            taskNum = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new GepitException(
                    "" + input + " isn't a valid task number"
            );
        }

        int index = taskNum - 1;
        if (index < 0 || index >= taskCount) {
            throw new GepitException(
                    "Task " + taskNum + " doesn't exist"
            );
        }

        return index;
    }
}
