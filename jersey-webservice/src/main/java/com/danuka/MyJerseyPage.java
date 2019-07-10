package com.danuka;

import javafx.util.Pair;

import javax.servlet.ServletContext;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Path("/")
public class MyJerseyPage {

    @Context
    private ServletContext context;

    @POST
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    public void getSum(String number, @Suspended AsyncResponse asyncResponse) {
        try {
            BlockingQueue<Pair<Integer, AsyncResponse>> numberAndResponseQueue = (BlockingQueue<Pair<Integer, AsyncResponse>>) context.getAttribute("number-and-response-queue");
            if ("end".equals(number)) {
                List<Pair<Integer, AsyncResponse>> numbersAndResponses = new ArrayList<>();
                numberAndResponseQueue.drainTo(numbersAndResponses);
                int sum = 0;
                for (Pair<Integer, AsyncResponse> value: numbersAndResponses) {
                    sum += value.getKey();
                }
                for (Pair<Integer, AsyncResponse> value: numbersAndResponses) {
                    value.getValue().resume(Response.ok(String.valueOf(sum)).build());
                }
                asyncResponse.resume(Response.ok(String.valueOf(sum)).build());
            } else {
                numberAndResponseQueue.put(new Pair<Integer, AsyncResponse>(Integer.valueOf(number), asyncResponse));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            asyncResponse.resume(Response.status(500).entity("Interrupted Exception").build());
        }
    }
}
