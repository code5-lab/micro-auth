package pt.code5.micro.auth.requests;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;
import pt.code5.micro.auth.users.endpoints.*;
import pt.code5.micro.utils.Config;
import pt.code5.micro.utils.vertx.VertxManager;

public class RouterManager {

    private static RouterManager ourInstance = new RouterManager();
    private final Vertx vertx;
    private JWTAuth authProvider;
    private Router router;

    private RouterManager() {
        this.vertx = VertxManager.getInstance().getVertx();
    }

    public static RouterManager getInstance() {
        return ourInstance;
    }

    public JWTAuth getAuthProvider() {
        return authProvider;
    }

    public void boot(Handler<AsyncResult<Boolean>> onComplete) {
        this.router = Router.router(this.vertx);
        this.router.route().handler(BodyHandler.create());
        this.router.route().handler(CorsHandler.create("*")
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedMethod(HttpMethod.DELETE)
                .allowedHeader("Content-Type")
                .allowedHeader("Authorization"));

        Config.getInstance().getConfig("http", config -> {
            if(config.failed()){
                System.err.println("keyStore::" + config.cause());
                System.exit(-1);
            }
            JsonObject result = config.result().getJsonObject("result");


            JsonObject cfg = new JsonObject()
                    .put("permissionsClaimKey", "roles")
                    .put("keyStore", result.getJsonObject("keyStore"));

            this.authProvider = JWTAuth.create(this.vertx, cfg);

            this.addEndPoints();
            this.vertx.executeBlocking(future -> {

                HttpServer httpServer = this.vertx.createHttpServer();
                httpServer.requestHandler(router::accept);
                httpServer.listen(result.getInteger("port"));

                future.complete(true);
            }, onComplete);

        });

    }

    private void addEndPoints() {
        //this.router.routeWithRegex("/api/.*").handler(JWTAuthHandler.create(authProvider));
        this.router.post("/auth").consumes("application/json").produces("application/json").handler(Auth::new);
        this.router.get("/recover").consumes("application/json").handler(Recover::new);
        this.router.post("/api/register").consumes("application/json").produces("application/json").handler(Register::new);
        this.router.get("/api/me").produces("application/json").handler(Me::new);
        this.router.get("/api/users").produces("application/json").handler(All::new);

    }
}
