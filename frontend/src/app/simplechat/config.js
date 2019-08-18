export default {
    host: process.env.API_URL || 'http://localhost:8080',
    endpoint: {
        socket: process.env.SOCKET_URL || 'http://localhost:3000'
    },
    lang: {
        dialog: 'Pick a contact to begin a conversation.',
        type: 'Type yout message here',
    }
};