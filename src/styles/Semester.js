import { makeStyles } from '@mui/styles';

const useStyles = makeStyles({

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
    padding: "2rem 3rem 2.5rem",
    marginTop:"7.5% !important",
    borderRadius:"20px !important",
},

addSemester:{

    backgroundColor:"#F5F5F5 !important",
    padding: "0.5rem 1rem 0.5rem !important",
    borderRadius:"10px !important",
    
},

semesterInput:{
    
    "& .MuiOutlinedInput-root .MuiOutlinedInput-notchedOutline": {
      borderColor: "#E5E5E5",
    },
    "&:hover .MuiOutlinedInput-root .MuiOutlinedInput-notchedOutline": {
      borderColor: "#E5E5E5"
    },
    "& .MuiOutlinedInput-root.Mui-focused .MuiOutlinedInput-notchedOutline": {
      borderColor: "#E5E5E5"
    },
    width: "90%",
    backgroundColor: "#E5E5E5",
    borderRadius:"10px !important",
    margin:"5% 5%",
    textAlign:"left",
},

input:{
    '&::placeholder': { color: 'blue' },
},

button_addSemester:{
    width: "90%",
    fontSize: "1.3rem !important",
    color:"black !important",
    borderRadius:"10px !important",
    backgroundColor:"#FFC107 !important",
    marginBottom:"5%",
    fontWeight:"500",
},

card:{
    width: "100%",
    backgroundColor: "#E5E5E5",
    borderRadius:"20px !important",
    marginTop:"1.5%",
    boxShadow:"none",
    padding:"2%",
},

semestername:{
    fontFamily:"Saira Semi Condensed, sans-serif",
    fontWeight:"bold",
    fontSize:"1.2rem !important",
    textAlign:"left",
    marginLeft:"7.5%",
    width:"10%",
},

cardbutton:{
    fontSize:"1.2rem !important",
    marginLeft:"67.5%",
    marginBottom:"0.2%",
    color:"#FF6536",
},

savebutton:{
    width: "100%",
    fontSize: "1.3rem !important",
    color:"black !important",
    borderRadius:"10px !important",
    backgroundColor:"#00FF94 !important",
    margin:"5% 0%",
    fontWeight:"700",
    lineHeight:"2.5",
}

});

export default useStyles;