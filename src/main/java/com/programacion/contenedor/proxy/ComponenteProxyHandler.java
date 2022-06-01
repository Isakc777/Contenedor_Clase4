package com.programacion.contenedor.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ComponenteProxyHandler implements InvocationHandler { //InvocationHandler no permite definir proxys en java

    private Object target;

    public ComponenteProxyHandler(Object target){ //contructor le pasamos a quine vamos a delegar
    this.target = target;
    }

    public Object getTarget(){
        return  this.target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        long inicio = System.nanoTime();//vamos a medir el tiempo inicial

        Object ret = method.invoke(target, args); //delegamos al componente original

        long tiempo = System.nanoTime()-inicio;

        System.out.printf("********** método '%s', tiempo=%d\n", method.getName(), tiempo);
        return ret;
    }
}
