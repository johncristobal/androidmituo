package miituo.com.miituo.cotizar;


import java.util.List;

/**
 * Created by john.cristobal on 10/04/18.
 */

public class CotizaTipos {

    private int Id;
    private List<RateList> RateList;

    public CotizaTipos(int id, List<RateList> rateList) {
        Id = id;
        RateList = rateList;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public List<RateList> getRateList() {
        return RateList;
    }

    public void setRateList(List<RateList> rateList) {
        RateList = rateList;
    }
}
