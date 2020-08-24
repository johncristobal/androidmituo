package miituo.com.miituo.utils;

import android.content.Context;
import android.os.Bundle;
import com.google.firebase.analytics.FirebaseAnalytics;
import miituo.com.miituo.api.ApiClient;

public class Evento {

    public static final String COTIZAR = "a_1_cotizar";
    public static final String COTIZAR_MENU = "a_1_cotizar_menu";
    public static final String CONTRATAR = "a_2_contratar";
    public static final String CONTRATANTE = "a_3_contratante";
    public static final String AUTO = "a_4_auto";
    public static final String TOKEN = "a_5_token";
    public static final String PAGO = "a_6_pago";
    public static final String POLIZA = "a_7_poliza";
    public static final String REPORTE = "a_reporte";

    public static void eventRecord(Context c,String evento){
        if(ApiClient.ambiente==4) {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "0");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, evento);
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "text");
            FirebaseAnalytics.getInstance(c).logEvent(evento, bundle);
        }
    }
}
