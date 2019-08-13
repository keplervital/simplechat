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
        case Actions.UPDATE_USER: {
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
        default: {
            return state;
        }
    }
};

export default reducer;