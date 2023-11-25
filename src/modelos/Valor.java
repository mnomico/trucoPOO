package modelos;

import static modelos.Palo.*;

public enum Valor {
    CUATRO(4),
    CINCO(5),
    SEIS(6),
    SIETE_FALSO(7),
    DIEZ(10),
    ONCE(11),
    DOCE(12),
    ANCHO_FALSO(1),
    DOS(2),
    TRES(3),
    SIETE_DE_ORO(7, ORO),
    SIETE_DE_ESPADA(7, ESPADA),
    ANCHO_DE_BASTO(1, BASTO),
    ANCHO_DE_ESPADA(1, ESPADA),
    ;

    private final int numero;
    private final Palo palo;

    Valor(int numero){
        this.numero = numero;
        this.palo = null;
    }

    Valor(int numero, Palo palo) {
        this.numero = numero;
        this.palo = palo;
    }

    public static Valor valueOf(int numero){
        for (Valor valor : Valor.values()){
            if (valor.numero == numero){
                return valor;
            }
        }
        return null;
    }

}
