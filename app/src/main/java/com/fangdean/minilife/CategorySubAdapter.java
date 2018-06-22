package com.fangdean.minilife;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.fangdean.minilife.app.App;
import com.fangdean.minilife.data.local.CategoryDao;
import com.fangdean.minilife.data.local.GoodsDao;
import com.fangdean.minilife.model.Category;
import com.fangdean.minilife.model.CategoryBean;
import com.fangdean.minilife.model.Goods;
import com.fangdean.minilife.util.KeyboardUtil;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

public class CategorySubAdapter extends RecyclerView.Adapter<CategorySubAdapter.CategorySubViewHold> {

    private final CategoryDao categoryDao;
    private List<Category> datas = new ArrayList<>();
    private GoodsDao goodsDao;
    private SparseArray<Integer> editStateArray = new SparseArray<>();
    private GoodsActivity activity;

    public CategorySubAdapter(GoodsDao goodsDao, GoodsActivity activity) {
        this.goodsDao = goodsDao;
        categoryDao = App.dbMiniLife.categoryDao();
        this.activity = activity;
    }

    public static int dp2px(Context context, float dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
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
    public CategorySubViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategorySubViewHold(parent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull CategorySubViewHold holder, int position) {
        Category data = datas.get(position);
        holder.tvName.setText(data.getName());
        holder.tvDelete.setOnClickListener(v -> {
            holder.swipeLayout.close(true);
            showDeleteDialog(v, datas.get(position));
        });
        goodsDao.getGoodsByCategory(data.getId()).observe(activity, goodsList -> {
            //比较数据只有改变了的项才进行一下操作,以数量和name作为比较的依据
            holder.fblGoods.removeAllViews();
            goodsList.forEach(goods -> {
                TextView textView = new TextView(holder.itemView.getContext());
                Integer state = goods.getState();
                int color = Color.BLACK;
                int bg = R.drawable.select_state_0;
                if (0 == state) {
                    color = Color.BLACK;
                    bg = R.drawable.select_state_0;
                } else if (1 == state) {
                    color = Color.BLUE;
                    bg = R.drawable.select_state_1;
                } else if (2 == state) {
                    color = Color.RED;
                    bg = R.drawable.select_state_2;
                } else if (3 == state) {
                    color = Color.GRAY;
                    bg = R.drawable.select_state_3;
                }
                textView.setTag(goods);
                textView.setTextColor(color);
                textView.setText(goods.getName());
                textView.setBackgroundResource(bg);
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setPadding(dp2px(activity, 2), dp2px(activity, 1), dp2px(activity, 2), dp2px(activity, 1));
                textView.setIncludeFontPadding(false);
                textView.setGravity(Gravity.CENTER);
                textView.setOnClickListener(v -> {
                    Goods tag = (Goods) v.getTag();
                    Integer st = tag.getState();
                    if (3 == st) {
                        tag.setState(0);
                    } else {
                        tag.setState(tag.getState() + 1);
                    }
                    goodsDao.update(tag);
                });
                textView.setOnLongClickListener(v -> {
                    Goods tag = (Goods) v.getTag();
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("确定要删除'" + tag.getName() + "'吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    goodsDao.delete(tag);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create()
                            .show();
                    return true;
                });
                holder.fblGoods.addView(textView);
            });

            holder.fblGoods.addView(getTailView(data, position));
        });
    }

    private void showDeleteDialog(View v, Category category) {
        new AlertDialog.Builder(v.getContext())
                .setTitle("确定要删除分类'" + category.getName() + "'吗？")
                .setPositiveButton("确定", (dialog, which) -> {
                    int cateNum = categoryDao.delete(category);
                    int goodsNum = goodsDao.deleteGoodsByCategory(category.getId());
                })
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private View getTailView(Category category, int position) {
        View tailView = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.layout_tail, null, false);
        TextView tvAdd = tailView.findViewById(R.id.tv_add);
        TextView tvOk = tailView.findViewById(R.id.tv_ok);
        TextView tvCancel = tailView.findViewById(R.id.tv_cancel);
        EditText etName = tailView.findViewById(R.id.et_name);
        View llEdit = tailView.findViewById(R.id.ll_edit);
        Integer editState = editStateArray.get(position);
        if (editState == null || editState == 0) {
            llEdit.setVisibility(View.GONE);
            tvAdd.setVisibility(View.VISIBLE);
        } else {
            llEdit.setVisibility(View.VISIBLE);
            tvAdd.setVisibility(View.GONE);
            //弹出键盘
            etName.setFocusable(true);
            etName.setFocusableInTouchMode(true);
            etName.requestFocus();
            KeyboardUtil.openKeyboard(etName);
        }
        tvAdd.setOnClickListener(v -> {
            llEdit.setVisibility(View.VISIBLE);
            tvAdd.setVisibility(View.GONE);
            editStateArray.put(position, 1);
            //弹出键盘
            etName.setFocusable(true);
            etName.setFocusableInTouchMode(true);
            etName.requestFocus();
            KeyboardUtil.openKeyboard(etName);
        });
        tvOk.setOnClickListener(v -> {
            doOk(category, etName);
        });
        tvCancel.setOnClickListener(v -> {
            editStateArray.put(position, 0);
            llEdit.setVisibility(View.GONE);
            tvAdd.setVisibility(View.VISIBLE);
            KeyboardUtil.closeKeyboard(activity);
        });
        etName.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                doOk(category, etName);
                return true;
            }
            return false;
        });
        return tailView;
    }

    private void doOk(Category category, EditText etName) {
        //添加操作
        String name = etName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(activity, "分类名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        Goods goods = new Goods();
        goods.setName(name);
        goods.setCategory_id(category.getId());
        goods.setState(0);
        Long insert = goodsDao.insert(goods);
        if (insert > 0) {
            //隐藏键盘
        } else {
            Toast.makeText(activity, "物品插入失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class CategorySubViewHold extends RecyclerView.ViewHolder {

        TextView tvName;
        FlexboxLayout fblGoods;
        SwipeLayout swipeLayout;
        TextView tvDelete;

        public CategorySubViewHold(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sub_category, parent, false));
            tvName = itemView.findViewById(R.id.tv_category_name);
            fblGoods = itemView.findViewById(R.id.fbl_goods);
            swipeLayout = itemView.findViewById(R.id.swipe);
            tvDelete = itemView.findViewById(R.id.tv_delete);
        }
    }

//    interface OnAddOkClickListener {
//        void onAddOkClick(Goods goods);
//    }
//
//    private OnAddOkClickListener listener;
//
//    public void setListener(OnAddOkClickListener listener) {
//        this.listener = listener;
//    }
}
