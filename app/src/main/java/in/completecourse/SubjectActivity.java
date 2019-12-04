package in.completecourse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import in.completecourse.adapter.CarouselPagerAdapter;
import in.completecourse.fragment.ClassDetailsFragment;
import in.completecourse.helper.HelperMethods;

public class SubjectActivity extends AppCompatActivity implements View.OnClickListener {
    public static ViewPager subjectViewPager;
    public final static int LOOPS = 4;
    public static final int count = 4;
    public static Intent intent;
    //public static Spinner classSpinner;
    public static String subjectString, classString;

    /**
     * You shouldn't define first page = 0
     * Let's define first page = 'viewpager size' to make endless carousel
     */
    public static final int FIRST_PAGE = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        subjectViewPager = findViewById(R.id.viewpagerr);

        //finding view by id and setting the clickListener
        findViewById(R.id.nextItmLy).setOnClickListener(this);
        findViewById(R.id.previceItmLy).setOnClickListener(this);
        findViewById(R.id.imgBack).setOnClickListener(this);
        findViewById(R.id.ic_competition_updates).setOnClickListener(this);

        intent = getIntent();
        intent.getStringExtra("type");
        subjectString = getIntent().getStringExtra("subjectCode");
        classString = getIntent().getStringExtra("classCode");

        subjectViewPager.setPageMargin(-HelperMethods.getPageMargin(SubjectActivity.this));

        CarouselPagerAdapter adapter = new CarouselPagerAdapter(this, getSupportFragmentManager());
        subjectViewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        subjectViewPager.addOnPageChangeListener(adapter);
        subjectViewPager.setOffscreenPageLimit(3);

        setViewPagerItem(classString, subjectString);

        HelperMethods.loadFragment(new ClassDetailsFragment(), SubjectActivity.this);

    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.imgBack:
                finish();
                break;
            case R.id.ic_competition_updates:
                intent = new Intent(SubjectActivity.this, CompetitionUpdatesActivity.class);
                startActivity(intent);
                break;
            case R.id.nextItmLy:
                subjectViewPager.setCurrentItem(subjectViewPager.getCurrentItem()+1);
                break;
            case R.id.previceItmLy:
                subjectViewPager.setCurrentItem(subjectViewPager.getCurrentItem()-1);
                break;
        }
    }



    private void setViewPagerItem(String classString, String subjectString){
        if (classString.equalsIgnoreCase("4") && subjectString.equalsIgnoreCase("1")
                || classString.equalsIgnoreCase("1") && subjectString.equalsIgnoreCase("2")
                || classString.equalsIgnoreCase("2") && subjectString.equalsIgnoreCase("7")
                || classString.equalsIgnoreCase("3") && subjectString.equalsIgnoreCase("8")){
            subjectViewPager.setCurrentItem(0);
        }else if (classString.equalsIgnoreCase("4") && subjectString.equalsIgnoreCase("16")
                || classString.equalsIgnoreCase("1") && subjectString.equalsIgnoreCase("13")
                || classString.equalsIgnoreCase("2") && subjectString.equalsIgnoreCase("9")
                || classString.equalsIgnoreCase("3") && subjectString.equalsIgnoreCase("10")){
            subjectViewPager.setCurrentItem(1);
        }else if (classString.equalsIgnoreCase("2") && subjectString.equalsIgnoreCase("5")
                || classString.equalsIgnoreCase("3") && subjectString.equalsIgnoreCase("6")
                || classString.equalsIgnoreCase("4") && subjectString.equalsIgnoreCase("4")
                || classString.equalsIgnoreCase("1") && subjectString.equalsIgnoreCase("3")){
            subjectViewPager.setCurrentItem(2);
        }else if (classString.equalsIgnoreCase("2") && subjectString.equalsIgnoreCase("11")
                || classString.equalsIgnoreCase("3") && subjectString.equalsIgnoreCase("12")
                || classString.equalsIgnoreCase("4") && subjectString.equalsIgnoreCase("15")
                || classString.equalsIgnoreCase("1") && subjectString.equalsIgnoreCase("14")){
            subjectViewPager.setCurrentItem(3);
        }
    }
}
