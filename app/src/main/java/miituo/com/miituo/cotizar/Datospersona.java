package miituo.com.miituo.cotizar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import miituo.com.miituo.R;

import static miituo.com.miituo.cotizar.PagerAdapter.pager;

//import static com.miituo.cotizamiituo.cotiza.CotizaActivity.viewPager;

/**
 * Created by john.cristobal on 12/04/18.
 */

public class Datospersona extends Fragment {

    Button back;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.datospersona_layout, container, false);

        //button
        back = v.findViewById(R.id.button22);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("Launch","Show Persona layout");
                pager.setCurrentItem(1);

            }
        });
        return v;
    }
}
