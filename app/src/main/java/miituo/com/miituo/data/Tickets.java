package miituo.com.miituo.data;

public class Tickets {

    public int Id;
    public int Status;
    public int PolicyId;

    public Tickets(int id, int status, int policyId) {
        Id = id;
        Status = status;
        PolicyId = policyId;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getPolicyId() {
        return PolicyId;
    }

    public void setPolicyId(int policyId) {
        PolicyId = policyId;
    }
}
