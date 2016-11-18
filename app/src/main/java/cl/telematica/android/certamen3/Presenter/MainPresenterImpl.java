package cl.telematica.android.certamen3.Presenter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cl.telematica.android.certamen3.MainActivity;
import cl.telematica.android.certamen3.Models.Feed;
import cl.telematica.android.certamen3.MyAsyncTaskExecutor;
import cl.telematica.android.certamen3.R;

/**
 * Created by neo_free on 18/11/2016.
 */

public class MainPresenterImpl implements  MainPresenter{
    private Context ctx;

    public MainPresenterImpl(final RecyclerView mRecyclerView,final RecyclerView.LayoutManager mLayoutManager, Context ctx){
        this.ctx = ctx;
        createMyRecyclerView(mRecyclerView, mLayoutManager);
        MyAsyncTaskExecutor.getInstance().executeMyAsynctask((MainActivity) ctx,mRecyclerView, this);
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

   // @Override
    /*public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            /**
             * You should manage the action to show the favorite items saved by the user
             */
        /*    return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
