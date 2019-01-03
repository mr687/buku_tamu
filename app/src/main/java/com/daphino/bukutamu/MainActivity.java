package com.daphino.bukutamu;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Tamu> listTamu;
    Cursor cursor;
    DBHelper helper;
    Toolbar toolbar;
    RecyclerAdapterTamu adapterTamu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Daftar Tamu </font>"));

        listTamu = new ArrayList<Tamu>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        helper = new DBHelper(getApplicationContext());

        refreshList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_button:
                Intent it = new Intent(MainActivity.this,InputActivity.class);
                startActivity(it);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void refreshList(){
        try{
            cursor = helper.getAllData();
            if(cursor != null){
                if(cursor.moveToFirst()){
                    do{
                        Tamu tamu = new Tamu();
                        tamu.setGuest_name(cursor.getString(0));
                        tamu.setCompany_name(cursor.getString(1));
                        tamu.setMeet(cursor.getString(2));
                        tamu.setNeed(cursor.getString(3));
                        tamu.setSignature((cursor.getString(4) != null) ? cursor.getString(4) : "");

                        listTamu.add(tamu);
                    }while (cursor.moveToNext());

                }

                if(listTamu.size() > 0){
                    adapterTamu  = new RecyclerAdapterTamu(this,listTamu);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapterTamu);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
