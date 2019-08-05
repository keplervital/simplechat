const ACTION_ID = 'CHAT';

export const SET_APIKEY  = `[${ACTION_ID}] SET API KEY`;
export const GET_CHATBAR = `[${ACTION_ID}] GET CHAT BAR`;

export function setApiKey(apiKey) {
    return {
        type: SET_APIKEY,
        payload: {
            user: {
                apiKey: apiKey
            }
        }
    }
}