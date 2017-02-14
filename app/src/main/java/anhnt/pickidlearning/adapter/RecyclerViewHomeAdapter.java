package anhnt.pickidlearning.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import anhnt.pickidlearning.R;
import anhnt.pickidlearning.models.Category;

/**
 * Created by AnhNT on 2/15/2017.
 */

public class RecyclerViewHomeAdapter extends RecyclerView.Adapter<RecyclerViewHomeAdapter.MyViewHolder> {
    private Context context;
    private List<Category> typeList;
    private LayoutInflater inflater;
    private RecyclerViewAdapter.IOnItemClickListener itemClickListener;

    public RecyclerViewHomeAdapter(Context context, List<Category> typeList) {
        this.context = context;
        this.typeList = typeList;
        inflater = LayoutInflater.from(context);
    }

    public void setOnItemClick(RecyclerViewAdapter.IOnItemClickListener itemClick) {
        this.itemClickListener = itemClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycle_view_home_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.btnType.setText(typeList.get(position).getName());
        int resource = context.getResources().getIdentifier(typeList.get(position).getPathImage().toString(), "mipmap", context.getPackageName());
        holder.imageView.setImageDrawable(context.getResources().getDrawable(resource));
    }

    @Override
    public int getItemCount() {
        return typeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView btnType;
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            btnType = (TextView) itemView.findViewById(R.id.tv_name);
            imageView = (ImageView) itemView.findViewById(R.id.imgIcon);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.setOnItemClick(getAdapterPosition());
                }
            });
        }
    }

}
