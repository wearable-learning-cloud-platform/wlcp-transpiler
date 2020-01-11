FROM openjdk:8-jre-alpine
RUN apk add curl jq
COPY start.sh /start.sh
RUN chmod +x /start.sh
COPY /target/wlcp-transpiler-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8083
CMD ["sh", "/start.sh"]