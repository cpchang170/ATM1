package com.askey.hahow.atm1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.zip.Inflater;

public class NewsFragment extends Fragment {
    private static NewsFragment instance;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.news_fragment, container,false);

    }
    public static NewsFragment getinstance(){
        if (instance==null){
            instance = new NewsFragment();
        }
        return instance;
    }
}
