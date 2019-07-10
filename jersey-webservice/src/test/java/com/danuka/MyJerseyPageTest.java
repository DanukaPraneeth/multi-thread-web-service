package com.danuka;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.grizzly2.servlet.GrizzlyWebContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.glassfish.jersey.test.spi.TestContainer;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.Test;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.concurrent.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class MyJerseyPageTest extends JerseyTest {

    private static final String PATH = "";


    @Override
    public Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return new ResourceConfig(MyJerseyPage.class);
    }


    /**
     *  Loading the ServletContext to test the API requests
     *
     **/
    @Override
    protected TestContainerFactory getTestContainerFactory() throws TestContainerException {
        return new TestContainerFactory() {
            @Override
            public TestContainer create(URI uri, DeploymentContext deploymentContext) {
                return new TestContainer() {
                    private HttpServer server;

                    @Override
                    public ClientConfig getClientConfig() {
                        return null;
                    }

                    @Override
                    public URI getBaseUri() {
                        return uri;
                    }

                    @Override
                    public void start() {
                        try {
                            this.server = GrizzlyWebContainerFactory.create(
                                    uri, Collections.singletonMap("jersey.config.server.provider.packages", "com.danuka.LoadConfigurationListener")
                            );
                        } catch (ProcessingException e) {
                            throw new TestContainerException(e);
                        } catch (IOException e) {
                            throw new TestContainerException(e);
                        }
                    }

                    @Override
                    public void stop() {
                        this.server.stop();
                    }
                };
            }

        };
    }



    /**
     * Test asynchronous POST.
     * <p/>
     * Send 3 async POST requests and wait to receive the responses. Check the response content
     * (this ensures async request execution).
     *
     * @throws Exception in case of a test error.
     */
    @Test
    public  void testGetSum_for_SimpleValidInputs() throws Exception {


        final Future<Response> rf1 = target(PATH).request().async().post(Entity.text("1"));
        final Future<Response> rf2 = target(PATH).request().async().post(Entity.text("2"));
        final Future<Response> rf3 = target(PATH).request().async().post(Entity.text("end"));

        final String r1 = rf1.get().readEntity(String.class);
        final String r2 = rf2.get().readEntity(String.class);
        final String r3 = rf3.get().readEntity(String.class);


        assertEquals("3", r1);
        assertEquals("3", r2);
        assertEquals("3", r3);

    }

    @Test
    public  void testGetSum_for_ComplexValidValidInputs() throws Exception {

        final Future<Response> rf4 = target(PATH).request().async().post(Entity.text("end"));
        final Future<Response> rf5 = target(PATH).request().async().post(Entity.text("3"));
        final Future<Response> rf6 = target(PATH).request().async().post(Entity.text("4"));
        final Future<Response> rf7 = target(PATH).request().async().post(Entity.text("end"));

        final String r4 = rf4.get().readEntity(String.class);
        final String r5 = rf5.get().readEntity(String.class);
        final String r6 = rf6.get().readEntity(String.class);
        final String r7 = rf6.get().readEntity(String.class);

        assertNotEquals("0", r4);
        assertEquals("7", r5);
        assertEquals("7", r6);
        assertEquals("7", r7);

    }


}