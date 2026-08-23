import java.util.Scanner;

public class Gepit {
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

            String output = "____________________________________________________________\n" +
                    input +
                    "\n____________________________________________________________";

            System.out.println(output);
        }
    }
}
