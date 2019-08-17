module.exports = {
    headers: {
        auth: 'API-Key'
    },
    endpoint: {
        host: 'http://localhost:8080'
    },
    redis: {
        host: 'localhost',
        port: '6379'
    },
    workQueue: 'simplechat-broker',
    rabbitmq: {
        host: 'localhost', 
        port: 5672, 
        login: 'guest', 
        password: 'guest', 
        connectionTimeout: 10000, 
        authMechanism: 'AMQPLAIN', 
        vhost: '/', 
        noDelay: true, 
        ssl: { 
            enabled : false
        }
    }
};