package com.example.todoapplication;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapplication.databinding.ItemTaskBinding;
import com.example.todoapplication.room.TaskRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TaskItemAdapter extends RecyclerView.Adapter<TaskItemAdapter.TaskViewHolder> {
    private static String TAG = "TaskItemAdapter";

    private List<Task> tasks;

    private TaskRepository taskRepository;

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public TaskItemAdapter(List<Task> tasks, TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        this.tasks = tasks;
    }
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        ItemTaskBinding binding;

        public TaskViewHolder(@NonNull ItemTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    @Override
    public int getItemCount() {
        return tasks.size();
    }

    @NonNull
    @Override
    public TaskItemAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemTaskBinding binding = ItemTaskBinding.inflate(inflater, parent, false);
        return new TaskViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.binding.titleTextView.setText(task.getTitle());
        holder.binding.radioButton.setChecked(task.isComplete());
        holder.binding.radioButton.setOnClickListener(v -> {
            boolean newStatus = !task.isComplete();
            Log.d(TAG, "onBindViewHolder: RadioButton clicked");
            task.setComplete(newStatus);
            Log.d(TAG, "onBindViewHolder: " + task.isComplete());
            taskRepository.update(task)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> notifyItemChanged(position),
                            throwable -> Log.e(TAG, "Update failed", throwable)
                    );
        });

    }
}