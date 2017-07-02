package me.zhaoliufeng.mylab.ItemMask.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.zhaoliufeng.customviews.ItemMask.ItemMask;
import me.zhaoliufeng.mylab.R;

/**
 * 单视图
 */

public class FragmentSingle extends Fragment implements View.OnClickListener {

    @BindView(R.id.view_top)
    View viewTop;
    @BindView(R.id.view_down)
    View viewDown;
    Unbinder unbinder;
    @BindView(R.id.itemMask)
    ItemMask itemMask;
    @BindView(R.id.view_small)
    View viewSmall;

    private int mOffsetY;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sigle_view, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
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

    @OnClick({R.id.view_top, R.id.view_down, R.id.view_small})
    @Override
    public void onClick(View v) {
        itemMask.setItemView(v, mOffsetY);
    }
}
