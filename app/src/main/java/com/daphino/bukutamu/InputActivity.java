package com.daphino.bukutamu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.BitSet;

public class InputActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    public String pathSign1;
    Bitmap bitmap;
    DBHelper helper;
    EditText guest_name,company_name,meet,need;
    Button save_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Tambah Buku Tamu </font>"));

        guest_name = (EditText) findViewById(R.id.guest_name);
        company_name = (EditText) findViewById(R.id.company_name);
        meet = (EditText) findViewById(R.id.meet);
        need = (EditText) findViewById(R.id.need);
        save_button = (Button) findViewById(R.id.save_button);

        helper = new DBHelper(getApplicationContext());

        findViewById(R.id.signature_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getBaseContext(),SignatureActivity.class);
                startActivityForResult(it,123);
            }
        });

        save_button.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 123){
            if(resultCode == Activity.RESULT_OK){
                pathSign1 = data.getStringExtra("path");
                File signatureimage = new File(pathSign1);
                if(signatureimage.exists()){
                    bitmap = BitmapFactory.decodeFile(signatureimage.getAbsolutePath());
                    ImageView signature_view = (ImageView) findViewById(R.id.signature_view);
                    signature_view.setImageBitmap(bitmap);
                }
            }
        }
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
        String msg="";
        if(v == save_button){
            if(pathSign1 == ""){
                msg="Tanda tangan harus diisi.";
                return;
            }
            if(helper.insertData(guest_name.getText().toString(),company_name.getText().toString(),
                    meet.getText().toString(),need.getText().toString(),pathSign1)){
                Toast.makeText(getApplicationContext(),"Data berhasil disimpan.",Toast.LENGTH_LONG).show();
                Intent it = new Intent(getApplicationContext(),MainActivity.class);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(it);
            }
        }else{
            msg="Data gagal disimpan.";
        }
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }
}
