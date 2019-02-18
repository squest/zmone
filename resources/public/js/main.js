'use strict';

/*
import React from 'react';
import ReactDOM from 'react-dom';
import Button from '@material-ui';
*/

function App(props) {
    const styles = {
        root: {
            flexGrow: 1,
        },
        grow: {
            flexGrow: 1,
        },
        menuButton: {
            marginLeft: -12,
            marginRight: 20,
        },
    };

    const {classes} = props;

    return (
        <div>
            <AppBar position="static">
                <Toolbar>
                    <IconButton className={classes.menuButton} color="inherit" aria-label="Menu">
                    </IconButton>
                    <Typography variant="h6" color="inherit">
                        News
                    </Typography>
                    <Button color="inherit">Login</Button>
                </Toolbar>
            </AppBar>
        </div>
    );
}


ReactDOM.render( <App classes={withStyles}/>, document.querySelector('#app'));