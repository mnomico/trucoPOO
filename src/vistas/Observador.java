package vistas;

import modelos.Observado;

public interface Observador {
    void update(Observado o, Object arg);
}