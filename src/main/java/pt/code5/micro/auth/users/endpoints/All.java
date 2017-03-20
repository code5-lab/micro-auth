package pt.code5.micro.auth.users.endpoints;

import io.vertx.ext.web.RoutingContext;

/**
 * Created by eduardo on 17/03/2017.
 */
public class All {

    public All(RoutingContext routingContext) {
        System.out.println("all");
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("bye");
    }
}
