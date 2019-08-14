import * as Actions from '../actions/chat.actions';

export const initialState = {
    title: 'Simple Chat',
    apiKey: null,
    me: {},
    open: false,
    bar: {
        directs: [],
        groups: [],
        users: {}
    },
    conversation: {
        open: false,
        id: null,
        messages: []
    }
};

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case Actions.SET_APIKEY: {
            return {
                ...state,
                ...action.payload
            }
        }
        case Actions.SHOW_CHATBAR: {
            return {
                ...state,
                ...action.payload
            }
        }
        case Actions.UPDATE_ME: {
            return {
                ...state,
                ...action.payload
            }
        }
        case Actions.UPDATE_BAR: {
            return {
                ...state,
                ...action.payload
            }
        }
        case Actions.OPEN_CHAT: {
            return {
                ...state,
                ...action.payload
            }
        }
        case Actions.NEW_MESSAGE: {
            return {
                ...state,
                conversation: {
                    ...state.conversation,
                    messages: [...state.conversation.messages, action.payload.message]
                }
            }
        }
        default: {
            return state;
        }
    }
};

export default reducer;