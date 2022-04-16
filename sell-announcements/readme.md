# PROJ

start database:

    docker run --rm -it -e POSTGRES_PASSWORD=admin -e POSTGRES_USER=admin -e POSTGRES_DB=announcements -p 5432:5432 postgres:13

start gui:

    npm run start

start api:

    mvnd spring-boot:run
