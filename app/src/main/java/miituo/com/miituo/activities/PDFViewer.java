package miituo.com.miituo.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;

import java.util.List;

import miituo.com.miituo.R;
import miituo.com.miituo.tuto.TutorialActivity;

public class PDFViewer extends BaseActivity implements OnPageChangeListener,OnLoadCompleteListener {
    PDFView pdfView;
    Integer pageNumber = 0;
    String pdfFileName;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
                finish();
        }
        else if (item.getItemId() == R.id.menu_share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            Uri screenshotUri = Uri.parse(DetallesActivity.pdf.getAbsolutePath());
            sharingIntent.setType("*/*");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
            startActivity(Intent.createChooser(sharingIntent, "Share image using"));
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);

        boolean isPoliza = getIntent().getBooleanExtra("isPoliza",true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (isPoliza) {
            toolbar.setTitle("Póliza");
        } else {
            toolbar.setTitle("Estado de cuenta");
        }
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pdfView= (PDFView)findViewById(R.id.pdfView);
        displayPDF();
    }

    private void displayPDF() {
//        pdfView.fromAsset(SAMPLE_FILE)
        pdfView.fromFile(DetallesActivity.pdf)
                .defaultPage(pageNumber)
                .enableSwipe(true)

                .swipeHorizontal(false)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }


    @Override    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }


    @Override    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        printBookmarksTree(pdfView.getTableOfContents(), "-");
    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            print(String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }
}
