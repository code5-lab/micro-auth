package pt.code5.micro.auth.users.endpoints;

import io.vertx.ext.web.RoutingContext;
import pt.code5.micro.auth.requests.Endpoint;
import pt.code5.micro.auth.users.dao.UserDAO;
import pt.code5.micro.auth.users.entities.User;
import pt.code5.micro.utils.Config;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by eduardo on 22/03/2017.
 */
public class Activate extends Endpoint {
    private UserDAO userDAO = UserDAO.getInstance();

    public Activate(RoutingContext ctx) {
        super(ctx);
        String token = ctx.request().getParam("token");
        try {
            UUID uuid = UUID.fromString(token);
            HashMap<String, String> values = new HashMap<>();
            values.put("activationToken", uuid.toString());
            User user = userDAO.getUserFromValues(values);
            if (user != null) {
                user.setActive(true);
                userDAO.save(user);
                Config.getInstance().getConfig("application", result -> {
                    if (result.failed()) {
                        System.out.println("activation_callback::NOT_FOUND");
                        ctx.response().setStatusCode(500).end();
                    }
                    ctx.response().setStatusCode(301).putHeader("location", result.result().getJsonObject("result").getString("activation_callback")).end();
                });
            } else {
                Config.getInstance().getConfig("application", result -> {
                    if (result.failed()) {
                        System.out.println("activation_callback::NOT_FOUND");
                        ctx.response().setStatusCode(500).end();
                    }

                    ctx.response().setStatusCode(301).putHeader("location", result.result().getJsonObject("result").getString("activation_callback") + "/not_found").end();
                });
            }
        } catch (IllegalArgumentException exception) {
            ctx.response().setStatusCode(400).end();
        }
    }
}
