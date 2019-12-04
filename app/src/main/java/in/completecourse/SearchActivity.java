package in.completecourse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import in.completecourse.adapter.SpinAdapter;
import in.completecourse.utils.ListConfig;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private String[] spinnerItemsArray;
    private static String mClassString;
    private Spinner classSelectionSpinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTitle(getResources().getString(R.string.app_name));

        //Find the required Views
        findViewById(R.id.layout_physics).setOnClickListener(this);
        findViewById(R.id.layout_chemistry).setOnClickListener(this);
        findViewById(R.id.layout_maths_11_12).setOnClickListener(this);
        findViewById(R.id.layout_biology).setOnClickListener(this);
        findViewById(R.id.science_layout).setOnClickListener(this);
        findViewById(R.id.mathematics_9_10_layout).setOnClickListener(this);
        findViewById(R.id.science_english).setOnClickListener(this);
        //findViewById(R.id.imgCall).setOnClickListener(this);
        findViewById(R.id.math_layout_eng).setOnClickListener(this);

        final LinearLayout mNineTenLayout = findViewById(R.id.nineTen);
        final LinearLayout mElevenTwelveLayout = findViewById(R.id.elevenTwelve);

        classSelectionSpinner = findViewById(R.id.classSelection);
        spinnerItemsArray = new String[]{"9", "10", "11", "12"};
        SpinAdapter spinnerAdapter = new SpinAdapter(SearchActivity.this, R.layout.spinner_row , spinnerItemsArray);
        classSelectionSpinner.setAdapter(spinnerAdapter);
        classSelectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mClassString = spinnerItemsArray[position];

                if (mClassString.equalsIgnoreCase("9") || mClassString.equalsIgnoreCase("10")){
                    mNineTenLayout.setVisibility(View.VISIBLE);
                    mElevenTwelveLayout.setVisibility(View.GONE);
                }else{
                    mElevenTwelveLayout.setVisibility(View.VISIBLE);
                    mNineTenLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        String classString = classSelectionSpinner.getSelectedItem().toString();
        switch (view.getId()){
            case R.id.layout_physics:
                intent = new Intent(SearchActivity.this, SubjectActivity.class);
                if (classString.equalsIgnoreCase("11")){
                    intent.putExtra("classCode", ListConfig.classCode[2]);
                    intent.putExtra("subjectCode", ListConfig.subjectCodeEleven[0]);
                    startActivity(intent);
                }else {
                    intent.putExtra("classCode", ListConfig.classCode[3]);
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTwelve[0]);
                    startActivity(intent);
                }
                break;
            case R.id.layout_chemistry:
                intent = new Intent(SearchActivity.this, SubjectActivity.class);
                if (classString.equalsIgnoreCase("11")){
                    intent.putExtra("classCode", ListConfig.classCode[2]);
                    intent.putExtra("subjectCode", ListConfig.subjectCodeEleven[1]);
                    startActivity(intent);
                }else {
                    intent.putExtra("classCode", ListConfig.classCode[3]);
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTwelve[1]);
                    startActivity(intent);
                }
                break;
            case R.id.layout_maths_11_12:
                intent = new Intent(SearchActivity.this, SubjectActivity.class);
                if (classString.equalsIgnoreCase("11")){
                    intent.putExtra("classCode", ListConfig.classCode[2]);
                    intent.putExtra("subjectCode", ListConfig.subjectCodeEleven[2]);
                    startActivity(intent);
                }else {
                    intent.putExtra("classCode", ListConfig.classCode[3]);
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTwelve[2]);
                    startActivity(intent);
                }
                break;
            case R.id.layout_biology:
                intent = new Intent(SearchActivity.this, SubjectActivity.class);
                if (classString.equalsIgnoreCase("11")){
                    intent.putExtra("classCode", ListConfig.classCode[2]);
                    intent.putExtra("subjectCode", ListConfig.subjectCodeEleven[3]);
                    startActivity(intent);
                }else {
                    intent.putExtra("classCode", ListConfig.classCode[3]);
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTwelve[3]);
                    startActivity(intent);
                }
                break;
            case R.id.science_layout:
                intent = new Intent(SearchActivity.this, SubjectActivity.class);
                if (classString.equalsIgnoreCase("9")){
                    intent.putExtra("classCode", ListConfig.classCode[0]);
                    intent.putExtra("subjectCode", ListConfig.subjectCodeNinth[0]);
                    startActivity(intent);
                }else {
                    intent.putExtra("classCode", ListConfig.classCode[1]);
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTenth[0]);
                    startActivity(intent);
                }
                break;
            case R.id.science_english:
                intent = new Intent(SearchActivity.this, SubjectActivity.class);
                if (classString.equalsIgnoreCase("9")){
                    intent.putExtra("classCode", ListConfig.classCode[0]);
                    intent.putExtra("subjectCode", ListConfig.subjectCodeNinth[1]);
                    startActivity(intent);
                }else {
                    intent.putExtra("classCode", ListConfig.classCode[1]);
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTenth[1]);
                    startActivity(intent);
                }
                break;
            case R.id.mathematics_9_10_layout:
                intent = new Intent(SearchActivity.this, SubjectActivity.class);
                if (classString.equalsIgnoreCase("9")){
                    intent.putExtra("classCode", ListConfig.classCode[0]);
                    intent.putExtra("subjectCode", ListConfig.subjectCodeNinth[2]);
                    startActivity(intent);
                }else {
                    intent.putExtra("classCode", ListConfig.classCode[1]);
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTenth[2]);
                    startActivity(intent);
                }
                break;
            case R.id.math_layout_eng:
                intent = new Intent(SearchActivity.this, SubjectActivity.class);
                if (classString.equalsIgnoreCase("9")){
                    intent.putExtra("classCode", ListConfig.classCode[0]);
                    intent.putExtra("subjectCode", ListConfig.subjectCodeNinth[3]);
                    startActivity(intent);
                }else {
                    intent.putExtra("classCode", ListConfig.classCode[1]);
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTenth[3]);
                    startActivity(intent);
                }
                break;
        }
    }
}
