package com.example.todoapplication.room;

import android.app.Application;

import com.example.todoapplication.Task;

import java.util.List;
import io.reactivex.Flowable;
import io.reactivex.Completable;

public class TaskRepository {

    private TaskDao taskDao;

    public TaskRepository(Application application) {
        TaskDatabase db = TaskDatabase.getInstance(application);
        taskDao = db.taskDao();
    }

    public Completable update(Task task) {return taskDao.update(task);}

    public Completable insert(Task task) {
        return taskDao.insert(task);
    }

    public Completable delete(Task task) {
        return taskDao.delete(task);
    }

    public Flowable<List<Task>> getAllTasks() {
        return taskDao.getAllTasks();
    }
}
