const express = require('express');
const app = express();
const port = (process.env.PORT || 3000);
const server = require('http').createServer(app);
const io = require('socket.io').listen(server);
const logger = require('./modules/logger');
const config = require('./configs/config');
const redis = require("redis").createClient(config.redis);
const rabbitmq = require('amqp').createConnection(config.rabbitmq);
const api = require('./modules/api').setContext({io, redis, config, rabbitmq});
const events = require('./enums/events');

rabbitmq.on('error', e => logger.error("Error from amqp: ", e));

io.sockets.on('connection', socket => {
	socket.on(events.MESSAGE_SEND, 
		(data, callback) => api.sendMessage({data, callback, socket}));
	socket.on(events.USER_ONLINE, 
		(data, callback) => api.setUserOnline({data, callback, socket}));
	socket.on(events.DISCONNECT, 
		(data, callback) => api.disconnectUser({data, callback, socket}));
});

server.on('ready', () => {
	server.listen(port, () => console.log(`Server listening on port ${port}.`));
});

rabbitmq.on('ready', () => {
	api.createQueue(rabbitmq);
	server.emit('ready');
});