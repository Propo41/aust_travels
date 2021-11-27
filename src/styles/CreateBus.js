import { makeStyles } from '@mui/styles';

const useStyles = makeStyles({
paperdiv:
{
    textAlign: "left",
    padding: "5rem 5rem !important",
    borderRadius:"20px !important",
    margin: "2% 20% 5% 20% !important",
    
},

busRoutediv:{
    padding:"0% 5%",
},

addRouteInfo:{

    backgroundColor:"#F5F5F5 !important",
    padding: "1rem 0rem !important",
    borderRadius:"10px !important",
    margin:"3% 0%",
    
},

buslocationInfo:{
    display:"flex !important",
    flexDirection:"row !important",
},

busInput:{
    
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
    margin:"1% 5%",
    textAlign:"left",
},

button_addRoute:{
    display: "flex",
    justifyContent:"center",
    width: "90%",
    fontSize: "1.3rem !important",
    color:"black !important",
    borderRadius:"10px !important",
    backgroundColor:"#FFC107 !important",
    margin:"2.5% auto",
    alignItems:"center",
    fontWeight:"500",
},

savebutton:{
    display: "flex",
    justifyContent:"center",
    width: "90%",
    fontSize: "1.3rem !important",
    color:"black !important",
    borderRadius:"10px !important",
    backgroundColor:"#00FF94 !important",
    margin:"5% auto",
    alignItems:"center",
    fontWeight:"700",
    lineHeight:"2.5",
}
});

export default useStyles;