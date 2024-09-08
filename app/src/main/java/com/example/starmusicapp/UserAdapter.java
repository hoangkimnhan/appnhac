package com.example.starmusicapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {

    private Context context;
    private List<Users> usersList;
    public UserAdapter(Context context, List<Users> usersList) {
        this.context = context;
        this.usersList = usersList;
    }


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_usermanage,parent,false);
        return new UserViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.recName.setText(usersList.get(position).getName());
        holder.recEmail.setText(usersList.get(position).getEmail());
        holder.recUsertype.setText(Integer.toString(usersList.get(position).getUsertype()));

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,DetailUserManageActivity.class);
                intent.putExtra("Name", usersList.get(holder.getAdapterPosition()).getName());
                intent.putExtra("Email", usersList.get(holder.getAdapterPosition()).getEmail());
                intent.putExtra("Usertype", usersList.get(holder.getAdapterPosition()).getUsertype());
                intent.putExtra("Uid",usersList.get(holder.getAdapterPosition()).getUid());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public void searchUserList(ArrayList<Users> searchList){
        usersList = searchList;
        notifyDataSetChanged();
    }
}

class UserViewHolder extends RecyclerView.ViewHolder{

    TextView recName, recEmail, recUsertype;
    CardView recCard;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        recName = itemView.findViewById(R.id.recName);
        recEmail = itemView.findViewById(R.id.recEmail);
        recUsertype = itemView.findViewById(R.id.recUsertype);
        recCard = itemView.findViewById(R.id.recCard);

    }
}