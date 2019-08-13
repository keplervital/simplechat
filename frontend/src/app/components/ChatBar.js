import React, { Component } from "react";
import { StateContext } from '../simplechat/state';
import { withStyles, Grid, Tooltip } from '@material-ui/core';
import styles from '../styles/chat.module.css';
import classNames from 'classnames';

const useStyles = theme => ({
    
});

class ChatBar extends Component {

    static contextType = StateContext;

    render() {

        const [{chat}, _] = this.context;
        const users = chat.bar.users;

        return (
            <Grid container>
                {chat.bar.directs.map(conversation => {
                    const userInfo = users[conversation.participants.find((uid) => uid != chat.me.id)];
                    return (
                        <Tooltip key={conversation.id} title={userInfo.name} placement="left">
                            <Grid item xs={12} className={classNames(styles.user)}>
                                <div className={classNames(styles.avatar)}></div>
                                <div className={classNames(styles.userStatus)}></div>
                            </Grid>
                        </Tooltip>
                    );
                })}
            </Grid>
        );
    }

}

export default withStyles(useStyles, {withTheme: true})(ChatBar);