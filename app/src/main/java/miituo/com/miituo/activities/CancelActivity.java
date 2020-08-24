package miituo.com.miituo.activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import miituo.com.miituo.R;
import miituo.com.miituo.data.IinfoClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

public class CancelActivity extends AppCompatActivity {

    private Typeface typeface;
    private TextView lb1, lbAceptar;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Cancelar póliza");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        //get back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        typeface = Typeface.createFromAsset(getAssets(), "fonts/herne1.ttf");

        lb1=(TextView) findViewById(R.id.lb1);
        lb1.setTypeface(typeface);
        lbAceptar=(TextView) findViewById(R.id.lbAceptar);
        lbAceptar.setTypeface(typeface);
        String vacacionesHelp="Sí deseas cancelar tu póliza por favor ponte en contacto con nosotros al (55) 84.21.05.00";
        SpannableStringBuilder str = new SpannableStringBuilder(vacacionesHelp);
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                vacacionesHelp.indexOf(" ("), vacacionesHelp.indexOf(".00")+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        lb1.setText(str);

        lbAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "(55)84210500"));
                if (ActivityCompat.checkSelfPermission(CancelActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    String[] PERMISSIONS = {Manifest.permission.CALL_PHONE};
                    ActivityCompat.requestPermissions((Activity) CancelActivity.this, PERMISSIONS, 122);
                } else {
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 122: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String number = "(55)84210500";
                    Intent intent4 = new Intent(Intent.ACTION_CALL);
                    intent4.setData(Uri.parse("tel:" + number));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(intent4);
                } else {
                    Toast.makeText(CancelActivity.this, "La aplicación no permite abrir el telefono.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
