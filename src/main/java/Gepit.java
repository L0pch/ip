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

    private static void runChatBot() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();

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

            if (parts[0].equals("mark")) {
                int index = Integer.parseInt(parts[1]) - 1;
                System.out.println(markTask(tasks[index]));

                continue;
            }

            if (parts[0].equals("unmark")) {
                int index = Integer.parseInt(parts[1]) - 1;
                System.out.println(unmarkTask(tasks[index]));

                continue;
            }

            tasks[taskCount] = new Task(input);
            taskCount++;

            String output = BAR + "\n" +
                    "added: " + input +
                    "\n" + BAR;

            System.out.println(output);
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
}
