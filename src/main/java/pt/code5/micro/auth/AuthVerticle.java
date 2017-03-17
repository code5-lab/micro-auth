package pt.code5.micro.auth;

import io.vertx.core.AbstractVerticle;
import pt.code5.micro.utils.Config;
import pt.code5.micro.utils.database.MorphiaManager;

/**
 * Created by eduardo on 17/03/2017.
 */
public class AuthVerticle extends AbstractVerticle {

    private Config config;

    @Override
    public void start() throws Exception {
        super.start();
        this.config = Config.getInstance();
        this.config.boot(vertx, this::boot);
    }

    private void boot(Boolean b) {
        this.config.getConfig("auth_mongo", config -> {
            System.out.println(config);
            MorphiaManager.getInstance().boot(this.getClass().getPackage(), config.getJsonObject("result"));
        }, System.out::println);
    }
}
