package com.example.ryan.news_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Info>>, SwipeRefreshLayout.OnRefreshListener {

    private InfoAdapter iadapt;

    private static int ID_load = 0;

    SwipeRefreshLayout refreshShit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        refreshShit = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);

        refreshShit.setOnRefreshListener(this);

        refreshShit.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        ListView listView = (ListView) findViewById(R.id.list_view);

        iadapt = new InfoAdapter(this);

        listView.setAdapter(iadapt);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Info news = iadapt.getItem(i);

                String url = news.url;

                Intent intent = new Intent(Intent.ACTION_VIEW);

                intent.setData(Uri.parse(url));

                startActivity(intent);
            }
        });

        getSupportLoaderManager().initLoader(ID_load, null, this);
    }

    @Override
    public Loader<List<Info>> onCreateLoader(int id, Bundle args) {
        return new InfoLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Info>> loader, List<Info> data) {
        refreshShit.setRefreshing(false);

        if (data != null) {

            iadapt.setNotifyOnChange(false);

            iadapt.clear();

            iadapt.setNotifyOnChange(true);

            iadapt.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Info>> loader) {

    }

    @Override
    public void onRefresh() {
        getSupportLoaderManager().restartLoader(ID_load, null, this);
    }
}
