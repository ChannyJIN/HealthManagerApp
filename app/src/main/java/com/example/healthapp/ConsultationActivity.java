package com.example.healthapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.healthapp.models.OpenAIRequest;
import com.example.healthapp.models.OpenAIResponse;
import com.example.healthapp.network.ChatGPTApi;
import com.example.healthapp.network.RetrofitClient;
import java.io.IOException;
import java.util.Collections;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsultationActivity extends AppCompatActivity {
    private Spinner exerciseSpinner;
    private TextView resultTextView;
    private ChatGPTApi chatGPTApi;
    private static final String API_KEY = "Bearer MY_APIKEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation);

        exerciseSpinner = findViewById(R.id.exerciseSpinner);
        resultTextView = findViewById(R.id.resultTextView);
        Button consultButton = findViewById(R.id.consultButton);
        ImageButton backButton = findViewById(R.id.backButton);

        chatGPTApi = RetrofitClient.getClient().create(ChatGPTApi.class);

        consultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedExercise = exerciseSpinner.getSelectedItem().toString();
                getExerciseRecommendation(selectedExercise);
            }
        });

        backButton.setOnClickListener(v -> onBackPressed());
    }

    private void getExerciseRecommendation(String exerciseType) {
        String prompt = "운동 추천: " + exerciseType + "에 대한 추천 운동을 추천해줘.";
        OpenAIRequest request = new OpenAIRequest(
                "gpt-3.5-turbo",
                Collections.singletonList(new OpenAIRequest.Message("user", prompt)),
                400
        );

        Call<OpenAIResponse> call = chatGPTApi.getExerciseRecommendation(API_KEY, request);

        call.enqueue(new Callback<OpenAIResponse>() {
            @Override
            public void onResponse(Call<OpenAIResponse> call, Response<OpenAIResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String recommendation = response.body().getChoices().get(0).getMessage().getContent();
                    resultTextView.setText(recommendation);
                } else {
                    try {
                        ResponseBody errorBody = response.errorBody();
                        if (errorBody != null) {
                            String errorResponse = errorBody.string();
                            Log.e("ConsultationActivity", "Error response: " + errorResponse);
                            if (errorResponse.contains("insufficient_quota")) {
                                resultTextView.setText("추천을 가져오는 데 실패했습니다: 할당된 API 사용량을 초과했습니다. 요금제를 업그레이드하세요.");
                            } else if (errorResponse.contains("message")) {
                                resultTextView.setText("추천을 가져오는 데 실패했습니다: " + errorResponse);
                            } else {
                                resultTextView.setText("추천을 가져오는 데 실패했습니다.");
                            }
                        } else {
                            Log.e("ConsultationActivity", "Response failed with no error body");
                            resultTextView.setText("추천을 가져오는 데 실패했습니다.");
                        }
                    } catch (IOException e) {
                        Log.e("ConsultationActivity", "Error reading error body", e);
                        resultTextView.setText("추천을 가져오는 데 실패했습니다.");
                    }
                }
            }

            @Override
            public void onFailure(Call<OpenAIResponse> call, Throwable t) {
                Log.e("ConsultationActivity", "API call failed: " + t.getMessage());
                resultTextView.setText("추천을 가져오는 데 실패했습니다: " + t.getMessage());
            }
        });
    }
}
