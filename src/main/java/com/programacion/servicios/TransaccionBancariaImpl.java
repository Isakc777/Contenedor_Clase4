package com.programacion.servicios;

import com.programacion.contenedor.anotaciones.MiComponente;
import com.programacion.contenedor.anotaciones.MiDependencia;

@MiComponente(nombre= "transaccionBancaria")
public class TransaccionBancariaImpl implements TransaccionBancaria {

   @MiDependencia(nombre = "manejadorPersistencia")//DI forma basica
   private ManejadorPersistencia mp;//DI forma basica

   public void realizarTransferencia (String cuenta1, String cuenta2, float monto){
       // System.out.printf("TransaccionBancariaImpl::realizarTransferencia(%s,%s,%f)\n", cuenta1, cuenta2, monto);
        //%s es para las cadenas de caracteres.

       CuentaBancaria c1 = mp.buscarCuenta(cuenta1);//lookup con el buscar
       CuentaBancaria c2 = mp.buscarCuenta(cuenta2);

       c1.deposito(monto);
       c2.deposito(monto);
    }

}
