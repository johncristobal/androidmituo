package miituo.com.miituo.cotizar;

/**
 * Created by john.cristobal on 10/04/18.
 */

public class TiposVehiculos {

    private int Id;
    private String Description;

    public TiposVehiculos(int id, String descritcion) {
        Id = id;
        Description = descritcion;
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
