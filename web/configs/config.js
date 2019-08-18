module.exports = {
    headers: {
        auth: 'API-Key'
    },
    endpoint: {
        host: process.env.API_URL || 'http://localhost:8080'
    },
    redis: {
        host: process.env.REDIS_HOST || 'localhost',
        port: process.env.REDIS_PORT || '6379'
    },
    workQueue: 'simplechat-broker',
    rabbitmq: {
        host: process.env.RABBITMQ_HOST || 'localhost', 
        port: process.env.RABBITMQ_PORT || 5672, 
        login: process.env.RABBITMQ_LOGIN || 'guest', 
        password: process.env.RABBITMQ_PWD || 'guest', 
        connectionTimeout: 10000, 
        authMechanism: 'AMQPLAIN', 
        vhost: '/', 
        noDelay: true, 
        ssl: { 
            enabled : false
        }
    }
};