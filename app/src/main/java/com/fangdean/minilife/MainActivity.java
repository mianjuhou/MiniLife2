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

import com.alibaba.fastjson.JSON;
import com.fangdean.minilife.app.App;
import com.fangdean.minilife.data.local.CategoryDao;
import com.fangdean.minilife.data.local.GoodsDao;
import com.fangdean.minilife.data.remote.GoodsService;
import com.fangdean.minilife.data.remote.Services;
import com.fangdean.minilife.databinding.ActivityMainBinding;
import com.fangdean.minilife.model.Category;
import com.fangdean.minilife.model.Goods;
import com.fangdean.minilife.model.ResponseBean;
import com.fangdean.minilife.util.KeyboardUtil;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import java.io.IOException;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableSubscriber;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private CategoryAdapter adapter;
    private CategoryDao categoryDao;
    private GoodsDao goodsDao;
    private HeaderAndFooterWrapper footerWrapper;

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        categoryDao = App.dbMiniLife.categoryDao();
        goodsDao = App.dbMiniLife.goodsDao();
        binding.tvUp.setOnClickListener(v -> sync());
        binding.tvDown.setOnClickListener(v -> downData());

        adapter = new CategoryAdapter();
        footerWrapper = new HeaderAndFooterWrapper(adapter);
        footerWrapper.addFootView(getFooterView());

        adapter.setListener(category -> GoodsActivity.start(MainActivity.this, category));
        binding.rcvCategory.setLayoutManager(new LinearLayoutManager(this));
        binding.rcvCategory.setAdapter(footerWrapper);

        categoryDao.queryTopCategory(App.loginedUser.getId()).observe(this, categories -> {
            if (categories != null && !categories.isEmpty()) {
                adapter.setDatas(categories);
                footerWrapper.notifyDataSetChanged();
            }
        });
    }

    /**
     * 从服务端获取数据
     */
    private void downData() {
        //下载分类和物品数据
        //根据更新时间确定不更新，更新，插入
        Services.createService(GoodsService.class)
                .downloadCategory(App.loginedUser.getId() + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ResponseBean<List<Category>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(ResponseBean<List<Category>> listResponseBean) {
                        if (listResponseBean.getCode() == 1) {
                            Toast.makeText(MainActivity.this, "获取分类成功", Toast.LENGTH_SHORT).show();
                            List<Category> categoryList = listResponseBean.getContent();
                            for (Category category : categoryList) {
                                Category query = categoryDao.queryCategory(category.getId(), category.getUserId());
                                if (query == null) {
                                    Long insert = categoryDao.insert(category);
                                } else if (query.getUpdateTime() < category.getUpdateTime()) {
                                    categoryDao.update(category);
                                } else {

                                }
                            }
                            Toast.makeText(MainActivity.this, "插入分类成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "获取分类失败：" + listResponseBean.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, "获取分类失败", Toast.LENGTH_SHORT).show();
                    }
                });
        Services.createService(GoodsService.class)
                .downloadGoods(App.loginedUser.getId() + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ResponseBean<List<Goods>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(ResponseBean<List<Goods>> listResponseBean) {
                        if (listResponseBean.getCode() == 1) {
                            Toast.makeText(MainActivity.this, "获取物品成功", Toast.LENGTH_SHORT).show();
                            List<Goods> goodsList = listResponseBean.getContent();
                            for (Goods goods : goodsList) {
                                Goods query = goodsDao.queryGoods(goods.getId(), goods.getUserId());
                                if (query == null) {
                                    Long insert = goodsDao.insert(goods);
                                } else if (query.getUpdateTime() < goods.getUpdateTime()) {
                                    int update = goodsDao.update(goods);
                                } else {

                                }
                            }
                            Toast.makeText(MainActivity.this, "插入物品成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "获取物品失败：" + listResponseBean.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, "获取物品失败", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    /**
     * 把数据上传到服务端
     */
    private void sync() {
        //显示同步动画
        Toast.makeText(MainActivity.this, "开始上传", Toast.LENGTH_SHORT).show();
        Flowable
                .create((FlowableOnSubscribe<List<Category>>) e -> {
                    //查询本地顶级目录
                    List<Category> topCategory = categoryDao.getTopCategory(App.loginedUser.getId());
                    e.onNext(topCategory);
                    e.onComplete();
                }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .flatMap((Function<List<Category>, Publisher<Category>>) categories -> {
                    //保存顶级目录到远程
                    if (!categories.isEmpty()) {
                        String json = JSON.toJSONString(categories);
                        Call<ResponseBean<String>> call = Services
                                .createService(GoodsService.class)
                                .uploadCategory(json);
                        ResponseBean<String> body = call.execute().body();
                        if (body.getCode() == 1) {
                            System.out.println("保存顶级目录到远程成功:" + categories.size());
                        } else {
                            System.out.println("保存顶级目录到远程失败:" + categories.size());
                            throw new RuntimeException(body.getContent());
                        }
                    }
                    return Flowable.fromIterable(categories);
                })
                .flatMap((Function<Category, Publisher<Category>>) category -> {
                    //获取次级目录
                    List<Category> subCategory = categoryDao.getSubCategory(category.getId());
                    //保存次级目录到远程
                    if (!subCategory.isEmpty()) {
                        Call<ResponseBean<String>> call = Services
                                .createService(GoodsService.class)
                                .uploadCategory(JSON.toJSONString(subCategory));
                        ResponseBean<String> body = call.execute().body();
                        if (body.getCode() == 1) {
                            System.out.println("保存次级目录到远程成功:" + subCategory.size());
                        } else {
                            System.out.println("保存次级目录到远程失败:" + subCategory.size());
                            throw new RuntimeException(body.getContent());
                        }
                    }
                    return Flowable.fromIterable(subCategory);
                })
                .subscribe(new FlowableSubscriber<Category>() {

                    private Subscription sub;

                    @Override
                    public void onSubscribe(Subscription s) {
                        sub = s;
                        sub.request(1);
                    }

                    @Override
                    public void onNext(Category category) {
                        //获取物品列表
                        List<Goods> goodsList = goodsDao.getGoodsByCategory(category.getId());
                        //保存到远程
                        if (!goodsList.isEmpty()) {
                            try {
                                Call<ResponseBean<String>> call = Services
                                        .createService(GoodsService.class)
                                        .uploadGoods(JSON.toJSONString(goodsList));
                                ResponseBean<String> body = call.execute().body();
                                if (body.getCode() == 1) {
                                    System.out.println("保存物品到远程成功:" + goodsList.size());
                                } else {
                                    System.out.println("保存物品到远程失败:" + goodsList.size());
                                    throw new RuntimeException(body.getContent());
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        //获取下一个目录
                        sub.request(1);
                    }

                    @Override
                    public void onError(Throwable t) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //同步错误提示
                                Toast.makeText(MainActivity.this, "同步错误:" + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onComplete() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //结束同步动画
                                Toast.makeText(MainActivity.this, "同步结束", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
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
                    long maxCategoryId = categoryDao.getMaxId();
                    category.setId(maxCategoryId + 1);
                    category.setName(name);
                    category.setParentId(0L);
                    category.setUserId(App.loginedUser.getId());
                    category.setOrderNum(0);
                    category.setUpdateTime(System.currentTimeMillis());
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

}
