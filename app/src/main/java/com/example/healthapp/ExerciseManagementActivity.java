package com.example.healthapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class ExerciseManagementActivity extends AppCompatActivity {
    private LinearLayout exerciseContainer;
    private static final String FILE_NAME = "exercises.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_management);

        exerciseContainer = findViewById(R.id.exerciseContainer);
        Button addExerciseButton = findViewById(R.id.addExerciseButton);
        Button removeExerciseButton = findViewById(R.id.removeExerciseButton);
        ImageButton backButton = findViewById(R.id.backButton);

        addExerciseButton.setOnClickListener(v -> addNewExercise("", "", "", false));
        removeExerciseButton.setOnClickListener(v -> removeLastExercise());
        backButton.setOnClickListener(v -> onBackPressed());

        loadExercises();
    }

    private void addNewExercise(String exerciseName, String sets, String reps, boolean isChecked) {
        LinearLayout exerciseLayout = new LinearLayout(this);
        exerciseLayout.setOrientation(LinearLayout.HORIZONTAL);

        EditText exerciseNameEditText = new EditText(this);
        exerciseNameEditText.setHint("종목");
        exerciseNameEditText.setText(exerciseName);
        exerciseNameEditText.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        EditText setsEditText = new EditText(this);
        setsEditText.setHint("세트 수");
        setsEditText.setText(sets);
        setsEditText.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        EditText repsEditText = new EditText(this);
        repsEditText.setHint("횟수");
        repsEditText.setText(reps);
        repsEditText.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        CheckBox checkBox = new CheckBox(this);
        checkBox.setChecked(isChecked);

        exerciseLayout.addView(exerciseNameEditText);
        exerciseLayout.addView(setsEditText);
        exerciseLayout.addView(repsEditText);
        exerciseLayout.addView(checkBox);

        exerciseContainer.addView(exerciseLayout);

        saveExercises(); // 저장
    }

    private void removeLastExercise() {
        int childCount = exerciseContainer.getChildCount();
        if (childCount > 0) {
            exerciseContainer.removeViewAt(childCount - 1);
            saveExercises(); // 저장
        }
    }

    private void saveExercises() {
        ArrayList<String> exercises = new ArrayList<>();
        for (int i = 0; i < exerciseContainer.getChildCount(); i++) {
            LinearLayout exerciseLayout = (LinearLayout) exerciseContainer.getChildAt(i);
            EditText exerciseNameEditText = (EditText) exerciseLayout.getChildAt(0);
            EditText setsEditText = (EditText) exerciseLayout.getChildAt(1);
            EditText repsEditText = (EditText) exerciseLayout.getChildAt(2);
            CheckBox checkBox = (CheckBox) exerciseLayout.getChildAt(3);
            String exercise = exerciseNameEditText.getText().toString() + ";" + setsEditText.getText().toString() + ";" + repsEditText.getText().toString() + ";" + checkBox.isChecked();
            exercises.add(exercise);
        }

        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
            for (String exercise : exercises) {
                writer.write(exercise);
                writer.newLine();
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadExercises() {
        try {
            FileInputStream fis = openFileInput(FILE_NAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                String exerciseName = parts[0];
                String sets = parts[1];
                String reps = parts[2];
                boolean isChecked = Boolean.parseBoolean(parts[3]);
                addNewExerciseWithoutSaving(exerciseName, sets, reps, isChecked);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addNewExerciseWithoutSaving(String exerciseName, String sets, String reps, boolean isChecked) {
        LinearLayout exerciseLayout = new LinearLayout(this);
        exerciseLayout.setOrientation(LinearLayout.HORIZONTAL);

        EditText exerciseNameEditText = new EditText(this);
        exerciseNameEditText.setHint("종목");
        exerciseNameEditText.setText(exerciseName);
        exerciseNameEditText.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        EditText setsEditText = new EditText(this);
        setsEditText.setHint("세트 수");
        setsEditText.setText(sets);
        setsEditText.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        EditText repsEditText = new EditText(this);
        repsEditText.setHint("횟수");
        repsEditText.setText(reps);
        repsEditText.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        CheckBox checkBox = new CheckBox(this);
        checkBox.setChecked(isChecked);

        exerciseLayout.addView(exerciseNameEditText);
        exerciseLayout.addView(setsEditText);
        exerciseLayout.addView(repsEditText);
        exerciseLayout.addView(checkBox);

        exerciseContainer.addView(exerciseLayout);
    }
}
