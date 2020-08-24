package miituo.com.miituo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

/**
 * Created by john.cristobal on 30/06/17.
 */

public class MenuAdapterSelf extends BaseAdapter {

    private Context mContext;
    public Typeface tipo;
    private int count=0;
    public String[] values = new String[] { "Tus pólizas","Alertas","Método de pago",
            "Acerca de","Contacto","Blog","Preguntas frecuentes","Cancelar póliza","Términos y condiciones"};


    public MenuAdapterSelf(Context c, int count, Typeface t){
        mContext = c;
        tipo = t;
        this.count=count;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        View v=View.inflate(mContext,R.layout.menuitem,null);
        TextView opcion=(TextView)v.findViewById(R.id.textView63);
        ImageView imagen = (ImageView)v.findViewById(R.id.imageView11);

        opcion.setTypeface(tipo);
        //CardView card = (CardView)v.findViewById(R.id.view2);
        //ImageView circulo = (ImageView)v.findViewById(R.id.imageView4);
        //set values

        opcion.setText(values[position]);

        if(position == 0){
            imagen.setImageResource(R.drawable.ico_poliza);
        }else if(position == 1){
            imagen.setImageResource(R.drawable.menu_notif);
            if(count>0){
//                imagen.setImageResource(R.drawable.noti_pink);
                TextView alert=(TextView)v.findViewById(R.id.lbAlert);
                alert.setVisibility(View.VISIBLE);
                alert.setText(""+count);
            }
        }else if(position == 2){
            imagen.setImageResource(R.drawable.cambiar_pago);
        }else if(position == 3){
            imagen.setImageResource(R.drawable.ico_ayuda);
        }else if(position == 4){
            imagen.setImageResource(R.drawable.icon_contact);
        }else if(position == 5){
            imagen.setImageResource(R.drawable.icon_blog);
        }else if(position == 6){
            imagen.setImageResource(R.drawable.icon_faq);
        }else if(position==7){
            imagen.setImageResource(R.drawable.menu_cancelar);
        }else if(position==8){
            imagen.setImageResource(R.drawable.icono_terminos);
        }
        return v;
    }
}
