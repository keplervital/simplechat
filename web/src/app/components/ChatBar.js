import React, { Component } from "react";
import { StateContext } from '../simplechat/state';
import { withStyles, Grid } from '@material-ui/core';
import styles from '../styles/chat.module.css';
import classNames from 'classnames';
import defaultAvatar from './../images/avatar.jpg'

const useStyles = theme => ({
    
});

class ChatBar extends Component {

    static contextType = StateContext;

    render() {

        const [{chat}, _] = this.context;
        const users = chat.bar.users;

        for(let i = 1; i <= 20; ++i) {
            //users.push(<Grid item xs={12} className={classNames(styles.user)} key={i}>{i}</Grid>);
        }

        return (
            <Grid container>
                {chat.bar.directs.map(conversation => {
                    const userInfo = users[conversation.participants.find((uid) => uid != chat.me.id)];
                    return (
                        <Grid item xs={12} className={classNames(styles.user)} key={conversation.id}>
                            <div className={classNames(styles.avatar)}></div>
                            <div className={classNames(styles.userStatus)}></div>
                        </Grid>
                    );
                })}
            </Grid>
        );
    }

}

export default withStyles(useStyles, {withTheme: true})(ChatBar);