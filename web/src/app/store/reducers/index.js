import chatReducer from './chat.reducer';

const mainReducer = ({chat}, action) => {
    return {
        chat: chatReducer(chat, action)
    };
};

export default mainReducer;