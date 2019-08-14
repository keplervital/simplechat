import React, { Component } from "react";
import { StateContext } from '../simplechat/state';
import Config from '../simplechat/config';
import { withStyles, Grid, Typography } from '@material-ui/core';
import styles from '../styles/chat.module.css';
import classNames from 'classnames';
import ChatOutlined from '@material-ui/icons/ChatOutlined';
import Paper from '@material-ui/core/Paper';
import InputBase from '@material-ui/core/InputBase';
import Divider from '@material-ui/core/Divider';
import IconButton from '@material-ui/core/IconButton';
import DirectionsIcon from '@material-ui/icons/Directions';
import * as Actions from '../store/actions';
import socketIOClient from "socket.io-client";

const useStyles = theme => ({
    icon: {
        fontSize: 126,
    },
    holder: {
        maxHeight: '100%'
    },
    root: {
        padding: '2px 4px',
        display: 'flex',
        alignItems: 'center',
        width: 260,
        marginLeft: 10,
        marginTop: 15
    },
    input: {
        marginLeft: 8,
        flex: 1,
    },
    iconButton: {
        padding: 10,
    },
    divider: {
        width: 1,
        height: 28,
        margin: 4,
    },
});

class ChatDialog extends Component {

    static contextType = StateContext;

    constructor(props) {
        super(props)
        this.state = {
            chatId: null,
            message: ''
        }
        this.socket = socketIOClient(Config.endpoint.socket);
        this.updateChatId.bind(this);
        this.sendMessage.bind(this);
        this.disableSocketOnChat.bind(this);
        this.enableSocketOnChat.bind(this);
    }

    componentDidMount() {
        this.updateChatId();
    }

    componentWillUnmount() {
        this.disableSocketOnChat(this.state.chatId);
    }

    componentDidUpdate() {
        this.updateChatId();
    }

    updateChatId() {
        const [{chat}, _] = this.context;
        if(chat.conversation.id !== this.state.chatId) {
            this.disableSocketOnChat(this.state.chatId);
            this.enableSocketOnChat(chat.conversation.id);
            this.setState({
                chatId: chat.conversation.id,
                message: ''
            });
        }
    }

    disableSocketOnChat(chatId) {
        if(chatId == null)
            return;
        this.socket.off(chatId);
    }

    enableSocketOnChat(chatId) {
        if(chatId == null)
            return;
        const [_, dispatch] = this.context;
        this.socket.on(chatId, response => {
            switch(response.type) {
                case 'new-message':
                    dispatch(Actions.newMessageToOpenChat(response.data));
                    break;
                default:
                    break;
            }
        });
    }

    setMessage(e) {
        this.setState({ message: e.target.value });
    }

    onEnter(e) {
        if(e.which === 13 && this.state.message.length > 0) {
            this.sendMessage();
        }
    }

    sendMessage() {
        const [{chat}] = this.context;
        this.socket.emit('send-message', {
            auth: chat.apiKey,
            chatId: this.state.chatId,
            message: this.state.message
        }, () => {});
        this.setState({ message: '' });
    }

    render() {

        const [{chat}, _] = this.context;
        const {classes} = this.props;
        const users = chat.bar.users;

        return (
            <Grid container className={classNames(styles.dialogHolder)}>
                <Grid item xs={12} className={classNames(classes.holder)}>
                    {!chat.conversation.open ? (
                        <div className={classNames(styles.dialogIcon)}>
                            <ChatOutlined className={classes.icon} />
                            <Typography variant="subtitle2">{Config.lang.dialog}</Typography>
                            </div>
                    ): (
                        <React.Fragment></React.Fragment>
                    )}
                    {chat.conversation.open ? (
                        <React.Fragment>
                            <Grid item xs={12} className={classNames(styles.dialogTop)}>
                                {chat.conversation.messages.slice(0).reverse().map(message => {
                                    const userInfo = users[message.userID];
                                    return (
                                        <div key={message.id} className={classNames(styles.messageHolder, (userInfo.id == chat.me.id ? styles.messageRight : ''))}>
                                            <div className={classNames(styles.messageTitle)}>{userInfo.name}</div>
                                            <div className={classNames(styles.messageBody)}>{message.body}</div>
                                            <div className={classNames(styles.messageDate)}>{new Date(message.dateAdded).toLocaleString()}</div>
                                        </div>
                                    )
                                })}
                            </Grid>
                            <Grid item xs={12} className={classNames(styles.dialogBottom)}>
                                <Paper className={classes.root}>
                                    <InputBase
                                        className={classes.input}
                                        placeholder={Config.lang.type}
                                        inputProps={{ 'aria-label': Config.lang.type }}
                                        onChange={this.setMessage.bind(this)}
                                        onKeyPress={this.onEnter.bind(this)}
                                        value={this.state.message}
                                    />
                                    <Divider className={classes.divider} />
                                    <IconButton 
                                        color="primary" 
                                        className={classes.iconButton} 
                                        aria-label="directions"
                                        onClick={() => this.sendMessage()}
                                    >
                                        <DirectionsIcon />
                                    </IconButton>
                                </Paper>
                            </Grid>
                        </React.Fragment>
                    ) : (
                        <React.Fragment></React.Fragment>
                    )}
                </Grid>
            </Grid>
        );
    }

}

export default withStyles(useStyles, {withTheme: true})(ChatDialog);