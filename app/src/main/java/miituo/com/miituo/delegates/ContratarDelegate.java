package miituo.com.miituo.delegates;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import miituo.com.miituo.activities.AseguradorasActivity;
import miituo.com.miituo.activities.BaseActivity;
import miituo.com.miituo.activities.ContratarActivity;
import miituo.com.miituo.api.ApiClient;
import miituo.com.miituo.cotizar.ApiClientCotiza;
import miituo.com.miituo.utils.Evento;
import miituo.com.miituo.utils.SimpleCallBack;

public class ContratarDelegate {
    public static ContratarActivity activity;
    public static Context context;
    public ContratarDelegate(ContratarActivity ca,Context c){
        activity=ca;
        context=c;
    }

    public String contractModelMaker(){
        String request="";
        JSONObject json=new JSONObject();
        try {
            json.put("CoverageId",activity.rl.getCoverageId());
            json.put("QuotationId",activity.idCotizacion);
            json.put("Bill",false);
            json.put("Token",activity.token);
            json.put("Finish",true);
            if(activity.cupon!=null) {
                json.put("Coupon", activity.cupon);
            }
            json.put("Device","App_Android");
            JSONObject pr=new JSONObject();
            pr.put("ClientUserId",0);
            pr.put("CuponId",0);
            pr.put("Quantity",0);
            json.put("Promocion",pr);

            JSONObject v=new JSONObject();
            v.put("Garaje",Integer.parseInt(activity.garage));
            v.put("numPlates",activity.placasVehicle);
            v.put("numVin",activity.serieVehicle);
            v.put("IdVehTMMSD",activity.vehicleId);
            v.put("colorCar",activity.colorVehicle);
//            v.put("capacity",JSONObject.NULL);
//            v.put("BenefitPreferente",JSONObject.NULL);
//            v.put("IdPoliza",0);
            json.put("Vehiculo",v);

            JSONObject p=new JSONObject();
            p.put("MethodPay",activity.tipoPago);
            p.put("IdPayReferences",-1);
            p.put("IdClient",activity.idCotizacion);
            p.put("ObjectPay",activity.pago);
//            p.put("NombreProcesador",JSONObject.NULL);
//            p.put("OldReference",JSONObject.NULL);
//            p.put("Fecha_pago",JSONObject.NULL);
//            p.put("PolicyId",0);
//            p.put("Reference",JSONObject.NULL);
            json.put("PaymentReferences",p);

            JSONArray array=new JSONArray();
            if(!activity.lbSi.isChecked()) {
                array.put(getArrayCliente(1,new String[]{
                        activity.nombreD,activity.apD,activity.amD,activity.formatNacD,
                        "",activity.mailD,"",activity.celD,
                        activity.sexD,activity.colC,activity.cpD,"",
                        "","","",
                        "","","",
                        "",""
                }));
                array.put(getArrayCliente(2,new String[]{
                        activity.nombreC,activity.apC,activity.amC,activity.formatNacC,
                        activity.profesionIdC,activity.mailC,activity.rfcC,activity.celC,
                        activity.sexC,activity.colC,activity.cpC,activity.colDescC,
                        activity.delC,activity.etDelC.getText().toString(),activity.edoC,
                        activity.etEdoC.getText().toString(),activity.calleC,activity.intC,
                        activity.extC,activity.homoclaveC
                }));
            }
            else{
                array.put(getArrayCliente(3,new String[]{
                        activity.nombreC,activity.apC,activity.amC,activity.formatNacC,
                        activity.profesionIdC,activity.mailC,activity.rfcC,activity.celC,
                        activity.sexC,activity.colC,activity.cpC,activity.colDescC,
                        activity.delC,activity.etDelC.getText().toString(),activity.edoC,
                        activity.etEdoC.getText().toString(),activity.calleC,activity.intC,
                        activity.extC,activity.homoclaveC
                            }));
            }
            json.put("Client",array);
            request=json.toString();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return request;
    }

    private JSONObject getArrayCliente(int clientType,String[] vals){
        JSONObject o=new JSONObject();
        try {
            o.put("ClientType",clientType);
            o.put("Name",vals[0]);
            o.put("LastName",vals[1]);
            o.put("MotLastName",vals[2]);
            o.put("BirthDate",vals[3]);
            o.put("Nationality","MEXICANA");
            o.put("Job",vals[4]);
            o.put("Email",vals[5]);
            o.put("RFC",clientType==1?"":vals[6]);
            o.put("Celphone", vals[7]);
            o.put("GenderId",Integer.valueOf(vals[8]));
//            o.put("Id",0);
//            o.put("CivilStatus",JSONObject.NULL);
//            o.put("CityBirth",JSONObject.NULL);
//            o.put("CURP",JSONObject.NULL);
//            o.put("PlaceResidence",JSONObject.NULL);
//            o.put("Telphone",JSONObject.NULL);
//            o.put("Fiel",JSONObject.NULL);
            o.put("homoclave",clientType==1?"":vals[19]);
            if(clientType!=1){
//                o.put("colonia",vals[9]);
                if(clientType==2){
                    o.put("GenderId2",vals[8]);
                    o.put("NationalityC","MEXICANA");
                    o.put("cp",vals[10]);
                }
            }
            JSONObject a=new JSONObject();
            a.put("NeighborhoodId",Integer.valueOf(vals[9]));
            a.put("ZipCode",vals[10]);
            a.put("DescNeighborhood",clientType==1?"":vals[11]);
            a.put("MunicipalyId",Integer.valueOf(clientType==1?"0":vals[12]));
            a.put("DescMunicipaly",clientType==1?"0":vals[13]);
            a.put("StateId",Integer.valueOf(clientType==1?"0":vals[14]));
            a.put("DescState",clientType==1?"":vals[15]);
            a.put("Street",clientType==1?"":vals[16]);
            a.put("NoInt",vals[17]);
            a.put("NoExt",clientType==1?"":vals[18]);
//            a.put("CodeMunicipaly", 0);
//            a.put("CodeState",0);
            o.put("Address",a);
        }catch (Exception e){
            e.printStackTrace();
        }
        return o;
    }

    //===========================Thread call to fill combo==============================================
    public static class saveInsurance extends AsyncTask<String,Void,Void> {

        ProgressDialog progress = new ProgressDialog(context);
        String ErrorCode,res,model;
        SimpleCallBack callBack;
        boolean isGenerateContract=false;

        public saveInsurance(String model,boolean isGenerateContract,SimpleCallBack cb){
            this.callBack=cb;
            this.model=model;
            this.isGenerateContract=isGenerateContract;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(isGenerateContract) {
                progress.setTitle("Información");
                progress.setMessage("Generando póliza. Ésto puede tardar...");
                progress.setIndeterminate(true);
                progress.setCancelable(false);
                progress.show();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if(isGenerateContract) {
                progress.dismiss();
            }
        }

        @Override
        protected Void doInBackground(String... strings) {

            final ApiClientCotiza client = new ApiClientCotiza(context);
            try {
                //recupera datos
                if(isGenerateContract) {
                    this.res = client.saveData("Quotation/SaveAllDataQuotation",model);
//                    activity.runOnUiThread(new Runnable() {
//                        public void run() {
//                            try {
//                    this.res =   client.saveAllData("Contract/GenerateContract", model);
//                            }catch (Exception e1){
//                                e1.printStackTrace();
//                            }
//                        }
//                    });
                }else{
                    this.res = client.saveData("Quotation/SaveAllDataQuotation",model);
                }
            } catch (Exception ex) {
                ErrorCode = ex.getMessage();
                callBack.run(false,ErrorCode);
                cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(isGenerateContract) {
                progress.dismiss();
            }
            callBack.run(true, res);
        }
    }

    //===========================Thread call to fill combo==============================================
    public static class getCarriers extends AsyncTask<String,Void,Void> {

        String ErrorCode,res;
        SimpleCallBack cb;
        public getCarriers(SimpleCallBack cb){
            this.cb=cb;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {

            final ApiClientCotiza client = new ApiClientCotiza(context);
            try {
                res = client.getDatos("InsuranceCarrier/");
                ContratarActivity.carriers = res;
                AseguradorasActivity.carriers=res;
            } catch (Exception ex) {
                ex.printStackTrace();
                cb.run(false,"");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            cb.run(true,res);
        }
    }

    //===========================Thread call to fill combo==============================================
    public static class generateInsurance extends AsyncTask<String,Void,Void> {

//        ProgressDialog progress = new ProgressDialog(context);
        String ErrorCode,res,model;
        SimpleCallBack cb;

        public generateInsurance(String model, SimpleCallBack cb){
            this.model=model;
            this.cb=cb;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progress.setTitle("Información");
//            progress.setMessage("Generando póliza. Ésto puede tardar...");
//            progress.setIndeterminate(true);
//            progress.setCancelable(false);
//            progress.show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
//            if (progress != null) {
//                progress.dismiss();
//            }
            cb.run(false,ErrorCode);
        }

        @Override
        protected Void doInBackground(String... strings) {

            final ApiClientCotiza client = new ApiClientCotiza(context);
//            try {
//                ContratarActivity.carriers = client.getDatos("InsuranceCarrier/");
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
            try {
                this.res =   client.saveAllData("Contract/GenerateContract", model);
            } catch (Exception ex) {
                ErrorCode = ex.getMessage();
                cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Evento.eventRecord(context,Evento.POLIZA);
//            if (progress != null) {
//                progress.dismiss();
//            }
            cb.run(true,"");
        }
    }

    //===========================Thread call to fill combo==============================================
    public static class getReference extends AsyncTask<String,Void,Void> {

        ProgressDialog progress = new ProgressDialog(context);
        String ErrorCode,res;
        SimpleCallBack callBack;
        double amount=0.0;
        int idQuotation=0, type=1;

        public getReference(int idQuotation,double amount,int type,SimpleCallBack cb){
            this.callBack=cb;
            this.idQuotation=idQuotation;
            this.amount=amount;
            this.type=type;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setTitle("Información");
            progress.setMessage("Solicitando información de pago...");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progress.dismiss();
            Toast message = Toast.makeText(context, ErrorCode, Toast.LENGTH_LONG);
            message.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            ApiClientCotiza client = new ApiClientCotiza(context);
            try {
                //recupera datos
                this.res = client.getReference("DistancePayment",amount,idQuotation,type);
            } catch (Exception ex) {
                ErrorCode = ex.getMessage();
                this.cancel(true);
                callBack.run(false,ErrorCode);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progress.dismiss();
            callBack.run(true, res);
        }
    }

    //===========================Thread call to fill combo==============================================
    public static class getDataQuotation extends AsyncTask<String,Void,Void> {

        ProgressDialog progress = new ProgressDialog(context);
        String ErrorCode,res;
        SimpleCallBack callBack;
        int idQuotation=0;

        public getDataQuotation(int idQuotation, SimpleCallBack cb){
            this.callBack=cb;
            this.idQuotation=idQuotation;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setTitle("Información");
            progress.setMessage("Solicitando información de pago...");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progress.dismiss();
            Toast message = Toast.makeText(context, ErrorCode, Toast.LENGTH_LONG);
            message.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            ApiClientCotiza client = new ApiClientCotiza(context);
            try {
                //recupera datos
                this.res = client.getQuotationInfo("Quotation/GetQuotation/"+idQuotation);
            } catch (Exception ex) {
                ErrorCode = ex.getMessage();
                this.cancel(true);
                callBack.run(false,ErrorCode);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progress.dismiss();
            callBack.run(true, res);
        }
    }

    //===========================Thread call to fill combo==============================================
    public static class getIsPaySucceded extends AsyncTask<String,Void,Void> {

        String ErrorCode,res;
        String referencia="",tipoPago="";
        SimpleCallBack cb;
        Context c;

        public getIsPaySucceded(Context c,String referencia, String tipoPago, SimpleCallBack cb){
            this.referencia=referencia;
            this.tipoPago=tipoPago;
            this.cb=cb;
            this.c=c;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Void doInBackground(String... strings) {

            ApiClientCotiza client = new ApiClientCotiza(context);
            try {
                //recupera datos
                this.res = client.getIsPaySucceded("payment/Searchreference/"+referencia+"/"+tipoPago+"/");
                JSONObject j=new JSONObject(res);
                if(res==null || res.contains("AUN NO SE ENCUENTRA") || j.has("Message")){
                    cb.run(false,null);
                    cancel(true);
                }
            } catch (Exception ex) {
                ErrorCode = ex.getMessage();
                if (ErrorCode.equalsIgnoreCase("402")){
                    cb.run(false,ErrorCode);
                }
                else{
                cb.run(false,null);
                }
                cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
                cb.run(false,res);
        }
    }


}
