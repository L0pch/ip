package gepit;

import java.util.List;

import gepit.parser.Parser;
import gepit.storage.Storage;
import gepit.task.Task;
import gepit.ui.Ui;

/**
 * Runs the Gepit chatbot and coordinates command processing.
 */
public class Gepit {
    private static final String BAR =
            "____________________________________________________________";
    private static final String GOT_IT_MESSAGE =
            BAR + "\n Got it. task added" + "\n     ";

    private final Storage storage;
    private TaskList tasks;

    /**
     * Creates a Gepit chatbot and loads previously saved tasks.
     */
    public Gepit() {
        storage = new Storage("data/gepit.txt");

        try {
            tasks = new TaskList(storage.load());
        } catch (GepitException e) {
            tasks = new TaskList();
        }
    }

    /**
     * Runs Gepit using the text-based user interface.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        Gepit gepit = new Gepit();
        Ui ui = new Ui();

        ui.showWelcome();

        while (ui.hasNextCommand()) {
            String input = ui.readCommand();

            if (input.equals("bye")) {
                break;
            }

            ui.showMessage(gepit.getResponse(input));
        }

        ui.showGoodbye();
    }

    /**
     * Returns Gepit's response to the specified user input.
     *
     * @param input User input to process.
     * @return Response to display to the user.
     */
    public String getResponse(String input) {
        try {
            if (input.equals("bye")) {
                return "Bye Friendo\nSee you again next time :)";
            }

            if (input.equals("list")) {
                return getTaskListMessage();
            }

            String command = Parser.getCommand(input);

            if (command.equals("find")) {
                String keyword = Parser.getArgument(input);
                return getTaskMatchesMessage(tasks.find(keyword));
            }

            if (command.equals("delete")) {
                String argument = Parser.getArgument(input);
                int taskNumber = Parser.parseTaskNumber(argument);
                int index = getTaskIndex(taskNumber);
                return deleteTask(index);
            }

            if (command.equals("mark")) {
                String argument = Parser.getArgument(input);
                int taskNumber = Parser.parseTaskNumber(argument);
                int index = getTaskIndex(taskNumber);
                return markTask(tasks.get(index));
            }

            if (command.equals("unmark")) {
                String argument = Parser.getArgument(input);
                int taskNumber = Parser.parseTaskNumber(argument);
                int index = getTaskIndex(taskNumber);
                return unmarkTask(tasks.get(index));
            }

            if (command.equals("todo")) {
                String argument = Parser.getArgument(input);
                Task task = Parser.parseTodo(argument);
                return addTask(task);
            }

            if (command.equals("deadline")) {
                String argument = Parser.getArgument(input);
                Task task = Parser.parseDeadline(argument);
                return addTask(task);
            }

            if (command.equals("event")) {
                String argument = Parser.getArgument(input);
                Task task = Parser.parseEvent(argument);
                return addTask(task);
            }

            throw new GepitException(
                    "Sorry m8, IDK what " + command + " is supposed to mean");

        } catch (GepitException e) {
            return e.getMessage();
        }
    }

    private String getTaskListMessage() {
        if (tasks.size() == 0) {
            return "You don't have any tasks yet.";
        }

        StringBuilder taskOutput = new StringBuilder("Here are your tasks:");

        for (int i = 0; i < tasks.size(); i++) {
            taskOutput.append("\n")
                    .append(i + 1)
                    .append(". ")
                    .append(tasks.get(i));
        }

        return taskOutput.toString();
    }

    private String getTaskMatchesMessage(List<Task> matchingTasks) {
        if (matchingTasks.isEmpty()) {
            return "I couldn't find any matching tasks.";
        }

        StringBuilder output =
                new StringBuilder("Here are the matching tasks in your list:");

        for (int i = 0; i < matchingTasks.size(); i++) {
            output.append("\n")
                    .append(i + 1)
                    .append(". ")
                    .append(matchingTasks.get(i));
        }

        return output.toString();
    }

    private String deleteTask(int index) throws GepitException {
        Task task = tasks.remove(index);
        storage.save(tasks);

        return "I've done it boss. Deleted this guy:"
                + "\n" + task
                + getTaskCountMessage();
    }

    private String markTask(Task task) throws GepitException {
        task.markDone();
        storage.save(tasks);

        return "Gr8 job it's done mate"
                + "\n" + task;
    }

    private String unmarkTask(Task task) throws GepitException {
        task.markNotDone();
        storage.save(tasks);

        return "Get to it soon bruv"
                + "\n" + task;
    }

    private String getTaskCountMessage() {
        return "\nNow you have " + tasks.size() + " tasks in the list";
    }

    private String addTask(Task task) throws GepitException {
        tasks.add(task);
        storage.save(tasks);

        return "Got it. Task added:"
                + "\n" + task
                + getTaskCountMessage();
    }

    private int getTaskIndex(int taskNumber) throws GepitException {
        int index = taskNumber - 1;

        if (index < 0 || index >= tasks.size()) {
            throw new GepitException(
                    "Task " + taskNumber + " doesn't exist");
        }

        return index;
    }
}
