package de.bitvale.anjunar.shared.system;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class CreatedForm {

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm", timezone = "UTC")
    private LocalDateTime start;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm", timezone = "UTC")
    private LocalDateTime end;

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}
