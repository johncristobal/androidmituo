package miituo.com.miituo.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class EnviosPendientes implements Comparable{

    // Id para uso interno, campo autogenerado.
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private int tipoEnvio;

    @DatabaseField
    private int pictureType;

    @DatabaseField
    private int idPoliza;

    @DatabaseField
    private String uri;

    @DatabaseField
    private String request;

    @DatabaseField
    private String body;

    @DatabaseField
    private int odometro;

    // Constructor vacio necesario para ORM-lite
    public EnviosPendientes() {
    }

    public EnviosPendientes(int tipoEnvio, String uri,String request,String body){
        this.tipoEnvio=tipoEnvio;
        this.uri=uri;
        this.request=request;
        this.body=body;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTipoEnvio() {
        return tipoEnvio;
    }

    public void setTipoEnvio(int tipoEnvio) {
        this.tipoEnvio = tipoEnvio;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getIdPoliza() {
        return idPoliza;
    }

    public void setIdPoliza(int idPoliza) {
        this.idPoliza = idPoliza;
    }

    public int getPictureType() {
        return pictureType;
    }

    public void setPictureType(int pictureType) {
        this.pictureType = pictureType;
    }

    public int getOdometro() {
        return odometro;
    }

    public void setOdometro(int odometro) {
        this.odometro = odometro;
    }

    @Override
    public int compareTo(Object comparestu) {
        int compareage=((EnviosPendientes)comparestu).getId();
        /* For Ascending order*/
        return this.id-compareage;

        /* For Descending order do like this */
        //return compareage-this.studentage;
    }

//    @Override
//    public int compareTo(@NonNull Object o) {
//        return 0;
//    }
}