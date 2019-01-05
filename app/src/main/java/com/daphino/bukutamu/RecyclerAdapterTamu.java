package com.daphino.bukutamu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class RecyclerAdapterTamu extends RecyclerView.Adapter<RecyclerAdapterTamu.ViewHolder> {
    private Context context;
    private List<Tamu> listTamu;
    private OnItemClickListener listener;
    private String imageUri;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public RecyclerAdapterTamu(Context context,List<Tamu> listTamu){
        this.context = context;
        this.listTamu = listTamu;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item,viewGroup,false);
        return new ViewHolder(v,this.listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        Utils utils = new Utils();
        final  Tamu tamu = listTamu.get(i);
        holder.guest_name.setText(tamu.getGuest_name());
        holder.company_name.setText(tamu.getCompany_name());
        holder.meet.setText(tamu.getMeet());
        holder.need.setText(tamu.getNeed());
        holder.arrival.setText(tamu.getArrival());
        holder.out.setText(tamu.getOut());
//        holder.img_v.setImageBitmap(utils.convert(tamu.getSignature()));
        Picasso.with(this.context)
                .load(tamu.getSignature())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.img_v);
    }

    @Override
    public int getItemCount() {
        return listTamu.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView guest_name,company_name,meet,need,arrival,out;
        public ImageView img_v;
        public ViewHolder(View v, final OnItemClickListener listener) {
            super(v);
            guest_name = v.findViewById(R.id.guest_name);
            company_name  = v.findViewById(R.id.company_name);
            meet = v.findViewById(R.id.meet);
            need = v.findViewById(R.id.need);
            arrival = v.findViewById(R.id.arrival);
            out = v.findViewById(R.id.out);
            img_v = v.findViewById(R.id.img_v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
