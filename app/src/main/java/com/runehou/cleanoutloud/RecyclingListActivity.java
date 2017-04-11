package com.runehou.cleanoutloud;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecyclingListActivity extends ListActivity {
    private CustomAdapter adapter;
    private List<String> recycleList = new ArrayList<String>();
    private List<String> scoreList = new ArrayList<String>();
    private FrameLayout frameFragment;

    private TextView txt;
    private ImageView img;
    String tent, pavilion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);

        adapter = new CustomAdapter();

        scoreList.add("Camp Vildhest: 13 kg.");
        scoreList.add("Camp CoL: 11,5 kg.");
        scoreList.add("Camp Grøn: 7 kg.");

        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return scoreList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.recycle_item, null);
            }

            txt = (TextView) view.findViewById(R.id.txt);
            txt.setText((position + 1) + ". " + scoreList.get(position));

//            img = (ImageView) view.findViewById(R.id.img);


//            if (recycleList.get(position).equals(tent)) {
//                img.setImageResource(R.mipmap.tent);
//                txt.setText(tent);
//            }
//            if (recycleList.get(position).equals(pavilion)) {
//                img.setImageResource(R.mipmap.pavilion);
//                txt.setText(pavilion);
//            }

            return view;
        }
    }
}