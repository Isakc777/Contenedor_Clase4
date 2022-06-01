package com.programacion.contenedor;

import com.programacion.contenedor.anotaciones.MiComponente;

public class ContenedorFactory {
    public static ContenedorAvanzada newInstance(){
        return new ContenedorAvanzadaImpl();
    }
}
