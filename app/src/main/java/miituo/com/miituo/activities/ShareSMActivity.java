package miituo.com.miituo.activities;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/*import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareMediaContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;*/

import java.util.List;

import miituo.com.miituo.R;

public class ShareSMActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_share_sm);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/herne1.ttf");
        TextView leyenda = (TextView)findViewById(R.id.textView79);
        TextView res = (TextView)findViewById(R.id.textView81);
        leyenda.setTypeface(typeface);
        res.setTypeface(typeface);

        //events images
        ImageView face = (ImageView)findViewById(R.id.imageView14);
        ImageView twi = (ImageView)findViewById(R.id.imageView15);
        ImageView ins = (ImageView)findViewById(R.id.imageView16);

        face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareThread("facebook");
            }
        });

        twi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //compartir("twitter");
                shareThread("twitter");
            }
        });

        ins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //compartir("instagram");
                shareThread("instagram");
            }
        });
    }

    //back to polizas
    public void regresar(View v){
        //shareThread("facebook");
    }

    public void shareThread(String red){
        //thread to call api and save furst odometer
        AsyncTask<String, Void, Void> sendOdometro = new AsyncTask<String, Void, Void>() {
            ProgressDialog progress = new ProgressDialog(ShareSMActivity.this);
            String ErrorCode = "";

            public void compartir(String src){
                Intent share = new Intent(Intent.ACTION_SEND);
                //share.setType("text/plain");
                //share.putExtra(Intent.EXTRA_TEXT,"Gracias por ser parte del consumo equitativo.");
                share.setType("image/*");
                String _ImageFile = "android.resource://" + getResources().getResourceName(R.drawable.sharefinal).replace(":", "/");
                Uri imageUri = Uri.parse(_ImageFile);

                share.putExtra(Intent.EXTRA_STREAM,imageUri);
                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                //startActivity(Intent.createChooser(share,"Share via"));

                boolean flag = false;
                PackageManager pm = getPackageManager();
                List<ResolveInfo> activityList = pm.queryIntentActivities(share, 0);
                for (final ResolveInfo app : activityList) {
                    if ((app.activityInfo.name).contains(src)) {
                        final ActivityInfo activity = app.activityInfo;
                        final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                        share.addCategory(Intent.CATEGORY_LAUNCHER);
                        share.setComponent(name);
                        startActivity(Intent.createChooser(share,"Share via"));
                        flag = true;
                        break;
                    }
                }

                //no existe la app
                if(!flag){
                    ErrorCode = "Instala la App para compartir.";
                    this.cancel(true);
                    //onCancelled();
                    //Toast.makeText(ShareSMActivity.this,"Instala la App para compartir.",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected void onPreExecute() {
                //progress.setTitle("Cargando...");
                progress.setMessage("Cargando...");
                //progress.setIcon(R.drawable.miituo);
                progress.setCancelable(false);
                progress.setIndeterminate(true);
                progress.show();
            }

            @Override
            protected Void doInBackground(String... params) {
                try {

                    while(true){
                        String redsocial = params[0];
                        //String redsocial = "nop";

                        switch (redsocial){
                            case "facebook":
                                /*Bitmap image = BitmapFactory.decodeResource(ShareSMActivity.this.getResources(),R.drawable.sharefinal);
                                SharePhoto photo = new SharePhoto.Builder()
                                        .setBitmap(image)
                                        .build();
                                SharePhotoContent content = new SharePhotoContent.Builder()
                                        .addPhoto(photo)
                                        .build();

                                ShareLinkContent contentlink = new ShareLinkContent.Builder()
                                        .setContentUrl(Uri.parse("https://miituo.com"))
                                        .build();

                                ShareContent shareContent = new ShareMediaContent.Builder()
                                        .addMedium(photo)
                                        .build();

                                ShareDialog shareDialog = new ShareDialog(ShareSMActivity.this);

                                shareDialog.show(shareContent);*/
                                break;

                            case "twitter":
                                compartir("twitter");
                                break;

                            case "instagram":
                                compartir("instagram");
                                break;

                            default:break;
                        }
                    }



                } catch (Exception ex) {
                    ErrorCode = ex.getMessage();
                    this.cancel(true);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                progress.dismiss();
            }

            @Override
            protected void onCancelled() {
                if (ErrorCode.equals("1000")) {
                    progress.dismiss();
                    Toast msg = Toast.makeText(getApplicationContext(), ErrorCode, Toast.LENGTH_LONG);
                    msg.show();

                    super.onCancelled();

                } else {
                    progress.dismiss();

                    Toast msg = Toast.makeText(getApplicationContext(), ErrorCode, Toast.LENGTH_LONG);
                    msg.show();
                }
            }
        };

        try {

            sendOdometro.execute(red);//.get(5000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        } /*catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
            sendOdometro.cancel(true);

            Toast.makeText(this,"Hilo cancelado",Toast.LENGTH_SHORT).show();
        }*/
    }
}
