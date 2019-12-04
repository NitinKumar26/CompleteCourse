package in.completecourse;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.behavior.HideBottomViewOnScrollBehavior;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import in.completecourse.fragment.mainFragment.HomeFragment;
import in.completecourse.fragment.mainFragment.NewArrivalFragment;
import in.completecourse.fragment.mainFragment.NotificationFragment;
import in.completecourse.helper.HelperMethods;


public class MainActivity extends AppCompatActivity {
    private TextView titleText;
    private HomeFragment homeFragment;
    private NewArrivalFragment newArrivalFragment;
    private NotificationFragment notificationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //HelperMethods.changeStatusBarColor(MainActivity.this);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        titleText = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setTitle(null);
        }

        homeFragment = new HomeFragment();
        newArrivalFragment = new NewArrivalFragment();
        notificationFragment = new NotificationFragment();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setItemIconTintList(null);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new HideBottomViewOnScrollBehavior());
        navigation.setSelectedItemId(R.id.action_home);
        HelperMethods.loadFragment(homeFragment, this);

        navigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.action_home:
                    HelperMethods.showFragment(homeFragment, MainActivity.this);
                    HelperMethods.hideFragment(newArrivalFragment, MainActivity.this);
                    HelperMethods.hideFragment(notificationFragment, MainActivity.this);
                    titleText.setText(R.string.home);
                    return true;
                case R.id.action_new_arrivals:
                    HelperMethods.showFragment(newArrivalFragment, MainActivity.this);
                    HelperMethods.hideFragment(homeFragment, MainActivity.this);
                    HelperMethods.hideFragment(notificationFragment, MainActivity.this);
                    titleText.setText(R.string.new_arrivals);
                    return true;
                case R.id.action_notifications:
                    HelperMethods.showFragment(notificationFragment, MainActivity.this);
                    HelperMethods.hideFragment(homeFragment, MainActivity.this);
                    HelperMethods.hideFragment(newArrivalFragment, MainActivity.this);
                    titleText.setText(R.string.notifications);
                    return true;
            }
            return false;
        });
    }
}
