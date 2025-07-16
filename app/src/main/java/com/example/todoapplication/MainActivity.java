package com.example.todoapplication;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.todoapplication.databinding.ActivityMainBinding;
import com.example.todoapplication.fragments.AddTaskBottomSheetDialog;
import com.example.todoapplication.room.TaskRepository;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;



public class MainActivity extends AppCompatActivity implements  AddTaskBottomSheetDialog.OnTaskAddedListener{

    private final static String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private TaskRepository taskRepository;
    private TaskItemAdapter adapter;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private boolean currentShowCompleted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        taskRepository = new TaskRepository(getApplication());

        adapter = new TaskItemAdapter(new ArrayList<>(), taskRepository);

        loadTasks(currentShowCompleted);

        binding.tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.tasksRecyclerView.setAdapter(adapter);

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Pending"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Completed"));

        binding.addTaskButton.setOnClickListener((view -> {
            AddTaskBottomSheetDialog dialog = new AddTaskBottomSheetDialog();
            dialog.setOnTaskAddedListener(this);
            dialog.show(getSupportFragmentManager(), AddTaskBottomSheetDialog.TAG);
        }));


        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentShowCompleted = tab.getPosition() == 1;
                loadTasks(currentShowCompleted);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    @Override
    protected void onResume() {
        loadTasks(currentShowCompleted);
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    private void loadTasks(boolean showCompleted) {
        compositeDisposable.add(
                taskRepository.getAllTasks()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(tasks -> filterTasks(showCompleted, tasks))
                        .subscribe(
                                filteredTasks -> adapter.setTasks(filteredTasks),
                                throwable -> Log.e(TAG, "Error loading tasks", throwable)
                        )
        );
    }


    private List<Task> filterTasks(boolean showCompleted, List<Task> tasks) {
        return tasks.stream()
                .filter(task -> task.isComplete() == showCompleted)
                .collect(Collectors.toList());
    }


    @Override
    public void onTaskAdded() {
        loadTasks(currentShowCompleted);
    }
}