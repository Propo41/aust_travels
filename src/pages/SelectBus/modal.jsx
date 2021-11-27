import Modal from '@mui/material/Modal';
import { Button,Box,FormControl,Select,MenuItem,OutlinedInput } from '@mui/material';
import * as React from 'react';
import useStyles from '../../styles/SelectBus';
import Appbar from 'src/components/Appbar';
import { Paper } from '@mui/material';
import busNamelist from '../../_mocks_/busname';
import busStartTimelist from '../../_mocks_/busStartTime';
import CardComponent from 'src/components/Card';
import bustracklist from '../../_mocks_/bustrack';
import { useState ,useEffect } from 'react';
import { Icon } from '@iconify/react';

const Style = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: '30%',
  bgcolor: 'background.paper',
  p: 6,
};

const ViewBusPage = () =>{
    const [open, setOpen] = React.useState(false);
    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);

    const [click, isClicked] = useState(false);

    const classes = useStyles();

    const [busName, setbusName] = React.useState('');
    const [busStartTime, setbusStartTime] = React.useState('');

    const [tmpbusName,setTmpbusName]= React.useState('');
    const [tmpbusStartTime, settmpBusStartTime] = React.useState('');

    useEffect(() => {
        const json = sessionStorage.getItem("my-open");
        const openstate = JSON.parse(json);
        if (openstate) {
            setOpen(openstate);
        }
    }, []);

    useEffect(() => {
        const json = JSON.stringify(open);
        sessionStorage.setItem("my-open", json);
    }, [open]);

    useEffect(() => {
        const json = sessionStorage.getItem("my-click");
        const clickstate = JSON.parse(json);
        if (clickstate) {
            isClicked(clickstate);
        }
    }, []);

    useEffect(() => {
        const json = JSON.stringify(click);
        sessionStorage.setItem("my-click", json);
    }, [click]);

    useEffect(() => {
        const json = sessionStorage.getItem("my-busname");
        const Busname = JSON.parse(json);
        if (Busname) {
            setTmpbusName(Busname);
        }
    }, []);

    useEffect(() => {
        const json = JSON.stringify(tmpbusName);
        sessionStorage.setItem("my-busname", json);
    }, [tmpbusName]);

    useEffect(() => {
        const json = sessionStorage.getItem("my-bustime");
        const Bustime = JSON.parse(json);
        if (Bustime) {
            settmpBusStartTime(Bustime);
        }
    }, []);

    useEffect(() => {
        const json = JSON.stringify(tmpbusStartTime);
        sessionStorage.setItem("my-bustime", json);
    }, [tmpbusStartTime]);


    const handleChangebusName = (event) => {
        setbusName(event.target.value);
        console.log(busName);
    };
    const handleChangebusStartTime = (event) => {
        setbusStartTime(event.target.value);
        console.log(busStartTime);
    };

    let message,cardfound=0,notfound=false,found=false,index=0;

    const handleclick = () =>{
       isClicked(true);
       setOpen(false);
       setTmpbusName(busName);
       settmpBusStartTime(busStartTime);
       setbusName('');
       setbusStartTime('');
       cardfound=0;
       notfound=false;
       found=false;
       index=0;
    };

    const deletebuttonClick = () =>
    {
        isClicked(false);
        setOpen(false);
        setTmpbusName('');
        settmpBusStartTime('');
        cardfound=0;
        notfound=false;
        found=false;
        index=0;
    };

    function createBusNamelist(busNamelist)
    {
        return (<MenuItem value={busNamelist.value}>{busNamelist.name}</MenuItem>);
    }

    function createBusStartTimelist(busStartTimelist)
    {
        return (<MenuItem value={busStartTimelist.value}>{busStartTimelist.time}</MenuItem>);
    }

    

    if(!click && !found)
    {
        message = <p className={classes.message}>PLEASE SELECT A BUS TO CONTINUE</p>;
    }



    return(
    <div>
        <Appbar/>
        <h2 style={{textAlign:"center",marginTop:"2%"}}>BUSES</h2>
        <Paper elevation={7} className={classes.paperdiv}>
            <div className={classes.topdiv}>
                <Button onClick={handleOpen} className={classes.topdivButton}>SELECT BUS</Button>
                
                {   
                    bustracklist.map((val)=>
                    {
                        if(tmpbusName === val.value && tmpbusStartTime === val.time && click && !found)
                        {
                            found=true;
                            return(
                            <>
                                <div className={classes.businfo}>
                                    <div className={classes.businfoContainer}>
                                        <p style={{fontWeight:"bold"}}>Bus name:</p>
                                        <p style={{marginLeft:"4%",fontSize:"1.4rem"}}>{val.name}</p>
                                    </div>
                                    <div className={classes.businfoContainer}>
                                        <p style={{fontWeight:"bold"}}>Bus timing:</p>
                                        <p style={{marginLeft:"4%",fontSize:"1.4rem"}}>{val.time}</p>
                                    </div>
                                </div>
                                <Button onClick={deletebuttonClick} className={classes.deletebutton}><Icon icon="icomoon-free:bin2" /></Button>
                            </>
                            );
                        }
                       
                        
                    })
                }

                
            </div>

                {message}    

                {   
                    bustracklist.map((val)=>
                    {
                        index++;
                        if(tmpbusName === val.value && tmpbusStartTime === val.time && click)
                        {   
                            cardfound++;

                            return(
                                <CardComponent
                                    CardNo={cardfound}
                                    name={val.name}
                                    time={val.time}
                                    Estimated_time={val.Estimated_time}
                                    Latitude={val.Latitude}
                                    Longitude={val.Longitude}
                                    Map_Place_ID={val.Map_Place_ID}
                                    Map_Place_Name={val.Map_Place_Name}
                                    Place_Name={val.Place_Name}
                                    display={"none"}
                                />
                            );
                        }
                    if(click && !notfound && !cardfound && index === bustracklist.length) 
                    {
                        notfound=true;
                        return <p className={classes.message}>BUS NOT FOUND</p>;
                    }
                        
                    })
                }
                
            
            
        </Paper>

        
        <Modal
            open={open}
            onClose={handleClose}
            aria-labelledby="modal-modal-title"
            aria-describedby="modal-modal-description"
        >
            <Box sx={Style} className={classes.paper}>
            
            
                        <div style={{textAlign:"center"}}>
                            <h2>SELECT BUS</h2>
                            
                            <FormControl className={classes.selectbusName} style={{marginTop:"20px"}}>
                            
                                <Select className={classes.selectbusName}

                                    value={busName}
                                    label="ENTER BUS NAME"
                                    displayEmpty
                                    input={<OutlinedInput />}
                                    onChange={handleChangebusName}>

                                    <MenuItem disabled value="">
                                        ENTER BUS NAME
                                    </MenuItem>

                                    {busNamelist.map(createBusNamelist)}

                                </Select>      
                            </FormControl>  

                            <FormControl className={classes.selectStartTime} style={{marginTop:"20px"}}>
                            
                                <Select className={classes.selectStartTime}
                                    value={busStartTime}
                                    label="ENTER BUS START TIME"
                                    displayEmpty
                                    input={<OutlinedInput />}
                                    onChange={handleChangebusStartTime}>

                                    <MenuItem disabled value="">
                                       ENTER BUS START TIME
                                    </MenuItem>
                                    
                                    {busStartTimelist.map(createBusStartTimelist)}

                                </Select>      
                            </FormControl>  

                            <Button variant="contained" onClick={handleclick} className={classes.button_select} style={{marginTop:"30px"}}>
                                <p style={{lineHeight:"2"}}>SELECT</p>
                            </Button>
                        </div>
            
            </Box>       
        </Modal>
    </div>
     
    );
}

export default ViewBusPage;