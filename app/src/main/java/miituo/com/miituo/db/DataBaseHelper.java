package miituo.com.miituo.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import miituo.com.miituo.data.IinfoClient;
import miituo.com.miituo.utils.AlarmaForPendingShipments;

public class DataBaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "miituodb.db";
    private static final int DATABASE_VERSION = 8;

    private Context contexto;

    private Dao<EnviosPendientes, Integer> enviosPendientes = null;
    private Dao<Policies, Integer> policies = null;
    private Dao<Notifications, Integer> notifications = null;
    private Dao<Simulations, Integer> simulations = null;
    private Dao<Logger, Integer> logger = null;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.contexto = context;
    }

    public void setContexto(Context contexto) {
        this.contexto = contexto;
    }

    /**
     *
     * Metodo que crea la BD por primera vez. Se usa el metodo createTable para
     * crear todas las tablas de la app.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource cs) {
        try {
            Log.i("dbH", "onCreate-Creando la base de datos!");
            // Primero los catalogos
//            TableUtils.createTableIfNotExists(cs, EnviosPendientes.class);
//            TableUtils.createTableIfNotExists(cs, Policies.class);
            TableUtils.createTableIfNotExists(cs, Notifications.class);
            TableUtils.createTableIfNotExists(cs, Simulations.class);
            TableUtils.createTableIfNotExists(cs, Logger.class);
        } catch (Exception e) {
            Log.e("dbH", "No se pudo crear la BD.", e);
            throw new RuntimeException(e);
        }
    }

    public void deleteDataBase(ConnectionSource connectionSource) {
        try {
//            TableUtils.dropTable(connectionSource, EnviosPendientes.class, true);
//            TableUtils.dropTable(connectionSource, Policies.class, true);
            TableUtils.dropTable(connectionSource, Notifications.class, true);
            TableUtils.dropTable(connectionSource, Simulations.class, true);
            TableUtils.dropTable(connectionSource, Logger.class, true);
        } catch (Exception e) {
            Log.e("dbH", "No se pudo borrar la BD.", e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    /* Metodo que se ejecuta en caso de una actualizacion. Nos permite crear una
     * rutina para ajustar los datos a nuevas versiones.*/
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        Log.i(DataBaseHelper.class.getName(), "onUpgrade");
        deleteDataBase(connectionSource);
        // Despues de borrar la BD vieja, creamos la nueva.
        onCreate(db, connectionSource);
    }

    public Dao<Notifications, Integer> getDaoNotifications() throws SQLException {
        if (notifications == null) {
            try {
                notifications = getDao(Notifications.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return notifications;
    }

    public Dao<Logger, Integer> getDaoLogger() throws SQLException {
        if (logger == null) {
            try {
                logger = getDao(Logger.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return logger;
    }

    public Dao<Simulations, Integer> getDaoSimulations() throws SQLException {
        if (simulations == null) {
            try {
                simulations = getDao(Simulations.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return simulations;
    }

    public Dao<EnviosPendientes, Integer> getDaoEnviosPendientes() throws SQLException {
        if (enviosPendientes == null) {
            try {
                enviosPendientes = getDao(EnviosPendientes.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return enviosPendientes;
    }

    public Dao<Policies, Integer> getDaoPolicies() throws SQLException {
        if (policies == null) {
            try {
                policies = getDao(Policies.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return policies;
    }

    /* Cierra la conexion a la BD.*/
    @Override
    public void close() {
        super.close();
    }



    public static void crearEnvioPendiente(Context c,int tipoEnvio, String uri,int pictureType,String request,int odometro){
        Dao<EnviosPendientes, Integer> enviosDAO = getHelper(c).getDaoEnviosPendientes();
        EnviosPendientes ep=new EnviosPendientes();
        ep.setTipoEnvio(tipoEnvio);
        ep.setIdPoliza(IinfoClient.getInfoClientObject().getPolicies().getId());
        ep.setUri(uri);
        ep.setPictureType(pictureType);
        ep.setRequest(request);
        ep.setOdometro(odometro);
        try {
            enviosDAO.create(ep);
            AlarmaForPendingShipments a=new AlarmaForPendingShipments();
            a.cancelAlarm(c);
            a.setAlarm(c,180000);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

    }
    private static DataBaseHelper getHelper(Context c) {
        DataBaseHelper databaseHelper = OpenHelperManager.getHelper(c, DataBaseHelper.class);
        databaseHelper.setContexto(c);
        return databaseHelper;
    }
}
