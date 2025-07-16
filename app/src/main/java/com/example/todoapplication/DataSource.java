package com.example.todoapplication;

import java.util.ArrayList;
import java.util.List;

public class DataSource {

    public static List<Task> createTasksList() {
        List<Task> tasks = new ArrayList<>();

        tasks.add(new Task("Take out the trash", "Before 8 PM", true, 3));
        tasks.add(new Task("Walk the dog", "30 min around the park", false, 2));
        tasks.add(new Task("Make my bed", "After waking up", true, 1));
        tasks.add(new Task("Unload the dishwasher", "Check top and bottom racks", false, 0));
        tasks.add(new Task("Make dinner", "Cook pasta or order food", true, 5));
        tasks.add(new Task("Study RxJava", "Finish the basics chapter", false, 4));
        tasks.add(new Task("Workout", "15 mins of bodyweight exercise", false, 3));
        tasks.add(new Task("Call parents", "Evening video call", true, 2));
        tasks.add(new Task("Read book", "At least 10 pages", false, 1));
        tasks.add(new Task("Clean desk", "Organize stationery and files", false, 2));

        return tasks;
    }
}
