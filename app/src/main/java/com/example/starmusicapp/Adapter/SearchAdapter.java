package com.example.starmusicapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.starmusicapp.DataClass;
import com.example.starmusicapp.DetailActivity;
import com.example.starmusicapp.MyAdapter;
import com.example.starmusicapp.R;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder>{
    private Context context;
    private List<DataClass> dataList;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener songClicked) {
        listener = songClicked;
    }



    public SearchAdapter(Context context, List<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_search, parent, false);
        return new SearchViewHolder(view, listener);
    }


    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        Glide.with(context).load(dataList.get(position).getImageurl()).into(holder.recImage);
        holder.recTitle.setText(dataList.get(position).getSongname());
        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("Image", dataList.get(holder.getAdapterPosition()).getImageurl());
                intent.putExtra("Title", dataList.get(holder.getAdapterPosition()).getSongname());
                intent.putExtra("Key",dataList.get(holder.getAdapterPosition()).getKey());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<DataClass> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }
}
class SearchViewHolder extends RecyclerView.ViewHolder{
    ImageView recImage, delete_id;
    TextView recTitle, recDesc, recLang;
    CardView recCard;
    public SearchViewHolder(@NonNull View itemView, SearchAdapter.OnItemClickListener listener) {
        super(itemView);
        recImage = itemView.findViewById(R.id.recImage);
        recCard = itemView.findViewById(R.id.recCard);
        recTitle = itemView.findViewById(R.id.recTitle);
        recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(getAdapterPosition());
            }
        });

    }
}