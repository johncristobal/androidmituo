package miituo.com.miituo.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable
public class Logger implements Comparable{

    // Id para uso interno, campo autogenerado.
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String event;

    @DatabaseField
    private String url;

    @DatabaseField
    private String msg;

    @DatabaseField
    private String input;

    @DatabaseField
    private String output;

    @DatabaseField
    private String date;

    @DatabaseField
    private String inshuranceData;

    @DatabaseField
    private String excepcion;

    public Logger(){}

    // Constructor vacio necesario para ORM-lite
    public Logger(String evento, String url, String msg, String input, String output, String datosPoliza, String excepcion){
        this.event =evento;
        this.url=url;
        this.msg=msg;
        this.input=input;
        this.output=output;
        this.excepcion =excepcion;
        this.inshuranceData =datosPoliza;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInshuranceData() {
        return inshuranceData;
    }

    public void setInshuranceData(String inshuranceData) {
        this.inshuranceData = inshuranceData;
    }

    public String getExcepcion() {
        return excepcion;
    }

    public void setExcepcion(String excepcion) {
        this.excepcion = excepcion;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public int compareTo(Object comparestu) {
        int compareage=((Logger)comparestu).getId();
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