package com.daphino.bukutamu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class RecyclerAdapterTamu extends RecyclerView.Adapter<RecyclerAdapterTamu.ViewHolder> {
    private Context context;
    private List<Tamu> listTamu;

    public RecyclerAdapterTamu(Context context,List<Tamu> listTamu){
        this.context = context;
        this.listTamu = listTamu;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        final  Tamu tamu = listTamu.get(i);
        holder.guest_name.setText(tamu.getGuest_name());
        holder.company_name.setText(tamu.getCompany_name());
        holder.meet.setText(tamu.getMeet());
        holder.need.setText(tamu.getNeed());
        File img = new File(tamu.getSignature());
        if(img.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(img.getAbsolutePath());
            holder.img_v.setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemCount() {
        return listTamu.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView guest_name,company_name,meet,need;
        public ImageView img_v;
        public ViewHolder(View v) {
            super(v);
            guest_name = v.findViewById(R.id.guest_name);
            company_name  = v.findViewById(R.id.company_name);
            meet = v.findViewById(R.id.meet);
            need = v.findViewById(R.id.need);
            img_v = v.findViewById(R.id.img_v);
//            super(v);
        }
    }
}
