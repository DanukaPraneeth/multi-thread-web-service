package com.danuka;

import javafx.util.Pair;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.ws.rs.container.AsyncResponse;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class LoadConfigurationListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext context = servletContextEvent.getServletContext();

        BlockingQueue<Integer> numberQueue = new ArrayBlockingQueue<>(20);
        context.setAttribute("number-queue", numberQueue);

        BlockingQueue<Pair<Integer, AsyncResponse>> numberAndResponseQueue = new ArrayBlockingQueue<>(20);
        context.setAttribute("number-and-response-queue", numberAndResponseQueue);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ServletContext context = servletContextEvent.getServletContext();
        context.removeAttribute("number-queue");
        context.removeAttribute("number-and-response-queue");
    }
}
