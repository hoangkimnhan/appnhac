package com.example.starmusicapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.starmusicapp.R;
import com.example.starmusicapp.SliderItem;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {
    private List<SliderItem> sliderItems;
    Context context;
    public SliderAdapter(List<SliderItem> sliderItems){ this.sliderItems=sliderItems;}
    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new SliderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.alan_slideritem,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setImage(sliderItems.get(position));

    }

    @Override
    public int getItemCount() {
        return sliderItems.size()   ;
    }

    class SliderViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView =itemView.findViewById(R.id.alan_slide);
        }
        void setImage(SliderItem sliderItem){
            Glide.with(context)
                    .load(sliderItem.getImage())
                    .override(300,300)
                    .into(imageView);
        }
    }
}
