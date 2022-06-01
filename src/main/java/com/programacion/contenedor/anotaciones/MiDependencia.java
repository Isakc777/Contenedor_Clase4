package com.programacion.contenedor.anotaciones;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)//ya no va ser a clases sino a variables de instancia
public @interface MiDependencia {
    String nombre();
}
