package miituo.com.miituo.miltiaseguradora;

public enum Aseguradoras {
    Atlas(7),
    Ana(2),
    Gnp(3)
    ; // semicolon needed when fields / methods follow
    public static final int atlas=7;
    public static final int ana=1;
    public static final int gnp=3;

    private final int levelCode;

    Aseguradoras(int levelCode) {
        this.levelCode = levelCode;
    }

    public int getLevelCode() {
        return this.levelCode;
    }
}
