package in.completecourse.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.completecourse.PDFActivity;
import in.completecourse.R;
import in.completecourse.SubjectActivity;
import in.completecourse.VideoActivity;
import in.completecourse.adapter.ClassChaptersAdapter;
import in.completecourse.app.AppConfig;
import in.completecourse.model.ChapterItem;
import in.completecourse.utils.ListConfig;


public class ClassDetailsFragment extends Fragment {
    private ArrayList<ChapterItem> chapterItemArrayList;
    private static String subjectStringFinal;
    private static String classStringFinal;
    private ClassChaptersAdapter adapter;
    private InterstitialAd mInterstitialAd;
    private FirebaseFirestore db;
    private Boolean adsense, inHouse, interstitial, banner;
    private String mBannerUrl, mIconUrl, mInstallUrl, mName, mRating;
    private AdRequest adRequest;
    private ProgressDialog pDialog;

    @BindView(R.id.important_concepts_view) View importantConcepts;
    @BindView(R.id.answer_key_view) View answerKey;
    @BindView(R.id.video_view) View video;
    @BindView(R.id.other_view) View impQues;
    @BindView(R.id.adView_banner_class_details) AdView mAdView;
    @BindView(R.id.linear_in_house) LinearLayout mLinearInHouse;
    @BindView(R.id.app_banner) ImageView mInHouseBanner;
    @BindView(R.id.app_icon) ImageView mInHouseAppIcon;
    @BindView(R.id.app_name) TextView mInhouseAppName;
    @BindView(R.id.app_rating) TextView mInHouseRating;
    @BindView(R.id.btn_install) Button mInHouseInstallButton;
    @BindView(R.id.image_answer_key) ImageView imageAnswerKey;
    @BindView(R.id.image_important_concepts) ImageView imageImaportantConcepts;
    @BindView(R.id.image_video) ImageView imageVideo;
    @BindView(R.id.image_other) ImageView imageImportantQues;
    @BindView(R.id.text_answer_key) TextView textAnswerKey;
    @BindView(R.id.text_imp_concepts) TextView textImportantConcepts;
    @BindView(R.id.tv_adhyay_ka_naam) TextView addhyayeTextView;
    @BindView(R.id.text_total_answer_key) TextView textTotalAnswerKey;
    @BindView(R.id.text_total_important_concepts) TextView textTotalImportantConcepts;
    @BindView(R.id.text_total_video) TextView textTotalVideo;
    @BindView(R.id.text_video) TextView textVideo;
    @BindView(R.id.text_total_other) TextView textTotalImpQues;
    @BindView(R.id.text_other) TextView textImportantQues;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_class_details, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();

        adRequest = new AdRequest.Builder().build();

        setAds();

        mInterstitialAd = new InterstitialAd(Objects.requireNonNull(getActivity()));
        mInterstitialAd.setAdUnitId(getActivity().getResources().getString(R.string.interstitial_ad_id));

        answerKey.setSelected(true);
        importantConcepts.setSelected(false);
        video.setSelected(false);

        chapterItemArrayList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        if (SubjectActivity.intent != null) {
            subjectStringFinal = SubjectActivity.subjectString;
            classStringFinal = SubjectActivity.classString;
            String[] dataObj = new String[2];
            dataObj[0] = classStringFinal;
            dataObj[1] = subjectStringFinal;
            JSONTransmitter jsonTransmitter = new JSONTransmitter(ClassDetailsFragment.this);
            jsonTransmitter.execute(dataObj);
        }


        SubjectActivity.subjectViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (classStringFinal.equalsIgnoreCase(ListConfig.classCode[0])){
                    subjectStringFinal = ListConfig.subjectCodeNinth[i % 4];
                }else if (classStringFinal.equalsIgnoreCase(ListConfig.classCode[1])){
                    subjectStringFinal = ListConfig.subjectCodeTenth[i % 4];
                }else if (classStringFinal.equalsIgnoreCase(ListConfig.classCode[2])){
                    subjectStringFinal = ListConfig.subjectCodeEleven[i % 4];
                }else{
                    subjectStringFinal = ListConfig.subjectCodeTwelve[i % 4];
                }

