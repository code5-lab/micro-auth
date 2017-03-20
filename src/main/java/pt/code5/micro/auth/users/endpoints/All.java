package pt.code5.micro.auth.users.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.ext.web.RoutingContext;
import pt.code5.micro.auth.users.dao.UserDAO;
import pt.code5.micro.auth.users.entities.User;
import pt.code5.micro.auth.users.views.Views;

import java.util.List;

/**
 * Created by eduardo on 17/03/2017.
 */
public class All {

    public All(RoutingContext routingContext) {
        System.out.println("all");
        List<User> users = UserDAO.getInstance().find().asList();

        try {
            routingContext.response().putHeader("Content-type", "application/json").end(new ObjectMapper().writerWithView(Views.Public.class).writeValueAsString(users));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            routingContext.response().setStatusCode(500).end();
        }
    }
}
