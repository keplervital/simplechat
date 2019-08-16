const winston = require('winston');
const consoleTransport = new winston.transports.Console();

const myWinstonOptions = {
    transports: [consoleTransport]
}

module.exports = new winston.createLogger(myWinstonOptions);