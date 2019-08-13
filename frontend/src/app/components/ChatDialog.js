import React, { Component } from "react";
import { StateContext } from '../simplechat/state';
import Config from '../simplechat/config';
import { withStyles, Grid, Typography } from '@material-ui/core';
import styles from '../styles/chat.module.css';
import classNames from 'classnames';
import ChatOutlined from '@material-ui/icons/ChatOutlined';

const useStyles = theme => ({
    icon: {
        fontSize: 126,
    },
});

class ChatDialog extends Component {

    static contextType = StateContext;

    render() {
        const {classes} = this.props;

        return (
            <Grid container className={classNames(styles.dialogHolder)}>
                <Grid item xs={12}>
                    <div className={classNames(styles.dialogIcon)}>
                        <ChatOutlined className={classes.icon} />
                        <Typography variant="subtitle2">{Config.lang.dialog}</Typography>
                    </div>
                </Grid>
            </Grid>
        );
    }

}

export default withStyles(useStyles, {withTheme: true})(ChatDialog);