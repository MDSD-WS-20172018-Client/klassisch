package com.example.frioui.hochladen;



import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.widget.ListView;
import android.widget.Toast;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import android.widget.AdapterView;
import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,Serializable  {

    String token,FolderId;
    ListView mListView;
    List<String> FoldersName = new ArrayList<String>();
    List<String> FoldersID = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        token = this.getIntent().getExtras().getString("Token");
        FolderId = this.getIntent().getExtras().getString("FolderId");
        mListView = (ListView) findViewById(R.id.listView);


        GetListFolder();
        mListView.setClickable(true);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                FoldersView(position);
            }
        });

    }

 // diese methode Zeigt die Inhalt von Folder
public void FoldersView(int position)
{
    Intent FolderIntent = new Intent(this, FoldersActivity.class);
    FolderIntent.putExtra("FolderMain",FolderId);
    FolderIntent.putExtra("FolderId",FoldersID.get(position));
    FolderIntent.putExtra("Token",token);
    FolderIntent.putExtra("FolderName",FoldersName.get(position));
    FolderIntent.putExtra("Folderpatent",token);
    startActivity(FolderIntent);
}






    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.Folder_Erstellen) {
           Intent FolderIntent = new Intent(this, ErstellenFolderActivity.class);
            FolderIntent.putExtra("FolderId",FolderId);
            FolderIntent.putExtra("Token",token);
            startActivity(FolderIntent);

        } else if (id == R.id.nav_Abmelden) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            this.finish();
            startActivity(loginIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





//diese methode Zeigt list von Folder in eine ListView

    public void GetListFolder() {

        AsyncHttpClient client = new AsyncHttpClient();
        try {
            String Url ="http://34.238.158.85:8080/api/"+ this.token+"/"+this.FolderId;
            client.get( Url,  null, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, final String content) {
                    try {
                        JSONObject jsonObj=new JSONObject(content);
                        FoldersName = new ArrayList<String>();
                        FoldersID = new ArrayList<String>();
                        if (statusCode == 200) {
                            JSONArray  Folderslist= (JSONArray) jsonObj.get("subFolders");
                           for(int i=0;i<Folderslist.length();i++)
                            {
                               JSONObject Folder= (JSONObject) Folderslist.get(i);
                                FoldersName.add(Folder.getString("name"));
                                FoldersID.add(Folder.getString("id"));
                          }
                            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                                    android.R.layout.simple_list_item_1, FoldersName);
                            mListView.setAdapter(adapter);
                        }
                        // Else display error message
                        else {
                            Toast.makeText(getApplicationContext(), content, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        Toast.makeText(getApplicationContext(), e.getMessage() + "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Throwable error, final String content) {
                    Toast.makeText(getApplicationContext(), content+"Requested resource not found", Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
        }
    }




}
