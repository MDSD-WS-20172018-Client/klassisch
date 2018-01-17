package com.example.frioui.hochladen;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.app.Activity;
import android.util.Log;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.Manifest;
import android.database.Cursor;



public class FoldersActivity extends AppCompatActivity implements  EasyPermissions.PermissionCallbacks {

    private static final String TAG = FoldersActivity.class.getSimpleName();
    Uri uri;
    List<String> FilesName = new ArrayList<String>();
    List<String> FilessID = new ArrayList<String>();
    List<String> FilessUrl = new ArrayList<String>();
    ListView mListView;
    String token, FolderId, FolderName , FolderMain;
    ProgressDialog progressDialog;
    private Service uploadService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        token = this.getIntent().getExtras().getString("Token");
        FolderMain=this.getIntent().getExtras().getString("FolderMain");
        FolderId = this.getIntent().getExtras().getString("FolderId");
        FolderName = this.getIntent().getExtras().getString("FolderName");

        this.setTitle(FolderName);

        mListView = (ListView) findViewById(R.id.listFile);
        GetListFiles();

        mListView.setClickable(true);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
             FileView(position);

            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
    }


    public void FileView( int position){

final int pos =position;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(FilesName.get(position))
                .setPositiveButton("löschen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DeleteFile(FilessID.get(pos));

                    }
                });
        builder.setNegativeButton("öffnen", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String url =FilessUrl.get(pos);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
               dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();




    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
       if(id==R.id.home){
           Intent FolderIntent = new Intent(FoldersActivity.this, MainActivity.class);
           FolderIntent.putExtra("FolderId",FolderMain);
           FolderIntent.putExtra("Token", token);
           startActivityForResult(FolderIntent, 0);
            return true;
        }else if (id == R.id.action_delete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("möchten Sie der Folder "+FolderName +" löschen")
                    .setPositiveButton("löschen", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            DeleteFolders();
                        }
                    });
            builder.setNegativeButton("abbrechen", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }else if (id == R.id.action_foto_machen) {
           TakeImage();

       } else if (id == R.id.action_Datei_wählen) {
            openImageFromGallery();
        }

        return super.onOptionsItemSelected(item);
    }

    private void TakeImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,200);
    }

    private void openImageFromGallery() {
       Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
       openGalleryIntent.setType("image/*");
        startActivityForResult(openGalleryIntent, 100);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            Filehochladen(uri);


        }
            if (requestCode == 200 && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Object xx = data.getData();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Uri tempUri = getImageUri(imageBitmap);
                Filehochladen(tempUri);
                  }

    }


    // Convert Image to Uri
    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(
                getApplicationContext().getContentResolver(), inImage,
                "Title", null);
        return Uri.parse(path);
    }

    // Rest service delete Folders
    public void DeleteFolders() {

        AsyncHttpClient client = new AsyncHttpClient();
        try {
            String Url ="http://34.238.158.85:8080/api/"+ this.token+"/"+this.FolderId;
          //  client.delete(this,)
            client.delete(this, Url, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, final String content) {
                    try {
                      //  JSONObject jsonObj=new JSONObject(content);

                        if (statusCode == 200) {
                            Toast.makeText(getApplicationContext(), "Folder  gelöscht", Toast.LENGTH_LONG).show();
                            Intent FolderIntent = new Intent(FoldersActivity.this, MainActivity.class);
                            FolderIntent.putExtra("FolderId",FolderMain);
                            FolderIntent.putExtra("Token", token);
                            startActivityForResult(FolderIntent, 0);
                            FoldersActivity.this.finish();
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

    // Rest service delete File
    public void DeleteFile(String IDfile) {

        AsyncHttpClient client = new AsyncHttpClient();
        try {
            String Url ="http://34.238.158.85:8080/api/"+ this.token+"/"+this.FolderId+"/files/"+IDfile;
            //  client.delete(this,)
            client.delete(this, Url, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, final String content) {
                    try {

                        if (statusCode == 200) {
                            Toast.makeText(getApplicationContext(), "File  gelöscht", Toast.LENGTH_LONG).show();
                            GetListFiles();
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
                    Toast.makeText(getApplicationContext(), content+"Requested resource not found ", Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }






    // Rest service get List von Files
    public void GetListFiles() {

        AsyncHttpClient client = new AsyncHttpClient();
        try {
            String Url ="http://34.238.158.85:8080/api/"+ this.token+"/"+this.FolderId;
            client.get( Url,  null, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, final String content) {
                    try {
                        JSONObject jsonObj=new JSONObject(content);
                         FilesName = new ArrayList<String>();
                         FilessID = new ArrayList<String>();
                         FilessUrl = new ArrayList<String>();
                        if (statusCode == 200) {
                            JSONArray Folderslist= (JSONArray) jsonObj.get("files");
                            for(int i=0;i<Folderslist.length();i++)
                            {
                                JSONObject Folder= (JSONObject) Folderslist.get(i);
                                FilesName.add(Folder.getString("name"));
                                FilessID.add(Folder.getString("id"));
                                FilessUrl.add(Folder.getString("url"));
                            }
                            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(FoldersActivity.this,
                                    android.R.layout.simple_list_item_1, FilesName);
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








    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, FoldersActivity.this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

       if (uri != null) {
            progressDialog.show();
        }
        String filePath = getRealPathFromURIPath(uri, FoldersActivity.this);
        File file = new File(filePath);
        RequestBody mFile = RequestBody.create(MediaType.parse("**"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        Call<UploadObject> fileUpload = uploadService.uploadSingleFile(fileToUpload, filename);

        fileUpload.enqueue(new Callback<UploadObject>() {
            @Override
            public void onResponse(Call<UploadObject> call, Response<UploadObject> response) {
                progressDialog.dismiss();
                Toast.makeText(FoldersActivity.this, "Success " + response.message(), Toast.LENGTH_LONG).show();
                Toast.makeText(FoldersActivity.this, "Success " + response.body().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<UploadObject> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, "Error " + t.getMessage());
            }
        });

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "Permission has been denied");
    }


    // Rest service Hochladen File

    public void Filehochladen(Uri uriContent)
    {
        String Url ="http://34.238.158.85:8080/api/"+ this.token+"/"+this.FolderId+"/";
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        uploadService = new Retrofit.Builder()
                .baseUrl(Url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Service.class);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");

        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            progressDialog.show();
          String filePath = getRealPathFromURIPath(uriContent, FoldersActivity.this);
          //  String filePath = uriContent.getPath();
            File file = new File(filePath);
            Log.d(TAG, "filePath=" + filePath);
            //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
            RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

            Call<UploadObject> fileUpload = uploadService.uploadSingleFile(fileToUpload, filename);
            fileUpload.enqueue(new Callback<UploadObject>() {
                @Override
                public void onResponse(Call<UploadObject> call, Response<UploadObject> response) {
                    progressDialog.dismiss();
                    Toast.makeText(FoldersActivity.this, "Datei wird hochgeladen", Toast.LENGTH_LONG).show();
                    GetListFiles();
                  }

                @Override
                public void onFailure(Call<UploadObject> call, Throwable t) {
                    progressDialog.dismiss();

                    Log.d(TAG, "Error " + t.getMessage());
                }

            });
        }


    }

    //  Gibt path von URI zurück

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }



}