package io.fusionbit.vcarrycustomer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityWebInvoice extends BaseActivity {

    private ProgressDialog pd;

    private int salesId;

    @BindView(R.id.wb_printReceipt)
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        setTitle("Trip Invoice");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        salesId = getIntent().getIntExtra("sales_id", 0);

        if (salesId == 0) {
            Toast.makeText(this, "Invalid request", Toast.LENGTH_SHORT).show();
            finish();
        }

        pd = ProgressDialog.show(this, "Please Wait...", "Loading Invoice...", true, false);
        pd.setCanceledOnTouchOutside(false);

        doWebViewPrint(salesId);


    }

    private void doWebViewPrint(int receiptId) {
        // Create a WebView object specifically for printing

        mWebView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i(App.APP_TAG, "page finished loading " + url);
                mWebView = view;
                pd.dismiss();
            }
        });

        // Generate an HTML document on the fly:
        /*String htmlDocument = "<html><body><h1>Test Content</h1><p>Testing, " +
                "testing, testing...</p></body></html>";
        webView.loadDataWithBaseURL(null, htmlDocument, "text/HTML", "UTF-8", null);*/
        mWebView.clearHistory();
        mWebView.clearCache(true);

        String newUA = "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
        mWebView.getSettings().setUserAgentString(newUA);

        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);

        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(false);

        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.setScrollbarFadingEnabled(false);

        try {
            mWebView.loadUrl("http://tapandtype.com/vcarry/invoice/index.php?view=invoice&id=" + receiptId);
        } catch (Exception e) {
            pd.dismiss();
            Toast.makeText(this, "Failed to load URL", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_web_invoice;
    }

    @Override
    protected void internetNotAvailable() {

    }

    @Override
    protected void internetAvailable() {

    }


    public static void start(final Context context, final int salesId) {
        Intent i = new Intent(context, ActivityWebInvoice.class);
        i.putExtra("sales_id", salesId);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
