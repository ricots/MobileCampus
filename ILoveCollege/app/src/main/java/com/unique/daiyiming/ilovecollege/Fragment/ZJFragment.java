package com.unique.daiyiming.ilovecollege.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unique.daiyiming.ilovecollege.R;

/**
 * Created by daiyiming on 2015/6/19.
 * 自己
 */
public class ZJFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zj, null);
        return view;
    }
}
