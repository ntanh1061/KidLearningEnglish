package anhnt.pickidlearning.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import anhnt.pickidlearning.R;
import anhnt.pickidlearning.models.Item;

/**
 * Created by Administrator on 2/13/2017.
 */

public class DetailPagerAdapter extends PagerAdapter {

    private List<Item> mPaths;
    private Context mContext;
    private LayoutInflater mInflater;

    public DetailPagerAdapter(Context mContext, List<Item> mPaths) {
        this.mPaths = mPaths;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mPaths.size();
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
                getIdentifier(mPaths.get(position).getImage().toString(), "drawable", mContext.getPackageName()));
        img.setImageDrawable(drawable);
        container.addView(view);


        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
