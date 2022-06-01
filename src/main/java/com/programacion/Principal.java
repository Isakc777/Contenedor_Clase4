package com.programacion;


import com.programacion.contenedor.ContenedorAvanzada;
import com.programacion.contenedor.ContenedorFactory;
import com.programacion.servicios.CuentaBancaria;
import com.programacion.servicios.ManejadorPersistencia;
import com.programacion.servicios.TransaccionBancaria;

public class Principal {
    public static void main(String[] args) {

        ContenedorAvanzada container = ContenedorFactory.newInstance();

         container.inicializar();

         //TransaccionBancaria tb = (TransaccionBancaria)container.buscar("transaccionBancaria");//como retornamos Object hay q hacer un casting explicito
        TransaccionBancaria tb = container.buscar("transaccionBancaria", TransaccionBancaria.class);//quitamos el casting explicito

        tb.realizarTransferencia("001", "002", 100.0f); // el resultado es la llamada a nuestro componente

        //si quiero usar el otro compoennte es decir manejador persistecnia
        ManejadorPersistencia mp = container.buscar("manejadorPersistencia", ManejadorPersistencia.class);
        //CuentaBancaria cuenta3 = mp.buscarCuenta("003");
        //System.out.println(cuenta3);//resultado: com.programacion.servicios.CuentaBancaria@7905a0b8

        System.out.println(tb);
        System.out.println(mp);


         container.destruir();

    }
}
