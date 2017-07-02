package me.zhaoliufeng.mylab.PayPwView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhaoliufeng.customviews.PayPwView.PayPwView;
import me.zhaoliufeng.mylab.R;
import me.zhaoliufeng.toolslib.ToastUtils;

public class PayPwActivity extends AppCompatActivity implements PayPwView.OnFinishInputListener{

    @BindView(R.id.ppv1)
    PayPwView ppv1;
    @BindView(R.id.ppv2)
    PayPwView ppv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pw);
        ButterKnife.bind(this);
        ppv1.setOnFinishInputListener(this);
        ppv2.setOnFinishInputListener(this);
    }

    @Override
    public void onFinishInput(String pw) {
        ToastUtils.showToast(getBaseContext(), pw);
    }
}
