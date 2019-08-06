import React, { Component } from "react";
import { StateContext } from '../simplechat/state';
import { Button } from '@material-ui/core';
import uuid from 'uuid';
import * as Actions from '../store/actions';

class Chat extends Component {

    static contextType = StateContext;

    render() {
        
        const [{chat}, dispatch] = this.context;

        return (
            <div>
                <Button 
                    variant="contained" 
                    color="primary"
                    onClick={() => dispatch(Actions.setApiKey(uuid().replace(/-/g,"")))}
                >
                    GENERATE KEY
                </Button><br/><br/>
                <span>API KEY: {chat ? chat.user.apiKey : 'n/a'}</span>
            </div>
          );
    }
}

export default Chat;