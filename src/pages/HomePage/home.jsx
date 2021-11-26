import React from 'react';
import { Grid, Paper, Button } from '@mui/material';
import Appbar from 'src/components/Appbar';
import useStyles from '../../styles/Home';
import { Link } from "react-router-dom";

const HomePage = () =>{
    const classes = useStyles();
    return(
    <>
       <Appbar/>

        <div className={classes.div}>
            <Grid container spacing={2} className={classes.grid}>
                <Grid item xs={7} style={{ textAlign: "center" }}>
                    <Paper elevation={3} className={classes.paper}>

                        <div style={{textAlign:"left"}}>
                            <h2>USERS</h2>
                            <Button variant="contained" className={classes.button_users}>
                                USERS
                            </Button>
                             <Button variant="contained" className={classes.button_users} style={{marginTop:"15px"}}>
                                VOLUNTEERS
                            </Button>
                        </div>

                        <div style={{textAlign:"left",marginTop:"40px"}}>
                            <h2>BUSES</h2>
                            <Link  to={{pathname: '/viewbus'}} style={{textDecoration: "none"}}>
                            <Button variant="contained" className={classes.button_buses}>
                                VIEW BUSES
                            </Button>
                            </Link>
                             <Button variant="contained" className={classes.button_buses} style={{marginTop:"15px"}}>
                                CREATE NEW BUS
                            </Button>
                        </div>

                        <div style={{textAlign:"left",marginTop:"40px"}}>
                            <h2>UNIVERSITY</h2>
                            <Link  to={{pathname: '/semseter'}} style={{textDecoration: "none"}}>
                                <Button variant="contained" className={classes.button_university}>
                                    SEMESTERS
                                </Button>
                            </Link>
                            <Link  to={{pathname: '/department'}} style={{textDecoration: "none"}}>
                                <Button variant="contained" className={classes.button_university} style={{marginTop:"15px"}}>
                                    DEPARTMENTS
                                </Button>
                            </Link>
                        </div>

                    </Paper>
                </Grid>
            </Grid>
        </div>
    </>
    );
};

export default HomePage;