package com.example.lab_emailcheck;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EmailValidationService {
    @GET("check")
    Call<EmailValidationResponse> validateEmail(
            @Query("access_key") String accessKey,
            @Query("email") String email,
            @Query("smtp") int smtp,
            @Query("format") int format
    );
}

