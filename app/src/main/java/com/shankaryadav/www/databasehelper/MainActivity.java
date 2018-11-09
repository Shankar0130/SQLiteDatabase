package com.shankaryadav.www.databasehelper;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DbHelper dbHelper;
    ListView listView;
    private static ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView =  findViewById(R.id.listView);
        dbHelper = new DbHelper(this);
        loadTask();
    }

    public void loadTask(){
        ArrayList<String> task;
        task = dbHelper.getTaskList();

        if (mAdapter == null){
            mAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.row,R.id.task,task);
            listView.setAdapter(mAdapter);
        }else {
            mAdapter.clear();
            mAdapter.addAll(task);
            mAdapter.notifyDataSetChanged();
        }

    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.addTask:
                final EditText editText = new EditText(this);
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle("Add new Task")
                        .setMessage("enter the task")
                        .setView(editText)
                        .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String task = String.valueOf(editText.getText());
                                dbHelper.insertNewTask(task);

                               loadTask();
                            }
                        })
                        .setNegativeButton("CANCEL",null)
                        .create();
                alertDialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void removeTask(View view) {

        try{
            int index = listView.getPositionForView(view);
            index++;
            String task = mAdapter.getItem(index);
            dbHelper.deletetask(task);
            loadTask();
        }catch (Exception e){
            Toast.makeText(this, "Exception " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.delete:
                removeTask(view);
                break;
        }
    }
}
