package miituo.com.miituo.activities;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import miituo.com.miituo.R;

public class ContactaActivity extends AppCompatActivity {

    private static final int REQUEST = 112;
    public String telefon = "";
    public Typeface typeface;
    private TextView lbQuestion,lbFindUs,lbT1,lbT2,lbE,lbAddress;
    private ImageView face,twit,link,inst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Contacto");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        //get back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/herne1.ttf");
        lbQuestion=findViewById(R.id.lbQuestion);
        lbQuestion.setTypeface(typeface,Typeface.BOLD);
        lbFindUs=findViewById(R.id.lbFindUs);
        lbFindUs.setTypeface(typeface,Typeface.BOLD);
        lbAddress=findViewById(R.id.lbAddress);
        lbAddress.setTypeface(typeface);
        lbT1=findViewById(R.id.lbT1);
        lbT1.setTypeface(typeface);
        lbT2=findViewById(R.id.lbT2);
        lbT2.setTypeface(typeface,Typeface.BOLD);
        lbE=findViewById(R.id.lbE);
        lbE.setTypeface(typeface,Typeface.BOLD);

        face=findViewById(R.id.imgFace);
        twit=findViewById(R.id.imgTwit);
        link=findViewById(R.id.imgInk);
        inst=findViewById(R.id.imgIns);

        lbAddress.setText("Bosque de Duraznos 61 Piso 12\n" +
                "Col. Bosques de las Lomas  11700,\n" +
                "Ciudad de México");

        face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(newFacebookIntent());
            }
        });

        twit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String urlString="https://twitter.com/miituo";
                    Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setPackage("com.android.chrome");
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        // Chrome browser presumably not installed so allow user to choose instead
                        intent.setPackage(null);
                        startActivity(intent);
                    }
            }
        });

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlString="https://www.linkedin.com/company/miituo/";
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.chrome");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    // Chrome browser presumably not installed so allow user to choose instead
                    intent.setPackage(null);
                    startActivity(intent);
                }
            }
        });

        inst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlString="https://www.instagram.com/miituo/";
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.chrome");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    // Chrome browser presumably not installed so allow user to choose instead
                    intent.setPackage(null);
                    startActivity(intent);
                }
            }
        });

    }


    /* @param pm
     *     The {@link PackageManager}. You can find this class through {@link
     *     Context#getPackageManager()}.
     * @param url
     *     The full URL to the Facebook page or profile.
     * @return An intent that will open the Facebook page/profile.
     */
    public Intent newFacebookIntent() {
        Uri uri = Uri.parse("https://www.facebook.com/miituo.seguros/");
        PackageManager pm=this.getPackageManager();
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                // http://stackoverflow.com/a/24547437/1048340
                uri = Uri.parse("fb://facewebmodal/f?href=" +"https://www.facebook.com/miituo.seguros/");
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return new Intent(Intent.ACTION_VIEW, uri);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            Intent i=new Intent(this,PrincipalActivity.class);
            startActivity(i);
        }
        return true;
    }

    public void returnView(View v){
        finish();
    }

    public void callUno(View view) {

        telefon = "(55)8421 0500";

        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "(55)8421 0500"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            String[] PERMISSIONS = {Manifest.permission.CALL_PHONE};
            ActivityCompat.requestPermissions((Activity) this, PERMISSIONS, REQUEST);
        } else {
            startActivity(intent);
        }
    }

    public void callDos(View view) {
        telefon = "(01 800 953 0059";
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "01 800 953 0059"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            String[] PERMISSIONS = {Manifest.permission.CALL_PHONE};
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST);

        } else {
            startActivity(intent);
        }
    }

    public void openMail(View view) {
        final Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","ayuda@miituo.com", null));
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "ayuda");
//        emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
        /* Fill it with Data */
//        emailIntent.setType("plain/text");
//        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ayuda@miituo.com"});
        //emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Ayuda");
        //emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");
//        startActivity(Intent.createChooser(emailIntent, "Contácto desde app..."));
        startActivity(emailIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String number = telefon;
                    Intent intent4 = new Intent(Intent.ACTION_CALL);
                    intent4.setData(Uri.parse("tel:" + number));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                       return;
                    }
                    startActivity(intent4);
                } else {
                    Toast.makeText(ContactaActivity.this, "La aplicación no permite abrir el telefono.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
