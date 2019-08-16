const axios = require('axios');
const autoBind = require('auto-bind');
const events = require('../enums/events');
const logger = require('./logger');

class Api {

    constructor() {
        autoBind(this);
    };

    setContext({io, redis, config}) {
        this.config = config;
        this.io = io;
        this.redis = redis;
        return this;
    }

    createQueue(rabbitmq) {
        if(this._queue)
            return;
        const _this = this;
        _this.rabbitmq = rabbitmq;
        _this._queue = rabbitmq.queue(this.config.workQueue, queue => {
            queue.subscribe(message => {
                const data = JSON.parse(message.data);
                switch(data.type) {
                    case events.USER_NOTIFY_ONLINE:
                        _this.io.sockets.emit(events.USER_NOTIFY_ONLINE, data.payload);
                        break;
                    case events.MESSAGE_NEW:
                        _this.io.sockets.emit(data.payload.chatId, {
                            type: events.MESSAGE_NEW,
                            data: data.payload.message
                        });
                        break;
                    default:
                        logger.warn('Unknown event type published.');
                        break;
                }
            });
        });
    }

    objectToBuffer(o) {
        return Buffer.from(JSON.stringify(o));
    }
 
    sendMessage({data, callback}) {
        const _this = this;
        const headers = { [this.config.headers.auth]: data.auth };
        const request = axios.post(`${this.config.endpoint.host}/message`, {
            chatID: data.chatId,
            body: data.message
        }, { headers });
        request.then((response) => {
            _this.rabbitmq.publish(_this.config.workQueue, _this.objectToBuffer({
                type: events.MESSAGE_NEW,
                payload: {
                    chatId: data.chatId,
                    message: response.data.message
                }
            }));
        });
        callback();
    }

    setUserOnline({data, callback, socket}) {
        socket.userId = data.userId;
        this.redis.sadd(['online', socket.userId], () => this.notifyOnlineUsers());
        callback();
    }

    notifyOnlineUsers() {
        const _this = this;
        this.redis.smembers('online', function(_, reply) {
            _this.rabbitmq.publish(_this.config.workQueue, _this.objectToBuffer({
                type: events.USER_NOTIFY_ONLINE,
                payload: reply
            }));
        });
    }

    disconnectUser({socket}) {
        if(!socket.userId || socket.userId == null)
			return;
        this.redis.srem(['online', socket.userId], () => this.notifyOnlineUsers());
    }

}

module.exports = new Api();