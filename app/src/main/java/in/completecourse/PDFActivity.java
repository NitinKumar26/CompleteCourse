package in.completecourse;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PDFActivity extends AppCompatActivity {


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        /*
        Uri uri = Uri.parse(getIntent().getStringExtra("url"));

        PDFView pdfView = findViewById(R.id.pdfView);
        pdfView.fromSource(url).load();*/
        //String url = getIntent().getStringExtra("url");
        //Using the configuration builder, you can define options for the activity.
        //final PdfActivityConfiguration config = new PdfActivityConfiguration.Builder(this).build();

        //Launch the activity, viewing the PDF document directly from the assets.
        //PdfActivity.showDocument(this, Uri.parse(url), config);
        String url = getIntent().getStringExtra("url");


        //WebView myWebView = new WebView(this);
        //setContentView(myWebView);
        WebView webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        //webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);


        //webView.clearCache(true);

        //myWebView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);
        //finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
