package modelos;

public class Mano {
    private static int numeroMano = 0;
    private int puntosTruco = 1;
    private int puntosEnvido;
    private final int jugadorMano;
    private int ganadorMano;
    private int ganadorEnvido;
    private boolean envidoCantado;
    private boolean trucoCantado;
    private final Ronda[] rondas;
    private int rondaActual;

    public Mano(int jugadorMano) {
        this.rondas = new Ronda[3];
        for (int i = 0; i < 3; i++) {
            rondas[i] = new Ronda(i);
        }
        this.rondaActual = 0;
        this.envidoCantado = false;
        this.trucoCantado = false;
        this.jugadorMano = jugadorMano;
        numeroMano++;
    }

    public static int getNumeroMano() {
        return numeroMano;
    }

    // Getters y setters
    public int getNumeroRonda() {
        return this.rondaActual;
    }

    public int getGanadorRonda() {
        if (rondaActual < 3) {
            return this.rondas[rondaActual].getGanadorRonda();
        }
        return this.rondas[rondaActual-1].getGanadorRonda();
    }

    public int getGanadorMano() {
        return this.ganadorMano;
    }

    public int getGanadorEnvido() {
        return this.ganadorEnvido;
    }

    public Carta getCarta(int jugador) {
        if (rondaActual < 3) {
            return this.rondas[rondaActual].getCarta(jugador);
        }
        return this.rondas[rondaActual-1].getCarta(jugador);
    }

    public Carta getCartaGanadora() {
        if (rondaActual < 3) {
            return this.rondas[rondaActual].getCartaGanadora();
        }
        return this.rondas[rondaActual-1].getCartaGanadora();
    }

    public boolean getTrucoCantado() {
        return this.trucoCantado;
    }

    public boolean getEnvidoCantado() {
        return this.envidoCantado;
    }

    public int getPuntosTruco() {
        return this.puntosTruco;
    }

    public int getPuntosEnvido() {
        return this.puntosEnvido;
    }

    // Setters

    // Métodos principales
    public void jugarCarta(Carta carta, int jugador) {
        this.rondas[rondaActual].jugarCarta(carta, jugador);
    }

    public void determinarGanador() {
        int rondasJ1 = 0;
        int rondasJ2 = 0;
        int pardas = 0;
        int ganadorRonda;

        for (int i = 0; i < 3; i++) {
            ganadorRonda = rondas[i].getGanadorRonda();
            switch (ganadorRonda) {
                case 0: pardas++;
                case 1: rondasJ1++;
                case 2: rondasJ2++;
            }
        }

        if (rondasJ1 >= 2) {
            this.ganadorMano = 1;
        } else if (rondasJ2 >= 2) {
            this.ganadorMano = 2;
        } else if (pardas == 1) {
            // Si ambos jugadores ganan una ronda pero empaten en una, gana el primero que haya ganado una ronda
            for (int i = 0; i < 3; i++) {
                ganadorRonda = rondas[i].getGanadorRonda();
                if (ganadorRonda != 0){
                    this.ganadorMano = ganadorRonda;
                }
            }
        } else {
            // Si todas las rondas se empatan, gana el jugador que fue mano
            this.ganadorMano = jugadorMano;
        }
    }

    public void gana(int jugador) {
        this.ganadorMano = jugador;
    }

    public void nuevaRonda() {
        this.rondaActual++;
    }

    public void cantarTruco() {
        this.trucoCantado = true;
    }

    public void cantarEnvido() {
        this.envidoCantado = true;
    }

    public void aumentarPuntos(Apuesta apuesta, boolean quiero) {
        if (quiero) {
            switch (apuesta) {
                case TRUCO -> puntosTruco = 2;
                case RETRUCO -> puntosTruco = 3;
                case VALECUATRO -> puntosTruco = 4;
                case ENVIDO -> puntosEnvido = 2;
                case REAL_ENVIDO -> puntosEnvido = 3;
                case ENVIDO_ENVIDO -> puntosEnvido = 4;
                case ENVIDO_REAL_ENVIDO -> puntosEnvido = 5;
                case ENVIDO_ENVIDO_REAL_ENVIDO -> puntosEnvido = 7;
            }
        } else {
            switch (apuesta) {
                case TRUCO -> puntosTruco = 1;
                case RETRUCO -> puntosTruco = 2;
                case VALECUATRO -> puntosTruco = 3;
                case ENVIDO, REAL_ENVIDO, FALTA_ENVIDO -> puntosEnvido = 1;
                case ENVIDO_ENVIDO, ENVIDO_REAL_ENVIDO, ENVIDO_FALTA_ENVIDO -> puntosEnvido = 2;
                case REAL_ENVIDO_FALTA_ENVIDO -> puntosEnvido = 3;
                case ENVIDO_ENVIDO_REAL_ENVIDO -> puntosEnvido = 4;
                case ENVIDO_REAL_ENVIDO_FALTA_ENVIDO -> puntosEnvido = 5;
                case ENVIDO_ENVIDO_REAL_ENVIDO_FALTA_ENVIDO -> puntosEnvido = 7;
            }
        }
    }

    public int calcularGanadorEnvido(int tantoJ1, int tantoJ2) {
        if (tantoJ1 > tantoJ2) {
            this.ganadorEnvido = 1;
        } else if (tantoJ2 > tantoJ1){
            this.ganadorEnvido = 2;
        } else this.ganadorEnvido = jugadorMano;
        return ganadorEnvido;
    }

}
