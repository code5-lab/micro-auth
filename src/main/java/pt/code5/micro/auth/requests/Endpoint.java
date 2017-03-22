package pt.code5.micro.auth.requests;

import io.vertx.ext.web.RoutingContext;

/**
 * Created by eduardo on 20/03/2017.
 */
public abstract class Endpoint {

    public Endpoint(RoutingContext ctx) {
        //http://stackoverflow.com/questions/3561381/custom-http-headers-naming-conventions
        String applicationId = ctx.request().getHeader("Application-Id");
    }
}
