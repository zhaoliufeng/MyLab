package me.zhaoliufeng.mylab.MusicPlayer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import me.zhaoliufeng.mylab.MusicPlayer.bean.Song;
import me.zhaoliufeng.mylab.R;

/**
 * Created by ZhaoLiufeng on 2017/8/9 15 15:54.
 * 音乐列表适配器
 * 添加了点击的回调事件， 开放itemChange()提供当前选中项的视觉提示
 */

public class MusicAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Song> datas;
    private ItemSelectListener mItemSelectListener;
    private int selectItem;

    public MusicAdapter(Context context, List<Song> datas) {
        this.mContext = context;
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MusicHolder(LayoutInflater.from(mContext).inflate(R.layout.item_music_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MusicHolder viewHolder = (MusicHolder) holder;
        viewHolder.mTvMusicName.setText(datas.get(position).getMusicName());
        viewHolder.mTvSinger.setText(datas.get(position).getArtist());
        if (position == selectItem){
            viewHolder.mTvMusicName.setTextColor(Color.parseColor("#FF00CEFC"));
            viewHolder.mTvSinger.setTextColor(Color.parseColor("#109DC0"));
        }else {
            viewHolder.mTvMusicName.setTextColor(Color.parseColor("#FFFFFFFF"));
            viewHolder.mTvSinger.setTextColor(Color.parseColor("#A9A0B3"));
        }
        viewHolder.mLlayoutItemBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItem = position;
                notifyDataSetChanged();
                if (mItemSelectListener != null)
                    mItemSelectListener.itemSelect(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void itemChange(int position){
        selectItem = position;
        notifyDataSetChanged();
    }

    private class MusicHolder extends RecyclerView.ViewHolder{

        private TextView mTvMusicName, mTvSinger;
        private LinearLayout mLlayoutItemBg;

        MusicHolder(View itemView) {
            super(itemView);
            mLlayoutItemBg = (LinearLayout) itemView.findViewById(R.id.llayout_item_bg);
            mTvMusicName = (TextView) itemView.findViewById(R.id.tv_item_music_name);
            mTvSinger = (TextView) itemView.findViewById(R.id.tv_item_singer);
        }
    }

    public void setItemSelectListener(ItemSelectListener itemSelectListener){
        this.mItemSelectListener = itemSelectListener;
    }

    public interface ItemSelectListener{
        void itemSelect(int index);
    }
}
