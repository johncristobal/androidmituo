package miituo.com.miituo.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import miituo.com.miituo.R;
import miituo.com.miituo.utils.SimpleCallBack;

/**
 * Created by john.cristobal on 30/06/17.
 */

public class TerminosAdapter extends BaseAdapter {

    private Context mContext;
    public Typeface tipo;
    public SimpleCallBack cb;
    public String[] values = new String[] { "Aviso de Privacidad","Términos y condiciones miituo",
            "Condiciones Generales del Seguro",
            "Folleto de Derechos del Asegurado"};


    public TerminosAdapter(Context c, Typeface t, SimpleCallBack cb){
        mContext = c;
        tipo = t;
        this.cb=cb;
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public Object getItem(int position) {
        return values[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v=View.inflate(mContext,R.layout.terminos_item,null);
        TextView opcion=(TextView)v.findViewById(R.id.lbOp);
        ImageView down = (ImageView)v.findViewById(R.id.imgDown);
        ImageView view = (ImageView)v.findViewById(R.id.imgView);

        opcion.setTypeface(tipo);
        opcion.setText(values[position]);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb.run(false,""+position);
            }
        });
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb.run(true,""+position);
            }
        });

        return v;
    }
}
