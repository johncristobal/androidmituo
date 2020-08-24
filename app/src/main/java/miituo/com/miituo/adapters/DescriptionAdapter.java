package miituo.com.miituo.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import miituo.com.miituo.R;
import miituo.com.miituo.cotizar.MarcasVehiculos;
import miituo.com.miituo.utils.CallBack;

public class DescriptionAdapter extends BaseAdapter {

    private Context mContext;
    List<MarcasVehiculos> tokenDescripciones;
    public Typeface tipo;

    public DescriptionAdapter(Context mContext, List<MarcasVehiculos> tokenDescripciones, Typeface t) {
        this.mContext = mContext;
        this.tokenDescripciones = tokenDescripciones;
        tipo = t;
    }

    @Override
    public int getCount() {
        return tokenDescripciones.size();
    }

    @Override
    public Object getItem(int position) {
        return tokenDescripciones.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=View.inflate(mContext, R.layout.description_item,null);
        TextView TxtAuto=(TextView)v.findViewById(R.id.Vehicle);

        TxtAuto.setTypeface(tipo,Typeface.BOLD);
        TxtAuto.setText(tokenDescripciones.get(position).getDescription());
        if(tokenDescripciones.get(position).getDescription()==null || tokenDescripciones.get(position).getDescription().length()<2){
            View vi=v.findViewById(R.id.viewLine);
            vi.setVisibility(View.GONE);
        }
            v.setTag(tokenDescripciones.get(position));
        return v;
    }

}

