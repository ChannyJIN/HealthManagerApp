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

public class DietManagementActivity extends AppCompatActivity {
    private LinearLayout dietContainer;
    private static final String FILE_NAME = "diet.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_management);

        dietContainer = findViewById(R.id.dietContainer);
        Button addDietButton = findViewById(R.id.addDietButton);
        Button removeDietButton = findViewById(R.id.removeDietButton);
        ImageButton backButton = findViewById(R.id.backButton);

        addDietButton.setOnClickListener(v -> addNewDiet("", "", "", false));
        removeDietButton.setOnClickListener(v -> removeLastDiet());
        backButton.setOnClickListener(v -> onBackPressed());

        loadDiet();
    }

    private void addNewDiet(String time, String food, String amount, boolean isChecked) {
        LinearLayout dietLayout = new LinearLayout(this);
        dietLayout.setOrientation(LinearLayout.HORIZONTAL);

        EditText timeEditText = new EditText(this);
        timeEditText.setHint("시간대");
        timeEditText.setText(time);
        timeEditText.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        EditText foodEditText = new EditText(this);
        foodEditText.setHint("음식 이름");
        foodEditText.setText(food);
        foodEditText.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        EditText amountEditText = new EditText(this);
        amountEditText.setHint("음식 양");
        amountEditText.setText(amount);
        amountEditText.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        CheckBox checkBox = new CheckBox(this);
        checkBox.setChecked(isChecked);

        dietLayout.addView(timeEditText);
        dietLayout.addView(foodEditText);
        dietLayout.addView(amountEditText);
        dietLayout.addView(checkBox);

        dietContainer.addView(dietLayout);

        saveDiet(); // 저장
    }

    private void removeLastDiet() {
        int childCount = dietContainer.getChildCount();
        if (childCount > 0) {
            dietContainer.removeViewAt(childCount - 1);
            saveDiet(); // 저장
        }
    }

    private void saveDiet() {
        ArrayList<String> diets = new ArrayList<>();
        for (int i = 0; i < dietContainer.getChildCount(); i++) {
            LinearLayout dietLayout = (LinearLayout) dietContainer.getChildAt(i);
            EditText timeEditText = (EditText) dietLayout.getChildAt(0);
            EditText foodEditText = (EditText) dietLayout.getChildAt(1);
            EditText amountEditText = (EditText) dietLayout.getChildAt(2);
            CheckBox checkBox = (CheckBox) dietLayout.getChildAt(3);
            String diet = timeEditText.getText().toString() + ";" + foodEditText.getText().toString() + ";" + amountEditText.getText().toString() + ";" + checkBox.isChecked();
            diets.add(diet);
        }

        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
            for (String diet : diets) {
                writer.write(diet);
                writer.newLine();
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDiet() {
        try {
            FileInputStream fis = openFileInput(FILE_NAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                String time = parts[0];
                String food = parts[1];
                String amount = parts[2];
                boolean isChecked = Boolean.parseBoolean(parts[3]);
                addNewDietWithoutSaving(time, food, amount, isChecked);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addNewDietWithoutSaving(String time, String food, String amount, boolean isChecked) {
        LinearLayout dietLayout = new LinearLayout(this);
        dietLayout.setOrientation(LinearLayout.HORIZONTAL);

        EditText timeEditText = new EditText(this);
        timeEditText.setHint("시간");
        timeEditText.setText(time);
        timeEditText.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        EditText foodEditText = new EditText(this);
        foodEditText.setHint("음식");
        foodEditText.setText(food);
        foodEditText.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        EditText amountEditText = new EditText(this);
        amountEditText.setHint("음식 양");
        amountEditText.setText(amount);
        amountEditText.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        CheckBox checkBox = new CheckBox(this);
        checkBox.setChecked(isChecked);

        dietLayout.addView(timeEditText);
        dietLayout.addView(foodEditText);
        dietLayout.addView(amountEditText);
        dietLayout.addView(checkBox);

        dietContainer.addView(dietLayout);
    }
}
