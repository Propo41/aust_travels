import { makeStyles } from '@mui/styles';

const useStyles = makeStyles({

topdiv:{
    borderBottom: "1px solid black",
    margin: "5% 10% 5% 10%",
},

topdivButton:
{
    width:"10%",
    backgroundColor:"#FFC107 !important",
    color:"black",
    lineHeight:"3",
    fontSize:"1.5rem",
    marginBottom:"1%",
},

paper:{
    textAlign: "center",
    padding: "3rem 3rem 5rem !important",
    borderRadius:"20px !important",
},

selectbusName:{
    "& .MuiOutlinedInput-input": {
      color: "black"
    },
    "& .MuiOutlinedInput-root .MuiOutlinedInput-notchedOutline": {
      borderColor: "#D3D3D3"
    },
    "&:hover .MuiOutlinedInput-input": {
      color: "black"
    },
    "&:hover .MuiOutlinedInput-root .MuiOutlinedInput-notchedOutline": {
      borderColor: "#D3D3D3"
    },
    "& .MuiOutlinedInput-root.Mui-focused .MuiOutlinedInput-input": {
      color: "black"
    },
    "& .MuiOutlinedInput-root.Mui-focused .MuiOutlinedInput-notchedOutline": {
      borderColor: "#D3D3D3"
    },
    width: "100% !important",
    backgroundColor: "#D3D3D3",
    borderRadius:"10px !important",
    textAlign:"left",
},
selectStartTime:{
    "& .MuiOutlinedInput-input": {
      color: "black"
    },
    "& .MuiOutlinedInput-root .MuiOutlinedInput-notchedOutline": {
      borderColor: "#D3D3D3"
    },
    "&:hover .MuiOutlinedInput-input": {
      color: "black"
    },
    "&:hover .MuiOutlinedInput-root .MuiOutlinedInput-notchedOutline": {
      borderColor: "#D3D3D3"
    },
    "& .MuiOutlinedInput-root.Mui-focused .MuiOutlinedInput-input": {
      color: "black"
    },
    "& .MuiOutlinedInput-root.Mui-focused .MuiOutlinedInput-notchedOutline": {
      borderColor: "#D3D3D3"
    },
    width: "100% !important",
    backgroundColor: "#D3D3D3",
    borderRadius:"10px !important",
    textAlign:"left",
    
},
button_select:{
    width: "100%",
    fontSize: "1.3rem !important",
    color:"black !important",
    borderRadius:"10px !important",
    backgroundColor:"#FFC107 !important",
},

});

export default useStyles;