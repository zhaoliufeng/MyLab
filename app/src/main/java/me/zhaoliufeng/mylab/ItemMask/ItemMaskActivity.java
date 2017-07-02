package me.zhaoliufeng.mylab.ItemMask;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhaoliufeng.mylab.ItemMask.adapter.FragmentAdapter;
import me.zhaoliufeng.mylab.ItemMask.fragment.FragmentList;
import me.zhaoliufeng.mylab.ItemMask.fragment.FragmentSingle;
import me.zhaoliufeng.mylab.R;

/**
 * 子视图凸显
 */

public class ItemMaskActivity extends FragmentActivity {

    private static final String TAG = "ItemMaskActivity";
    @BindView(R.id.tab_title)
    TabLayout tabTitle;
    @BindView(R.id.vp)
    ViewPager vp;

    private FragmentList fragmentList;
    private FragmentSingle fragmentSingle;

    private  List<Fragment> list_fragment;
    private List<String> list_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_mask);
        ButterKnife.bind(this);
        initViewData();

        FragmentAdapter fragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), list_fragment, list_title);
        vp.setAdapter(fragmentAdapter);
        //关联tablayout与viewpager
        tabTitle.setupWithViewPager(vp);

        ViewTreeObserver observer = tabTitle.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                tabTitle.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Log.i(TAG, tabTitle.getHeight() + "");
                fragmentSingle.setOffsetY(tabTitle.getHeight());
                fragmentList.setOffsetY(tabTitle.getHeight());
            }
        });
    }

    private void initViewData() {
        fragmentList = new FragmentList();
        fragmentSingle = new FragmentSingle();
        list_fragment = new ArrayList<>();
        list_title = new ArrayList<>();
        list_fragment.add(fragmentSingle);
        list_fragment.add(fragmentList);
        //添加设置TabLayout中的标题
        list_title.add("单视图");
        list_title.add("List列表");
        tabTitle.addTab(tabTitle.newTab().setText(list_title.get(0)));
        tabTitle.addTab(tabTitle.newTab().setText(list_title.get(1)));
    }
}
