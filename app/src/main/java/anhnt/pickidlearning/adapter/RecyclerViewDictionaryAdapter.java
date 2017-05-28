package anhnt.pickidlearning.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import anhnt.pickidlearning.R;
import anhnt.pickidlearning.databases.MyDatabaseFav;
import anhnt.pickidlearning.models.TuDien;
import anhnt.pickidlearning.myinterface.IOnItemClickListener;

/**
 * Created by HCD-Fresher046 on 1/10/2017.
 */

public class RecyclerViewDictionaryAdapter extends RecyclerView.Adapter<RecyclerViewDictionaryAdapter.MyViewHolder> {
    Context mContext;
    private List<TuDien> mTuDiens;
    private LayoutInflater mInflater;
    IOnItemClickListener mItemClick;
    private MyDatabaseFav mMyDatabaseFav;
    int fav;

    public void setOnItemClickListener(IOnItemClickListener itemClick) {
        this.mItemClick = itemClick;
    }

    public RecyclerViewDictionaryAdapter(Context context, List<TuDien> tuDiens) {
        this.mContext = context;
        this.mTuDiens = tuDiens;
        mInflater = LayoutInflater.from(context);
        mMyDatabaseFav = new MyDatabaseFav(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_view_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        mMyDatabaseFav = new MyDatabaseFav(mContext);
        List<TuDien> tuDiens = mMyDatabaseFav.getTuDienFav();
        String word = mTuDiens.get(position).getWord();
        holder.tvWord.setText(word);
        String htmlContent = Html.fromHtml(mTuDiens.get(position).getContent()).toString();
        holder.tvContent.setText(htmlContent);
        fav = mTuDiens.get(position).getFav();
        for (int i = 0; i < tuDiens.size(); i++) {
            if (word.equals(tuDiens.get(i).getWord())) {
                fav = tuDiens.get(i).getFav();
            }
        }
        holder.imgYourWord.setImageResource((fav == 1) ? R.mipmap.ic_start_selected : R.mipmap.ic_star_border_black);
        mMyDatabaseFav.close();
    }

    @Override
    public int getItemCount() {
        return mTuDiens == null ? 0 : mTuDiens.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvWord;
        TextView tvContent;
        ImageView imgYourWord;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvWord = (TextView) itemView.findViewById(R.id.tvWord);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
            imgYourWord = (ImageView) itemView.findViewById(R.id.imgYourWord);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClick.itemClick(getAdapterPosition());
                }
            });
            imgYourWord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeFavState(imgYourWord, getAdapterPosition());
                    mItemClick.yourWordClick(getAdapterPosition());
                }
            });
        }
    }

    public void changeFavState(ImageView imgYourWord, int position) {
        mMyDatabaseFav = new MyDatabaseFav(mContext);
        List<TuDien> tuDiens = mMyDatabaseFav.getTuDienFav();
        TuDien tuDien = mTuDiens.get(position);
        int id = tuDien.getId();
        fav = tuDien.getFav();
        for (int i = 0; i < tuDiens.size(); i++) {
            if (id == tuDiens.get(i).getId()) {
                fav = tuDiens.get(i).getFav();
            }
        }
        if (fav == 1) {
            tuDien.setFav(0);
            imgYourWord.setImageResource(R.mipmap.ic_star_border_black);
        } else {
            tuDien.setFav(1);
            imgYourWord.setImageResource(R.mipmap.ic_start_selected);
        }
    }
}