                importantConcepts.setSelected(false);
                answerKey.setSelected(true);
                answerKey.setBackground((getContext()).getResources().getDrawable(R.drawable.video_selected));
                imageAnswerKey.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_answer_key));
                textAnswerKey.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
                textTotalAnswerKey.setTextColor(getContext().getResources().getColor(R.color.colorWhite));

                importantConcepts.setBackground(getContext().getResources().getDrawable(R.drawable.not_selected));
                textTotalImportantConcepts.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
                imageImaportantConcepts.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_concept_default));
                textImportantConcepts.setTextColor(getContext().getResources().getColor(R.color.colorBlack));

                addhyayeTextView.setVisibility(View.VISIBLE);
                video.setBackground(getContext().getResources().getDrawable(R.drawable.not_selected));
                textTotalVideo.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
                imageVideo.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_video_player_default));
                textVideo.setTextColor(getContext().getResources().getColor(R.color.colorBlack));

                //Other Important Questions View
                impQues.setBackground(getContext().getResources().getDrawable(R.drawable.not_selected));
                textTotalImpQues.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
                imageImportantQues.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_imp_question_default));
                textImportantQues.setTextColor(getContext().getResources().getColor(R.color.colorBlack));

                String[] dataObj = new String[2];
                dataObj[0] = classStringFinal;
                dataObj[1] = subjectStringFinal;
                JSONTransmitter jsonTransmitter = new JSONTransmitter(ClassDetailsFragment.this);
                jsonTransmitter.execute(dataObj);
            }

            @Override
            public void onPageScrollStateChanged(int i) {


            }
        });
    }

    @OnClick(R.id.answer_key_view)
    void answerKey(){
        answerKey.setSelected(true);
        importantConcepts.setSelected(false);
        video.setSelected(false);
        impQues.setSelected(false);

        if (getContext() != null) {
            //NCERT Answer Key View
            answerKey.setBackground(getContext().getResources().getDrawable(R.drawable.video_selected));
            imageAnswerKey.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_answer_key));
            textAnswerKey.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
            textTotalAnswerKey.setTextColor(getContext().getResources().getColor(R.color.colorWhite));

            //Important Concepts View
            importantConcepts.setBackground(getContext().getResources().getDrawable(R.drawable.not_selected));
            textTotalImportantConcepts.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
            imageImaportantConcepts.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_concept_default));
            textImportantConcepts.setTextColor(getContext().getResources().getColor(R.color.colorBlack));

            //Video View
            video.setBackground(getContext().getResources().getDrawable(R.drawable.not_selected));
            imageVideo.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_video_player_default));
            textVideo.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
            textTotalVideo.setTextColor(getContext().getResources().getColor(R.color.colorBlack));

            //Other Important Questions View
            impQues.setBackground(getContext().getResources().getDrawable(R.drawable.not_selected));
            textTotalImpQues.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
            imageImportantQues.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_imp_question_default));
            textImportantQues.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
        }

        //Adhyay TextView
        addhyayeTextView.setVisibility(View.VISIBLE);
    }
    @OnClick(R.id.important_concepts_view)
    void importantConcepts(){
        importantConcepts.setSelected(true);
        answerKey.setSelected(false);
        video.setSelected(false);
        impQues.setSelected(false);

        if (getContext() != null) {
            //Important Concepts View
            importantConcepts.setBackground(getContext().getResources().getDrawable(R.drawable.video_selected));
            imageImaportantConcepts.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_concept));
            textImportantConcepts.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
            textTotalImportantConcepts.setTextColor(getContext().getResources().getColor(R.color.colorWhite));

            //NCERT Answer Key View
            answerKey.setBackground(getContext().getResources().getDrawable(R.drawable.not_selected));
            textTotalAnswerKey.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
            imageAnswerKey.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_answer_key_default));
            textAnswerKey.setTextColor(getContext().getResources().getColor(R.color.colorBlack));

            //Video View
            video.setBackground(getContext().getResources().getDrawable(R.drawable.not_selected));
            imageVideo.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_video_player_default));
            textVideo.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
            textTotalVideo.setTextColor(getContext().getResources().getColor(R.color.colorBlack));

            //Other Important Questions View
            impQues.setBackground(getContext().getResources().getDrawable(R.drawable.not_selected));
            textTotalImpQues.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
            imageImportantQues.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_imp_question_default));
            textImportantQues.setTextColor(getContext().getResources().getColor(R.color.colorBlack));

            textAnswerKey.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
        }

        addhyayeTextView.setVisibility(View.VISIBLE);
    }
    @OnClick(R.id.video_view)
    void video(){
        video.setSelected(true);
        answerKey.setSelected(false);
        importantConcepts.setSelected(false);
        impQues.setSelected(false);

        if (getContext() != null) {
            //Video View
            video.setBackground(getContext().getResources().getDrawable(R.drawable.video_selected));
            imageVideo.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_video_player));
            textVideo.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
            textTotalVideo.setTextColor(getContext().getResources().getColor(R.color.colorWhite));

            //Important Concepts View
            importantConcepts.setBackground(getContext().getResources().getDrawable(R.drawable.not_selected));
            textTotalImportantConcepts.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
            imageImaportantConcepts.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_concept_default));
            textImportantConcepts.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
            //Answer Key View
            answerKey.setBackground(getContext().getResources().getDrawable(R.drawable.not_selected));
            textTotalAnswerKey.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
            imageAnswerKey.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_answer_key_default));
            textAnswerKey.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
            //Other Important Questions View
            impQues.setBackground(getContext().getResources().getDrawable(R.drawable.not_selected));
            textTotalImpQues.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
            imageImportantQues.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_imp_question_default));
            textImportantQues.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
        }

        //Adhyay Ka Naam TextView
        addhyayeTextView.setVisibility(View.VISIBLE);
    }
    @OnClick(R.id.other_view)
    void otherView(){
        impQues.setSelected(true);
        answerKey.setSelected(false);
        importantConcepts.setSelected(false);
        video.setSelected(false);

        if (getContext() != null) {
            //Important Question View
            impQues.setBackground(getContext().getResources().getDrawable(R.drawable.video_selected));
            imageImportantQues.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_imp_question));
            textImportantQues.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
            textTotalImpQues.setTextColor(getContext().getResources().getColor(R.color.colorWhite));

            //Important Concepts View
            importantConcepts.setBackground(getContext().getResources().getDrawable(R.drawable.not_selected));
            textTotalImportantConcepts.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
            imageImaportantConcepts.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_concept_default));
            textImportantConcepts.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
            //Answer Key View
            answerKey.setBackground(getContext().getResources().getDrawable(R.drawable.not_selected));
            textTotalAnswerKey.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
            imageAnswerKey.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_answer_key_default));
            textAnswerKey.setTextColor(getContext().getResources().getColor(R.color.colorBlack));

            video.setBackground(getContext().getResources().getDrawable(R.drawable.not_selected));
            imageVideo.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_video_player_default));
            textVideo.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
            textTotalVideo.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
        }

        //Adhyaya Ka Naam TextView
        addhyayeTextView.setVisibility(View.VISIBLE);
    }

    private void setAds(){
        db.collection("flags").document("ads_flags").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(final DocumentSnapshot documentSnapshot) {
                adsense = documentSnapshot.getBoolean("adsense");
                inHouse = documentSnapshot.getBoolean("in_house");
                interstitial = documentSnapshot.getBoolean("interstitial");
                banner = documentSnapshot.getBoolean("banner");

                if (adsense){
                    if (banner) {
                        mAdView.setVisibility(View.VISIBLE);
                        mLinearInHouse.setVisibility(View.GONE);
                        mAdView.loadAd(adRequest);
                    }
                    if (interstitial) {
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
                        mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                                //Load the next interstitial ad
                                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                            }
                        });
                    }
                }else if (inHouse){
                    mLinearInHouse.setVisibility(View.VISIBLE);
                    mAdView.setVisibility(View.GONE);

                    db.collection("in_house_ads").whereEqualTo("is_live", true).get().addOnSuccessListener(document -> {
                        for (QueryDocumentSnapshot doc : document) {
                            Log.d("document", doc.getId() + " => " + doc.getData());
                            mBannerUrl = doc.getString("banner_url");
                            mIconUrl = doc.getString("icon_url");
                            mInstallUrl = doc.getString("install_url");
                            mName = doc.getString("name");
                            mRating = String.valueOf(doc.get("rating"));
                        }


                        if (getContext() != null) {
                            Glide.with(getContext()).load(mBannerUrl).into(mInHouseBanner);
                            Glide.with(getContext()).load(mIconUrl).into(mInHouseAppIcon);
                        }
                        mInhouseAppName.setText(mName);
                        mInHouseRating.setText(mRating);

                        mInHouseInstallButton.setOnClickListener(v -> {
                            Intent intentRate = new Intent("android.intent.action.VIEW",
                                    Uri.parse(mInstallUrl));
                            startActivity(intentRate);

                        });

                    }).addOnFailureListener(e -> Log.e("exception", "exception" + e.getMessage()));
                }else{
                    mAdView.setVisibility(View.GONE);
                    mLinearInHouse.setVisibility(View.GONE);
                }
            }
        });
    }

    static class JSONTransmitter extends AsyncTask<String, String, String> {
        private final WeakReference<ClassDetailsFragment> activityWeakReference;

        JSONTransmitter(ClassDetailsFragment context){
            activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            ClassDetailsFragment activity = activityWeakReference.get();
            activity.progressBar.setVisibility(View.VISIBLE);
            activity.clear();
        }

        @Override
        protected String doInBackground(String... params) {
            final ClassDetailsFragment activity = activityWeakReference.get();
            String urlString = AppConfig.URL_CHAPTERS;
            String studentclass =  params[0];
            String studentsubject = params[1];
            URL url;
            InputStream stream;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);

                String data = URLEncoder.encode("studentclass", "UTF-8")
                        + "=" + URLEncoder.encode(studentclass, "UTF-8");

                data += "&" + URLEncoder.encode("studentsubject", "UTF-8") + "="
                        + URLEncoder.encode(studentsubject, "UTF-8");
                urlConnection.connect();
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(data);
                wr.flush();

                stream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8), 8);

                String resFromServer = reader.readLine();
                String status;
                JSONObject jsonResponse;
                try {
                    jsonResponse = new JSONObject(resFromServer);
                    //Log.e("chatpterItem", String.valueOf(jsonResponse));
                    status = jsonResponse.getString("status");
                    if (status.equals("true")){
                        JSONObject jsonObject = new JSONObject(resFromServer);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ChapterItem item = new ChapterItem();
                            JSONObject chapterObject = jsonArray.getJSONObject(i);
                            item.setChapterKaName(chapterObject.getString("ChapterKaName"));
                            item.setChapterKaFlipURL(chapterObject.getString("ChapterKaFlipURL"));
                            item.setConceptKaFlipURL(chapterObject.getString("ConceptKaFlipURL"));
                            item.setChapterKaVideoID(chapterObject.getString("ChapterKaVideo"));
                            item.setOtherImportantQues(chapterObject.getString("otherimgques"));
                            item.setChapterSerial((i + 1) + ".");
                            activity.chapterItemArrayList.add(item);
                        }
                    }else{
                        final String msg = jsonResponse.getString("message");
                        if (activity.getActivity() != null) {
                            activity.getActivity().runOnUiThread(() -> Toast.makeText(activity.getContext(), msg, Toast.LENGTH_SHORT).show());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null; //reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.i("Result", "SLEEP ERROR");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonObject) {
            final ClassDetailsFragment activity = activityWeakReference.get();
            activity.progressBar.setVisibility(View.GONE);
            activity.adapter = new ClassChaptersAdapter(activity.getActivity(), activity.chapterItemArrayList);
            activity.recyclerView.setAdapter(activity.adapter);
            int count = activity.adapter.getItemCount();
            activity.textTotalAnswerKey.setText(String.valueOf(count));
            activity.textTotalImportantConcepts.setText(String.valueOf(count));
            activity.textTotalVideo.setText(String.valueOf(count));
            activity.textTotalImpQues.setText(String.valueOf(count));
            activity.recyclerView.addOnItemTouchListener(new ClassChaptersAdapter.RecyclerTouchListener(activity.getContext(), position -> {
                if (activity.mInterstitialAd.isLoaded()) activity.mInterstitialAd.show();
                Intent intent = new Intent(activity.getActivity(), PDFActivity.class);
                Intent intentVideo = new Intent(activity.getActivity(), VideoActivity.class);
                if (activity.answerKey.isSelected()) {
                    intent.putExtra("url", activity.chapterItemArrayList.get(position).getChapterKaFlipURL());
                    activity.startActivity(intent);
                }else if (activity.importantConcepts.isSelected()) {
                    intent.putExtra("url", activity.chapterItemArrayList.get(position).getConceptKaFlipURL());
                    activity.startActivity(intent);
                }else if (activity.video.isSelected()){
                    intentVideo.putExtra("videoID", activity.chapterItemArrayList.get(position).getChapterKaVideoID());
                    activity.startActivity(intentVideo);
                }else if (activity.impQues.isSelected()){
                    intent.putExtra("url", activity.chapterItemArrayList.get(position).getOtherImportantQues());
                    activity.startActivity(intent);
                }
            }));
        }
    }

    private void clear() {
        final int size = chapterItemArrayList.size();
        if (size > 0) {
            chapterItemArrayList.subList(0, size).clear();
            adapter.notifyItemRangeRemoved(0, size);
        }
    }

}