package com.programacion.contenedor;

public interface ContenedorAvanzada {
    void inicializar();
    void destruir();

   // Object buscar(String nombre);
    <T> T buscar(String nombre, Class<T> cls);

}
