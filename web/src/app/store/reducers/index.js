import chatReducer, {initialState as chatInitialState} from './chat.reducer';

export const initialStates = {
    chat: chatInitialState
};

const mainReducer = ({chat}, action) => {
    return {
        chat: chatReducer(chat, action)
    };
};

export default mainReducer;