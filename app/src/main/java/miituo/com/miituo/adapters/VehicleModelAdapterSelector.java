package miituo.com.miituo.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import miituo.com.miituo.R;
import miituo.com.miituo.activities.CambioPagoActivity;
import miituo.com.miituo.api.ApiClient;
import miituo.com.miituo.data.InfoClient;
import miituo.com.miituo.utils.CallBack;

public class VehicleModelAdapterSelector extends BaseAdapter {

    private Context mContext;
    private List<InfoClient> mInfoClientList;
    public Typeface tipo;
    public long time;
    private CallBack callBack;
    public String noTel = "";

    public String pathPhotos;

    public VehicleModelAdapterSelector(Context mContext, List<InfoClient> mInfoClientList, Typeface t, long tie, CallBack callBack) {
        this.mContext = mContext;
        this.mInfoClientList = mInfoClientList;
        this.callBack = callBack;
        tipo = t;
        this.time = tie;
        pathPhotos = new ApiClient(this.mContext).pathPhotos;
    }

    @Override
    public int getCount() {
        return mInfoClientList.size();
    }

    @Override
    public Object getItem(int position) {
        return mInfoClientList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = View.inflate(mContext, R.layout.infoclient_item_sellector, null);
        TextView TxtNoPolicy = (TextView) v.findViewById(R.id.Vehicle);
        TextView TxtMensajeLimite = (TextView) v.findViewById(R.id.mensajelimite);
        final RelativeLayout btnInfo = (RelativeLayout) v.findViewById(R.id.btnGral);
        ImageView imagen = (ImageView) v.findViewById(R.id.profile_image);
        TxtNoPolicy.setTypeface(tipo, Typeface.BOLD);
        TxtMensajeLimite.setTypeface(tipo);
        ImageView img = (ImageView) v.findViewById(R.id.StateImage);

        if(imagen != null){
            //pintaImg(imagen,position);
            pintaImg(imagen, mInfoClientList.get(position).getPolicies().getId());
        }

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.runInt(position);
            }
        });
        if(position== CambioPagoActivity.sellected){
            btnInfo.setBackgroundColor(Color.parseColor("#d9d9d9"));
        }
        TxtNoPolicy.setText("Póliza: " + String.valueOf(mInfoClientList.get(position).getPolicies().getNoPolicy()));
        v.setTag(mInfoClientList.get(position));
        TxtMensajeLimite.setText(mInfoClientList.get(position).getPolicies().getVehicle().getSubtype().getDescription());
        TxtMensajeLimite.setVisibility(View.VISIBLE);

        if (mInfoClientList.get(position).getPolicies().getState().getId() == 15) {
            img.setImageResource(R.drawable.reedmiituo);
        } else if (mInfoClientList.get(position).getPolicies().getReportState() == 24 ||
                mInfoClientList.get(position).getPolicies().getReportState() == 23 ||
                mInfoClientList.get(position).getPolicies().getReportState() == 21 ||
                mInfoClientList.get(position).getPolicies().getReportState() == 15 ||
                mInfoClientList.get(position).getPolicies().getReportState() == 14 ||
                mInfoClientList.get(position).getPolicies().getReportState() == 13 ||
                !mInfoClientList.get(position).getPolicies().isHasVehiclePictures() ||
                !mInfoClientList.get(position).getPolicies().isHasOdometerPicture()) {
            img.setImageResource(R.drawable.reedmiituo);
        }
        return v;
    }

    private void pintaImg(ImageView iv, int id) {

        //ruta del archivo...https://filesdev.miituo.com/21/FROM_VEHICLE.png

        Glide.with(mContext).asBitmap().
                load(pathPhotos + id + "/FROM_VEHICLE.png")
                //.into(iv);
                .listener(new RequestListener<Bitmap>() {
                              @Override
                              public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Bitmap> target, boolean b) {
                                  iv.setImageResource(R.drawable.foto);
                                  return false;
                              }

                              @Override
                              public boolean onResourceReady(Bitmap bitmap, Object o, Target<Bitmap> target, DataSource dataSource, boolean b) {
                                  iv.setImageBitmap(bitmap);// .setImage(ImageSource.bitmap(bitmap));
                                  return false;
                              }
                          }
                ).submit();
        /*
        String filePathString = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "frontal" + time + mInfoClientList.get(position).getPolicies().getNoPolicy() + ".png";
        String filePathStringfirst = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "frontal_" + mInfoClientList.get(position).getPolicies().getNoPolicy() + ".png";
        File f = new File(filePathString);
        File f2 = new File(filePathStringfirst);
        if (f.exists()) {
            Glide.with(mContext).load(filePathString).into(iv);
        } else if (f2.exists()) {
            Glide.with(mContext).load(filePathStringfirst).into(iv);
        } else {
            Glide.with(mContext).load(R.drawable.foto).into(iv);}*/

    }
}