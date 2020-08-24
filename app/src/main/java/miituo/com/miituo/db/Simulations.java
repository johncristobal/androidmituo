package miituo.com.miituo.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable
public class Simulations implements Comparable{

    // Id para uso interno, campo autogenerado.
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private int idSimulation;

    @DatabaseField
    private int idQuotation;

    @DatabaseField
    private String sexo;

    @DatabaseField
    private String vehicleId;

    @DatabaseField
    private String birthday;

    @DatabaseField
    private String formatNac;

    @DatabaseField
    private String garage;

    @DatabaseField
    private String cp;

    @DatabaseField
    private String plan;

    @DatabaseField
    private double pagoUnico;

    @DatabaseField
    private String vehicleDesc;

    @DatabaseField
    private int initialOdometer;

    @DatabaseField
    private int finalOdometer;

    @DatabaseField
    private Date CreatedDate;

    @DatabaseField
    private Date ReportNotificationDate;

    @DatabaseField
    private Date ContractNotificationDate;

    @DatabaseField
    private int Status;

    // Constructor vacio necesario para ORM-lite
    public Simulations() {
    }

    public Simulations(int idSimulation, int idQuotation, String sexo, String vehicleId, String birthday, String formatNac, String garage, String cp, String plan,
            double pagoUnico,  String vehicleDesc, int initialOdometer, int finalOdometer, Date CreatedDate, Date ReportNotificationDate, Date ContractNotificationDate, int Status){
        this.idSimulation=idSimulation;
        this.idQuotation=idQuotation;
        this.sexo=sexo;
        this.vehicleId=vehicleId;
        this.birthday=birthday;
        this.formatNac=formatNac;
        this.garage=garage;
        this.cp=cp;
        this.plan=plan;
        this.pagoUnico=pagoUnico;
        this.vehicleDesc=vehicleDesc;
        this.initialOdometer=initialOdometer;
        this.finalOdometer=finalOdometer;
        this.CreatedDate=CreatedDate;
        this.ReportNotificationDate=ReportNotificationDate;
        this.ContractNotificationDate=ContractNotificationDate;
        this.Status=Status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdSimulation() {
        return idSimulation;
    }

    public void setIdSimulation(int idSimulation) {
        this.idSimulation = idSimulation;
    }

    public int getIdQuotation() {
        return idQuotation;
    }

    public void setIdQuotation(int idQuotation) {
        this.idQuotation = idQuotation;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getFormatNac() {
        return formatNac;
    }

    public void setFormatNac(String formatNac) {
        this.formatNac = formatNac;
    }

    public String getGarage() {
        return garage;
    }

    public void setGarage(String garage) {
        this.garage = garage;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public double getPagoUnico() {
        return pagoUnico;
    }

    public void setPagoUnico(double pagoUnico) {
        this.pagoUnico = pagoUnico;
    }

    public String getVehicleDesc() {
        return vehicleDesc;
    }

    public void setVehicleDesc(String vehicleDesc) {
        this.vehicleDesc = vehicleDesc;
    }

    public int getInitialOdometer() {
        return initialOdometer;
    }

    public void setInitialOdometer(int initialOdometer) {
        this.initialOdometer = initialOdometer;
    }

    public int getFinalOdometer() {
        return finalOdometer;
    }

    public void setFinalOdometer(int finalOdometer) {
        this.finalOdometer = finalOdometer;
    }

    public Date getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(Date createdDate) {
        CreatedDate = createdDate;
    }

    public Date getReportNotificationDate() {
        return ReportNotificationDate;
    }

    public void setReportNotificationDate(Date reportNotificationDate) {
        ReportNotificationDate = reportNotificationDate;
    }

    public Date getContractNotificationDate() {
        return ContractNotificationDate;
    }

    public void setContractNotificationDate(Date contractNotificationDate) {
        ContractNotificationDate = contractNotificationDate;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    @Override
    public int compareTo(Object comparestu) {
        int compareage=((Simulations)comparestu).getId();
        /* For Ascending order*/
//        return this.id-compareage;
        /* For Descending order do like this */
        return compareage-this.id;
    }
}