package gepit.task;

public class Task {
    private final String descr;
    private boolean isDone;

    public Task(String text) {
        this.descr = text;
    }

    public void markDone() {
        this.isDone = true;
    }

    public void markNotDone() {
        this.isDone = false;
    }

    private String getStatusIcon() {
        if (isDone) {
            return "[X]";
        }
        return "[ ]";
    }

    private String getDescription() {
        return this.descr;
    }

    /**
     * Returns the String form of the gepit.task.Task for local storage.
     * Can get the necessary components of the task
     * @return description
     * @return isDone boolean as a String numeral
     */
    public String toDataString() {
        return (isDone ? "1" : "0") +
                " | " + this.descr;
    }

    @Override
    public String toString() {
        return getStatusIcon() + " " + this.descr;
    }
}
