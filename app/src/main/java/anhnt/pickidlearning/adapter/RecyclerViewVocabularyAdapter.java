package anhnt.pickidlearning.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public RecyclerViewVocabularyAdapter(Context context, List<Item> text) {
        this.context = context;
        this.text = text;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_view_vocabulary_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvText.setText(text.get(position).getName().toString());
    }

    @Override
    public int getItemCount() {
        return text.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvText;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvText = (TextView) itemView.findViewById(R.id.tv_vocabulary);
        }
    }
}
