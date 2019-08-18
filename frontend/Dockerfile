FROM nginx:stable
LABEL mantainer="Kepler Vital <kepler.vital@gmail.com>"

## LISTENING PORTS
EXPOSE 80
EXPOSE 443

## SETUP ENVIRONMENT
RUN mkdir /app
WORKDIR /app

## DEPENDENCIES
RUN apt-get update -y
RUN apt-get install -y npm
RUN npm install -D babel-loader @babel/core @babel/preset-env webpack

## BUILD
COPY ./package.json ./
RUN npm install
COPY ./ .
RUN cp -a dist/. /usr/share/nginx/html

## SETUP DOCKER FOR DEMOS
RUN apt install -y apt-transport-https ca-certificates curl software-properties-common
RUN curl -fsSL https://download.docker.com/linux/ubuntu/gpg | apt-key add -
RUN add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu bionic stable"
RUN apt-get update -y
RUN apt-get install -y docker-ce