package com.example.todoapplication.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todoapplication.Task;
import com.example.todoapplication.room.TaskRepository;
import com.example.todoapplication.databinding.FragmentAddTaskBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AddTaskBottomSheetDialog extends BottomSheetDialogFragment {

    public static String TAG = "AddTaskBottomSheetDialog";

    private FragmentAddTaskBinding binding;

    private TaskRepository taskRepository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private OnTaskAddedListener taskAddedListener;

    public interface OnTaskAddedListener {
        void onTaskAdded();
    }

    public void setOnTaskAddedListener(OnTaskAddedListener listener) {
        this.taskAddedListener = listener;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddTaskBinding.inflate(inflater, container, false);

        taskRepository = new TaskRepository(requireActivity().getApplication());

        binding.addTaskButton.setOnClickListener(view -> {
            String title = binding.titleEditText.getText().toString();
            String description = binding.descriptionEditText.getText().toString();
            if (title.isEmpty()) {
                binding.titleEditText.setError("Title cannot be empty");
                return;
            }
            if (description.isEmpty()) {
                binding.descriptionEditText.setError("Description cannot be empty");
                return;
            }

            int priority = Math.round(binding.prioritySlider.getValues().get(0));
            Task newTask = new Task(title, description, false, priority);
            compositeDisposable.add(
                    taskRepository.insert(newTask)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(() -> {
                                binding.titleEditText.setText("");
                                binding.descriptionEditText.setText("");
                                binding.prioritySlider.setValues(1f);

                                if (taskAddedListener != null) {
                                    taskAddedListener.onTaskAdded();
                                }

                                Log.d(TAG, "Task added successfully");
                                dismiss();
                            }, throwable -> {
                                Log.e(TAG, "Error adding task", throwable);
                            })
            );
        });

        binding.cancelButton.setOnClickListener(view -> dismiss());

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

}
