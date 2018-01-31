package com.ctvit.monic.headerdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private MyRecyclerAdapter adapter;
    private List<String> cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        init();
    }

    private void init() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyRecyclerAdapter(this, getData());
        HeaderDecoration decoration=new HeaderDecoration(this);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapter);
    }

    private List<String> getData() {
        cities = new ArrayList<>();
        for (int i = 0; i < 150; i++) {
            cities.add("item" + i);
        }
        return cities;
    }


}
