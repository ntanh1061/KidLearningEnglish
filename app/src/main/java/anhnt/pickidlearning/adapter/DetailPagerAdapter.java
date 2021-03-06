package anhnt.pickidlearning.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import anhnt.pickidlearning.R;
import anhnt.pickidlearning.models.Item;

/**
 * Created by Administrator on 2/13/2017.
 */

public class DetailPagerAdapter extends PagerAdapter {

    private List<Item> mItems;
    private Context mContext;
    private LayoutInflater mInflater;

    public DetailPagerAdapter(Context mContext, List<Item> items) {
        this.mItems = items;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mInflater.inflate(R.layout.view_pager_item, container, false);
        ImageView img = (ImageView) view.findViewById(R.id.img_detail);
        Drawable drawable = mContext.getResources().getDrawable(mContext.getResources().
                getIdentifier(mItems.get(position).getImage().toString(), "drawable", mContext.getPackageName()));
        img.setImageDrawable(drawable);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
