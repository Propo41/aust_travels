import React, { useState, useEffect } from 'react';
import { Grid, Card, Paper, Button, FormControl, OutlinedInput, CardActions} from '@mui/material';
import Appbar from '../../components/Appbar';
import useStyles from '../../styles/Semester';
import { Icon } from '@iconify/react';
import { Link } from "react-router-dom";

const SemesterPage = () =>{

    const classes = useStyles();


    const [semester,setSemester] = useState("");
    const [semesterlist,setSemesterlist] = useState([]);

    useEffect(() => {
        const json = sessionStorage.getItem("my-semesterlist");
        const savedSemesters = JSON.parse(json);
        if (savedSemesters) {
            setSemesterlist(savedSemesters);
        }
    }, []);

    useEffect(() => {
        const json = JSON.stringify(semesterlist);
        sessionStorage.setItem("my-semesterlist", json);
    }, [semesterlist]);


    const handleSemester = event =>{
        setSemester(event.target.value);
    }

    const handleclick = event =>{
        event.preventDefault();
        if(semester)
        {
            setSemesterlist([...semesterlist,{semester:semester}]);
        }
        setSemester("");
    }

    const deleteSemester = (index) =>
    {
        const new_list = [...semesterlist];
        new_list.splice(index,1);
        setSemesterlist(new_list);

    }


    return(
        <>
            <Appbar/>
            <h2 style={{textAlign:"center",marginTop:"5%"}} >SEMESTERS</h2>
            <div className={classes.div}>
                <Grid container spacing={2} className={classes.grid}>
                    <Grid item xs={6} style={{ textAlign: "center" }}>
                        <Paper elevation={3} className={classes.paper}>
                            <div className={classes.addSemester}>
                                 <FormControl className={classes.semesterInput} onChange={handleSemester}>
                                    <OutlinedInput value={semester} className={classes.input} style={{paddingLeft:"5%"}} 
                                     placeholder="ENTER SEMESTER NAME" />
                                </FormControl>
                                <Button variant="contained" onClick={handleclick} className={classes.button_addSemester}>ADD SEMESTER</Button>
                            </div>

                            <hr style={{width:"100%", margin:"5% 0%"}}></hr>
                                {
                                
                                    semesterlist.map((val,key)=>(

                                    <Card className={classes.card}>
                                        <CardActions>
                                            <p className={classes.semestername}>{val.semester}</p>
                                            <Button onClick={()=>deleteSemester(key)} className={classes.cardbutton}><Icon icon="icomoon-free:bin2" /></Button>
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

export default SemesterPage;
