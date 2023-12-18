package modelos;

import java.io.Serializable;

public enum Evento implements Serializable {
    MOSTRAR_MENU,
    JUGAR_CARTA,
    RESPONDER_APUESTA,
    DIJO_QUIERO,
    DIJO_NO_QUIERO,
    RESULTADO_ENVIDO,
    IRSE_AL_MAZO,
    FIN_RONDA,
    FIN_MANO,
    CAMBIO_TURNO,
    FIN_PARTIDA,
}
