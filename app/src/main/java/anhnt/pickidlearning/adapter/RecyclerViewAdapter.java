package anhnt.pickidlearning.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.support.annotation.IntegerRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import anhnt.pickidlearning.R;
import anhnt.pickidlearning.models.Category;

/**
 * Created by AnhNT on 1/25/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewholer> {
    private Context mContext;
    private List<Category> mCategories;
    private LayoutInflater mInflater;
    private IOnItemClickListener itemClickListener;

    public void setOnItemClickListener(IOnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public RecyclerViewAdapter(Context mContext, List<Category> mCategories) {
        this.mContext = mContext;
        this.mCategories = mCategories;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewholer onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewholer(mInflater.inflate(R.layout.recycler_view_items, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewholer holder, int position) {
        int resID = mContext.getResources().getIdentifier(mCategories.get(position).getPathImage().toString(), "drawable", mContext.getPackageName());
        holder.imgCategory.setImageDrawable(mContext.getResources().getDrawable(resID));
        holder.tvCategoryTitle.setText(mCategories.get(position).getName().toString());
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    public class MyViewholer extends RecyclerView.ViewHolder {
        ImageView imgCategory;
        TextView tvCategoryTitle;

        public MyViewholer(View itemView) {
            super(itemView);
            imgCategory = (ImageView) itemView.findViewById(R.id.img_categories);
            tvCategoryTitle = (TextView) itemView.findViewById(R.id.tv_title_categories);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.setOnItemClick(getAdapterPosition());
                }
            });
        }
    }

    public interface IOnItemClickListener {
        void setOnItemClick(int position);
    }
}
