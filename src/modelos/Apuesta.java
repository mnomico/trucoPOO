package modelos;

public enum Apuesta {
    ENVIDO("ENVIDO"),
    REAL_ENVIDO("REAL ENVIDO"),
    FALTA_ENVIDO("FALTA ENVIDO"),
    ENVIDO_ENVIDO("ENVIDO"),
    ENVIDO_REAL_ENVIDO("REAL ENVIDO"),
    ENVIDO_FALTA_ENVIDO("FALTA ENVIDO"),
    REAL_ENVIDO_FALTA_ENVIDO("FALTA ENVIDO"),
    ENVIDO_ENVIDO_REAL_ENVIDO("REAL ENVIDO"),
    ENVIDO_REAL_ENVIDO_FALTA_ENVIDO("FALTA ENVIDO"),
    ENVIDO_ENVIDO_REAL_ENVIDO_FALTA_ENVIDO("FALTA ENVIDO"),
    TRUCO("TRUCO"),
    RETRUCO("RETRUCO"),
    VALECUATRO("VALECUATRO");

    private final String string;

    Apuesta(String string){
        this.string = string;
    }

    public String toString(){
        return string;
    }
}
