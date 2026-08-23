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

    private static final String[] tasks = new String[100];
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

            if (input.equals("bye")) {
                return;
            }

            if (input.equals("list")) {
                String taskOutput = "";
                for (int i = 0; i < taskCount; i++) {
                    String task = tasks[i];
                    taskOutput = taskOutput + "\n" + (i + 1) + ". " + task;
                }
                System.out.println(BAR + taskOutput + "\n" + BAR);
                continue;
            }

            tasks[taskCount] = input;
            taskCount++;

            String output = BAR + "\n" +
                    "added: " + input +
                    "\n" + BAR;

            System.out.println(output);
        }
    }
}
