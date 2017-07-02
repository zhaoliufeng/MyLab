package me.zhaoliufeng.mylab.BootPage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhaoliufeng.customviews.BootPage.BootPage;
import me.zhaoliufeng.mylab.R;
import me.zhaoliufeng.toolslib.ToastUtils;

public class BootPageActivity extends AppCompatActivity {

    @BindView(R.id.bootPage)
    BootPage bootPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boot_page);
        ButterKnife.bind(this);
        bootPage.setOnPageChangeListener(new BootPage.OnPageChangeListener() {
            @Override
            public void onPageChange(int index) {
                ToastUtils.showToast(getBaseContext(), "第" + (index + 1) + "页");
            }
        });
    }

    public void bootPageOnClick(View view) {
        ToastUtils.showToast(getBaseContext(), "现在在第"+ (bootPage.getPageNum() + 1) + "页");
    }
}
