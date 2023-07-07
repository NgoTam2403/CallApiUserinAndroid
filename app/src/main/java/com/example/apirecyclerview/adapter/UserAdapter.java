package com.example.apirecyclerview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apirecyclerview.MainActivity;
import com.example.apirecyclerview.R;
import com.example.apirecyclerview.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private  List<User> list;
    private Context mContext;


    public UserAdapter(List<User> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = list.get(position);
        if(user==null)
        {
            return;
        }
        holder.tvid.setText(String.valueOf(user.getId()));
        holder.tvname.setText(user.getName());
        holder.tvjob.setText(user.getJob());
        holder.tvadd.setText(user.getAddress());
        holder.icDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity)mContext).deleteItem(user.getId()+"");
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((MainActivity)mContext).showDialogToAddData(1, user);
                return true; // Trả về true thay vì false
            }
        });


    }

    @Override
    public int getItemCount() {
        if(list !=null)
        {
            return list.size();
        }
        return 0;
    }

    public  class UserViewHolder extends RecyclerView.ViewHolder{
        private  TextView tvid,tvname,tvjob,tvadd;
        ImageView icDelete;
        public UserViewHolder(@NonNull View itemView){
            super(itemView);
            tvid = itemView.findViewById(R.id.tvid);
            tvname = itemView.findViewById(R.id.tvname);
            tvjob = itemView.findViewById(R.id.tvjob);
            tvadd = itemView.findViewById(R.id.tvaddress);
            icDelete = itemView.findViewById(R.id.icDelte);

        }
    }
}
