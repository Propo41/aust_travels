import { makeStyles } from '@mui/styles';

const useStyles = makeStyles({

card:{
    flexDirection: "row !important",
    display:"inline-block",
    width: "100%",
    backgroundColor: "#E5E5E5",
    borderRadius:"20px !important",
    boxShadow:"none",
    marginTop:"1%",
},

cardActions:{
    padding:"0px",
},

section1:{
    flex:"0.15 !important",
    fontFamily:"Saira Semi Condensed, sans-serif",
    fontSize:"2rem",
    textAlign:"center",
    borderRight:"5px solid #C4C4C4",
    padding:"10% 0%",
},

section2:{
    flex:"0.70 !important",
    padding:"2%",
    fontFamily:"Saira Semi Condensed, sans-serif",
    fontSize:"1.3rem",
},
section3:{
    flex:"0.15 !important",
    textAlign:"center",
},
cardbutton:{
    fontSize:"2rem !important",
    color:"#FF6536",
},
title:{
    fontFamily:"Saira Semi Condensed, sans-serif",
    fontSize:"1.3rem",
    fontWeight:"bold",
},

});

export default useStyles;