package anhnt.pickidlearning.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import anhnt.pickidlearning.R;
import anhnt.pickidlearning.models.Item;

/**
 * Created by AnhNT on 3/1/2017.
 */

public class RecyclerViewVocabularyAdapter extends RecyclerView.Adapter<RecyclerViewVocabularyAdapter.MyViewHolder> {
    private Context context;
    private List<Item> text;
    private LayoutInflater inflater;
    private IOnClickListener onItemClick;
    public View view;
    private int row = 0;

    public RecyclerViewVocabularyAdapter(Context context, List<Item> text) {
        this.context = context;
        this.text = text;
        inflater = LayoutInflater.from(context);
    }

    public void setOnItemClick(IOnClickListener onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_view_vocabulary_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tvText.setText(text.get(position).getName().toString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.setOnItemClick(position);
                row = position;
                notifyDataSetChanged();
            }
        });
        if (row == position) {
            holder.linearLayout.setBackgroundColor(Color.BLUE);
        } else {
            holder.linearLayout.setBackgroundColor(Color.WHITE);
        }

    }

    @Override
    public int getItemCount() {
        return text.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvText;
        LinearLayout linearLayout;

        public MyViewHolder(final View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linear_layout_item);
            tvText = (TextView) itemView.findViewById(R.id.tv_vocabulary);

        }

    }

    public interface IOnClickListener {
        void setOnItemClick(int position);
    }

}
