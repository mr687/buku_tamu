package com.daphino.bukutamu;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
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
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Tamu> listTamu;
    Cursor cursor;
    DBHelper helper;
    Toolbar toolbar;
    RecyclerAdapterTamu adapterTamu;
    TextView kosong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Daftar Tamu </font>"));

        listTamu = new ArrayList<Tamu>();
        kosong = (TextView) findViewById(R.id.kosong);

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
            listTamu = new ArrayList<>();
            cursor = helper.getAllData();
            if(cursor != null){
                if(cursor.moveToFirst()){
                    do{
                        Tamu tamu = new Tamu();
                        tamu.setId(cursor.getString(0));
                        tamu.setGuest_name(cursor.getString(1));
                        tamu.setCompany_name(cursor.getString(2));
                        tamu.setMeet(cursor.getString(3));
                        tamu.setNeed(cursor.getString(4));
                        tamu.setArrival(cursor.getString(5));
                        tamu.setOut(cursor.getString(6));
                        tamu.setSignature((cursor.getString(7) != null) ? cursor.getString(7) : "");

                        listTamu.add(tamu);
                    }while (cursor.moveToNext());

                }

                if(listTamu.size() > 0){
                    kosong.setVisibility(View.GONE);
                    adapterTamu  = new RecyclerAdapterTamu(this,listTamu);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapterTamu);
                }else{
                    kosong.setVisibility(View.VISIBLE);
                }

                adapterTamu.setOnItemClickListener(new RecyclerAdapterTamu.OnItemClickListener() {
                    @Override
                    public void onItemClick(final int position) {
                        final CharSequence[] menuItem = {"Tamu Keluar"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Aksi");
                        builder.setItems(menuItem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        Utils utils = new Utils();
                                        String id = listTamu.get(position).getId();
                                        if(helper.updateData(listTamu.get(position).getId().toString(),utils.getToday())){
                                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("bukutamu").child(id);
                                            Tamu tamu = new Tamu();
                                            tamu.setId(listTamu.get(position).getId());
                                            tamu.setGuest_name(listTamu.get(position).getGuest_name());
                                            tamu.setCompany_name(listTamu.get(position).getCompany_name());
                                            tamu.setMeet(listTamu.get(position).getMeet());
                                            tamu.setNeed(listTamu.get(position).getNeed());
                                            tamu.setArrival(listTamu.get(position).getArrival());
                                            tamu.setSignature(listTamu.get(position).getSignature());
                                            tamu.setOut(utils.getToday());
                                            databaseReference.setValue(tamu);
                                            Toast.makeText(MainActivity.this,"Tamu sudah keluar.",Toast.LENGTH_LONG).show();
                                            refreshList();
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        });
                        if(listTamu.get(position).getOut().equals("-")){
                            builder.create().show();
                        }
                    }
                });
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
