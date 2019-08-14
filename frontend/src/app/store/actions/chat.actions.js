import Config from '../../simplechat/config';
import axios from 'axios';

const ACTION_ID = 'CHAT';

export const SET_APIKEY   = `[${ACTION_ID}] SET API KEY`;
export const GET_CHATBAR  = `[${ACTION_ID}] GET CHAT BAR`;
export const SHOW_CHATBAR = `[${ACTION_ID}] SHOW CHAT BAR`;
export const UPDATE_ME    = `[${ACTION_ID}] UPDATE CHAT ME`;
export const UPDATE_BAR   = `[${ACTION_ID}] UPDATE CHAT BAR`;
export const OPEN_CHAT    = `[${ACTION_ID}] OPEN CHAT`;
export const SEND_MESSAGE = `[${ACTION_ID}] SEND MESSAGE`;
export const NEW_MESSAGE  = `[${ACTION_ID}] NEW MESSAGE`;
export const ONLINE_USERS = `[${ACTION_ID}] ONLINE USERS`;

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
            open: show,
            conversation: {
                open: false
            }
        }
    }
}

export function updateMe(dispatch) {
    const request = axios.get(`${Config.host}/user/me`);
    return () => {
        request.then((response) => {
            dispatch({
                type: UPDATE_ME,
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

export function openChat(dispatch, chatId, name, image) {
    const request = axios.get(`${Config.host}/chat/${chatId}/messages`);
    return () => {
        request.then((response) => {
            dispatch({
                type: OPEN_CHAT,
                payload: {
                    open: true,
                    conversation: {
                        open: true,
                        id: chatId,
                        name: name,
                        avatar: image,
                        messages: response.data
                    }
                }
            });
        });
    }
}

export function newMessageToOpenChat(message) {
    return {
        type: NEW_MESSAGE,
        payload: {
            message: message
        }
    }
}

export function onlineUsers(users) {
    return {
        type: ONLINE_USERS,
        payload: {
            users
        }
    }
}