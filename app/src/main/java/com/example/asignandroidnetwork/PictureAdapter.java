package com.example.asignandroidnetwork;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.asignandroidnetwork.model.Photo;

import java.util.List;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.HolderPicture> {
    private Context context;
    private List<Photo> list;
    private CallBack callBack;

    public PictureAdapter(Context context, List<Photo> list, CallBack callBack) {
        this.context = context;
        this.list = list;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public HolderPicture onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.picture_adapter,parent,false);
        return new HolderPicture(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderPicture holder, int position) {
        Glide.with(context).load(list.get(position).getUrlSq()).into(holder.imgItem);
        Log.i("SET", "onBindViewHolder: "+list.get(position).getUrlSq());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                callBack.showDialog(list.get(position).getUrlSq());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HolderPicture extends RecyclerView.ViewHolder {
        private ImageView imgItem;
        public HolderPicture(@NonNull View itemView) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.img_item);
        }
    }
}
