FROM openjdk:8

WORKDIR app

COPY ./build/libs/jalgoarena-problems-*.jar /app/jalgoarena-problems.jar
COPY ./ProblemsStore /app/ProblemsStore

EXPOSE 5002

ENTRYPOINT java -jar ./jalgoarena-problems.jar