package com.example.sytemy_mobilne_3;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskListFragment extends Fragment {
    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    public static final String KEY_EXTRA_TASK_ID = "tasklistfragment.task_id";
    private boolean subtitlevisable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        recyclerView = view.findViewById(R.id.task_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }
    private void updateView() {
        TaskStorage taskStorage = TaskStorage.getInstance();
        List<Task> tasks = taskStorage.getTasks();

        if (adapter == null) {
            adapter = new TaskAdapter(tasks);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        //updateSubtitle();
    }

    private class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView nameTextView, dateTextView;
        private ImageView iconImageView;
        private CheckBox checkBox;
        private Task task;

        public TaskHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_task, parent, false));
            itemView.setOnClickListener(this);
            checkBox=itemView.findViewById(R.id.checkBox);
            iconImageView= itemView.findViewById(R.id.imageView);
            nameTextView = itemView.findViewById(R.id.task_item_name);
            dateTextView = itemView.findViewById(R.id.task_item_date);
        }

        public void bind(Task task) {
            this.task = task;
            String taskName = task.getName();
            if (task.isDone()) {
                nameTextView.setText(Html.fromHtml("<s>" + (taskName) + "</s>"));
            } else {
                nameTextView.setText(taskName);
            }
            dateTextView.setText(task.getDate().toString());
            checkBox.setChecked(task.isDone());

            if( task.getCategory().equals(Category.HOME)){
                iconImageView.setImageResource(R.drawable.ic_house);
            } else {
                iconImageView.setImageResource(R.drawable.ic_university);
            }
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra(KEY_EXTRA_TASK_ID, task.getId());
            startActivity(intent);
        }
        public CheckBox getCheckBox() { return checkBox; }
    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {
        private List<Task> tasks;

        public TaskAdapter(List<Task> tasks) {
            this.tasks = tasks;
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new TaskHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            Task task = tasks.get(position);
            holder.bind(task);
            CheckBox checkBox=holder.getCheckBox();
            checkBox.setChecked(task.isDone());

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                task.setDone(isChecked);
            });
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_task_menu, menu);
        MenuItem subtitleitem = menu.findItem(R.id.show_subtitle);
        if(subtitlevisable){
            subtitleitem.setTitle(R.string.hide_subtitle);

        }else{
            subtitleitem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId()== R.id.new_task) {
            Task task = new Task();
            TaskStorage.getInstance().addTask(task);
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra(TaskListFragment.KEY_EXTRA_TASK_ID, task.getId());
            startActivity(intent);
            return true;
        }else if(item.getItemId() == R.id.show_subtitle){
            subtitlevisable = !subtitlevisable;
            getActivity().invalidateOptionsMenu();
            updateSubtitle();
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }

    }
    public void updateSubtitle(){
        TaskStorage taskStorage=TaskStorage.getInstance();
        List<Task>tasks=taskStorage.getTasks();
        int count=0;
        for(Task task:tasks){
            if(!task.isDone()){
                count++;
            }
        }
        String subtitle=getString(R.string.subtitle_format,count);
        if(!subtitlevisable){
            subtitle=null;
        }
        AppCompatActivity appCompatActivity=(AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setSubtitle(subtitle);
    }
}
