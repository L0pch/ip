public class Event extends Task{
    private String start;
    private String end;
    public Event(String text, String start, String end) {
        super(text);
        this.start = start;
        this.end = end;
    }

    @Override
    public String toDataString() {
        return "E | " + super.toDataString() +
                " | " + this.start + " | " + this.end;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() +
                " (from: " + this.start + " to: " + this.end + ")";
    }
}
