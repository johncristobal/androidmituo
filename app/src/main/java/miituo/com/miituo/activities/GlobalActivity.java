package miituo.com.miituo.activities;

import android.app.Activity;
import android.app.Application;

import java.util.List;

import miituo.com.miituo.data.InfoClient;

public class GlobalActivity extends Application {

    private List<InfoClient> polizas;

    public List<InfoClient> getPolizas() {
        return polizas;
    }

    public void setPolizas(List<InfoClient> polizas) {
        this.polizas = polizas;
    }
}
