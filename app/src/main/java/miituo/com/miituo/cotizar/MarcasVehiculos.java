package miituo.com.miituo.cotizar;

/**
 * Created by john.cristobal on 10/04/18.
 */

public class MarcasVehiculos {

    private int Id;
    private String Description;

    public MarcasVehiculos(int id, String description) {
        Id = id;
        Description = description;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
