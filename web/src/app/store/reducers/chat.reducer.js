import * as Actions from '../actions/chat.actions';

const initialState = {
    user: {}
};

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case Actions.SET_APIKEY: {
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