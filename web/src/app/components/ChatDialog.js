import React, { Component } from "react";
import { StateContext } from '../simplechat/state';
import { withStyles } from '@material-ui/core';

const useStyles = theme => ({
    
});

class ChatDialog extends Component {

    static contextType = StateContext;

    render() {
        return (
            <div>right</div>
        );
    }

}

export default withStyles(useStyles, {withTheme: true})(ChatDialog);