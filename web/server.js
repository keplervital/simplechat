const express = require('express');
const app = express();
const port = (process.env.PORT || 3000);
const server = require('http').createServer(app);
const io = require('socket.io').listen(server);
const axios = require('axios');
const {config} = require('./config/config');

const api = {
	message: {
		send: {
			id: 'send-message',
			handler(data, callback) {
				const headers = { [config.headers.auth]: data.auth };
				const request = axios.post(`${config.endpoint.host}/message`, {
					chatID: data.chatId,
					body: data.message
				}, { headers });
				request.then((response) => {
					io.sockets.emit(data.chatId, {
						type: 'new-message',
						data: response.data.message
					});
				});
				callback();
			}
		}
	}
}

io.sockets.on('connection', socket => {
	socket.on(api.message.send.id, api.message.send.handler);
});

server.listen(port, () => console.log(`Simplechat server listening on port ${port}.`));