package com.example.healthapp.network;

import com.example.healthapp.models.OpenAIRequest;
import com.example.healthapp.models.OpenAIResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ChatGPTApi {
    @POST("chat/completions")
    Call<OpenAIResponse> getExerciseRecommendation(@Header("Authorization") String apiKey, @Body OpenAIRequest request);
}
