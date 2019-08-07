import * as Actions from '../actions/chat.actions';

export const initialState = {
    user: {},
    open: false,
    title: 'Simple Chat'
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
        default: {
            return state;
        }
    }
};

export default reducer;