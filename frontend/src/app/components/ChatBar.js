import React, { Component } from "react";
import { StateContext } from '../simplechat/state';
import { withStyles, Grid, Tooltip } from '@material-ui/core';
import styles from '../styles/chat.module.css';
import classNames from 'classnames';
import * as Actions from '../store/actions';
import socketIOClient from "socket.io-client";
import Config from '../simplechat/config';

const useStyles = theme => ({
    
});

class ChatBar extends Component {

    static contextType = StateContext;

    constructor(props) {
        super(props);
        this.state = {
            userId: null,
            online: 'online-users',
            setUser: 'set-user',
            markMessageRead: 'messages-read',
        };
        this.socket = socketIOClient(Config.endpoint.socket);
        this.disableSocketOnlineUsers.bind(this);
        this.enableSocketOnlineUsers.bind(this);
    }

    componentDidMount() {
        this.enableSocketOnlineUsers();
    }

    componentDidUpdate(_, prevState) {
        const [{chat}] = this.context;
        if(this.state.userId !== chat.me.id) {
            this.setState({userId: chat.me.id});
        } else if(prevState.userId !== this.state.userId) {
            this.disableSocketOnlineUsers();
            this.enableSocketOnlineUsers();
        }
    }

    componentWillUnmount() {
        this.disableSocketOnlineUsers();
    }

    disableSocketOnlineUsers() {
        this.socket.off(this.state.online);
    }

    enableSocketOnlineUsers() {
        if(this.state.userId == null)
            return;
        const _this = this;
        const [{_}, dispatch] = this.context;
        this.socket.on(this.state.online, online => {
            dispatch(Actions.onlineUsers(online));
        });
        this.socket.on(this.state.userId, unreadMessages => {
            const [{chat}, _] = _this.context;
            dispatch(Actions.unreadMessages(unreadMessages));
            _this.markMessageRead(chat.conversation.id, unreadMessages);
        });
        this.socket.emit(this.state.setUser, {
            userId: this.state.userId
        }, () => {});
    }

    markMessageRead(openChatId, unreadMessages) {
        if(!unreadMessages.hasOwnProperty(openChatId) ||
            unreadMessages[openChatId] == 0
        ) return;
        this.socket.emit(this.state.markMessageRead, { 
            userId: this.state.userId,
            chatId: openChatId 
        });
    }

    render() {

        const [{chat}, dispatch] = this.context;
        const users = chat.bar.users;
        const _this = this;

        return (
            <Grid container>
                {chat.bar.directs.map(conversation => {
                    const userInfo = users[conversation.participants.find((uid) => uid != chat.me.id)];
                    const isOnline = chat.online.users.includes(userInfo.id);
                    let unreadMessages = chat.unread.hasOwnProperty(conversation.id) ? parseInt(chat.unread[conversation.id]) : 0;
                    if(chat.conversation.id === conversation.id) 
                        unreadMessages = 0;
                    return (
                        <Tooltip key={conversation.id} title={userInfo.name} placement="left">
                            <Grid 
                                item 
                                xs={12} 
                                className={classNames(styles.user, (conversation.id == chat.conversation.id) ? styles.chatSelected : '')} 
                                onClick={() => {
                                    _this.markMessageRead(conversation.id, chat.unread);
                                    dispatch(Actions.openChat(dispatch, conversation.id, userInfo.name, userInfo.avatar));
                                }}
                            >
                                <div className={classNames(styles.avatar)}></div>
                                <div className={classNames(styles.userStatus, isOnline ? styles.green : styles.yellow)}></div>
                                <div className={classNames(styles.unreadMessages, unreadMessages === 0 ? styles.hide : '')}>{unreadMessages}</div>
                            </Grid>
                        </Tooltip>
                    );
                })}
                <hr />
                {chat.bar.groups.map(conversation => {
                    let unreadMessages = chat.unread.hasOwnProperty(conversation.id) ? parseInt(chat.unread[conversation.id]) : 0;
                    if(chat.conversation.id === conversation.id) 
                        unreadMessages = 0;
                    return (
                        <Tooltip key={conversation.id} title={conversation.name} placement="left">
                            <Grid 
                                item 
                                xs={12} 
                                className={classNames(styles.user, (conversation.id == chat.conversation.id) ? styles.chatSelected : '')} 
                                onClick={() => {
                                    _this.markMessageRead(conversation.id, chat.unread);
                                    dispatch(Actions.openChat(dispatch, conversation.id, conversation.name, null));
                                }}
                            >
                                <div className={classNames(styles.avatar)}></div>
                                <div className={classNames(styles.userStatus, styles.grey)}></div>
                                <div className={classNames(styles.unreadMessages, unreadMessages === 0 ? styles.hide : '')}>{unreadMessages}</div>
                            </Grid>
                        </Tooltip>
                    );
                })}
            </Grid>
        );
    }

}

export default withStyles(useStyles, {withTheme: true})(ChatBar);