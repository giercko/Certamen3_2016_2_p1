package cl.telematica.android.certamen3;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.UnsupportedEncodingException;
import java.util.List;

import cl.telematica.android.certamen3.Database.Database;
import cl.telematica.android.certamen3.Models.Feed;

/**
 * Created by franciscocabezas on 11/18/16.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private List<Feed> mDataset;
    private Context mContext;
    private Database dbInstance;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ImageView mImageView;
        public Button mAddBtn;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.textTitle);
            mImageView = (ImageView) v.findViewById(R.id.imgBackground);
            mAddBtn = (Button) v.findViewById(R.id.add_btn);
        }
    }

    public DataAdapter(Context mContext, List<Feed> myDataset, Database dbInstance) {
        this.mContext = mContext;
        this.mDataset = myDataset;
        this.dbInstance = dbInstance;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Feed feed = mDataset.get(position);
        holder.mTextView.setText(feed.getTitle());
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = feed.getLink();
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "http://" + url;
                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                mContext.startActivity(browserIntent);
            }
        });

        Glide.with(mContext).load(feed.getImage()).into(holder.mImageView);

        if(feed.isFavorite()) {
            holder.mAddBtn.setText(mContext.getString(R.string.added));
        } else {
            holder.mAddBtn.setText(mContext.getString(R.string.like));
        }
        holder.mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * In this section, you have to manage the add behavior on local database
                 */
                SQLiteDatabase db = dbInstance.getWritableDatabase();
                if(db != null){
                    feed.setFavorite(!feed.isFavorite());
                    if(feed.isFavorite()) {
                        holder.mAddBtn.setText(mContext.getString(R.string.added));
                        //db.execSQL("INSERT INTO noticias (id, title, link, author, publishedDate, content, image) ('"+encode64(mDataset.get(position).getImage()).toString()+
                        //        "', '"+ mDataset.get(position).getTitle()+"', '"+mDataset.get(position).getLink()+"', '"+mDataset.get(position).getAuthor()+"', '"+
                        //        mDataset.get(position).getPublishedDate()+"', '"+mDataset.get(position).getContent()+"', '"+mDataset.get(position).getImage()+"')");
                        ContentValues valores = new ContentValues();
                        String key_id = encode64(mDataset.get(position).getImage());
                        valores.put("id", key_id.substring(0,key_id.length()-1));
                        valores.put("title", mDataset.get(position).getTitle());
                        valores.put("link", mDataset.get(position).getLink());
                        valores.put("author",mDataset.get(position).getAuthor());
                        valores.put("publishedDate", mDataset.get(position).getPublishedDate());
                        valores.put("content", mDataset.get(position).getContent());
                        valores.put("image", mDataset.get(position).getImage());
                        db.insert("noticias", null,valores);


                        Toast.makeText(mContext, "Agregado la noticia "+  mDataset.get(position).getTitle()+" a favoritos", Toast.LENGTH_SHORT).show();

                    } else {
                        holder.mAddBtn.setText(mContext.getString(R.string.like));
                        ContentValues valores = new ContentValues();

                        String key_id = encode64(mDataset.get(position).getImage());
                        valores.put("id", key_id.substring(0, key_id.length()-1));
                        db.delete("noticias", "id='"+valores.get("id")+"'", null);
                        //db.execSQL("DELETE FROM noticias where id="+encode64(mDataset.get(position).getImage()).toString()+";");
                        Toast.makeText(mContext, "Quitada la noticia "+(mDataset.get(position)).getTitle()+ " de favoritos", Toast.LENGTH_SHORT).show();

                    }
                   db.close();
                }/*else{
                    feed.setFavorite(!feed.isFavorite());
                    if(feed.isFavorite()) {
                        Toast.makeText(mContext, "Quitada la noticia "+(mDataset.get(position)).getTitle()+ " de favoritos", Toast.LENGTH_SHORT).show();
                        holder.mAddBtn.setText(mContext.getString(R.string.added));
                    } else {
                        holder.mAddBtn.setText(mContext.getString(R.string.like));
                    }
                }*/

            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public String encode64(String texto){
        byte[] data = null;
        try{
            data = texto.getBytes("UTF-8");
        }catch (UnsupportedEncodingException el){
            el.printStackTrace();
        }
        return  Base64.encodeToString(data, Base64.NO_WRAP);
    }
}
