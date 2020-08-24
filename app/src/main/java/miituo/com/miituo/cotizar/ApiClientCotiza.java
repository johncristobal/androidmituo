package miituo.com.miituo.cotizar;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import miituo.com.miituo.api.ApiClient;

/**
 * Created by john.cristobal on 10/04/18.
 */

public class ApiClientCotiza extends ApiClient{

    public ApiClientCotiza(Context c) {
        super(c);
    }

    public String getDatosSinURL(String Url) throws IOException {

        final String TAG = "JsonParser.java";
        InputStream in=null;
        try {
            URL url = new URL(Url+"");
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Authorization",miituoKey);
            urlConnection.connect();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else
                in = urlConnection.getInputStream();
            String jsonstring="";
            try {
                jsonstring=getStringFromInputStream(in);
                if(jsonstring!=null || jsonstring!="") {
                    return jsonstring;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    public String generatingCall(String Url, String Nombre,String tel,String email,String hora, int idQuotation, int idCatalogo) throws IOException, JSONException {
        InputStream in=null;
        try {
            URL url = new URL(UrlApi+Url);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Authorization",miituoKey);
            urlConnection.connect();
            JSONObject sendObject=new JSONObject();
            try {
                sendObject.put("Name",Nombre);
                sendObject.put("Telephone",tel);
                sendObject.put("Email",email);
                sendObject.put("ContactTime",hora);
                sendObject.put("QuotationId",idQuotation);
                sendObject.put("Id_Pagina_Origen",idCatalogo);
                sendObject.put("Pagina_Origen",idCatalogo==5?"APP_And_Enviar cotización por correo":"APP_And_Landing fuera de horario");
            }
            catch (JSONException ex)
            {
                throw new IOException(ex.getMessage());
            }
            OutputStreamWriter writter=new OutputStreamWriter(urlConnection.getOutputStream());
            writter.write(sendObject.toString());
            writter.flush();
            writter.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex){
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else {
                in = urlConnection.getInputStream();
                String jsonstring="";
                try {
                    jsonstring=getStringFromInputStream(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonstring;
            }
        }
        finally {
            if(in!=null){
                in.close();
            }
        }
    }

    public String getQuotationInfo(String Url) throws IOException {

        final String TAG = "JsonParser.java";
        InputStream in=null;
        try {
            URL url = new URL(UrlApi+Url+"");
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Authorization",miituoKey);
            urlConnection.connect();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else
                in = urlConnection.getInputStream();
            String jsonstring="";
            try {
                jsonstring=getStringFromInputStream(in);
                if(jsonstring!=null || jsonstring!="") {
                    return jsonstring;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    public String getIsPaySucceded(String Url) throws IOException {

        final String TAG = "JsonParser.java";
        InputStream in=null;
        try {
            URL url = new URL(UrlApi+Url+"");
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Authorization",miituoKey);
            urlConnection.connect();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(""+statusCode);
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else {
                in = urlConnection.getInputStream();
                String jsonstring = "";
                try {
                    jsonstring = getStringFromInputStream(in);
                    if (jsonstring != null || jsonstring != "") {
                        return jsonstring;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    public String getIsPaySuccededToken(String Url) throws IOException {

        final String TAG = "JsonParser.java";
        InputStream in=null;
        try {
            URL url = new URL(UrlApi+Url+"");
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Authorization",miituoKey);
            urlConnection.connect();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(statusCode==402?""+statusCode:jsonstring);
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else {
                in = urlConnection.getInputStream();
                String jsonstring = "";
                try {
                    jsonstring = getStringFromInputStream(in);
                    if (jsonstring != null || jsonstring != "") {
                        return jsonstring;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    public String tokenRequest(String Url) throws IOException {

        final String TAG = "JsonParser.java";
        InputStream in=null;
        try {
            URL url = new URL(UrlApi+Url+"");
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Authorization",miituoKey);
            urlConnection.connect();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else
                in = urlConnection.getInputStream();
            String jsonstring="";
            try {
                jsonstring=getStringFromInputStream(in);
                if(jsonstring!=null || jsonstring!="") {
                    return jsonstring;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    public String getRFC(String Url, String Nombre,String AP,String AM,String Fecha_Nacimiento) throws IOException, JSONException {
        InputStream in=null;
        try {
            URL url = new URL(UrlApi+Url);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Authorization",miituoKey);
            urlConnection.connect();
            JSONObject sendObject=new JSONObject();
            try {
                sendObject.put("Nombre",Nombre);
                sendObject.put("AP",AP);
                sendObject.put("AM",AM);
                sendObject.put("Fecha_Nacimiento",Fecha_Nacimiento);
            }
            catch (JSONException ex)
            {
                throw new IOException(ex.getMessage());
            }
            OutputStreamWriter writter=new OutputStreamWriter(urlConnection.getOutputStream());
            writter.write(sendObject.toString());
            writter.flush();
            writter.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex){
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else {
                in = urlConnection.getInputStream();
                String jsonstring="";
                try {
                    jsonstring=getStringFromInputStream(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonstring;
            }
        }
        finally {
            if(in!=null){
                in.close();
            }
        }
    }

    public String rfcVerifier(String Url) throws IOException {

        final String TAG = "JsonParser.java";
        InputStream in=null;
        try {
            URL url = new URL(UrlApi+Url+"");
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Authorization",miituoKey);
            urlConnection.connect();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else
                in = urlConnection.getInputStream();
            String jsonstring="";
            try {
                jsonstring=getStringFromInputStream(in);
                if(jsonstring!=null || jsonstring!="") {
                    return jsonstring;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    public String getReference(String Url,double amount, int idQuotation, int type) throws IOException, JSONException {
        InputStream in=null;
        try {
            URL url = new URL(UrlApi+Url);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Authorization",miituoKey);
            urlConnection.connect();
            JSONObject sendObject=new JSONObject();
            try {
                sendObject.put("Amount",amount);
                sendObject.put("QuotationId",idQuotation);
                sendObject.put("PaymentType",""+type);
            }
            catch (JSONException ex)
            {
                throw new IOException(ex.getMessage());
            }
            OutputStreamWriter writter=new OutputStreamWriter(urlConnection.getOutputStream());
            writter.write(sendObject.toString());
            writter.flush();
            writter.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex){
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else {
                in = urlConnection.getInputStream();
                String jsonstring="";
                try {
                    jsonstring=getStringFromInputStream(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonstring;
            }
        }
        finally {
            if(in!=null){
                in.close();
            }
        }
    }

    public String getNewReference(String Url) throws IOException, JSONException {
        InputStream in=null;
        try {
            URL url = new URL(UrlApi+Url);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Authorization",miituoKey);
            urlConnection.connect();
            OutputStreamWriter writter=new OutputStreamWriter(urlConnection.getOutputStream());
            writter.flush();
            writter.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex){
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else {
                in = urlConnection.getInputStream();
                String jsonstring="";
                try {
                    jsonstring=getStringFromInputStream(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonstring;
            }
        }
        finally {
            if(in!=null){
                in.close();
            }
        }
    }

    public String saveAllData(String Url, String model) throws IOException, JSONException {
        InputStream in=null;
        try {
            if(ambiente!=4){
                Log.d("APP","el model: "+model);
            }
            URL url = new URL(UrlApi+Url);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("Authorization",miituoKey);
            urlConnection.connect();
            OutputStreamWriter writter=new OutputStreamWriter(urlConnection.getOutputStream());
            writter.write(model);
            writter.flush();
            writter.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex){
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else {
                in = urlConnection.getInputStream();
                String jsonstring="";
                try {
                    jsonstring=getStringFromInputStream(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonstring;
            }
        }
        finally {
            if(in!=null){
                in.close();
            }
        }
    }

    public String saveData(String method,String model) throws IOException{

        InputStream in=null;
        try {
            URL url = new URL(UrlApi+method);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(0);
            urlConnection.setConnectTimeout(0);
            urlConnection.setRequestMethod("PUT");
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("Authorization",miituoKey);
            urlConnection.connect();

            OutputStreamWriter writter=new OutputStreamWriter(urlConnection.getOutputStream());
            writter.write(model);
            writter.flush();
            writter.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else {
                in = urlConnection.getInputStream();
                String jsonstring;
                jsonstring = (getStringFromInputStream(in));
                return  jsonstring;
            }
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    public String CPChecker(String Url, String Gender,String VehicleTMMSDId,String BirthdayDate,String ZipCode) throws IOException, JSONException {
        InputStream in=null;
        try {
            URL url = new URL(UrlApi+Url);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Authorization",miituoKey);
            urlConnection.connect();
            JSONObject sendObject=new JSONObject();
            try {
                sendObject.put("Gender",Gender);
                sendObject.put("VehicleTMMSDId",VehicleTMMSDId);
                sendObject.put("BirthdayDate",BirthdayDate);
                sendObject.put("ZipCode",ZipCode);
            }
            catch (JSONException ex)
            {
                throw new IOException(ex.getMessage());
            }
            OutputStreamWriter writter=new OutputStreamWriter(urlConnection.getOutputStream());
            writter.write(sendObject.toString());
            writter.flush();
            writter.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex){
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else {
                in = urlConnection.getInputStream();
                String jsonstring="";
                try {
                    jsonstring=getStringFromInputStream(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonstring;
            }
        }
        finally {
            if(in!=null){
                in.close();
            }
        }
    }

    public String checkVIN(String Url, String vin) throws IOException, JSONException {
        InputStream in=null;
        try {
            URL url = new URL(UrlApi+Url);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Authorization",miituoKey);
            urlConnection.connect();
            JSONObject sendObject=new JSONObject();
            try {
                sendObject.put("VIN",vin);
            }
            catch (JSONException ex)
            {
                throw new IOException(ex.getMessage());
            }
            OutputStreamWriter writter=new OutputStreamWriter(urlConnection.getOutputStream());
            writter.write(sendObject.toString());
            writter.flush();
            writter.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex){
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else {
                in = urlConnection.getInputStream();
                String jsonstring="";
                try {
                    jsonstring=getStringFromInputStream(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonstring;
            }
        }
        finally {
            if(in!=null){
                in.close();
            }
        }
    }

    public String setPreviousCarrier(String Url, int QuotationId, String InsuranceCarrier,String ChannelCommunication) throws IOException, JSONException {
        InputStream in=null;
        try {
            URL url = new URL(UrlApi+Url);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Authorization",miituoKey);
            urlConnection.connect();
            JSONObject sendObject=new JSONObject();
            try {
                sendObject.put("QuotationId",QuotationId);
                sendObject.put("InsuranceCarrier",InsuranceCarrier);
                sendObject.put("CommunicationChannel",ChannelCommunication);
                sendObject.put("Id_Pagina_Origen",16);
                sendObject.put("Pagina_Origen","APP_And_Conversion (Gracias)");
            }
            catch (JSONException ex)
            {
                throw new IOException(ex.getMessage());
            }
            OutputStreamWriter writter=new OutputStreamWriter(urlConnection.getOutputStream());
            writter.write(sendObject.toString());
            writter.flush();
            writter.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex){
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else {
                in = urlConnection.getInputStream();
                String jsonstring="";
                try {
                    jsonstring=getStringFromInputStream(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonstring;
            }
        }
        finally {
            if(in!=null){
                in.close();
            }
        }
    }

    public String tokenVerifier(String Url) throws IOException, JSONException {
        InputStream in=null;
        try {
            URL url = new URL(UrlApi+Url);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Authorization",miituoKey);
            urlConnection.connect();
            OutputStreamWriter writter=new OutputStreamWriter(urlConnection.getOutputStream());
            writter.flush();
            writter.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex){
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else {
                in = urlConnection.getInputStream();
                String jsonstring="";
                try {
                    jsonstring=getStringFromInputStream(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonstring;
            }
        }
        finally {
            if(in!=null){
                in.close();
            }
        }
    }

    public List<TiposVehiculos> getInfoTipoVehiculos(String Url) throws IOException {

        final String TAG = "JsonParser.java";
        List<TiposVehiculos> InfoList=null;
        InputStream in=null;
        try {
            URL url = new URL(UrlApi+Url+"");
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Authorization",miituoKey);
            urlConnection.connect();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else
                in = urlConnection.getInputStream();
            JSONArray resultJson=null;
            String jsonstring="";
            try {
                jsonstring=getStringFromInputStream(in);
                //Log.w("Data",jsonstring);
                if(jsonstring!=null || jsonstring!="") {
                    Gson parseJson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss").create();
                    InfoList = parseJson.fromJson(jsonstring, new TypeToken<List<TiposVehiculos>>() {
                    }.getType());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return InfoList;
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    public List<ModelosVehiculos> getInfoModelosVehiculos(String Url) throws IOException {

        final String TAG = "JsonParser.java";
        List<ModelosVehiculos> InfoList=null;
        InputStream in=null;
        try {
            URL url = new URL(UrlApi+Url+"");
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Authorization",miituoKey);
            urlConnection.connect();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else
                in = urlConnection.getInputStream();
            JSONArray resultJson=null;
            String jsonstring="";
            try {
                jsonstring=getStringFromInputStream(in);
                //Log.w("Data",jsonstring);
                if(jsonstring!=null || jsonstring!="") {
                    Gson parseJson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss").create();
                    InfoList = parseJson.fromJson(jsonstring, new TypeToken<List<ModelosVehiculos>>() {
                    }.getType());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return InfoList;
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    public static String getStringFromInputStream(InputStream stream) throws IOException{
        int n = 0;
        char[] buffer = new char[1024 * 4];
        InputStreamReader reader = new InputStreamReader(stream, "UTF8");
        StringWriter writer = new StringWriter();
        while (-1 != (n = reader.read(buffer))) writer.write(buffer, 0, n);
        return writer.toString();
    }

    public List<MarcasVehiculos> getInfoMarcaVehiculos(String Url, String string, String string1) throws IOException {
        final String TAG = "JsonParser.java";
        List<MarcasVehiculos> InfoList=null;
        InputStream in=null;
        try {
            URL url = new URL(UrlApi+Url+"/Model/"+string+"/Type/"+string1);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Authorization",miituoKey);
            urlConnection.connect();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else
                in = urlConnection.getInputStream();
            JSONArray resultJson=null;
            String jsonstring="";
            try {
                jsonstring=getStringFromInputStream(in);
                //Log.w("Data",jsonstring);
                if(jsonstring!=null || jsonstring!="") {
                    Gson parseJson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss").create();
                    InfoList = parseJson.fromJson(jsonstring, new TypeToken<List<MarcasVehiculos>>() {
                    }.getType());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return InfoList;
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    public List<MarcasVehiculos> getInfoSubtiposVehiculos(String Url, String string) throws IOException{
        final String TAG = "JsonParser.java";
        List<MarcasVehiculos> InfoList=null;
        InputStream in=null;
        try {
            URL url = new URL(UrlApi+Url+string);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Authorization",miituoKey);
            urlConnection.connect();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else
                in = urlConnection.getInputStream();
            JSONArray resultJson=null;
            String jsonstring="";
            try {
                jsonstring=getStringFromInputStream(in);
                //Log.w("Data",jsonstring);
                if(jsonstring!=null || jsonstring!="") {
                    Gson parseJson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss").create();
                    InfoList = parseJson.fromJson(jsonstring, new TypeToken<List<MarcasVehiculos>>() {
                    }.getType());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return InfoList;
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    public List<MarcasVehiculos> getInfoDescripcionVehiculos(String Url, String string) throws IOException{
        final String TAG = "JsonParser.java";
        List<MarcasVehiculos> InfoList=null;
        InputStream in=null;
        try {
            URL url = new URL(UrlApi+Url+string);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Authorization",miituoKey);
            urlConnection.connect();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else
                in = urlConnection.getInputStream();
            JSONArray resultJson=null;
            String jsonstring="";
            try {
                jsonstring=getStringFromInputStream(in);
                //Log.w("Data",jsonstring);
                if(jsonstring!=null || jsonstring!="") {
                    Gson parseJson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss").create();
                    InfoList = parseJson.fromJson(jsonstring, new TypeToken<List<MarcasVehiculos>>() {
                    }.getType());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return InfoList;
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    public List<GarageTipos> getInfoGarageTipos(String Url) throws IOException {
        final String TAG = "JsonParser.java";
        List<GarageTipos> InfoList=null;
        InputStream in=null;
        try {
            URL url = new URL(UrlApi+Url+"");
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Authorization",miituoKey);
            urlConnection.connect();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else
                in = urlConnection.getInputStream();
            JSONArray resultJson=null;
            String jsonstring="";
            try {
                jsonstring=getStringFromInputStream(in);
                //Log.w("Data",jsonstring);
                if(jsonstring!=null || jsonstring!="") {
                    Gson parseJson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss").create();
                    InfoList = parseJson.fromJson(jsonstring, new TypeToken<List<GarageTipos>>() {
                    }.getType());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return InfoList;
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    //Enviamos odometro por pirmera vez-----------------------------------------------------------------
    public CotizaTipos getQuotation(String Url, String gender, String vehicleTMMSDId, String birthdayDate, String hasGarage, String postal) throws IOException {
        CotizaTipos InfoList=null;
        InputStream in=null;
        try {
            URL url = new URL(UrlApi+Url);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(0);
            urlConnection.setConnectTimeout(0);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("Authorization",miituoKey);
            urlConnection.connect();
            JSONObject sendObject=new JSONObject();
            try {
                sendObject.put("Gender", gender);
                sendObject.put("VehicleTMMSDId",vehicleTMMSDId);
                sendObject.put("BirthdayDate",birthdayDate);
                sendObject.put("HasGarage",hasGarage);
                sendObject.put("ZipCode",postal);
                sendObject.put("ClientIp","");
                sendObject.put("Device","App_Android");
            }
            catch (JSONException ex)
            {
                throw new IOException(ex.getMessage());
            }

            OutputStreamWriter writter=new OutputStreamWriter(urlConnection.getOutputStream());
            writter.write(sendObject.toString());
            writter.flush();
            writter.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else {
                in = urlConnection.getInputStream();

                JSONArray resultJson=null;
                String jsonstring="";
                try {
                    jsonstring=getStringFromInputStream(in);
                    //Log.w("Data",jsonstring);
                    if(jsonstring!=null || jsonstring!="") {
                        Gson parseJson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss").create();
                        InfoList = parseJson.fromJson(jsonstring, new TypeToken<CotizaTipos>() {
                        }.getType());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return InfoList;
                //Boolean jsonstring;
                //jsonstring = Boolean.parseBoolean(getStringFromInputStream(in));
            }
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    public String updateQuotation(String quotation, int id, String nombre, String correo,String cel) throws IOException{

        InputStream in=null;
        try {
            URL url = new URL(UrlApi+quotation);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(0);
            urlConnection.setConnectTimeout(0);
            urlConnection.setRequestMethod("PUT");
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("Authorization",miituoKey);
            urlConnection.connect();
            JSONObject sendObject=new JSONObject();
            try {

                sendObject.put("Id", id);
                sendObject.put("Name",nombre);
                sendObject.put("Mail",correo);
                sendObject.put("Telefono",cel);
            }
            catch (JSONException ex)
            {
                throw new IOException(ex.getMessage());
            }

            OutputStreamWriter writter=new OutputStreamWriter(urlConnection.getOutputStream());
            writter.write(sendObject.toString());
            writter.flush();
            writter.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else {
                in = urlConnection.getInputStream();
                String jsonstring;
                jsonstring = (getStringFromInputStream(in));
                return  jsonstring;
            }
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    public String sendMailQuotation(String Url, int i, String string) throws IOException, JSONException {

        InputStream in=null;
        try {
            URL url = new URL(UrlApi+Url);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            //urlConnection.setReadTimeout(0);
            //urlConnection.setConnectTimeout(0);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            //urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("Authorization",miituoKey);
            urlConnection.connect();
            JSONObject sendObject=new JSONObject();
            JSONObject subItem=new JSONObject();

            String[]correos = new String[]{string};
            List<String> mails = new ArrayList<>();
            mails.add(string);

            try {
                String json = new Gson().toJson(mails);

                sendObject.put("To",string);
                sendObject.put("Type",i);
            }
            catch (JSONException ex)
            {
                throw new IOException(ex.getMessage());
            }

            OutputStreamWriter writter=new OutputStreamWriter(urlConnection.getOutputStream());
            writter.write(sendObject.toString());
            writter.flush();
            writter.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else {
                in = urlConnection.getInputStream();

                JSONArray resultJson=null;
                String jsonstring="";
                try {
                    jsonstring=getStringFromInputStream(in);
                    //Log.w("Data",jsonstring);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Boolean jsonstring;
                //jsonstring = Boolean.parseBoolean(getStringFromInputStream(in));
                return jsonstring;
            }
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }
}
