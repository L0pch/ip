package gepit.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import gepit.GepitException;
import gepit.task.Deadline;
import gepit.task.Event;

public class ParserTest {

    @Test
    public void parseDeadline_validInput_returnsDeadline()
            throws GepitException {
        Deadline deadline = Parser.parseDeadline(
                "submit report /by 2026-09-10");

        assertEquals("submit report", deadline.getDescription());
        assertEquals(
                LocalDate.of(2026, 9, 10),
                deadline.getDue());
    }

    @Test
    public void parseDeadline_invalidDate_throwsException() {
        assertThrows(GepitException.class, () ->
                Parser.parseDeadline(
                        "submit report /by definitely-not-a-date"));
    }

    @Test
    public void parseDeadline_missingBy_throwsException() {
        assertThrows(GepitException.class, () ->
                Parser.parseDeadline("submit report"));
    }

    @Test
    public void parseEvent_validInput_returnsEvent()
            throws GepitException {
        Event event = Parser.parseEvent(
                "meeting /from 2026-09-10 /to 2026-09-12");

        assertEquals("meeting", event.getDescription());
        assertEquals(
                LocalDate.of(2026, 9, 10),
                event.getStart());
        assertEquals(
                LocalDate.of(2026, 9, 12),
                event.getEnd());
    }

    @Test
    public void parseEvent_endBeforeStart_throwsException() {
        assertThrows(GepitException.class, () ->
                Parser.parseEvent(
                        "meeting /from 2026-09-12 /to 2026-09-10"));
    }
}