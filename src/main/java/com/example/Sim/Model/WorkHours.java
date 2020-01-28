package com.example.Sim.Model;

import lombok.Getter;
import org.joda.time.*;

@Getter
public class WorkHours {
    LocalTime startTime;
    LocalTime endTime;
    Duration workTime;

    public WorkHours(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        workTime = calcWorkTime();
    }

    private Duration calcWorkTime() {
        long diffInMillis = endTime.getMillisOfDay() - startTime.getMillisOfDay();
        return new Duration(diffInMillis);
    }

}
