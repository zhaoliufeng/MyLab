package me.zhaoliufeng.mylab.ItemMask.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.OnTouch;
import butterknife.Unbinder;
import me.zhaoliufeng.customviews.ItemMask.ItemMask;
import me.zhaoliufeng.mylab.R;
import me.zhaoliufeng.toolslib.ToastUtils;

/**
 * 列表视图
 * 点击list的item子项并在上方弹出菜单
 * 需要为mask添加OnTouch 以及 OnItemClickListener
 * OnTouch 目的是为了获取点击在item子项中的位置 x y
 * OnItemClickListener 是为了获取当前点击的是哪个视图
 */

public class FragmentList extends Fragment implements View.OnTouchListener, AdapterView.OnItemClickListener {

    private static final String TAG = "FragmentList";
    Unbinder unbinder;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.itemMask)
    ItemMask itemMask;
    private int mOffsetY;
    private String[] menuTitles = new String[]{"收藏", "购买", "分享"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        unbinder = ButterKnife.bind(this, view);
        initListView();
        //mask菜单选择监听
        itemMask.setOnMenuSelectListener(new ItemMask.OnMenuSelectListener() {
            @Override
            public void onMenuSelect(int index) {
                ToastUtils.showToast(getContext(), menuTitles[index]);
            }
        });
        return view;
    }

    private void initListView() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                new String[]{"1", "2", "3", "4", "5", "6", "7", "8"}
        );
        lv.setAdapter(arrayAdapter);
    }

    /**
     * @param offsetY 设置mask绘制时 在y轴上的偏移量
     */
    public void setOffsetY(int offsetY){
        mOffsetY = offsetY;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnTouch(R.id.lv)
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //mask显示菜单
        itemMask.showMenu((int)event.getRawX(), (int)event.getRawY(),menuTitles);
        return false;
    }

    @OnItemClick(R.id.lv)
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "Click at " + position);
        itemMask.setItemView(view, mOffsetY);
    }
}
