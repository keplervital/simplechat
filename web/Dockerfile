FROM node:alpine

LABEL mantainer="Kepler Vital <kepler.vital@gmail.com>"

## LISTENING PORTS
EXPOSE 3000

## DEPENDENCIES
RUN npm install -D babel-loader @babel/core @babel/preset-env webpack

## SETUP ENVIORNMENT
WORKDIR /app
COPY ./package.json ./
RUN npm install
COPY ./ .

CMD ["npm", "run", "start"]