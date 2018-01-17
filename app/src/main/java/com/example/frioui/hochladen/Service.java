package com.example.frioui.hochladen;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;


interface Service {

    @Multipart
    @POST("files")
    Call<UploadObject> uploadSingleFile(@Part MultipartBody.Part file, @Part("name") RequestBody name);



}
