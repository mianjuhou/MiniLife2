package com.fangdean.minilife;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fangdean.minilife.app.App;
import com.fangdean.minilife.data.local.CategoryDao;
import com.fangdean.minilife.data.local.GoodsDao;
import com.fangdean.minilife.databinding.ActivityGoodsBinding;
import com.fangdean.minilife.model.Category;
import com.fangdean.minilife.util.KeyboardUtil;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

public class GoodsActivity extends AppCompatActivity {

    private ActivityGoodsBinding binding;
    private Category topCategory;

    private CategoryDao categoryDao;
    private GoodsDao goodsDao;
    private CategorySubAdapter adapter;
    private HeaderAndFooterWrapper footerWrapper;

    public static void start(Context context, Category category) {
        Intent starter = new Intent(context, GoodsActivity.class);
        starter.putExtra("top_category", category);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_goods);
        topCategory = getIntent().getParcelableExtra("top_category");
        categoryDao = App.dbMiniLife.categoryDao();
        goodsDao = App.dbMiniLife.goodsDao();

        binding.tvTitle.setText(topCategory.getName());
        binding.tvBack.setOnClickListener(v -> finish());
        binding.tvSync.setOnClickListener(v -> sync());

        adapter = new CategorySubAdapter(goodsDao, this);
        footerWrapper = new HeaderAndFooterWrapper(adapter);
        footerWrapper.addFootView(getFooterView());


        binding.rcvCategory.setLayoutManager(new LinearLayoutManager(this));
        binding.rcvCategory.setAdapter(footerWrapper);

        categoryDao.querySubCategory(topCategory.getId()).observe(this, categories -> {
            adapter.setDatas(categories);
            footerWrapper.notifyDataSetChanged();
        });
    }

    private void sync() {

    }

    private void showDialog() {
        EditText editText = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("请填写分类")
                .setView(editText)
                .setNegativeButton("取消", (dialog, which) -> {
                    dialog.dismiss();
                    KeyboardUtil.closeKeyboard((GoodsActivity.this));
                })
                .setPositiveButton("确定", (dialog, which) -> {
                    dialog.dismiss();
                    KeyboardUtil.closeKeyboard(this);
                    String name = editText.getText().toString().trim();
                    if (TextUtils.isEmpty(name)) {
                        Toast.makeText(this, "分类名称不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Category category = new Category();
                    long maxCategoryId = categoryDao.getMaxId();
                    category.setId(maxCategoryId + 1);
                    category.setName(name);
                    category.setParentId(topCategory.getId());
                    category.setUserId(App.loginedUser.getId());
                    category.setUpdateTime(System.currentTimeMillis());
                    Long insertNum = categoryDao.insert(category);
                    if (insertNum > 0) {
                        KeyboardUtil.closeKeyboard((GoodsActivity.this));
                    } else {
                        Toast.makeText(this, "插入失败", Toast.LENGTH_SHORT).show();
                    }
                })
                .create()
                .show();
        KeyboardUtil.openKeyboard(editText);
    }

    private View getFooterView() {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button tvAdd = new Button(this);
        tvAdd.setText("添加");
        tvAdd.setTextSize(16);
        tvAdd.setGravity(Gravity.CENTER);
        tvAdd.setLayoutParams(layoutParams);
        tvAdd.setOnClickListener(v -> showDialog());
        return tvAdd;
    }
}
