package miituo.com.miituo.cotizar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import miituo.com.miituo.R;
import miituo.com.miituo.activities.DetallesPlanesActivity;


/**
 * Created by miituo on 26/01/2017.
 */
@SuppressWarnings("serial")
public class CotizaAdapter extends BaseAdapter {

    private Context mContext;
    private List<RateList> mInfoClientList;
    public Typeface tipo;
    public long time;

    public CotizaAdapter(Context mContext, List<RateList> mInfoClientList, Typeface t) {
        this.mContext = mContext;
        this.mInfoClientList = mInfoClientList;

        tipo = t;
    }

    @Override
    public int getCount() {
        return 2;
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
        View v= View.inflate(mContext,R.layout.plan_detail_layout,null);
        TextView planLabel=(TextView)v.findViewById(R.id.textViewPlan);
        TextView precioLabel=(TextView)v.findViewById(R.id.textViewPrecio);
        TextView detallesLabel=(TextView)v.findViewById(R.id.textView8);

        //planLabel.setTypeface(tipo);
        //precioLabel.setTypeface(tipo);

        //CardView card = (CardView)v.findViewById(R.id.view2);
        //ImageView circulo = (ImageView)v.findViewById(R.id.imageView4);
        //set values
        try {
            String planFinal="";
            String plantext = mInfoClientList.get(position).getName();
            if(plantext.equals("Cobertura Amplia"))
            {
                planFinal = "Plan Amplio";
            }else if(plantext.equals("Cobertura Limitada"))
            {
                planFinal = "Plan Limitado";
            }
            planLabel.setText(planFinal);

            precioLabel.setText(String.format("$ %s", mInfoClientList.get(position).getAmount()));

            //load detalles plan
            detallesLabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext, DetallesPlanesActivity.class);
                    i.putExtra("idplan",mInfoClientList.get(position).getCoverageId()+"");
                    i.putExtra("amount",mInfoClientList.get(position).getAmount()+"");
                    mContext.startActivity(i);
                    ((Activity) mContext).overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    //setupWindowAnimations();
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return v;
    }

}
