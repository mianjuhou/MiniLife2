package com.fangdean.minilife;

import android.app.AlertDialog;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.fangdean.minilife.app.App;
import com.fangdean.minilife.data.local.CategoryDao;
import com.fangdean.minilife.data.local.GoodsDao;
import com.fangdean.minilife.model.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHold> {

    private List<Category> datas = new ArrayList<>();
    private CategoryDao categoryDao;
    private GoodsDao goodsDao;

    public CategoryAdapter() {
        categoryDao = App.dbMiniLife.categoryDao();
        goodsDao = App.dbMiniLife.goodsDao();
    }

    public CategoryAdapter(List<Category> datas) {
        this.datas.addAll(datas);
    }

    public List<Category> getDatas() {
        return datas;
    }

    public void setDatas(List<Category> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
    }

    @NonNull
    @Override
    public CategoryViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHold(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHold holder, int position) {
        holder.bindData(datas.get(position));
        holder.llContent.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(datas.get(position));
            }
        });
        holder.tvDelete.setOnClickListener(v -> {
            holder.swipeLayout.close(true);
            showDeleteDialog(v.getContext(), datas.get(position));
        });
    }

    private void showDeleteDialog(Context context, Category category) {
        new AlertDialog.Builder(context)
                .setTitle("确定要删除分类'" + category.getName() + "'吗？")
                .setPositiveButton("确定", (dialog, which) -> {
                    List<Category> subCategory = categoryDao.getSubCategory(category.getId());
                    for (Category cate : subCategory) {
                        int goodsNum = goodsDao.deleteGoodsByCategory(cate.getId());
                        int cateNum = categoryDao.delete(cate);
                    }
                    int deleteNum = categoryDao.delete(category);
                })
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class CategoryViewHold extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvDelete;
        SwipeLayout swipeLayout;
        View llContent;

        public CategoryViewHold(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false));
            llContent = itemView.findViewById(R.id.ll_content);
            tvName = itemView.findViewById(R.id.tv_name);
            swipeLayout = itemView.findViewById(R.id.swipe);
            tvDelete = itemView.findViewById(R.id.tv_delete);
        }

        public void bindData(Category category) {
            tvName.setText(category.getName());
        }
    }

    private OnItemClickListener listener;


    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Category category);
    }
}
