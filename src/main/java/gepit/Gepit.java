package gepit;

import gepit.parser.Parser;
import gepit.storage.Storage;
import gepit.task.Task;
import gepit.ui.Ui;

public class Gepit {
    private static final Ui ui = new Ui();
    private static final String BAR =
            "____________________________________________________________";

    private static TaskList tasks = new TaskList();
    private static final Storage storage =
            new Storage("data/gepit.txt");

    public static void main(String[] args) {
        ui.showWelcome();

        try {
            tasks = new TaskList(storage.load());
        } catch (GepitException e) {
            ui.showError(e.getMessage());
            tasks = new TaskList();
        }

        runChatBot();
        ui.showGoodbye();
    }

    private static void runChatBot() {
        while (ui.hasNextCommand()) {
            String input = ui.readCommand();

            try {
                // exit
                if (input.equals("bye")) {
                    return;
                }

                //print list
                if (input.equals("list")) {
                    StringBuilder taskOutput = new StringBuilder();
                    for (int i = 0; i < tasks.size(); i++) {
                        Task task = tasks.get(i);
                        taskOutput.append("\n")
                                .append(i + 1)
                                .append(". ")
                                .append(task);
                    }
                    ui.showMessage(BAR + taskOutput + "\n" + BAR);
                    continue;
                }

                //split input
                String command = Parser.getCommand(input);

                if (command.equals("delete")) {
                    String argument = Parser.getArgument(input);
                    int taskNumber = Parser.parseTaskNumber(argument);
                    int index = getTaskIndex(taskNumber);

                    ui.showMessage(deleteTask(index));
                    continue;
                }

                if (command.equals("mark")) {
                    String argument = Parser.getArgument(input);
                    int taskNumber = Parser.parseTaskNumber(argument);
                    int index = getTaskIndex(taskNumber);

                    ui.showMessage(markTask(tasks.get(index)));
                    continue;
                }

                if (command.equals("unmark")) {
                    String argument = Parser.getArgument(input);
                    int taskNumber = Parser.parseTaskNumber(argument);
                    int index = getTaskIndex(taskNumber);

                    ui.showMessage(unmarkTask(tasks.get(index)));
                    continue;
                }

                if (command.equals("todo")) {
                    String argument = Parser.getArgument(input);
                    Task task = Parser.parseTodo(argument);

                    addTask(task);
                    continue;
                }

                if (command.equals("deadline")) {
                    String argument = Parser.getArgument(input);
                    Task task = Parser.parseDeadline(argument);

                    addTask(task);
                    continue;
                }

                if (command.equals("event")) {
                    String argument = Parser.getArgument(input);
                    Task task = Parser.parseEvent(argument);

                    addTask(task);
                    continue;
                }

                throw new GepitException(
                        "Sorry m8, IDK what " + command + " is supposed to mean"
                );

            } catch (GepitException e) {
                ui.showError(e.getMessage());
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

    private static final String GOT_IT_MESSAGE =
            BAR + "\n Got it. task added" + "\n     ";

    private static String getTaskCountMessage() {
        return "\n " + "Now you have " + tasks.size() + " tasks in the list\n" + BAR;
    }

    private static void addTask(Task task) throws GepitException {
        tasks.add(task);
        storage.save(tasks);

        ui.showMessage(
                GOT_IT_MESSAGE
                        + task
                        + getTaskCountMessage());
    }

    private static int getTaskIndex(int taskNumber) throws GepitException {
        int index = taskNumber - 1;

        if (index < 0 || index >= tasks.size()) {
            throw new GepitException(
                    "Task " + taskNumber + " doesn't exist");
        }

        return index;
    }
}
