package gepit.task;

public class Task {
    private final String description;
    private boolean isDone;

    public Task(String text) {
        this.description = text;
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

    public String getDescription() {
        return this.description;
    }

    /**
     * Returns the representation of this task used for data storage.
     *
     * @return Storage representation of this task.
     */
    public String toDataString() {
        return (isDone ? "1" : "0") +
                " | " + this.description;
    }

    @Override
    public String toString() {
        return getStatusIcon() + " " + this.description;
    }
}
