package miituo.com.miituo.activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.MenuItem;
import android.widget.TextView;
import miituo.com.miituo.R;
import miituo.com.miituo.data.IinfoClient;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class InfoCancelActivity extends AppCompatActivity {

    private TextView lb1,lb2,lb3;
    public Typeface typeface;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_cancel);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Tus pólizas");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        //get back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        typeface = Typeface.createFromAsset(getAssets(), "fonts/herne1.ttf");
        lb1=(TextView)findViewById(R.id.lb1);
        lb1.setTypeface(typeface);
        lb2=(TextView)findViewById(R.id.lb2);
        lb2.setTypeface(typeface);
        lb3=(TextView)findViewById(R.id.lb3);
        lb3.setTypeface(typeface);

        String vacacionesHelp="La cancelación de tu póliza\n"+ IinfoClient.getInfoClientObject().getPolicies().getNoPolicy()+"\n está en proceso.";
        SpannableStringBuilder str = new SpannableStringBuilder(vacacionesHelp);
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                27, vacacionesHelp.indexOf(" est"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        lb1.setText(str);
    }
}
