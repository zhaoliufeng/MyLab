package me.zhaoliufeng.mylab.CoutomClickView;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import me.zhaoliufeng.mylab.R;

public class MulitClickActivity extends AppCompatActivity {

    ListView lv;
    List<String> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mulit_click);
        lv = (ListView) findViewById(R.id.lv);
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("1");
        list.add("2");
        list.add("3");

        ListAdapter listAdapter = new ListAdapter(list, getBaseContext());
        lv.setAdapter(listAdapter);
    }

    private class ListAdapter extends BaseAdapter{

        private List<String> datas;
        private LayoutInflater inflater;

        public ListAdapter(List<String> datas, Context context) {
            this.datas = datas;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.lv_item_mutil_click, null);
           // ClickView clickView = (ClickView) convertView.findViewById(R.id.cv);
            return convertView;
        }
    }
}
