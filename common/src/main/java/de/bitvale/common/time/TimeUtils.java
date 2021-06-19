package de.bitvale.common.time;


import org.apache.commons.lang.time.DurationFormatUtils;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author Patrick Bittner on 03/12/2016.
 */
public class TimeUtils {

    public static String format(Instant instant) {

        if (instant == null) {
            return "";
        }

        Duration duration = Duration.between(instant, Instant.now());

        String pattern = "";

        if (duration.toDays() > 0) {
            return DateTimeFormatter.ofPattern("d MMM").withZone(ZoneId.systemDefault()).format(instant);
        }

        if (duration.toHours() > 0) {
            pattern += "H'h'";
        }

        pattern += "m'm'";

        return DurationFormatUtils.formatDuration(duration.toMillis(), pattern, true);
    }

}
