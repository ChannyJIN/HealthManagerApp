package com.example.healthapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private LinearLayout dynamicList;
    private boolean isListVisible = false;
    private ImageButton menu;
    private LinearLayout goalsContainer;
    private static final String FILE_NAME = "goals.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dynamicList = findViewById(R.id.dynamicList);

        menu = findViewById(R.id.list);
        menu.setOnClickListener(v -> {
            if(isListVisible){
                dynamicList.removeAllViews();
            }
            else {
                addMenuItems();
            }
            isListVisible = !isListVisible;
        });


        goalsContainer = findViewById(R.id.goalsContainer);
        Button addGoalButton = findViewById(R.id.addGoalButton);

        addGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewGoal("", false);
            }
        });
        Button removeGoalButton = findViewById(R.id.removeGoalButton);
        removeGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeLastGoal();
            }
        });

        loadGoals();


    }

    private void addMenuItems() {
        String[] menuItems = {"캘린더", "식단관리", "운동관리", "운동 상담"};

        for (String item : menuItems) {
            TextView textView = new TextView(this);
            textView.setText(item);
            textView.setTextSize(18);
            textView.setPadding(16, 16, 16, 16);
            textView.setOnClickListener(v -> {
                switch (item) {
                    case "캘린더":
                        startActivity(new Intent(MainActivity.this, CalendarActivity.class));
                        isListVisible = !isListVisible;
                        dynamicList.removeAllViews();
                        break;
                    case "식단관리":
                        startActivity(new Intent(MainActivity.this, DietManagementActivity.class));
                        isListVisible = !isListVisible;
                        dynamicList.removeAllViews();
                        break;
                    case "운동관리":
                        startActivity(new Intent(MainActivity.this, ExerciseManagementActivity.class));
                        isListVisible = !isListVisible;
                        dynamicList.removeAllViews();
                        break;
                    case "운동 상담":
                        startActivity(new Intent(MainActivity.this, ConsultationActivity.class));
                        break;
                }
            });
            dynamicList.addView(textView);
        }
    }

    private void addNewGoal(String goalText, boolean isChecked) {
        LinearLayout goalLayout = new LinearLayout(this);
        goalLayout.setOrientation(LinearLayout.HORIZONTAL);
        goalLayout.setGravity(View.FOCUS_RIGHT);  // 오른쪽 정렬

        EditText editText = new EditText(this);
        editText.setHint("목표를 입력하세요");
        editText.setText(goalText);
        editText.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        CheckBox checkBox = new CheckBox(this);
        checkBox.setChecked(isChecked);

        goalLayout.addView(editText);
        goalLayout.addView(checkBox);

        goalsContainer.addView(goalLayout);

        saveGoals(); // 목표가 추가될 때마다 저장
    }

    private void removeLastGoal() {
        int childCount = goalsContainer.getChildCount();
        if (childCount > 0) {
            goalsContainer.removeViewAt(childCount - 1);
            saveGoals(); // Save goals whenever a goal is removed
        }
    }

    private void saveGoals() {
        ArrayList<String> goals = new ArrayList<>();
        for (int i = 0; i < goalsContainer.getChildCount(); i++) {
            LinearLayout goalLayout = (LinearLayout) goalsContainer.getChildAt(i);
            CheckBox checkBox = (CheckBox) goalLayout.getChildAt(1);
            EditText editText = (EditText) goalLayout.getChildAt(0);
            String goal = checkBox.isChecked() + ";" + editText.getText().toString();
            goals.add(goal);
        }

        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
            for (String goal : goals) {
                writer.write(goal);
                writer.newLine();
            }
            writer.close();
            Log.d("MainActivity", "Goals saved: " + goals);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadGoals() {
        try {
            FileInputStream fis = openFileInput(FILE_NAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                boolean isChecked = Boolean.parseBoolean(parts[0]);
                String goalText = parts[1];
                addNewGoalWithoutSaving(goalText, isChecked); // 목표를 불러올 때 저장하지 않도록
            }
            reader.close();
            Log.d("MainActivity", "Goals loaded from file");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addNewGoalWithoutSaving(String goalText, boolean isChecked) {
        LinearLayout goalLayout = new LinearLayout(this);
        goalLayout.setOrientation(LinearLayout.HORIZONTAL);
        goalLayout.setGravity(View.FOCUS_RIGHT);  // 오른쪽 정렬

        EditText editText = new EditText(this);
        editText.setHint("목표를 입력하세요");
        editText.setText(goalText);
        editText.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        CheckBox checkBox = new CheckBox(this);
        checkBox.setChecked(isChecked);

        goalLayout.addView(editText);
        goalLayout.addView(checkBox);

        goalsContainer.addView(goalLayout);
    }
}