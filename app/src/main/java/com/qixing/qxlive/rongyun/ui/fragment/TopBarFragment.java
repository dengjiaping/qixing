package com.qixing.qxlive.rongyun.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.qixing.R;
import com.qixing.view.CircleImageView;

public class TopBarFragment extends Fragment {

    private static final String TAG = "TopBarFragment";

    private CircleImageView img_head;
    private TextView tv_head;
    private TextView tv_liveid;
    private TextView tv_livetime;
    private Button btn_attention;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topbar, container);
        img_head = (CircleImageView) view.findViewById(R.id.top_bar_img_head);
        tv_head = (TextView) view.findViewById(R.id.top_bar_tv_head);
        tv_liveid = (TextView) view.findViewById(R.id.top_bar_tv_liveid);
        tv_livetime = (TextView) view.findViewById(R.id.top_bar_tv_livetime);
        btn_attention = (Button) view.findViewById(R.id.top_bar_btn_attention);

        btn_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }


}
