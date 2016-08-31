package com.example.administrator.tencent_weibo;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tencent.connect.common.Constants;
import com.tencent.open.t.Weibo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;
import com.tencent.connect.UserInfo;
public class MainActivity extends AppCompatActivity {
    private Weibo mWeibo = null;
    private static Tencent mTencent = null;
    private final static String APP_ID = "1105379267";
    private static boolean isServerSideLogin = false;
    private UserInfo mInfo;
    private TextView display=null;
    private EditText weiboContent = null;
    private Button sendWeibo = null;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.d(TAG, "-->onActivityResult " + requestCode  + " resultCode=" + resultCode);
        if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode,resultCode,data,loginListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display = (TextView)findViewById(R.id.display);
        weiboContent = (EditText)findViewById(R.id.weiboContent);
        sendWeibo = (Button)findViewById(R.id.SendWeibo);

        // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。

// 其中APP_ID是分配给第三方应用的appid，类型为String。

        mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());

        if (!mTencent.isSessionValid()) {
            mTencent.login(this, "all", loginListener);
            isServerSideLogin = false;
            //Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
        } else {
            if (isServerSideLogin) { // Server-Side 妯″紡鐨勭櫥闄? 鍏堥€€鍑猴紝鍐嶈繘琛孲SO鐧婚檰
                mTencent.logout(this);
                mTencent.login(this, "all", loginListener);
                isServerSideLogin = false;
                //Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
                return;
            }
            mTencent.logout(this);
            updateUserInfo();
            //updateLoginButton();
        }

        sendWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mTencent == null) {
                    return;
                }
                boolean ready = mTencent.isSessionValid()
                        && mTencent.getQQToken().getOpenId() != null;
                if (ready) {
                    String content = weiboContent.getText().toString();
                    mWeibo.sendText(content, new TQQApiListener("add_t", false,MainActivity.this));
                    //Util.showProgressDialog(TQQInfoActivity.this, null, null);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void updateUserInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {
            IUiListener listener = new IUiListener() {

                @Override
                public void onError(UiError e) {

                }

                @Override
                public void onComplete(final Object response) {
                    Message msg = new Message();
                    msg.obj = response;
                    msg.what = 0;

                    //display.setText(response.toString());
                }

                @Override
                public void onCancel() {

                }
            };



            //mInfo = new UserInfo(this, mTencent.getQQToken());
            //mInfo.getUserInfo(listener);
            mWeibo = new Weibo(this, MainActivity.mTencent.getQQToken());
            //mWeibo.getWeiboInfo(new TQQApiListener("get_info", false,MainActivity.this));
        } else {

        }
    }

    private class TQQApiListener extends BaseUIListener {
        private String mScope = "all";
        private Boolean mNeedReAuth = false;
        private Activity mActivity;
        public TQQApiListener(String scope, boolean needReAuth,
                              Activity activity) {
            super(activity);
            this.mScope = scope;
            this.mNeedReAuth = needReAuth;
            this.mActivity = activity;
        }

        @Override
        public void onComplete(Object response) {
            final Activity activity = MainActivity.this;
            try {
                JSONObject json =(JSONObject)response;
                int ret = json.getInt("ret");
                if (json.has("data")) {
                    JSONObject data = json.getJSONObject("data");
                    if (data.has("id")) {
                        //mLastAddTweetId = data.getString("id");;
                    }
                }
                if (ret == 0) {
                    //Message msg = mHandler.obtainMessage(0, mScope);
                    Bundle data = new Bundle();
                    data.putString("response", response.toString());
                    //msg.setData(data);
                    //mHandler.sendMessage(msg);
                } else if (ret == 100030) {
                    if (mNeedReAuth) {

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }



    public static void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch(Exception e) {
        }
    }

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                //Util.showResultDialog(MainActivity.this, "杩斿洖涓虹┖", "鐧诲綍澶辫触");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                //Util.showResultDialog(MainActivity.this, "杩斿洖涓虹┖", "鐧诲綍澶辫触");
                return;
            }
            //Util.showResultDialog(MainActivity.this, response.toString(), "鐧诲綍鎴愬姛");
            // 鏈夊鍒嗕韩澶勭悊
            //handlePrizeShare();
            doComplete((JSONObject)response);
        }

        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onError(UiError e) {
            //Util.toastMessage(MainActivity.this, "onError: " + e.errorDetail);
            //Util.dismissDialog();
        }

        @Override
        public void onCancel() {
            //Util.toastMessage(MainActivity.this, "onCancel: ");
            //Util.dismissDialog();
            if (isServerSideLogin) {
                isServerSideLogin = false;
            }
        }
    }

    IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            //Log.d("SDKQQAgentPref", "AuthorSwitch_SDK:" + SystemClock.elapsedRealtime());
            initOpenidAndToken(values);
            updateUserInfo();
            //updateLoginButton();
        }
    };

}
