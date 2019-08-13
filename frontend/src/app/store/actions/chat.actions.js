import Config from '../../simplechat/config';
import axios from 'axios';

const ACTION_ID = 'CHAT';

export const SET_APIKEY   = `[${ACTION_ID}] SET API KEY`;
export const GET_CHATBAR  = `[${ACTION_ID}] GET CHAT BAR`;
export const SHOW_CHATBAR = `[${ACTION_ID}] SHOW CHAT BAR`;
export const UPDATE_USER  = `[${ACTION_ID}] UPDATE CHAT USER`;
export const UPDATE_BAR   = `[${ACTION_ID}] UPDATE CHAT BAR`;

export function setApiKey(apiKey) {
    axios.defaults.headers.common['API-Key'] = apiKey;
    return {
        type: SET_APIKEY,
        payload: {
            apiKey: apiKey
        }
    }
}

export function showChatBar(show) {
    return {
        type: SHOW_CHATBAR,
        payload: {
            open: show
        }
    }
}

export function updateUser(dispatch) {
    const request = axios.get(`${Config.host}/user/me`);
    return () => {
        request.then((response) => {
            dispatch({
                type: UPDATE_USER,
                payload: {
                    me: response.data
                }
            });
        });
    }
}

export function updateBar(dispatch) {
    const request = axios.get(`${Config.host}/chat/bar`);
    return () => {
        request.then((response) => {
            dispatch({
                type: UPDATE_BAR,
                payload: {
                    bar: response.data
                }
            });
        });
    }
}