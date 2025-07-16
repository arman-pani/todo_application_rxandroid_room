package com.example.todoapplication.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todoapplication.Task;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface TaskDao {
    @Insert
    Completable insert(Task task);

    @Delete
    Completable delete(Task task);

    @Update
    Completable update(Task task);


    @Query("SELECT * FROM tasks ORDER BY id DESC")
    Flowable<List<Task>> getAllTasks();

}
