package in.completecourse.fragment;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import in.completecourse.R;
import in.completecourse.SubjectActivity;
import in.completecourse.utils.CarouselLinearLayout;
import in.completecourse.utils.ListConfig;

public class ItemFragment extends Fragment {

    private static final String POSITION = "position";
    private static final String SCALE = "scale";
    private static final String DRAWABLE_RESOURE = "resource";


    public static Fragment newInstance(SubjectActivity context, int pos, float scale) {
        Bundle b = new Bundle();
        b.putInt(POSITION, pos);
        b.putFloat(SCALE, scale);

        return Fragment.instantiate(context, ItemFragment.class.getName(), b);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWidthAndHeight();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth / 2, screenHeight / 2);

        return inflater.inflate(R.layout.subject_row, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert this.getArguments() != null;
        final int position = this.getArguments().getInt(POSITION);
        float scale = this.getArguments().getFloat(SCALE);

        TextView textView = view.findViewById(R.id.pager_textview);
        CarouselLinearLayout root = view.findViewById(R.id.root_container);
        ImageView imageView = view.findViewById(R.id.imgLogo);
        if (SubjectActivity.classString.equalsIgnoreCase("4") ||
                SubjectActivity.classString.equalsIgnoreCase("1")){
            textView.setText(ListConfig.subjectHighSchool[position]);
            imageView.setImageResource(ListConfig.imagesHighSchool[position]);
        }else if (SubjectActivity.classString.equalsIgnoreCase("2") ||
                SubjectActivity.classString.equalsIgnoreCase("3")){
            textView.setText(ListConfig.subjectIntermediate[position]);
            imageView.setImageResource(ListConfig.imagesIntermediate[position]);
        }
        root.setScaleBoth(scale);
    }

    /**
     * Get device screen width and height
     */
    private void getWidthAndHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        if (getActivity() != null) {
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        }
        int screenHeight = displaymetrics.heightPixels;
        int screenWidth = displaymetrics.widthPixels;
    }
}