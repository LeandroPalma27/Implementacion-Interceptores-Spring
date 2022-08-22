package com.interceptores.app1.interceptors;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component("tiempoTranscurridoInterceptor")
public class TiempoTranscurridoInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(TiempoTranscurridoInterceptor.class);

    @Override
    // El parametro handler es un objeto de la clase controladora, donde almacenamos la informacion
    // del metodo (parametros, caracteristicas, etc).
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        // Evaluamos si el metodo es de tipo POST, para asi poder "saltearlo" y que siga con los demas
        // metodos:
        if (request.getMethod().equalsIgnoreCase("post")) {
            return true;
        }

        // Si se retorna true, el programa continua con la ejecucion del metodo handler y los posibles
        // demas interceptores.
        // Si retorna false (por algun error) se detiene el metodo handler y el controlador, con la posi
        // bilidad de incluir un redirect.

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            logger.info("El metodo handler: " + handlerMethod.getMethod().getName());
        }
        
        logger.info("TiempoTranscurridoInterceptor: Metodo preHandle() entrando...");
        /* logger.info("Interceptando: " + handler); */
        long tiempo = System.currentTimeMillis();
        request.setAttribute("tiempoInicio", tiempo);
        Random random = new Random();
        Integer delay = random.nextInt(4000);
        Thread.sleep(delay);

        return true;

        // Si retorna false (por algun login no autenticado por ejemplo):

        /*
        response.sendRedirect(request.getContextPath().concat("/login"));
        return false; 
        */
    }

    @Override
    // El model manejado esta disponible en cualquier vista.
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {

         // 1 - DE IGUAL MANERA, SALTEAMOS SI ES DE TIPO POST:       
        if (request.getMethod().equalsIgnoreCase("post")) {
            return;
        }        

        long tiempoFinal = System.currentTimeMillis();
        // 2 - Porque esto generaria un NPE:
        long tiempoInicio = (long) request.getAttribute("tiempoInicio");
        Long tiempoTranscurrido = tiempoFinal - tiempoInicio;

        // Es necesario esto, porque estamos aplicando la intercepcion a todos los archivos del proyecto
        // y algunos arrojan null como archivos CSS o JS.
        // Solo se agregara si los metodos handler son de tipo HandlerMethod.
        if (handler instanceof HandlerMethod/* modelAndView != null */) {
            modelAndView.addObject("tiempoTranscurrido", tiempoTranscurrido);
        }

        logger.info("Tiempo Transcurrido: " + tiempoTranscurrido + "ms.");
        logger.info("TiempoTranscurridoInterceptor: Metodo postHandle() saliendo...");
        
    }

    
}
