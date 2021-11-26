import { makeStyles } from '@mui/styles';

const useStyles = makeStyles({

appbar:{
    padding:"10px",
},

toolbar:{
     color:"black",
     fontSize:"2rem",
},

div:{
    marginLeft:"15%",
    marginRight:"15%",
},

grid:{
    flexWrap: "wrap",
    alignItems: "center",
    direction: "column",
  justifyContent: "center",
},

paper:{
    textAlign: "center",
    padding: "3rem 6rem 5rem",
    marginTop:"15% !important",
    borderRadius:"20px !important",
},

button_users:{
    width: "100%",
    fontSize: "1.3rem !important",
    color:"black !important",
    backgroundColor: "#FFE16A !important",
},

button_buses:{
    width: "100%",
    fontSize: "1.3rem !important",
    backgroundColor: "#00AB55 !important",
},

button_university:{
    width: "100%",
    fontSize: "1.3rem !important",
    color:"black !important",
    backgroundColor: "#FFC107 !important",
}

});

export default useStyles;