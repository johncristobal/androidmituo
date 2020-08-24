package miituo.com.miituo.cotizar;


/**
 * Created by john.cristobal on 10/04/18.
 */

public class GarageTipos {

    private int Id;
    private String Descripcion;
    private int Fact_Garaje;
    private int Id_Estatus;

    public GarageTipos(int id, String description, int fact_Garaje, int id_Estatus) {
        Id = id;
        Descripcion = description;
        Fact_Garaje = fact_Garaje;
        Id_Estatus = id_Estatus;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDescription() {
        return Descripcion;
    }

    public void setDescription(String description) {
        Descripcion = description;
    }

    public int getFact_Garaje() {
        return Fact_Garaje;
    }

    public void setFact_Garaje(int fact_Garaje) {
        Fact_Garaje = fact_Garaje;
    }

    public int getId_Estatus() {
        return Id_Estatus;
    }

    public void setId_Estatus(int id_Estatus) {
        Id_Estatus = id_Estatus;
    }
}
