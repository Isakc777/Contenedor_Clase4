package com.programacion.contenedor;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.programacion.contenedor.anotaciones.MiComponente;
import com.programacion.contenedor.anotaciones.MiDependencia;
import com.programacion.contenedor.proxy.ComponenteProxyHandler;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class ContenedorAvanzadaImpl implements ContenedorAvanzada {



    private String packageScan = "com.programacion"; //ponemos el nombre del paquete que queremos escanenar

    protected Map<String, Object> componentesRegistrados = new HashMap<>();

    private static Logger _logger = LoggerFactory.getLogger(ContenedorAvanzadaImpl.class);

    public void inicializar() {

        //System.out.println("Inicializar"); //remplazamos por _logger
        _logger.info("Inicializando");

        //1. BUSCAR TODAS LAS CLASES EN EL PROGRAMA
        try{
            ClassPath classPath = ClassPath.from( ContenedorAvanzadaImpl.class.getClassLoader());
            ImmutableSet<ClassPath.ClassInfo> clases = classPath.getTopLevelClassesRecursive(packageScan);

            clases.stream()
                    .forEach(s->{ //El método forEach se utiliza para iterar a través de cada elemento de la secuencia.
                       String className = s.getName();
                       Class<?> clase = null;

                        //System.out.println("*******"+ s.getName()); //escaneamos todo lo que esta en com.programacion

                        try{
                            clase = Class.forName(className); //obtenemos los metadatos de la clase
                            //estamos usando reflexion para obtener los metadatos en la variable clase

                            //2. IDENTIFICAR LAS QUE TIENEN UNA ANOTACION EN PARTICULAR
                           MiComponente ann =  clase.getAnnotation(MiComponente.class); //esto me retorna una instancia de mi anotacion

                            if(ann!=null){
                                //imprime la clase que tiene la anotacion
                                //la clase con la notacion ==> nombre que le estamos dando

                                ////System.out.printf("registrado %s==>%s\n", className, ann.nombre());
                                _logger.info("registrado {}==>{}\n", className, ann.nombre());


                                // 3. REGISTRARLOS COMO COMPONENTES
                                //cada vez que tenemos un componente que cumple con nuestros requerimientos vamos a tener una instancia
                                crearComponente (ann.nombre(), clase);

                            }

                        }catch (Exception e){
                            throw new RuntimeException(e.getMessage());
                        }

                    });

            // 4. PROCESAR LAS DEPENDENCIAS
            procesarDependencias();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    void crearComponente(String nombre, Class<?> cls) throws  Exception {

        //Object obj = clase.getDeclaredConstructors()[0].newInstance(); //antes de crear el metodo crearCompoente
       // componentesRegistrados.put(ann.nombre(), obj);//creamos el componente //antes de crear el metodo crearCompoente

       // componentesRegistrados.put(nombre, obj);
        Object target = cls.getDeclaredConstructors()[0].newInstance();

        ComponenteProxyHandler handler = new ComponenteProxyHandler(target);

        Object proxy = Proxy.newProxyInstance(ContenedorAvanzadaImpl.class.getClassLoader(), cls.getInterfaces(), handler);

        componentesRegistrados.put(nombre, proxy);

    }

    public void destruir() {
        //System.out.println("Destruir");
        _logger.info("Destruyendo");
    }

    //public Object buscar(String nombre) {
     //   return compoenentesRegistrados.get(nombre);
    //}
    public <T> T buscar(String nombre, Class<T> cls){
           Object obj = componentesRegistrados.get(nombre);
           if(obj==null){
               throw  new RuntimeException( String.format("Componente %s no encontrado", nombre));
           }
           return cls.cast(obj);
        }

        void procesarDependencias(){

            System.out.println("Procesando dependecias");

                //4.1. Listar los componentes registrados, listar los componenete a los que debemos pasar las dependecias

            for (String key: componentesRegistrados.keySet()) {
                System.out.printf("Procesando dependecias para %s\n", key); //usamos key para movernos dentro del HashMap

                //Object obj = componentesRegistrados.get(key);//luego seria el proxy ya no obj
                Object proxy = componentesRegistrados.get(key);
                ComponenteProxyHandler handler = (ComponenteProxyHandler)Proxy.getInvocationHandler(proxy);

                Object obj = handler.getTarget();

                //4.2. Listar las variables de instacia

                Field[] fields = obj.getClass().getDeclaredFields();

                Stream.of(fields)
                        .forEach(s->{
                            System.out.println("******************** variable de instancia: " + s.getName());//encuentra una variable de instancia llamada mp
                               MiDependencia ann = s.getAnnotation(MiDependencia.class);//verifiicar si la variable de instancia tiene la notaicion
                                if(ann!=null){
                                    System.out.printf("%s,%s==>%s\n"
                                            ,obj.getClass().getName(), s.getName(), ann.nombre());

                                    //obj.getClass().getName() nombre de la clase
                                    //s.getName() nombre de la variable de instancia
                                    //ann.nombre()  nombre de la dependecia que vamos a buscar (que se llama manejador persistencia)
                                    //obteniendo com.programacion.servicios.TransaccionBancariaImpl,mp==>manejadorPersistencia

                                    Object dependenciaObj = componentesRegistrados.get(ann.nombre());

                                    try{
                                        s.setAccessible(true);
                                        s.set(obj, dependenciaObj);
                                    }catch (Exception ex){
                                        ex.printStackTrace();
                                    }
                                }
                        });
            }
        }
}
