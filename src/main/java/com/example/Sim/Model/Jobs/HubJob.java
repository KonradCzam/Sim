package com.example.Sim.Model.Jobs;

import java.util.Arrays;

public class HubJob extends Job {


    public HubJob() {
        name = "Brothel";
        tasks = Arrays.asList(Task.FREE_TIME, Task.BROTHEL_STRIP, Task.BROTHEL_STREET_WHORE, Task.BROTHEL_WHORE, Task.BROTHEL_BARMAID, Task.BROTHEL_WAITRESS);
    }

}
