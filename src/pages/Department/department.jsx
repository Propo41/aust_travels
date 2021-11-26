import React, { useState, useEffect } from 'react';
import { Grid, Card, Paper, Button, FormControl, OutlinedInput, CardActions} from '@mui/material';
import Appbar from 'src/components/Appbar';
import useStyles from 'src/styles/Department';
import { Icon } from '@iconify/react';
import { Link } from "react-router-dom";

const DepartmentPage = () =>{

    const classes = useStyles();

    const [department,setdepartment] = useState("");
    const [departmentlist,setDepartmentlist] = useState([]);

    useEffect(() => {
        const json = sessionStorage.getItem("my-departmentlist");
        const saveddepartments = JSON.parse(json);
        if (saveddepartments) {
            setDepartmentlist(saveddepartments);
        }
    }, []);

    useEffect(() => {
        const json = JSON.stringify(departmentlist);
        sessionStorage.setItem("my-departmentlist", json);
    }, [departmentlist]);


    const handleDepartment = event =>{
        setdepartment(event.target.value);
    }

    const handleclick = event =>{
        event.preventDefault();
        if(department)
        {
            setDepartmentlist([...departmentlist,{department:department}]);
        }
        setdepartment("");
    }

    const deleteDepartment = (index) =>
    {
        const new_list = [...departmentlist];
        new_list.splice(index,1);
        setDepartmentlist(new_list);

    }

    return(
        <>
            <Appbar/>
            <h2 style={{textAlign:"center",marginTop:"5%"}} >DEPARTMENTS</h2>
            <div className={classes.div}>
                <Grid container spacing={2} className={classes.grid}>
                    <Grid item xs={6} style={{ textAlign: "center" }}>
                        <Paper elevation={3} className={classes.paper}>
                            <div className={classes.addSemester}>
                                 <FormControl className={classes.semesterInput} onChange={handleDepartment}>
                                    <OutlinedInput value={department} className={classes.input} style={{paddingLeft:"5%"}} placeholder="ENTER DEPARTMENT NAME" />
                                </FormControl>
                                <Button variant="contained" onClick={handleclick} className={classes.button_addSemester}>ADD DEPARTMENT</Button>
                            </div>

                            <hr style={{width:"100%", margin:"5% 0%"}}></hr>
                            
                            {
                                departmentlist.map((val,key)=>(

                                <Card className={classes.card}>
                                    <CardActions>
                                        <p className={classes.semestername}>{val.department}</p>
                                        <Button onClick={()=>deleteDepartment(key)} className={classes.cardbutton}><Icon icon="icomoon-free:bin2" /></Button>
                                    </CardActions>
                                </Card>

                                ))
                            }
                            
                            <Button variant="contained" className={classes.savebutton}>SAVE CHANGES</Button>
                        </Paper>
                    </Grid>
                </Grid>
            </div>
        </>
    );

};

export default  DepartmentPage;
