import Modal from '@mui/material/Modal';
import { Button,Box,FormControl,Select,MenuItem,OutlinedInput } from '@mui/material';
import * as React from 'react';
import useStyles from '../../styles/SelectBus';
import Appbar from '../../components/Appbar';
import { Paper } from '@mui/material';
import CardComponent from '../../components/Card';
import { useState ,useEffect } from 'react';
import { Icon } from '@iconify/react';
import { useAuth } from "auth/firebaseAuth";
import firebaseConfig from 'auth/config';
import { initializeApp } from '@firebase/app';
import { getDatabase, ref,set, onValue} from "firebase/database";
import { keys } from 'lodash';

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

    const [buslist, setBuslist] = useState([]);
    const [bustimelist, setBustimelist] = useState([]);
    const [bustracklist, setBustracklist] = useState([]);

    const classes = useStyles();

    const [busName, setbusName] = React.useState('');
    const [busStartTime, setbusStartTime] = React.useState('');

    const [tmpbusName,setTmpbusName]= React.useState('');
    const [tmpbusStartTime, settmpBusStartTime] = React.useState('');

    useEffect(()=>{
        const db = getDatabase();
        const busref = ref(db,'availableBusInfo');
        const busList = [];

        onValue(busref, (snapshot) => {
        snapshot.forEach((childSnapshot) => {
            const bus = childSnapshot.val();
            bus.id=childSnapshot.key;
            busList.push(bus.id);
        });

        setBuslist(busList);
        },
        {
            onlyOnce: true,
        });
    },[])
    
    useEffect(()=>{
    const db = getDatabase();
    const demo = ref(db,'routes');
    onValue(demo, (snapshot) => {
        snapshot.forEach((childSnapshot) => {
            const bus = childSnapshot.val();
            bus.id=childSnapshot.key;
            console.log(bus.id);
        });

    });
     },[])

    useEffect(() => {
        const json = sessionStorage.getItem("my-buslist");
        const buslist = JSON.parse(json);
        if (buslist) {
            setBustracklist(buslist);
        }
    }, []);

    useEffect(() => {
        const json = JSON.stringify(bustracklist);
        sessionStorage.setItem("my-buslist", json);
    }, [bustracklist]);

        
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
        const db = getDatabase();

        console.log(busName);
        const busref = ref(db,'availableBusInfo/'+event.target.value);
        const bustimeList = [];

        onValue(busref, (snapshot) => {
        snapshot.forEach((childSnapshot) => {
            childSnapshot.forEach((secondchildSnapshot) => {
                if(secondchildSnapshot.key =='startTime')
                {
                    const time=secondchildSnapshot.val();
                    console.log(time);
                    bustimeList.push(time);
                }
            });
        });

        setBustimelist(bustimeList);
        console.log(bustimeList);
        });

        console.log(busName);
    };

    const handleChangebusStartTime = (event) => {
        setbusStartTime(event.target.value);
        console.log(busStartTime);
    };

    let message,cardfound=0,found=false;

    const handleclick = () =>{
       isClicked(true);
       setOpen(false);
       setTmpbusName(busName);
       settmpBusStartTime(busStartTime);
       setbusName('');
       setbusStartTime('');
       cardfound=0;
       found=false;
       

        const db = getDatabase();

        console.log(busName);
        const busref = ref(db,'bus/'+busName+'/'+busStartTime+'/routes/');
        const buslist = [];

        onValue(busref, (snapshot) => {
        snapshot.forEach((childSnapshot) => {
            const list = childSnapshot.val();

            buslist.push({
            busName:busName,
            busStartTime:busStartTime,
            estTime:list.estTime,
            latitude:list.latitude,
            longitude:list.longitude,
            mapPlaceId:list.mapPlaceId,
            mapPlaceName:list.mapExactPlaceName,
            place:list.place,
            });

        });

        setBustracklist(buslist);

        });




    };

    const deletebuttonClick = () =>
    {
        const db = getDatabase();
        set(ref(db,'bus/'+tmpbusName+'/'+tmpbusStartTime+'/routes/'), {
            
        })

        isClicked(false);
        setOpen(false);
        setTmpbusName('');
        settmpBusStartTime('');
        cardfound=0;
        found=false;
    };

    function createBusNamelist(buslist)
    {
        return (<MenuItem onClick={handleChangebusName} value={buslist}>{buslist}</MenuItem>);
    }

    function createBusStartTimelist(bustimelist)
    {
        return (<MenuItem value={bustimelist}>{bustimelist}</MenuItem>);
    }

    function createlist()
    {
        
    }

    

    if(!click && !found)
    {
        message = <p className={classes.message}>PLEASE SELECT A BUS TO CONTINUE</p>;
    }

    const handleNotfoundmessage = (cardfound) =>
    {
        if(click && cardfound === 0) 
        {
            return <p className={classes.message}>BUS NOT FOUND</p>;
        }

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
                        if(tmpbusName && tmpbusStartTime && click && !found)
                        {
                            found=true;
                            return(
                            <>
                                <div className={classes.businfo}>
                                    <div className={classes.businfoContainer}>
                                        <p style={{fontWeight:"bold"}}>Bus name:</p>
                                        <p style={{marginLeft:"4%",fontSize:"1.4rem"}}>{tmpbusName}</p>
                                    </div>
                                    <div className={classes.businfoContainer}>
                                        <p style={{fontWeight:"bold"}}>Bus timing:</p>
                                        <p style={{marginLeft:"4%",fontSize:"1.4rem"}}>{tmpbusStartTime}</p>
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
                        if(tmpbusName && tmpbusStartTime && click)
                        {   
                            cardfound++;
                            return(
                                <CardComponent
                                    CardNo={cardfound}
                                    name={val.busName}
                                    time={val.busStartTime}
                                    Estimated_time={val.estTime}
                                    Latitude={val.latitude}
                                    Longitude={val.longitude}
                                    Map_Place_ID={val.mapPlaceId}
                                    Map_Place_Name={val.mapPlaceName}
                                    Place_Name={val.place}
                                    display={"none"}
                                />   
                            );
                        }
                        
                    })

                    
                }
                {handleNotfoundmessage(cardfound)}   
                
            
            
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

                                    {buslist.map(createBusNamelist)}

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
                                    
                                    {bustimelist.map(createBusStartTimelist)}

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