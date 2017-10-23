package com.example.ryan.news_app;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by Ryan on 10/21/2017.
 */

public class InfoLoader extends AsyncTaskLoader<List<Info>> {

    public InfoLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<Info> loadInBackground() {

        List<Info> listOfNews = null;

        try {
            URL url = ApiStuff.createUrl();

            String jsonResponse = ApiStuff.makeHttpRequest(url);

            listOfNews = ApiStuff.parseJson(jsonResponse);

        } catch (IOException e) {

            Log.e("ApiStuff", "Error LoadInBackground: ", e);

        }

        return listOfNews;
    }
}