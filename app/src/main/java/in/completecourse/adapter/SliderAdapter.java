package in.completecourse.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.completecourse.R;
import in.completecourse.model.Update;

public class SliderAdapter extends PagerAdapter {

    private final Activity mContext;
    private ArrayList<Update> mUpdateList;
    private View view;

    public SliderAdapter(Activity context, ArrayList<Update> updateList) {
        this.mContext = context;
        this.mUpdateList = updateList;
    }

    @Override
    public int getCount() {
        return mUpdateList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        Update update = mUpdateList.get(position);
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (layoutInflater != null) {
            view = layoutInflater.inflate(R.layout.slider_item, container, false);
            ImageView imageView = view.findViewById(R.id.slider_imageView);
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Glide.with(mContext)
                            .load(update.getUrl())
                            .placeholder(R.drawable.background_gradient)
                            .into(imageView);
                    ViewPager viewPager = (ViewPager) container;
                    viewPager.addView(view, 0);
                }
            });
        }

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
    public void setItems(ArrayList<Update> updates){
        this.mUpdateList = updates;
    }
}