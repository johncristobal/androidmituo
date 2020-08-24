package miituo.com.miituo.cotizar;

/**
 * Created by john.cristobal on 10/04/18.
 */

public class ModelosVehiculos {

    private int Id;
    private int Model;

    public ModelosVehiculos(int id, int Model) {
        this.Id = id;
        this.Model = Model;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getModel() {
        return Model;
    }

    public void setModel(int model) {
        Model = model;
    }
}
