package com.newsgroupmanagement.util;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeAgo {

    public static String toTimeAgo(LocalDateTime createdAt) {
        Duration duration = Duration.between(createdAt, LocalDateTime.now());

        if (duration.toMinutes() < 1) return "Just now";
        if (duration.toHours() < 1) return duration.toMinutes() + "m ago";
        if (duration.toDays() < 1) return duration.toHours() + "h ago";
        return duration.toDays() + "d ago";
    }
}
