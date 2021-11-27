import { Card, CardActions,Button } from "@mui/material";
import useStyles from "src/styles/cardstyle";
import { Icon } from '@iconify/react';


const CardComponent=(props)=>{
    const classes = useStyles();

    return(
        <Card className={classes.card}>
            <CardActions className={classes.cardActions}>
            <div className={classes.section1}>{props.CardNo}</div>

            <div className={classes.section2}>

                <CardActions>
                    <p className={classes.title}>Estimated time : </p>
                    <p >{props.Estimated_time}</p>
                </CardActions>

                <CardActions>
                    <p className={classes.title}>Latitude : </p>
                    <p >{props.Latitude}</p>
                </CardActions>

                <CardActions>
                    <p className={classes.title}>Longitude : </p>
                    <p >{props.Longitude}</p>
                </CardActions>

                <CardActions>
                    <p className={classes.title}>Map Place Id : </p>
                    <p >{props.Map_Place_Id}</p>
                </CardActions>

                <CardActions>
                    <p className={classes.title}>Map Place Name : </p>
                    <p >{props.Map_Place_Name}</p>
                </CardActions>

                <CardActions>
                    <p className={classes.title}>Place Name : </p>
                    <p >{props.Place_Name}</p>
                </CardActions>
                
            </div>

            <div className={classes.section3}>
                <Button style={{display:props.display}} className={classes.cardbutton}><Icon icon="icomoon-free:bin2" /></Button>
            </div>
            </CardActions>
        </Card>
    );
};

export default CardComponent;