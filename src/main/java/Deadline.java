public class Deadline extends Task{
    private String due;
    public Deadline(String text, String due) {
        super(text);
        this.due = due;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.due + ")";
    }
}
