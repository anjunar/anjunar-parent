package de.bitvale.anjunar.control.users;

import java.time.LocalDate;

public class BirthDateForm {

    private LocalDate start;

    private LocalDate end;

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }
}
