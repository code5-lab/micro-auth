package pt.code5.micro.auth;

import io.vertx.core.AbstractVerticle;
import pt.code5.micro.auth.routers.RoutesManager;
import pt.code5.micro.utils.Config;
import pt.code5.micro.utils.database.MorphiaManager;
import pt.code5.micro.utils.vertx.VertxManager;

/**
 * Created by eduardo on 17/03/2017.
 */
public class AuthVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        super.start();
        VertxManager.getInstance().boot(vertx);
        Config.getInstance().boot(this::boot);
    }

    private void boot(Boolean b) {
        MorphiaManager.getInstance().boot(this.getClass().getPackage(), "auth_mongo", event -> {
            System.out.println("Morphia Booted");
        });
        RoutesManager.getInstance().boot(event -> {
            System.out.println("Routes Booted");
        });
    }
}
