package miituo.com.miituo.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import miituo.com.miituo.ExpandibleListAdpter;
import miituo.com.miituo.R;
import miituo.com.miituo.activities.DetallesActivity;
import miituo.com.miituo.activities.PDFViewer;
import miituo.com.miituo.activities.TicketActivity;
import miituo.com.miituo.activities.WebActivity;
import miituo.com.miituo.data.IinfoClient;
import miituo.com.miituo.data.InfoSectionItem;
import miituo.com.miituo.data.Report;
import miituo.com.miituo.threats.GetPDFSync;
import miituo.com.miituo.utils.SimpleCallBack;


public class TabFragment1 extends Fragment {

    ExpandableListView expListView;
    TextView tDriver, tContractor, tAuto, tPlacas, tModelo, tLastReport, lbEdoCta, lbCambioPago;
    TextView lbNameDriver, lbCelDriver, lbNameContractor, lbCelContractor;
    TextView lbAuto2, lbPlacas2, lbModelo2, lbLastReport2;
    LinearLayout lbLastReport3, btnCambioPago;

    public Typeface tipo;
    public static AlertDialog alertaPago;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tab_fragment_1, container, false);
        expListView = new ExpandableListView(getContext());
        tDriver = v.findViewById(R.id.lbDriver);
        tDriver.setTypeface(tipo);
        tContractor = v.findViewById(R.id.lbContractor);
        tContractor.setTypeface(tipo);
        tAuto = v.findViewById(R.id.lbAuto);
        tAuto.setTypeface(tipo);
        tPlacas = v.findViewById(R.id.lbPlacas);
        tPlacas.setTypeface(tipo);
        tModelo = v.findViewById(R.id.lbModelo);
        tModelo.setTypeface(tipo);
        tLastReport = v.findViewById(R.id.lbLastReport);
        tLastReport.setTypeface(tipo);
        lbEdoCta = v.findViewById(R.id.lbEdoCta);
        lbEdoCta.setTypeface(tipo);
        lbCambioPago = v.findViewById(R.id.lbCambioPago);
        lbCambioPago.setTypeface(tipo);
        lbNameDriver = v.findViewById(R.id.lbNameDriver);
        lbNameDriver.setTypeface(tipo);
        lbNameContractor = v.findViewById(R.id.lbNameContractor);
        lbNameContractor.setTypeface(tipo);
        lbCelDriver = v.findViewById(R.id.lbCelDriver);
        lbCelDriver.setTypeface(tipo);
        lbCelContractor = v.findViewById(R.id.lbCelContractor);
        lbCelContractor.setTypeface(tipo);
        lbAuto2 = v.findViewById(R.id.lbAuto2);
        lbAuto2.setTypeface(tipo);
        lbPlacas2 = v.findViewById(R.id.lbPlacas2);
        lbPlacas2.setTypeface(tipo);
        lbModelo2 = v.findViewById(R.id.lbModelo2);
        lbModelo2.setTypeface(tipo);
        lbLastReport2 = v.findViewById(R.id.lbLastReport2);
        lbLastReport2.setTypeface(tipo);
        lbLastReport3 =(LinearLayout)  v.findViewById(R.id.lbLastReport3);
        btnCambioPago = (LinearLayout) v.findViewById(R.id.btnCambioPago);
        GetData();
        btnCambioPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), WebActivity.class).putExtra("isPoliza",false));
            }
        });
        List<Report> list = IinfoClient.getInfoClientObject().getPolicies().getReports();
//        if (list != null && list.size() > 1) {
        if (IinfoClient.getInfoClientObject().getPolicies().getMensualidad() > 0) {
            lbLastReport3.setEnabled(true);
            lbLastReport3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ver ticket de cobro anterior
//                    startActivity(new Intent(getContext(), TicketActivity.class));
                    new GetPDFSync(getActivity(),false, IinfoClient.InfoClientObject.getPolicies().getId(),IinfoClient.InfoClientObject.getPolicies().getNoPolicy(), new SimpleCallBack() {
                        @Override
                        public void run(boolean status, String res) {
                            if(!status){
//            Toast.makeText(c,"descarga fallida",Toast.LENGTH_LONG).show();
                                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("Descarga Fallida");
                                builder.setMessage("Por favor intentalo más tarde.");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        alertaPago.dismiss();
                                    }
                                });
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        alertaPago = builder.create();
                                        alertaPago.show();
                                    }
                                });
                            }else{
                                // Toast.makeText(c,"descarga exitosa",Toast.LENGTH_LONG).show();
                                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("Descarga completa");
                                builder.setMessage("Ahora tu estado de cuenta se encuentra en tus descargas.");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        alertaPago.dismiss();
//                    c.startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
                                        startActivity(new Intent(getActivity(), PDFViewer.class).putExtra("isPoliza",false));
                                    }
                                });
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        alertaPago = builder.create();
                                        alertaPago.show();
                                    }
                                });
                            }
                        }
                    }).execute();
                }
            });
        } else {
            lbLastReport3.setEnabled(false);
            lbLastReport3.setVisibility(View.GONE);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.height=lbLastReport3.getLayoutParams().height;
            layoutParams.setMargins(0, 0, 0, 0);
            btnCambioPago.setLayoutParams(layoutParams);

        }
        return v;
    }

    private void GetData() {
        lbNameDriver.setText(IinfoClient.InfoClientObject.getClient().getName() + " " + IinfoClient.InfoClientObject.getClient().getLastName() + " " + IinfoClient.InfoClientObject.getClient().getMotherName());
        lbCelDriver.setText(IinfoClient.InfoClientObject.getClient().getCelphone());
        lbNameContractor.setText(IinfoClient.InfoClientObject.getClient().getNameContract());
        lbCelContractor.setText(IinfoClient.InfoClientObject.getClient().getCelphoneContract());

        lbAuto2.setText(IinfoClient.InfoClientObject.getPolicies().getVehicle().getDescription().getDescription());
        lbPlacas2.setText(IinfoClient.InfoClientObject.getPolicies().getVehicle().getPlates());
        lbModelo2.setText(String.valueOf(IinfoClient.InfoClientObject.getPolicies().getVehicle().getModel().getModel()));

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        Double dos = Double.parseDouble(IinfoClient.InfoClientObject.getPolicies().getLastOdometer() + "");
        String yourFormattedString = formatter.format(dos);
        lbLastReport2.setText(yourFormattedString + " kms");
    }

}
