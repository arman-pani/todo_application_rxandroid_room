package com.example.todoapplication.room;

import android.content.Context;
import androidx.room.*;
import androidx.room.RoomDatabase;

import com.example.todoapplication.Task;

@Database(entities = {Task.class}, version = 1)
public abstract class TaskDatabase extends RoomDatabase {

    private static TaskDatabase instance;

    public abstract TaskDao taskDao();

    public static synchronized TaskDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            TaskDatabase.class, "task_database").build();
        }
        return instance;
    }
}

