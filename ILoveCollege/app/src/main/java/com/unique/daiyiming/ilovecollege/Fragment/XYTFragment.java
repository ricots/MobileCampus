package com.unique.daiyiming.ilovecollege.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.unique.daiyiming.ilovecollege.R;

/**
 * Created by daiyiming on 2015/6/19.
 * 校园通
 */
public class XYTFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout rl_xygzh = null; //校园公众号
    private RelativeLayout rl_kb = null; //课表
    private RelativeLayout rl_xyj = null; //校友记

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_xyt, null);

        rl_xygzh = (RelativeLayout) view.findViewById(R.id.rl_xygzh);
        rl_kb = (RelativeLayout) view.findViewById(R.id.rl_kb);
        rl_xyj = (RelativeLayout) view.findViewById(R.id.rl_xyj);

        rl_xygzh.setOnClickListener(this);
        rl_kb.setOnClickListener(this);
        rl_xyj.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_xygzh: {

            }break;
            case R.id.rl_kb: {

            }break;
            case R.id.rl_xyj: {

            }break;
        }
    }
}
