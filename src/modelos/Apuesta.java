package modelos;

public enum Apuesta {
    ENVIDO(1,2),
    REAL_ENVIDO(1,3),
    ENVIDO_ENVIDO(2,4),
    ENVIDO_REAL_ENVIDO(2,5),
    ENVIDO_ENVIDO_REAL_ENVIDO(4,7),
    TRUCO(1,2),
    RETRUCO(2,3),
    VALECUATRO(3,4);

    private final int noQuiero;
    private final int quiero;

    Apuesta(int noQuiero, int quiero){
        this.noQuiero = noQuiero;
        this.quiero = quiero;
    }

    public int getNoQuiero(){
        return this.noQuiero;
    }

    public int getQuiero(){
        return this.quiero;
    }
}
