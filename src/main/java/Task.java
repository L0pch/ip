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

    @Override
    public String toString() {
        return getStatusIcon() + " " + this.descr;
    }
}
