package com.programacion.contenedor.anotaciones;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) //nivel de retencion runtime
@Target(ElementType.TYPE) //necesitamos que se apliquen a clases es decir a tipos de datos
public @interface MiComponente {
   String nombre();
}
