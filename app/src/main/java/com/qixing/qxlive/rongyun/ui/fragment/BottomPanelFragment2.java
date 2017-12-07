package com.qixing.qxlive.rongyun.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qixing.R;
import com.qixing.qxlive.rongyun.widget.InputPanel;


public class BottomPanelFragment2 extends Fragment {

    private static final String TAG = "BottomPanelFragment";

    private ViewGroup buttonPanel;
    private ImageView btnInput;
    private ImageView btn_input2;
    private InputPanel inputPanel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottombar2, container);
        buttonPanel = (ViewGroup) view.findViewById(R.id.button_panel);
        btnInput = (ImageView) view.findViewById(R.id.btn_input);
        btn_input2 = (ImageView) view.findViewById(R.id.btn_input2);
        inputPanel = (InputPanel) view.findViewById(R.id.input_panel);

        btn_input2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPanel.setVisibility(View.GONE);
                inputPanel.setVisibility(View.VISIBLE);
                inputPanel.showKeyboard();
            }
        });

        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPanel.setVisibility(View.GONE);
                inputPanel.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }

    /**
     * back键或者空白区域点击事件处理
     *
     * @return 已处理true, 否则false
     */
    public boolean onBackAction() {
        if (inputPanel.onBackAction()) {
            return true;
        }
        if (buttonPanel.getVisibility() != View.VISIBLE) {
            inputPanel.hideKeyboard();
            inputPanel.setVisibility(View.GONE);
            buttonPanel.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }

    public void setInputPanelListener(InputPanel.InputPanelListener l) {
        inputPanel.setPanelListener(l);
    }
}
