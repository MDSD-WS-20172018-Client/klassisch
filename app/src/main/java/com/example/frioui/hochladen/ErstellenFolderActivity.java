package com.example.frioui.hochladen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import static android.app.PendingIntent.getActivity;

public class ErstellenFolderActivity extends AppCompatActivity implements View.OnClickListener {


    Button bErstellen, bAbbrechen;
    EditText etFolderName;
    boolean Result;
    String token,FolderId,FolderName;
    private MainActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erstellen_folder);
        etFolderName = (EditText) findViewById(R.id.etFolderName);
        bErstellen = (Button) findViewById(R.id.bErstellen);
        bAbbrechen = (Button) findViewById(R.id.bAbbrechen);

        bErstellen.setOnClickListener(this);
        bAbbrechen.setOnClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        token = this.getIntent().getExtras().getString("Token");
        FolderId = this.getIntent().getExtras().getString("FolderId");


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bErstellen:

                String FolderName = etFolderName.getText().toString();
                if(!FolderName.equals("")) {
                  FolderErstellen(FolderName);
                        Intent FolderIntent = new Intent(this, MainActivity.class);
                        FolderIntent.putExtra("FolderId", FolderId);
                        FolderIntent.putExtra("Token", token);
                        startActivityForResult(FolderIntent, 0);
                        this.finish();
                    break;
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Bitte geben Sie das vollständige Feld ein",Toast.LENGTH_SHORT).show();
                }



            case R.id.bAbbrechen:
                this.finish();
                break;

        }

    }


    public void FolderErstellen(String FolderName) {

        RequestParams params = new RequestParams();
        params.put("name", FolderName);
        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("name", FolderName);
            StringEntity entity = new StringEntity(jsonParams.toString());
            entity.setContentType("application/json");
            String Url ="http://34.238.158.85:8080/api/"+ this.token+"/"+this.FolderId;
            //  client.post("http://34.238.158.85:8080/api/users/login",params,new AsyncHttpResponseHandler(){
            client.post(this, Url,  entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                // public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                public void onSuccess(int statusCode, final String content) {


                        if (statusCode == 200) {

                            Toast.makeText(getApplicationContext(), "Folder Erstellung erfolgreich", Toast.LENGTH_SHORT).show();

                        }
                        // error message
                        else {
                            Toast.makeText(getApplicationContext(), content, Toast.LENGTH_LONG).show();
                        }

                }
                @Override
                // public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                public void onFailure(Throwable error, final String content) {
                    Toast.makeText(getApplicationContext(), content+"Requested resource not found", Toast.LENGTH_LONG).show();

                }
            });

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
        }

    }





}
