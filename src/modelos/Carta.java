package modelos;

public class Carta {

    private final int numero;
    private final Palo palo;
    private Valor valor;

    public Carta(int numero, Palo palo){
        this.numero = numero;
        this.palo = palo;
        switch (numero) {

            case 2,3,4,5,6,10,11,12 -> valor = Valor.valueOf(numero);

            case 1 -> {
                switch (palo) {
                    case ESPADA -> valor = Valor.ANCHO_DE_ESPADA;
                    case BASTO -> valor = Valor.ANCHO_DE_BASTO;
                    case COPA,ORO -> valor = Valor.ANCHO_FALSO;
                }
            }

            case 7 -> {
                switch (palo) {
                    case ESPADA -> valor = Valor.SIETE_DE_ESPADA;
                    case ORO -> valor = Valor.SIETE_DE_ORO;
                    case BASTO,COPA -> valor = Valor.SIETE_FALSO;
                }
            }

            default -> valor = null;
        }
    }

    public int getNumero(){
        return numero;
    }

    public Palo getPalo(){
        return palo;
    }

    public Valor getValor(){
        return valor;
    }

    @Override
    public String toString() {
        return numero + " de " + palo.toString();
    }
}
