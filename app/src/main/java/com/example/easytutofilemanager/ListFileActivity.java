package com.example.easytutofilemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Stack;

public class ListFileActivity extends AppCompatActivity {
    public static Stack<Intent> stack = new Stack<Intent>();  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        TextView noFilesText = findViewById(R.id.nofiles_textview);
        ImageButton backButton = findViewById(R.id.backButton);
        ImageButton optionButton = findViewById(R.id.optionButton);

        if( stack.isEmpty() == true){
            backButton.setVisibility(View.INVISIBLE);
        }
        else
            backButton.setVisibility(View.VISIBLE);
        Intent intent = new Intent(getApplicationContext(), ListFileActivity.class);
        String path = getIntent().getStringExtra("path");
        System.out.println(path);
        File root = new File(path);
        intent.putExtra("path",path);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ListFileActivity.stack.push(intent);

        File[] filesAndFolders = root.listFiles();

        if(filesAndFolders==null || filesAndFolders.length ==0){
            noFilesText.setVisibility(View.VISIBLE);
        }
        else{
            noFilesText.setVisibility(View.INVISIBLE);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new MenuAdapter(getApplicationContext(),filesAndFolders));
        }




        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stack.pop();
                Intent intent = stack.peek();
                stack.pop();
                startActivity(intent);
                System.out.println("back is clicked");
            }
        });

        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(getApplicationContext(),v);
                popupMenu.getMenu().add("Add Folder");
                popupMenu.getMenu().add("Add Txt");

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Add Folder")){
                            AlertDialog.Builder nameDialog = new AlertDialog.Builder(ListFileActivity.this);
                            nameDialog.setTitle("Folder name:");
                            final EditText nameInput = new EditText(ListFileActivity.this);
                            nameInput.setInputType(InputType.TYPE_CLASS_TEXT);

                            nameDialog.setView(nameInput);
                            nameDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if(nameInput.getText().toString() == null)
                                        return;
                                    File file = new File(path,nameInput.getText().toString());

                                    if (!file.exists()){

                                        file.mkdir();

                                        Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_SHORT).show();
                                    }else
                                    {

                                        Toast.makeText(getApplicationContext(),"Folder Already Exists",Toast.LENGTH_SHORT).show();


                                    }
                                }
                            });

                            nameDialog.show();
                        }

                        return true;
                    }
                });

                popupMenu.show();
            }
            });
}}