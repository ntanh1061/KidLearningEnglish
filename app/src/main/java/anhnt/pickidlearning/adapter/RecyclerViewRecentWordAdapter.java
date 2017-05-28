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
 * Created by AnhNT on 1/12/2017.
 */

public class RecyclerViewRecentWordAdapter extends RecyclerView.Adapter<RecyclerViewRecentWordAdapter.MyViewHoler> {
    private Context mContext;
    private List<TuDien> mTuDiens;
    private LayoutInflater mInflater;
    private IOnItemClickListener iOnClickListener;
    private int fav;
    private MyDatabaseFav myDatabaseFav;

    public void setItemListener(IOnItemClickListener iOnClickListener) {
        this.iOnClickListener = iOnClickListener;
        myDatabaseFav = new MyDatabaseFav(mContext);
    }

    public RecyclerViewRecentWordAdapter(Context context, List<TuDien> tuDiens) {
        this.mContext = context;
        this.mTuDiens = tuDiens;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_view_recent_word_item, parent, false);
        return new MyViewHoler(view);
    }

    @Override
    public void onBindViewHolder(MyViewHoler holder, int position) {
        holder.tvWord.setText(mTuDiens.get(position).getWord());
        String htmlContent = Html.fromHtml(mTuDiens.get(position).getContent()).toString();
        holder.tvContent.setText(htmlContent);
        holder.imgFav.setImageResource((mTuDiens.get(position).getFav() == 1) ? R.mipmap.ic_start_selected : R.mipmap.ic_star_border_black);
    }

    @Override
    public int getItemCount() {
        return mTuDiens.size();
    }

    public class MyViewHoler extends RecyclerView.ViewHolder {
        TextView tvWord;
        TextView tvContent;
        ImageView imgFav;
        ImageView imgSpeaker;

        public MyViewHoler(View itemView) {
            super(itemView);
            tvWord = (TextView) itemView.findViewById(R.id.tvRecentWord);
            tvContent = (TextView) itemView.findViewById(R.id.tvRecentContent);
            imgFav = (ImageView) itemView.findViewById(R.id.imgRecentYourWord);
            imgSpeaker = (ImageView) itemView.findViewById(R.id.imgSpeaker);
            imgSpeaker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iOnClickListener.speakerItemClick(getAdapterPosition());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iOnClickListener.itemClick(getAdapterPosition());
                }
            });

            imgFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeFavState(imgFav, getAdapterPosition());
                    iOnClickListener.yourWordClick(getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    iOnClickListener.itemLongClick(getAdapterPosition());
                    return false;
                }
            });
        }
    }

    public void changeFavState(ImageView imgYourWord, int position) {
        List<TuDien> tuDiens = myDatabaseFav.getTuDienFav();
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
