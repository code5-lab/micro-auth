FROM vertx/vertx3

COPY ./target /usr/verticles

ENTRYPOINT ["sh", "-c"]
#ENTRYPOINT ["tail", "-f", "/dev/null"] #debug
CMD ["export CLASSPATH=`find /usr/verticles -printf '%p:' | sed 's/:$//'`; exec vertx run pt.code5.micro.auth.AuthVerticle"]