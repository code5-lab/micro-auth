FROM vertx/vertx3

WORKDIR /usr/verticles

COPY ./target .
COPY env_docker.json .
COPY keystore.jks .
COPY ./templates ./templates

ENTRYPOINT ["sh", "-c"]

#ENTRYPOINT ["tail", "-f", "/dev/null"] #debug

CMD ["export CLASSPATH=`find /usr/verticles -printf '%p:' | sed 's/:$//'`; exec vertx run pt.code5.micro.auth.AuthVerticle -cluster"]