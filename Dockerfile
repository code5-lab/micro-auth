FROM vertx/vertx3

COPY ./target /usr/verticles

#ENTRYPOINT ["sh", "-c"]
ENTRYPOINT ["tail", "-f", "/dev/null"]
#CMD ["exec vertx run pt.code5.micro.auth.AuthVerticle -cp /usr/verticles/"]