package in.completecourse.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import in.completecourse.R;
import in.completecourse.SubjectActivity;
import in.completecourse.fragment.ItemFragment;
import in.completecourse.utils.CarouselLinearLayout;
import in.completecourse.utils.ListConfig;

public class CarouselPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

    private final static float BIG_SCALE = 1.0f;
    private final static float SMALL_SCALE = 0.7f;
    private final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;
    private final SubjectActivity context;
    private final FragmentManager fragmentManager;
    private float scale;

    public CarouselPagerAdapter(SubjectActivity context, FragmentManager fm) {
        super(fm);
        this.fragmentManager = fm;
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        // make the first pager bigger than others
        try {
            if (position == SubjectActivity.FIRST_PAGE)
                scale = BIG_SCALE;
            else
                scale = SMALL_SCALE;
            if (SubjectActivity.classString.equalsIgnoreCase("4") ||
                    SubjectActivity.classString.equalsIgnoreCase("1")) {
                position = position % ListConfig.subjectHighSchool.length;
            }else{
                position = position % SubjectActivity.count;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ItemFragment.newInstance(context, position, scale);
    }

    @Override
    public int getCount() {
        int count = 0;
        try {
            count = SubjectActivity.count * SubjectActivity.LOOPS;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        try {
            if (positionOffset >= 0f && positionOffset <= 1f) {
                CarouselLinearLayout cur = getRootView(position);
                CarouselLinearLayout next = getRootView(position + 1);
                cur.setScaleBoth(BIG_SCALE - (DIFF_SCALE * positionOffset));
                next.setScaleBoth(SMALL_SCALE + (DIFF_SCALE * positionOffset));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @SuppressWarnings("ConstantConditions")
    private CarouselLinearLayout getRootView(int position) {
        return (CarouselLinearLayout) fragmentManager.findFragmentByTag(this.getFragmentTag(position))
                .getView().findViewById(R.id.root_container);
    }

    private String getFragmentTag(int position) {
        return "android:switcher:" + SubjectActivity.subjectViewPager.getId() + ":" + position;
    }
}
