package pt.code5.micro.auth;

import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.bson.Document;
import org.bson.types.ObjectId;
import pt.code5.micro.auth.users.dao.UserDAO;
import pt.code5.micro.auth.users.entities.User;
import pt.code5.micro.utils.Config;
import pt.code5.micro.utils.database.MorphiaManager;
import pt.code5.micro.utils.vertx.VertxManager;

/**
 * Created by eduardo on 20/03/2017.
 */
public class Seed {


    public static void main(String[] args) {
        VertxManager.getInstance().boot(Vertx.vertx());

        Config.getInstance().boot(event -> {

            MorphiaManager.getInstance().boot(Seed.class.getPackage(), "auth_mongo", event2 -> {
                UserDAO userDAO = UserDAO.getInstance();

                User u1 = new User();
                u1.setName("User 1");
                u1.setEmail("user@domail.tld");
                u1.setUsername("th3Us3r");


                u1.setProperties(new JsonObject().put("address", new JsonArray().add(1).add(1).add(1).add(1).add(1).getList()).put("sdad", new JsonObject().put("asdad", "dadasd")).mapTo(Document.class));

                userDAO.save(u1);

                String str = Json.encode(u1);

                System.out.println(str);

                User u2 = Json.decodeValue(str, User.class);
                u2.setId(new ObjectId());
                userDAO.save(u2);
                userDAO.get(u2.getId());
                System.exit(0);
            });
        });


    }
}
