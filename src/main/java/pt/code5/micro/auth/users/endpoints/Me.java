package pt.code5.micro.auth.users.endpoints;

import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import org.bson.types.ObjectId;
import pt.code5.micro.auth.requests.Endpoint;
import pt.code5.micro.auth.users.dao.UserDAO;
import pt.code5.micro.auth.users.entities.User;

/**
 * Created by eduardo on 21/03/2017.
 */
public class Me extends Endpoint {
    public Me(RoutingContext ctx) {
        super(ctx);

        ObjectId loggedUserId = new ObjectId(ctx.user().principal().getString("sub"));
        User me = UserDAO.getInstance().get(loggedUserId);
        if (me == null) {
            ctx.response().setStatusCode(404).end();
            return;
        }
        ctx.response().putHeader("content-type", "application/json").setStatusCode(200).end(Json.encode(me));
    }
}
