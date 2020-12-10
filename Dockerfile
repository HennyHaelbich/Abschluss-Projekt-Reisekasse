FROM openjdk:15-oracle
ENV ENVIRONMENT=prod

MAINTAINER Henny Haelbich <henny.haelbich@gmail.com>

ADD backend/target/reisekasse.jar app.jar

CMD ["sh" , "-c", "java -jar -Dserver.port=$PORT -Djwt.secretkey=$JWT_SECRETKEY -Dspring.data.mongodb.uri=$MONGO_DB_URI app.jar"]
