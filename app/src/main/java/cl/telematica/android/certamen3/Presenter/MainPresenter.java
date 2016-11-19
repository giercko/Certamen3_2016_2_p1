package cl.telematica.android.certamen3.Presenter;

import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import cl.telematica.android.certamen3.Models.Feed;

/**
 * Created by neo_free on 18/11/2016.
 */

public interface MainPresenter{


    public void createMyRecyclerView(RecyclerView mRecyclerView, RecyclerView.LayoutManager mLayoutManager);
    public List<Feed> getFeeds(String result);
}
