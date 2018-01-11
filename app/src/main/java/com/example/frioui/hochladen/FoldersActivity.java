package com.example.frioui.hochladen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class FoldersActivity extends AppCompatActivity {
    String token,FolderId,FolderName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folders);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        token = this.getIntent().getExtras().getString("Token");
        FolderId = this.getIntent().getExtras().getString("FolderId");
        FolderName = this.getIntent().getExtras().getString("FolderName");
        this.setTitle(FolderName);
    }

     @Override
     public boolean onOptionsItemSelected(MenuItem item) {

         int id = item.getItemId();
         if (id == android.R.id.home) {
            this.finish();
         }

         return super.onOptionsItemSelected(item);
     }



}
