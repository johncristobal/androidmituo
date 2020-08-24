package miituo.com.miituo.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;

import miituo.com.miituo.R;

public class DetallesPlanesActivity extends AppCompatActivity {

    WebView vista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_planes);

        overridePendingTransition(R.anim.slide_in,R.anim.slide_out);

        Intent i = getIntent();
        String prima = i.getStringExtra("amount");
        String idcoverage = i.getStringExtra("idplan");

        String data="";

        switch (idcoverage){
            case "1":
                 data = "<!DOCTYPE html> \n" +
                        "                <!-- \n" +
                        "                To change this license header, choose License Headers in Project Properties. \n" +
                        "                To change this template file, choose Tools | Templates \n" +
                        "                and open the template in the editor. \n" +
                        "                --> \n" +
                        "                <html> \n" +
                        "                    <head> \n" +
                        "                        <title>TODO supply a title</title> \n" +
                        "                        <meta charset=\\UTF-8\\> \n" +
                        "                        <meta name=\\viewport\\ content=\\width=device-width, initial-scale=1.0\\> \n" +
                        "                        <style> \n" +
                        "                            @font-face { \n" +
                        "                                font-family: 'feast'; \n" +
                        "                                src: url('fonts/herne1.ttf'); \n" +
                        "                            } \n" +
                        "                 \n" +
                        "                            body {font-family: 'feast';} \n" +
                        "                        </style> \n" +
                        "                    </head> \n" +
                        "                    <body> \n" +
                        "                        <div> \n" +
                        "                            <p style='font-size:24px;'><strong>Plan amplio.</strong></p> \n" +
                        "                            <span style='font-size:26px;'><strong>"+prima+"</strong></span> por km. \n" +
                        "                            <span style='width:50px;'><hr width='60%' align='left'></span>\n" +
                        "                            <p align=\\left\\> \n" +
                        "                            <p style='color:#585858;'>\n" +
                        "                            <span style='font-size:14px;'><strong>Daños materiales.</strong></span> <br>\n" +
                        "                            <span style='font-size:12px;'>Indemnización: Valor comercial Deducible: 5%</span>\n" +
                        "                            <br><br>\n" +
                        "                            <span style='font-size:14px;'><strong>Robo total.</strong></span> <br>\n" +
                        "                            <span style='font-size:12px;'>Indemnización: Valor comercial Deducible: 10%</span>\n" +
                        "                            <br><br>\n" +
                        "                            <span style='font-size:14px;'><strong>RC por daños a terceros.</strong></span> <br>\n" +
                        "                            <span style='font-size:12px;'>$3,000 000</span>\n" +
                        "                            <br><br>\n" +
                        "                            <span style='font-size:14px;'><strong>Gastos médicos ocupantes.</strong></span> <br>\n" +
                        "                            <span style='font-size:12px;'>$200 000 autos</span>\n" +
                        "                            <br><br>\n" +
                        "                            <span style='font-size:14px;'><strong>Defensa legal/ jurídica.</strong></span> <br>\n" +
                        "                            <br>\n" +
                        "                            <span style='font-size:14px;'><strong>Servicios de asistencia.</strong></span> <br>                            \n" +
                        "                            </p>\n" +
                        "                            <span style='font-size:14px;'><strong>2 pagos únicos.</strong></span> <br>\n" +
                        "                            <table>\n" +
                        "                            <tr>\n" +
                        "                            <td width='85%'>\n" +
                        "                            \t<span style='font-size:14px;'><strong>1. Al contratar: </strong></span><span style='font-size:14px;'>Derecho a póliza</span>\n" +
                        "                            </td>\n" +
                        "                            <td width='15%' style='text-align:right;'>\n" +
                        "                            \t<span style='font-size:14px;'>$290</span>\n" +
                        "                            </td>                             \n" +
                        "                            </tr>\n" +
                        "                            <tr>\n" +
                        "                            <td width='85%'>\n" +
                        "                            \t<span style='font-size:14px;'><strong>2. El primer mes: </strong></span><span style='font-size:14px;'>Responsabilidad Civil Obligatoria</span>\n" +
                        "                            </td>\n" +
                        "                            <td width='15%' style='text-align:right;'>\n" +
                        "                            \t<span style='font-size:14px;'>$271</span>\n" +
                        "                            </td>                             \n" +
                        "                            </tr>\n" +
                        "\t\t\t\t\t\t\t</table>\n" +
                        "\t\t\t\t\t\t\t<br>\n" +
                        "\t\t\t\t\t\t\t<span style='font-size:14px;'><strong>Siguientes meses </strong></span><br>\n" +
                        "\t\t\t\t\t\t\t<span style='font-size:14px;'>Prima por kms. recorridos</span>\n" +
                        "\n" +
                        "                            </p> \n" +
                        "                 \n" +
                        "                        </div> \n" +
                        "                    </body> \n" +
                        "                </html>";
                break;
            case "2":

                data = "<!DOCTYPE html> \n" +
                        "                <!-- \n" +
                        "                To change this license header, choose License Headers in Project Properties. \n" +
                        "                To change this template file, choose Tools | Templates \n" +
                        "                and open the template in the editor. \n" +
                        "                --> \n" +
                        "                <html> \n" +
                        "                    <head> \n" +
                        "                        <title>TODO supply a title</title> \n" +
                        "                        <meta charset=\\UTF-8\\> \n" +
                        "                        <meta name=\\viewport\\ content=\\width=device-width, initial-scale=1.0\\> \n" +
                        "                        <style> \n" +
                        "                            @font-face { \n" +
                        "                                font-family: 'feast'; \n" +
                        "                                src: url('fonts/herne1.ttf'); \n" +
                        "                            } \n" +
                        "                 \n" +
                        "                            body {font-family: 'feast';} \n" +
                        "                        </style> \n" +
                        "                    </head> \n" +
                        "                    <body> \n" +
                        "                        <div> \n" +
                        "                            <p style='font-size:24px;'><strong>Plan amplio.</strong></p> \n" +
                        "                            <span style='font-size:26px;'><strong>"+prima+"</strong></span> por km. \n" +
                        "                            <span style='width:50px;'><hr width='60%' align='left'></span>\n" +
                        "                            <p align=\\left\\> \n" +
                        "                            <p style='color:#585858;'>\n" +
                        "                            <span style='font-size:14px;'><strong>Robo total.</strong></span> <br>\n" +
                        "                            <span style='font-size:12px;'>Indemnización: Valor comercial Deducible: 10%</span>\n" +
                        "                            <br><br>\n" +
                        "                            <span style='font-size:14px;'><strong>RC por daños a terceros.</strong></span> <br>\n" +
                        "                            <span style='font-size:12px;'>$3,000 000</span>\n" +
                        "                            <br><br>\n" +
                        "                            <span style='font-size:14px;'><strong>Gastos médicos ocupantes.</strong></span> <br>\n" +
                        "                            <span style='font-size:12px;'>$200 000 autos</span>\n" +
                        "                            <br><br>\n" +
                        "                            <span style='font-size:14px;'><strong>Defensa legal/ jurídica.</strong></span> <br>\n" +
                        "                            <br>\n" +
                        "                            <span style='font-size:14px;'><strong>Servicios de asistencia.</strong></span> <br>                            \n" +
                        "                            </p>\n" +
                        "                            <span style='font-size:14px;'><strong>2 pagos únicos.</strong></span> <br>\n" +
                        "                            <table>\n" +
                        "                            <tr>\n" +
                        "                            <td width='85%'>\n" +
                        "                            \t<span style='font-size:14px;'><strong>1. Al contratar: </strong></span><span style='font-size:14px;'>Derecho a póliza</span>\n" +
                        "                            </td>\n" +
                        "                            <td width='15%' style='text-align:right;'>\n" +
                        "                            \t<span style='font-size:14px;'>$290</span>\n" +
                        "                            </td>                             \n" +
                        "                            </tr>\n" +
                        "                            <tr>\n" +
                        "                            <td width='85%'>\n" +
                        "                            \t<span style='font-size:14px;'><strong>2. El primer mes: </strong></span><span style='font-size:14px;'>Responsabilidad Civil Obligatoria</span>\n" +
                        "                            </td>\n" +
                        "                            <td width='15%' style='text-align:right;'>\n" +
                        "                            \t<span style='font-size:14px;'>$271</span>\n" +
                        "                            </td>                             \n" +
                        "                            </tr>\n" +
                        "\t\t\t\t\t\t\t</table>\n" +
                        "\t\t\t\t\t\t\t<br>\n" +
                        "\t\t\t\t\t\t\t<span style='font-size:14px;'><strong>Siguientes meses </strong></span><br>\n" +
                        "\t\t\t\t\t\t\t<span style='font-size:14px;'>Prima por kms. recorridos</span>\n" +
                        "\n" +
                        "                            </p> \n" +
                        "                 \n" +
                        "                        </div> \n" +
                        "                    </body> \n" +
                        "                </html>";
                break;

            default:break;
        }

        vista = findViewById(R.id.webplanes);

        vista.loadDataWithBaseURL("file:///android_asset/",data,"text/html","utf-8",null);
    }

    public void closeView(View v){
        finish();
    }
}
