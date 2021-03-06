package cl.telematica.android.certamen3.Presenter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cl.telematica.android.certamen3.DataAdapter;
import cl.telematica.android.certamen3.Database.Database;
import cl.telematica.android.certamen3.MainActivity;
import cl.telematica.android.certamen3.Models.Feed;
import cl.telematica.android.certamen3.MyAsyncTaskExecutor;
import cl.telematica.android.certamen3.R;

/**
 * Created by neo_free on 18/11/2016.
 */

public class MainPresenterImpl implements  MainPresenter{
    private Context ctx;
    private RecyclerView.Adapter mAdapter;

    public MainPresenterImpl(final RecyclerView mRecyclerView,final RecyclerView.LayoutManager mLayoutManager, Context ctx, Database dbInstance, boolean flag_fav){
        this.ctx = ctx;
        createMyRecyclerView(mRecyclerView, mLayoutManager);
        if (flag_fav){
            mAdapter = new DataAdapter(ctx, getFav(dbInstance), dbInstance);
            mRecyclerView.setAdapter(mAdapter);
        }else{
            MyAsyncTaskExecutor.getInstance().executeMyAsynctask((MainActivity) ctx,mRecyclerView, this, dbInstance);
        }
    }


    @Override
    public void createMyRecyclerView(RecyclerView mRecyclerView, RecyclerView.LayoutManager mLayoutManager) {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public List<Feed> getFeeds(String result) {
        List<Feed> feeds = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject responseData = jsonObject.getJSONObject("responseData");
            JSONObject feedObj = responseData.getJSONObject("feed");

            JSONArray entries = feedObj.getJSONArray("entries");
            int size = entries.length();
            for(int i = 0; i < size; i++){
                JSONObject entryObj = entries.getJSONObject(i);
                Feed feed = new Feed();

                feed.setTitle(entryObj.optString("title"));
                feed.setLink(entryObj.optString("link"));
                feed.setAuthor(entryObj.optString("author"));
                feed.setPublishedDate(entryObj.optString("publishedDate"));
                feed.setContent(entryObj.optString("content"));
                feed.setImage(entryObj.optString("image"));

                feeds.add(feed);
            }

            return feeds;
        } catch (JSONException e) {
            e.printStackTrace();
            return feeds;
        }
    }

    public List<Feed> getFav(Database dbInstance){
        List<Feed> fav = new ArrayList<>();
        SQLiteDatabase db = dbInstance.getReadableDatabase();
        String[] valores_recuperar ={"id", "title", "link", "author", "publishedDate", "content", "image"};
        Cursor c = db.query("noticias", valores_recuperar, null, null, null, null, null, null);
        c.moveToFirst();
        do{
            Feed feed = new Feed();
            feed.setTitle(c.getString(1));
            feed.setLink(c.getString(2));
            feed.setAuthor(c.getString(3));
            feed.setPublishedDate(c.getString(4));
            feed.setContent(c.getString(5));
            feed.setImage(c.getString(6));
            feed.setFavorite(true);
            fav.add(feed);

        }while (c.moveToNext());
        db.close();
        return fav;
    }
}
