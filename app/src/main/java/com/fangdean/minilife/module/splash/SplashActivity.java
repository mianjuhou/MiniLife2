package com.fangdean.minilife.module.splash;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fangdean.minilife.MainActivity;
import com.fangdean.minilife.R;
import com.fangdean.minilife.app.App;
import com.fangdean.minilife.data.local.UserDao;
import com.fangdean.minilife.model.User;
import com.fangdean.minilife.module.login.LoginActivity;

import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        userDao = App.dbMiniLife.userDao();
        List<User> loginedUser = userDao.getLoginedUser();
        if (loginedUser != null && !loginedUser.isEmpty()) {
            User user = loginedUser.get(0);
            App.loginedUser = user;
            MainActivity.start(this);
        } else {
            LoginActivity.start(this);
        }
        finish();
    }
}
