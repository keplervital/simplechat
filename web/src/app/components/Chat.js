import React, { Component } from "react";
import { StateContext } from '../simplechat/state';
import styles from '../styles/chat.module.css';
import { withStyles, IconButton, Typography } from '@material-ui/core';
import { ChatOutlined, CloseOutlined } from '@material-ui/icons';
import classNames from 'classnames';
import * as Actions from '../store/actions';

const useStyles = theme => ({
    iconButton: {
        marginTop: 10,
        marginLeft: 20,
        color: '#FAFAFA',
        padding: 0,
        float: 'left'
    },
    iconClose: {
        position: 'absolute',
        right: 10,
        top: 0
    },
    icon: {
      fontSize: 36,
    },
    title: {
        lineHeight: '55px',
        color: '#FAFAFA'
    }
});

class Chat extends Component {

    static contextType = StateContext;

    componentDidMount() {
        this.updateChat();
    }

    componentDidUpdate() {
        
    }

    updateChat() {
        const [{}, dispatch] = this.context;
        dispatch(Actions.setApiKey('f379ead2b46911e9a99a2b24df60637c'));
        dispatch(Actions.updateUser(dispatch));
        dispatch(Actions.updateBar(dispatch));
    }

    render() {

        const {classes} = this.props;
        const [{chat}, dispatch] = this.context;
        
        return (
            <div className={classNames(styles.holder, chat.open ? styles.open : '')}>
                <div className={styles.holderTop}>
                    <IconButton onClick={() => dispatch(Actions.showChatBar(true))} className={classes.iconButton}>
                        <ChatOutlined className={classes.icon} />
                    </IconButton>
                    {chat.open ? (
                            <React.Fragment>
                                <Typography className={classNames(classes.title)}>{chat.title}</Typography>
                                <IconButton onClick={() => dispatch(Actions.showChatBar(false))} className={classNames(classes.iconButton, classes.iconClose)}>
                                    <CloseOutlined className={classes.icon} />
                                </IconButton>
                            </React.Fragment>
                        ) : ''
                    }
                </div>
            </div>
          );
    }
}

export default withStyles(useStyles, {withTheme: true})(Chat);