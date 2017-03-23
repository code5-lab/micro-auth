package pt.code5.micro.auth.users.endpoints;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTOptions;
import io.vertx.ext.web.RoutingContext;
import org.mindrot.jbcrypt.BCrypt;
import pt.code5.micro.auth.requests.Endpoint;
import pt.code5.micro.auth.requests.RouterManager;
import pt.code5.micro.auth.users.dao.UserDAO;
import pt.code5.micro.auth.users.entities.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by eduardo on 17/03/2017.
 */
public class Auth extends Endpoint {

    private UserDAO userDAO = UserDAO.getInstance();

    public Auth(RoutingContext routingContext) {
        super(routingContext);
        System.out.println("auth");
        JsonObject json = routingContext.getBodyAsJson();

        Map<String, String> values = new HashMap<>();
        values.put("username", json.getString("username"));
        values.put("email", json.getString("email"));

        User user = userDAO.getUserFromValues(values);

        if (user == null || !BCrypt.checkpw(routingContext.getBodyAsJson().getString("password"), user.getPassword())) {
            routingContext.response().setStatusCode(401).end();
            return;
        }


        JsonObject token = new JsonObject();
        token.put("sub", user.getId().toString());
        token.put("name", user.getName());

        JsonObject rtn = (new JsonObject()).put("token", RouterManager.getInstance().getAuthProvider().generateToken(token, new JWTOptions().setAlgorithm("RS256")));

        routingContext.response().putHeader("Content-type", "application/json").end(rtn.encode());
    }
}
