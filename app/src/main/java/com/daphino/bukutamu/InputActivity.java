package com.daphino.bukutamu;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kyanogen.signatureview.SignatureView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.BitSet;

public class InputActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    DBHelper helper;
    EditText guest_name,company_name,meet,need;
    Button save_button;
    Uri imageUri;
    ProgressBar progressBar;
    Utils utils;
    SignatureView signatureView;
    Bitmap bmp;
    TextView resetSignature;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Tambah Buku Tamu </font>"));

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        guest_name = (EditText) findViewById(R.id.guest_name);
        company_name = (EditText) findViewById(R.id.company_name);
        meet = (EditText) findViewById(R.id.meet);
        need = (EditText) findViewById(R.id.need);
        save_button = (Button) findViewById(R.id.save_button);
        signatureView = (SignatureView)  findViewById(R.id.signature_view);
        resetSignature = (TextView) findViewById(R.id.resetSignature);

        utils = new Utils();

        storageReference = FirebaseStorage.getInstance().getReference("signatures");
        databaseReference = FirebaseDatabase.getInstance().getReference("bukutamu");

        helper = new DBHelper(getApplicationContext());

        resetSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signatureView.clearCanvas();
            }
        });

        save_button.setOnClickListener(this);
    }

    private String getBase64Signature(){
        bmp = signatureView.getSignatureBitmap();
        if(bmp != null){
            return utils.convert(bmp);
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.list_button:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v == save_button){
            uploadToFirebase();
        }
    }

    private String getFileExtension(Uri uri){
        return MimeTypeMap.getFileExtensionFromUrl(uri.toString());
    }

    private boolean uploadToSQLITE(String id){
        if(helper.insertData(id,guest_name.getText().toString(),company_name.getText().toString(),
                    meet.getText().toString(),need.getText().toString(),utils.getToday(),"-",getBase64Signature())){
                return true;
            }
        return false;
    }

    private void uploadToFirebase(){
        bmp = signatureView.getSignatureBitmap();
        if(bmp == null){
            Toast.makeText(InputActivity.this,"Tanda tangan kosong.",Toast.LENGTH_LONG).show();
            return;
        }
        imageUri = utils.getImageUri(getBaseContext(),bmp);
        Toast.makeText(InputActivity.this,"Sedang upload...",Toast.LENGTH_LONG).show();
        final String unique = System.currentTimeMillis()+"";
        StorageReference fileReference = storageReference.child(unique
                                + ".jpg");
        fileReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(0);
                            }
                        }, 500);
                        Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Tamu tamu = new Tamu();
                                tamu.setId(unique);
                                tamu.setGuest_name(guest_name.getText().toString());
                                tamu.setCompany_name(company_name.getText().toString());
                                tamu.setMeet(meet.getText().toString());
                                tamu.setNeed(need.getText().toString());
                                tamu.setArrival(utils.getToday());
                                tamu.setOut("-");
                                tamu.setSignature(uri.toString());
                                databaseReference.child(unique).setValue(tamu);
                            }
                        });
                        if(uploadToSQLITE(unique)){
                            Toast.makeText(getApplicationContext(),"Data berhasil disimpan.",Toast.LENGTH_SHORT).show();
                            Intent it = new Intent(getApplicationContext(),MainActivity.class);
                            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(it);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(InputActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressBar.setProgress((int) progress);
                    }
                });
    }
}
