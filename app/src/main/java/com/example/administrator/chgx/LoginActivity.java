package com.example.administrator.chgx;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {


    private Button login, logout,mainlogin;
    private Tencent mTencent;
    private static final String APPID = "1106969101";
    private QQLoginListener mListener;
    private UserInfo mInfo;
    private SimpleDraweeView figure;
    private TextView nickName;
    private String name, figureurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_login);
        initView();
        mainlogin = (Button) findViewById(R.id.main_login);
        mainlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(mainIntent);
                LoginActivity.this.finish();
            }
        });
    }
    private void initView() {
        figure = (SimpleDraweeView) findViewById(R.id.iv_figure);
        nickName = (TextView) findViewById(R.id.tv_nickname);

        mListener = new QQLoginListener();
        // 实例化Tencent
        if (mTencent == null) {
            mTencent = Tencent.createInstance(APPID, this);
        }

        // 点击登录
        login = (Button) findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QQLogin();
            }
        });
        // 点击退出
        logout = (Button) findViewById(R.id.btn_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTencent.isSessionValid()) {
                    mTencent.logout(LoginActivity.this);
                    figure.setImageResource(R.mipmap.ic_launcher_round);
                    nickName.setText("QQ 昵称");
                }
            }
        });
    }

    /**
     * 登录方法
     */
    private void QQLogin() {
        //如果session不可用，则登录，否则说明已经登录
        if (!mTencent.isSessionValid()) {
            mTencent.login(this, "all", mListener);
        }
    }

    // 实现登录成功与否的接口
    private class QQLoginListener implements IUiListener {

        @Override
        public void onComplete(Object object) { //登录成功
            //获取openid和token
            initOpenIdAndToken(object);
            //获取用户信息
            getUserInfo();
            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
            LoginActivity.this.startActivity(mainIntent);
            LoginActivity.this.finish();
        }

        @Override
        public void onError(UiError uiError) {  //登录失败
        }

        @Override
        public void onCancel() {                //取消登录
        }
    }

    private void initOpenIdAndToken(Object object) {
        JSONObject jb = (JSONObject) object;
        try {
            String openID = jb.getString("openid");  //openid用户唯一标识
            String access_token = jb.getString("access_token");
            String expires = jb.getString("expires_in");

            mTencent.setOpenId(openID);
            mTencent.setAccessToken(access_token, expires);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getUserInfo() {
        QQToken token = mTencent.getQQToken();
        mInfo = new UserInfo(LoginActivity.this, token);
        mInfo.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object object) {
                JSONObject jb = (JSONObject) object;
                try {
                    name = jb.getString("nickname");
                    figureurl = jb.getString("figureurl_qq_2");  //头像图片的url
                    /*Log.i("imgUrl",figureurl.toString()+"");*/
                    nickName.setText(name);
                    /*Glide.with(MainActivity.this).load(figureurl).into(figure);*/
                    Uri parse = Uri.parse(figureurl);
                    figure.setImageURI(parse);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(UiError uiError) {
            }

            @Override
            public void onCancel() {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTencent.onActivityResultData(requestCode, resultCode, data, mListener);
    }
}
