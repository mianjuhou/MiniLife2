package com.fangdean.minilife.module.login;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fangdean.minilife.R;
import com.fangdean.minilife.model.User;

import java.util.ArrayList;
import java.util.List;

public class DropDownAdapter extends RecyclerView.Adapter<DropDownAdapter.DropDownViewHolder> {

    private List<User> datas = new ArrayList<>();

    public DropDownAdapter(List<User> datas) {
        this.datas.addAll(datas);
    }

    @NonNull
    @Override
    public DropDownViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DropDownViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull DropDownViewHolder holder, int position) {
        holder.bindData(datas.get(position));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class DropDownViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;

        public DropDownViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drop_down, parent, false));
            tvName = itemView.findViewById(R.id.tv_name);
        }

        public void bindData(User user) {
            tvName.setText(user.getEmail());
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(user.getEmail());
                }
            });
        }
    }

    interface OnItemClickListener {
        void onItemClick(String name);
    }

    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
