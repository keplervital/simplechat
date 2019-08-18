FROM ubuntu:18.04
LABEL mantainer="Kepler Vital <kepler.vital@gmail.com>"

## LISTENING PORTS
EXPOSE 80
EXPOSE 443

## WEB SERVER
RUN apt-get update -y
RUN apt-get install -y software-properties-common htop git poppler-utils curl nano wget apache2 nodejs npm openjdk-8-jre-headless
RUN chown -R www-data:www-data /var/www/html
RUN echo "ServerName localhost" >> /etc/apache2/apache2.conf

RUN npm install -D babel-loader @babel/core @babel/preset-env webpack

## APP DEPENDENCIES DIR
RUN mkdir /app

## INIT NODE SERVER
RUN mkdir /app/web
WORKDIR /app/web

COPY ./web .
RUN rm -r node_modules

RUN npm install

## INIT JAVA SERVER
RUN mkdir /app/api
WORKDIR /app/api

COPY ./app/simplechat/target/simplechat-0.1.0.jar ./simplechat.jar

## INIT FRONTEND
RUN mkdir /app/frontend
WORKDIR /app/frontend

COPY ./frontend .
RUN rm -r node_modules
RUN npm install
RUN npm build

RUN cp -a /app/frontend/dist/. /var/www/html/

## RESTART WEBSERVER
RUN a2enmod rewrite
RUN service apache2 restart

COPY ./start.sh /

CMD ["/start.sh"]