package miituo.com.miituo.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Policies{

    @DatabaseField(id = true)
    private int Id=0;
    @DatabaseField
    private boolean HasVehiclePictures=false;
    @DatabaseField
    private boolean HasOdometerPicture=false;
    @DatabaseField
    private boolean HasSiniester=false;
    @DatabaseField
    private int ReportState=0;
    @DatabaseField
    private String NoPolicy = "";
    @DatabaseField
    private String StartDate = "";
    @DatabaseField
    private String VigencyDate = "";
    @DatabaseField
    private double Rate = 0.00;
    @DatabaseField
    private int LastOdometer = 0;
    @DatabaseField
    private int Mensualidad = 0;
    @DatabaseField
    private String LimitReportDate = "";
    @DatabaseField
    private int InsuranceCarrierId = 0;
    @DatabaseField
    private String InsuranceCarrierName = "";
    @DatabaseField
    private int StateId = 0;
    @DatabaseField
    private String StateDescription = "";
    @DatabaseField
    private int CoverageId = 0;
    @DatabaseField
    private String CoverageDescription = "";
    @DatabaseField
    private String Tickets = "";
    @DatabaseField
    private String PaymentType = "";

    @DatabaseField
    private int clientId = 0;
    @DatabaseField
    private String clientCelphone = "";
    @DatabaseField
    private String clientToken = "";
    @DatabaseField
    private String clientName = "";
    @DatabaseField
    private String clientLastName = "";
    @DatabaseField
    private String clientMetherName = "";
    @DatabaseField
    private String clientEmail = "";

    @DatabaseField
    private int VehicleId = 0;
    @DatabaseField
    private String VehicleColor = "";
    @DatabaseField
    private String VehiclePlates = "";
    @DatabaseField
    private String VehicleNoMotor = "";
    @DatabaseField
    private String VehicleCapacity = "";
    @DatabaseField
    private int VehicleBrandId = 0;
    @DatabaseField
    private String VehicleBrandDescription = "";
    @DatabaseField
    private int VehicleModelId = 0;
    @DatabaseField
    private int VehicleModelModel = 0;
    @DatabaseField
    private int VehicleSubtypeId = 0;
    @DatabaseField
    private String VehicleSubtypeDescription = "";
    @DatabaseField
    private int VehicleTypeId = 0;
    @DatabaseField
    private String VehicleTypeDescription = "";
    @DatabaseField
    private int VehicleDescriptionId = 0;
    @DatabaseField
    private String VehicleDescriptionDescription = "";


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public boolean isHasVehiclePictures() {
        return HasVehiclePictures;
    }

    public void setHasVehiclePictures(boolean hasVehiclePictures) {
        HasVehiclePictures = hasVehiclePictures;
    }

    public boolean isHasOdometerPicture() {
        return HasOdometerPicture;
    }

    public void setHasOdometerPicture(boolean hasOdometerPicture) {
        HasOdometerPicture = hasOdometerPicture;
    }

    public boolean isHasSiniester() {
        return HasSiniester;
    }

    public void setHasSiniester(boolean hasSiniester) {
        HasSiniester = hasSiniester;
    }

    public int getReportState() {
        return ReportState;
    }

    public void setReportState(int reportState) {
        ReportState = reportState;
    }

    public String getNoPolicy() {
        return NoPolicy;
    }

    public void setNoPolicy(String noPolicy) {
        NoPolicy = noPolicy;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getVigencyDate() {
        return VigencyDate;
    }

    public void setVigencyDate(String vigencyDate) {
        VigencyDate = vigencyDate;
    }

    public double getRate() {
        return Rate;
    }

    public void setRate(double rate) {
        Rate = rate;
    }

    public int getLastOdometer() {
        return LastOdometer;
    }

    public void setLastOdometer(int lastOdometer) {
        LastOdometer = lastOdometer;
    }

    public int getMensualidad() {
        return Mensualidad;
    }

    public void setMensualidad(int mensualidad) {
        Mensualidad = mensualidad;
    }

    public String getLimitReportDate() {
        return LimitReportDate;
    }

    public void setLimitReportDate(String limitReportDate) {
        LimitReportDate = limitReportDate;
    }

    public int getInsuranceCarrierId() {
        return InsuranceCarrierId;
    }

    public void setInsuranceCarrierId(int insuranceCarrierId) {
        InsuranceCarrierId = insuranceCarrierId;
    }

    public String getInsuranceCarrierName() {
        return InsuranceCarrierName;
    }

    public void setInsuranceCarrierName(String insuranceCarrierName) {
        InsuranceCarrierName = insuranceCarrierName;
    }

    public int getStateId() {
        return StateId;
    }

    public void setStateId(int stateId) {
        StateId = stateId;
    }

    public String getStateDescription() {
        return StateDescription;
    }

    public void setStateDescription(String stateDescription) {
        StateDescription = stateDescription;
    }

    public int getCoverageId() {
        return CoverageId;
    }

    public void setCoverageId(int coverageId) {
        CoverageId = coverageId;
    }

    public String getCoverageDescription() {
        return CoverageDescription;
    }

    public void setCoverageDescription(String coverageDescription) {
        CoverageDescription = coverageDescription;
    }

    public String getTickets() {
        return Tickets;
    }

    public void setTickets(String tickets) {
        Tickets = tickets;
    }

    public String getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(String paymentType) {
        PaymentType = paymentType;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getClientCelphone() {
        return clientCelphone;
    }

    public void setClientCelphone(String clientCelphone) {
        this.clientCelphone = clientCelphone;
    }

    public String getClientToken() {
        return clientToken;
    }

    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientLastName() {
        return clientLastName;
    }

    public void setClientLastName(String clientLastName) {
        this.clientLastName = clientLastName;
    }

    public String getClientMetherName() {
        return clientMetherName;
    }

    public void setClientMetherName(String clientMetherName) {
        this.clientMetherName = clientMetherName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public int getVehicleId() {
        return VehicleId;
    }

    public void setVehicleId(int vehicleId) {
        VehicleId = vehicleId;
    }

    public String getVehicleColor() {
        return VehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        VehicleColor = vehicleColor;
    }

    public String getVehiclePlates() {
        return VehiclePlates;
    }

    public void setVehiclePlates(String vehiclePlates) {
        VehiclePlates = vehiclePlates;
    }

    public String getVehicleNoMotor() {
        return VehicleNoMotor;
    }

    public void setVehicleNoMotor(String vehicleNoMotor) {
        VehicleNoMotor = vehicleNoMotor;
    }

    public String getVehicleCapacity() {
        return VehicleCapacity;
    }

    public void setVehicleCapacity(String vehicleCapacity) {
        VehicleCapacity = vehicleCapacity;
    }

    public int getVehicleBrandId() {
        return VehicleBrandId;
    }

    public void setVehicleBrandId(int vehicleBrandId) {
        VehicleBrandId = vehicleBrandId;
    }

    public String getVehicleBrandDescription() {
        return VehicleBrandDescription;
    }

    public void setVehicleBrandDescription(String vehicleBrandDescription) {
        VehicleBrandDescription = vehicleBrandDescription;
    }

    public int getVehicleModelId() {
        return VehicleModelId;
    }

    public void setVehicleModelId(int vehicleModelId) {
        VehicleModelId = vehicleModelId;
    }

    public int getVehicleModelModel() {
        return VehicleModelModel;
    }

    public void setVehicleModelModel(int vehicleModelModel) {
        VehicleModelModel = vehicleModelModel;
    }

    public int getVehicleSubtypeId() {
        return VehicleSubtypeId;
    }

    public void setVehicleSubtypeId(int vehicleSubtypeId) {
        VehicleSubtypeId = vehicleSubtypeId;
    }

    public String getVehicleSubtypeDescription() {
        return VehicleSubtypeDescription;
    }

    public void setVehicleSubtypeDescription(String vehicleSubtypeDescription) {
        VehicleSubtypeDescription = vehicleSubtypeDescription;
    }

    public int getVehicleTypeId() {
        return VehicleTypeId;
    }

    public void setVehicleTypeId(int vehicleTypeId) {
        VehicleTypeId = vehicleTypeId;
    }

    public String getVehicleTypeDescription() {
        return VehicleTypeDescription;
    }

    public void setVehicleTypeDescription(String vehicleTypeDescription) {
        VehicleTypeDescription = vehicleTypeDescription;
    }

    public int getVehicleDescriptionId() {
        return VehicleDescriptionId;
    }

    public void setVehicleDescriptionId(int vehicleDescriptionId) {
        VehicleDescriptionId = vehicleDescriptionId;
    }

    public String getVehicleDescriptionDescription() {
        return VehicleDescriptionDescription;
    }

    public void setVehicleDescriptionDescription(String vehicleDescriptionDescription) {
        VehicleDescriptionDescription = vehicleDescriptionDescription;
    }
}
