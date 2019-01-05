package com.daphino.bukutamu;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.borax12.materialdaterangepicker.time.RadialPickerLayout;
import com.borax12.materialdaterangepicker.time.TimePickerDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements TimePickerDialog.OnTimeSetListener {
    RecyclerView recyclerView;
    List<Tamu> listTamu;
    Cursor cursor;
    DBHelper helper;
    Toolbar toolbar;
    RecyclerAdapterTamu adapterTamu;
    DatabaseReference databaseReference;
    ProgressBar progressBar;
    String tm;
    int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Daftar Tamu </font>"));

        listTamu = new ArrayList<Tamu>();
        tm = "";

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        helper = new DBHelper(getApplicationContext());
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        databaseReference = FirebaseDatabase.getInstance().getReference("bukutamu");


        loadFirebase();
//        refreshList();
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
    public void loadFirebase(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                listTamu.clear();
                progressBar.setVisibility(View.GONE);
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Tamu tamu = snapshot.getValue(Tamu.class);
                    listTamu.add(tamu);
                }

                adapterTamu  = new RecyclerAdapterTamu(MainActivity.this,listTamu);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapterTamu);

                adapterTamu.setOnItemClickListener(new RecyclerAdapterTamu.OnItemClickListener() {
                    @Override
                    public void onItemClick(final int position) {
                        pos = position;
                        if(!listTamu.get(position).getOut().equals("-")){
                            return;
                        }
                        Calendar now = Calendar.getInstance();
                        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                                MainActivity.this,
                                now.get(Calendar.HOUR_OF_DAY),
                                now.get(Calendar.MINUTE),
                                true);
                        timePickerDialog.show(getFragmentManager(),"Time Picker Dialog");
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
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

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd) {
        tm = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute);
        if(tm != ""){
            Utils utils = new Utils();
            String id = listTamu.get(pos).getId();
            DatabaseReference dr = FirebaseDatabase.getInstance().getReference("bukutamu").child(id);
            Tamu tamu = new Tamu();
            tamu.setId(listTamu.get(pos).getId());
            tamu.setGuest_name(listTamu.get(pos).getGuest_name());
            tamu.setCompany_name(listTamu.get(pos).getCompany_name());
            tamu.setMeet(listTamu.get(pos).getMeet());
            tamu.setNeed(listTamu.get(pos).getNeed());
            tamu.setArrival(listTamu.get(pos).getArrival());
            tamu.setSignature(listTamu.get(pos).getSignature());
            tamu.setOut(utils.getTodays() + " " + tm);
            dr.setValue(tamu);
            Toast.makeText(MainActivity.this,"Tamu sudah keluar.",Toast.LENGTH_LONG).show();
            listTamu.clear();
        }
        tm ="";
    }
}
