package anhnt.pickidlearning.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import anhnt.pickidlearning.R;
import anhnt.pickidlearning.models.Category;
import anhnt.pickidlearning.models.Type;

/**
 * Created by AnhNT on 1/25/2017.
 */

public class RecyclerViewTypeAdapter extends RecyclerView.Adapter<RecyclerViewTypeAdapter.MyViewholer> {
    private Context mContext;
    private List<Type> types;
    private LayoutInflater mInflater;
    private IOnItemClickListener itemClickListener;
    private int curentPosition = 0;

    public void setOnItemClickListener(IOnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public RecyclerViewTypeAdapter(Context mContext, List<Type> types) {
        this.mContext = mContext;
        this.types = types;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewholer onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewholer(mInflater.inflate(R.layout.recycler_view_type_item, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(MyViewholer holder, final int position) {
        int resID = mContext.getResources().getIdentifier(types.get(position).getImage().toString(), "drawable", mContext.getPackageName());
        holder.imgCategory.setImageDrawable(mContext.getResources().getDrawable(resID));
        holder.tvCategoryTitle.setText(types.get(position).getTitle().toString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curentPosition = position;
                itemClickListener.setOnItemClick(position);
                notifyDataSetChanged();
            }
        });

        if (curentPosition == position) {
            holder.imgCategory.setBackgroundColor(Color.parseColor("#088fd7"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.imgCategory.setImageTintList(ColorStateList.valueOf(Color.WHITE));
            }
        } else {
            holder.imgCategory.setBackground(mContext.getDrawable(R.drawable.border_image_custom));
            holder.imgCategory.setImageTintList(ColorStateList.valueOf(Color.parseColor("#088fd7")));
        }
    }

    @Override
    public int getItemCount() {
        return types.size();
    }

    public class MyViewholer extends RecyclerView.ViewHolder {
        ImageView imgCategory;
        TextView tvCategoryTitle;

        public MyViewholer(View itemView) {
            super(itemView);
            imgCategory = (ImageView) itemView.findViewById(R.id.img_categories);
            tvCategoryTitle = (TextView) itemView.findViewById(R.id.tv_title_categories);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    itemClickListener.setOnItemClick(getAdapterPosition());
//                }
//            });
        }
    }

    public interface IOnItemClickListener {
        void setOnItemClick(int position);
    }
}
