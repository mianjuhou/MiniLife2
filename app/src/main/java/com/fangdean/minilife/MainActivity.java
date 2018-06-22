package com.fangdean.minilife;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.fangdean.minilife.app.App;
import com.fangdean.minilife.data.local.CategoryDao;
import com.fangdean.minilife.databinding.ActivityMainBinding;
import com.fangdean.minilife.model.Category;
import com.fangdean.minilife.util.KeyboardUtil;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private CategoryAdapter adapter;
    private CategoryDao categoryDao;
    private HeaderAndFooterWrapper footerWrapper;

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        this.setTitle("顶级分类");
        categoryDao = App.dbMiniLife.categoryDao();

        adapter = new CategoryAdapter();
        footerWrapper = new HeaderAndFooterWrapper(adapter);
        footerWrapper.addFootView(getFooterView());

        adapter.setListener(category -> GoodsActivity.start(MainActivity.this, category));
        binding.rcvCategory.setLayoutManager(new LinearLayoutManager(this));
        binding.rcvCategory.setAdapter(footerWrapper);

        categoryDao.queryTopCategory(App.loginedUser.getId()).observe(this, categories -> {
            adapter.setDatas(categories);
            footerWrapper.notifyDataSetChanged();
        });
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

    private void showDialog() {
        EditText editText = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("请填写分类")
                .setView(editText)
                .setNegativeButton("取消", (dialog, which) -> {
                    dialog.dismiss();
                    KeyboardUtil.closeKeyboard((MainActivity.this));
                })
                .setPositiveButton("确定", (dialog, which) -> {
                    dialog.dismiss();
                    String name = editText.getText().toString().trim();
                    if (TextUtils.isEmpty(name)) {
                        Toast.makeText(this, "分类名称不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Category category = new Category();
                    category.setName(name);
                    category.setParent_id(0L);
                    category.setUser_id(App.loginedUser.getId());
                    Long insertNum = categoryDao.insert(category);
                    if (insertNum > 0) {
                        KeyboardUtil.closeKeyboard((MainActivity.this));
                    } else {
                        Toast.makeText(MainActivity.this, "插入失败", Toast.LENGTH_SHORT).show();
                    }
                })
                .create()
                .show();
        KeyboardUtil.openKeyboard(editText);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //此处请求网络
        // 根据登录人，时间获取可更新条目
    }
}
