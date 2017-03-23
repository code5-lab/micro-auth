package pt.code5.micro.auth.users.endpoints;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.FreeMarkerTemplateEngine;
import pt.code5.micro.auth.requests.Endpoint;
import pt.code5.micro.auth.users.dao.UserDAO;
import pt.code5.micro.auth.users.entities.User;
import pt.code5.micro.utils.Config;
import pt.code5.micro.utils.vertx.VertxManager;

import java.time.LocalTime;

/**
 * Created by eduardo on 21/03/2017.
 */
public class Register extends Endpoint {

    private EventBus eb = VertxManager.getInstance().getVertx().eventBus();

    public Register(RoutingContext ctx) {
        super(ctx);

        try {
            User newUser = Json.decodeValue(ctx.getBodyAsString(), User.class);

            newUser.setActive(false);
            newUser.setActivationRequestTime(LocalTime.now());

            UserDAO.getInstance().save(newUser);

            Future<JsonObject> httpConfig = Future.future();
            Config.getInstance().getConfig("http", httpConfig.completer());
            Future<JsonObject> applicationConfig = Future.future();
            Config.getInstance().getConfig("application", applicationConfig.completer());

            CompositeFuture.all(httpConfig, applicationConfig).setHandler(result -> {
                if (result.failed()) {
                    System.err.println(result.cause());
                } else {
                    ctx.put("callback_url", ((JsonObject) result.result().list().get(0)).getJsonObject("result").getString("url"));
                    ctx.put("application_config", ((JsonObject) result.result().list().get(0)).getJsonObject("result"));
                    ctx.put("user_token", newUser.getActivationToken());

                    Future<Buffer> htmlFuture = Future.future();
                    FreeMarkerTemplateEngine.create().render(ctx, "templates/activate_html", htmlFuture.completer());
                    Future<Buffer> plainFuture = Future.future();
                    FreeMarkerTemplateEngine.create().render(ctx, "templates/activate_plain", plainFuture.completer());

                    CompositeFuture.all(htmlFuture, plainFuture).setHandler(event -> {
                        if (event.succeeded()) {
                            System.out.println(event.result());

                            JsonObject email = new JsonObject();
                            email.put("content", new JsonObject()
                                    .put("html", event.result().list().get(0).toString())
                                    .put("plain", event.result().list().get(1).toString())
                            );
                            email.put("to", newUser.getEmail());


                            eb.send("mail", email);
                        } else {
                            System.out.println(event.cause());
                        }
                        ctx.response().end();
                    });
                }

            });

        } catch (DecodeException e) {
            e.printStackTrace();
            ctx.response().setStatusCode(400).end();
            return;
        }
    }
}
