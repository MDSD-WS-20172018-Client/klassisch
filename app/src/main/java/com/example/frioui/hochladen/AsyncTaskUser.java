package com.example.frioui.hochladen;

/**
 * Created by Frioui on 30.12.2017.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import com.loopj.android.http.RequestParams;

import org.apache.http.entity.StringEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;







public class AsyncTaskUser  extends  AsyncTask<String,String,String> {


    private ProgressDialog pDialog;
    private Activity activity;
    private User user;

    public AsyncTaskUser(Activity activity, User user) {

        this.activity = activity;
        this.user = user;


    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(activity);
        pDialog.setMessage(activity.getResources().getString(R.string.txt_Show));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }




    @Override
    protected String doInBackground(String... strings) {

        RequestParams params = new RequestParams();
        JSONObject jsonParams = new JSONObject();

        params.add("username", user.getName());
        params.add("password", user.getPassword());
        params.setHttpEntityIsRepeatable(true);
        AsyncHttpClient client = new AsyncHttpClient();


        try {

            jsonParams.put("username", user.getName());
            jsonParams.put("password", user.getPassword());
            StringEntity entity = new StringEntity(jsonParams.toString());
            entity.setContentType("application/json");

           client.post(this.activity, strings[0],  entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onStart() {
                    super.onStart();
                }
                @Override
                public void onFinish() {
                    super.onFinish();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pDialog.dismiss();
                        }
                    });
                }
               @Override
                 public void onFailure(Throwable error, final String content) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, content, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                @Override
                public void onSuccess(int statusCode, final String content) {

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if( activity.getLocalClassName().equals("RegisterActivity")) {
                                Toast.makeText(activity, "Registrierung erfolgreich", Toast.LENGTH_SHORT).show();
                            }
                            else {

                                Toast.makeText(activity, "Anmeldung erfolgreich", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    if (statusCode == 200) {
                       if( activity.getLocalClassName().equals("RegisterActivity")) {
                           Intent intent = new Intent(activity, LoginActivity.class);
                           activity.startActivity(intent);
                       } else{
                           Intent intent = new Intent(activity, MainActivity.class);
                           activity.startActivity(intent);
                       }

                    } else {
                        Toast.makeText(activity, content, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }






}
