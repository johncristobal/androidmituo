package miituo.com.miituo.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable
public class Notifications implements Comparable{

    // Id para uso interno, campo autogenerado.
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private int tipoPush;

    @DatabaseField
    private String title;

    @DatabaseField
    private String msg;

    @DatabaseField
    private String tarifa;

    @DatabaseField
    private String extra;

    @DatabaseField
    private Date postDate;

    @DatabaseField
    private boolean isRead;

    // Constructor vacio necesario para ORM-lite
    public Notifications() {
    }

    public Notifications(int tipoPush, String title, String msg, String tarifa,String extra, boolean isRead, Date postDate){
        this.tipoPush=tipoPush;
        this.title=title;
        this.msg=msg;
        this.tarifa=tarifa;
        this.extra=extra;
        this.isRead=isRead;
        this.postDate=postDate;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public int getTipoPush() {
        return tipoPush;
    }

    public void setTipoPush(int tipoPush) {
        this.tipoPush = tipoPush;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTarifa() {
        return tarifa;
    }

    public void setTarifa(String tarifa) {
        this.tarifa = tarifa;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    @Override
    public int compareTo(Object comparestu) {
        int compareage=((Notifications)comparestu).getId();
        /* For Ascending order*/
//        return this.id-compareage;
        /* For Descending order do like this */
        return compareage-this.id;
    }
}