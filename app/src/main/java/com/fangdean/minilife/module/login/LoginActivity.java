package com.fangdean.minilife.module.login;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.fangdean.minilife.MainActivity;
import com.fangdean.minilife.R;
import com.fangdean.minilife.app.App;
import com.fangdean.minilife.data.local.UserDao;
import com.fangdean.minilife.data.remote.Services;
import com.fangdean.minilife.data.remote.UserService;
import com.fangdean.minilife.databinding.ActivityLoginBinding;
import com.fangdean.minilife.model.ResponseBean;
import com.fangdean.minilife.model.User;
import com.fangdean.minilife.util.PopUtil;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private UserDao userDao;

    public static void start(Context context) {
        Intent starter = new Intent(context, LoginActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        userDao = App.dbMiniLife.userDao();

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            Services.createService(UserService.class)
                    .login(email, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<ResponseBean<User>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(ResponseBean<User> response) {
                            if (response.getCode() == 1) {
                                User user = response.getContent();
                                userDao.updateLoginState();
                                user.setLoginState(1);
                                userDao.insert(user);
                                App.loginedUser = user;
                                MainActivity.start(LoginActivity.this);
                                finish();
                            } else {
                                String msg = response.getMsg();
                                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        binding.tvChange.setOnClickListener(v -> {
            PopUtil popUtil = PopUtil.init();
            popUtil
                    .setWidth(PopUtil.MATCH_PARENT)
                    .setHeight(PopUtil.WRAP_CONTENT)
                    .setPopView(getListName(popUtil))
                    .showAsDropdown(binding.etEmail);
        });
    }

    private View getListName(PopUtil popUtil) {
        View view = PopUtil.inflate(R.layout.pop_list_name);

        List<User> allUser = userDao.getAllLocalUser();

        DropDownAdapter adapter = new DropDownAdapter(allUser);
        adapter.setListener(name -> {
            binding.etEmail.setText(name);
            popUtil.dismiss();
        });

        RecyclerView rcv = view.findViewById(R.id.rcv);
        rcv.setLayoutManager(new LinearLayoutManager(this));
        rcv.setAdapter(adapter);
        return view;
    }
}
