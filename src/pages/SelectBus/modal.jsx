import Modal from '@mui/material/Modal';
import { Button,Box,FormControl,Select,MenuItem,OutlinedInput } from '@mui/material';
import * as React from 'react';
import useStyles from '../../styles/SelectBus';
import Appbar from 'src/components/Appbar';
import { Paper } from '@mui/material';

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

    const classes = useStyles();

    const [busName, setbusName] = React.useState('');
    const [busStartTime, setbusStartTime] = React.useState('');

    const handleChangebusName = (event) => {
        setbusName(event.target.value);
    };
    const handleChangebusStartTime = (event) => {
        setbusStartTime(event.target.value);
    };
    return(
    <div style={{height:"100%"}}>
        <Appbar/>
        <h2 style={{textAlign:"center",marginTop:"2%"}}>BUSES</h2>
        <Paper elevation={3} className={classes.paperdiv}>
            <div className={classes.topdiv}>
                <Button onClick={handleOpen} className={classes.topdivButton}>SELECT BUS</Button>
            </div>
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
                                    <MenuItem value={1}>Demo 1</MenuItem>
                                    <MenuItem value={2}>Demo 2</MenuItem>

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
                                    <MenuItem value={3}>Demo 1</MenuItem>
                                    <MenuItem value={4}>Demo 2</MenuItem>

                                </Select>      
                            </FormControl>  

                            <Button variant="contained" className={classes.button_select} style={{marginTop:"30px"}}>
                                <p style={{lineHeight:"2"}}>SELECT</p>
                            </Button>
                        </div>
            
            </Box>       
        </Modal>
    </div>
    );
}

export default ViewBusPage;