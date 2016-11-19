package cl.telematica.android.certamen3;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import cl.telematica.android.certamen3.Connections.HttpServerConnection;
import cl.telematica.android.certamen3.Database.Database;
import cl.telematica.android.certamen3.Presenter.MainPresenterImpl;

/**
 * Created by franciscocabezas on 11/18/16.
 */

public class MyAsyncTaskExecutor {

    private RecyclerView.Adapter mAdapter;

    private static MyAsyncTaskExecutor instance;

    public static MyAsyncTaskExecutor getInstance() {
        if(instance == null) {
            instance = new MyAsyncTaskExecutor();
        }
        return instance;
    }

    public void executeMyAsynctask(final MainActivity activity, final RecyclerView mRecyclerView, final MainPresenterImpl presenter, final Database dbInstance) {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

            @Override
            protected void onPreExecute(){

            }

            @Override
            protected String doInBackground(Void... params) {
                String resultado = new HttpServerConnection().connectToServer("http://www.mocky.io/v2/582eea8b2600007b0c65f068", 15000);
                return resultado;
            }

            @Override
            protected void onPostExecute(String result) {
                if(result != null){
                    System.out.println(result);

                    //Why god... why
                    mAdapter = new DataAdapter(activity, presenter.getFeeds(result), dbInstance);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }
        };

        task.execute();
    }

}
