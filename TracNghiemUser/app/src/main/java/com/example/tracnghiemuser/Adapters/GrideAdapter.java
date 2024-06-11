package com.example.tracnghiemuser.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.tracnghiemuser.QuestionActivity;
import com.example.tracnghiemuser.R;


public class GrideAdapter extends BaseAdapter {
    public int sets = 0;
    private String mon;

    public GrideAdapter(int sets, String mon) {
        this.sets = sets;
        this.mon = mon;
    }

    @Override
    public int getCount() {
        return sets+1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1;
        if(view==null){
            view1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sets, viewGroup, false);
        }
        else {
            view1=view;
        }
        if(i==0){
            ((CardView)view1.findViewById(R.id.setsCard)).setVisibility(View.GONE);
        }
        else {
            ((TextView)view1.findViewById(R.id.setName)).setText(String.valueOf(i));
        }
        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(viewGroup.getContext(), QuestionActivity.class);
                    intent.putExtra("setNum", i);
                    intent.putExtra("monName", mon);
                    viewGroup.getContext().startActivity(intent);

            }
        });
        return view1;
    }

}
