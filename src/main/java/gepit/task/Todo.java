package gepit.task;

public class Todo extends Task {

    public Todo(String text) {
        super(text);
    }

    @Override
    public String toDataString() {
        return "T | " + super.toDataString();
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
