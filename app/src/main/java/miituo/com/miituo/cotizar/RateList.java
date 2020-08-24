package miituo.com.miituo.cotizar;

public class RateList {

    private String Name;
    private double Amount;
    private int MovementTypePolicyId;
    private int CoverageId;
    private String HelpText;

    public RateList(String name, double amount, int movementTypePolicyId, int coverageId, String helpText) {
        Name = name;
        Amount = amount;
        MovementTypePolicyId = movementTypePolicyId;
        CoverageId = coverageId;
        HelpText = helpText;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public int getMovementTypePolicyId() {
        return MovementTypePolicyId;
    }

    public void setMovementTypePolicyId(int movementTypePolicyId) {
        MovementTypePolicyId = movementTypePolicyId;
    }

    public int getCoverageId() {
        return CoverageId;
    }

    public void setCoverageId(int coverageId) {
        CoverageId = coverageId;
    }

    public String getHelpText() {
        return HelpText;
    }

    public void setHelpText(String helpText) {
        HelpText = helpText;
    }
}
