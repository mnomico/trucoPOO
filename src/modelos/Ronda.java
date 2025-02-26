package modelos;

public class Ronda {
    private final int numeroRonda;
    private Carta cartaJ1;
    private Carta cartaJ2;
    private Carta cartaGanadora;
    private int ganadorRonda;

    public Ronda(int numeroRonda) {
        this.numeroRonda = numeroRonda;
    }

    // Getters y setters
    public int getNumeroRonda() {
        return this.numeroRonda;
    }

    public int getGanadorRonda() {
        return this.ganadorRonda;
    }

    public Carta getCarta(int jugador) {
        if (jugador == 1) {
            return cartaJ1;
        } else if (jugador == 2) {
            return cartaJ2;
        }
        return null;
    }

    public Carta getCartaGanadora() {
        return cartaGanadora;
    }

    // Setters

    // Métodos principales
    public void jugarCarta(Carta carta, int jugador) {
        if (jugador == 1) {
            cartaJ1 = carta;
        } else if (jugador == 2) {
            cartaJ2 = carta;
        }

        if (cartaJ1 != null && cartaJ2 != null) {
            determinarGanador();
        }
    }

    private void determinarGanador() {
        int diferencia = cartaJ1.getValor().compareTo(cartaJ2.getValor());
        if (diferencia < 0) {
            this.cartaGanadora = cartaJ2;
            this.ganadorRonda = 2;
        } else if (diferencia > 0){
            this.cartaGanadora = cartaJ1;
            this.ganadorRonda = 1;
        } else {
            this.cartaGanadora = null;
            this.ganadorRonda = 0;
        }
    }
}
