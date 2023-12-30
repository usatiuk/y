FROM node:20-bullseye as client

WORKDIR /usr/src/app/client
COPY ./client/package*.json ./
RUN npm i
COPY ./client/. .
RUN npm run build

FROM azul/prime:17 as server

WORKDIR /usr/src/app/server
COPY ./server/. .
RUN ./gradlew clean build && bash -c "rm build/libs/*-plain.jar && mv build/libs/*.jar server.jar"


FROM azul/prime:17

WORKDIR /usr/src/app
COPY --from=server /usr/src/app/server/server.jar .
RUN mkdir -p client/dist
COPY --from=client /usr/src/app/client/dist ./client/dist

ENV spring_profiles_active=prod

COPY ./dockerentry.sh .

RUN ["chmod", "+x", "./dockerentry.sh"]

CMD [ "./dockerentry.sh" ]