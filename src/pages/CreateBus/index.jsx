import React, { useState, useEffect } from "react";
import { Paper, FormControl, OutlinedInput, Button } from "@mui/material";
import Appbar from "../../components/Appbar";
import useStyles from "../../styles/CreateBus";
import CardComponent from "../../components/Card";
import { getDatabase, ref, set, update, onValue } from "firebase/database";
import { Icon } from "@iconify/react";

const CreateBusPage = () => {
  const classes = useStyles();

  const [busName, setbusName] = useState("");
  const [busStartTime, setbusStartTime] = useState("");
  const [estTime, setEstTime] = useState("");
  const [latitude, setLatitude] = useState("");
  const [longitude, setLongitude] = useState("");
  const [mapExactPlaceName, setMapExactPlaceName] = useState("");
  const [placeName, setPlaceName] = useState("");

  const [routeInfo, setRouteInfo] = useState([]);

  useEffect(() => {
    const db = getDatabase();
    const semesterref = ref(db, "bus/");
  }, []);

  useEffect(() => {
    const json = sessionStorage.getItem("my-routeInfo");
    const savedrouteInfo = JSON.parse(json);
    if (savedrouteInfo) {
      setRouteInfo(savedrouteInfo);
    }
  }, []);

  useEffect(() => {
    const json = JSON.stringify(routeInfo);
    sessionStorage.setItem("my-routeInfo", json);
  }, [routeInfo]);

  const handlebusName = (event) => {
    setbusName(event.target.value);
  };

  const handlebusStartTime = (event) => {
    setbusStartTime(event.target.value);
  };

  const handleestTime = (event) => {
    setEstTime(event.target.value);
  };

  const handlelatitude = (event) => {
    setLatitude(event.target.value);
  };

  const handlelongitude = (event) => {
    setLongitude(event.target.value);
  };

  const handlemapExactPlaceName = (event) => {
    setMapExactPlaceName(event.target.value);
  };

  const handleplaceName = (event) => {
    setPlaceName(event.target.value);
  };

  let CardNo = 1;

  const handleclick = (event) => {
    event.preventDefault();
    if (
      busName &&
      busStartTime &&
      estTime &&
      latitude &&
      longitude &&
      mapExactPlaceName &&
      placeName
    ) {
      setRouteInfo([
        ...routeInfo,
        {
          busName: busName,
          busStartTime: busStartTime,
          estTime: estTime,
          latitude: latitude,
          longitude: longitude,
          mapExactPlaceName: mapExactPlaceName,
          placeName: placeName,
        },
      ]);
    }
    setbusName("");
    setbusStartTime("");
    setEstTime("");
    setLatitude("");
    setLongitude("");
    setMapExactPlaceName("");
    setPlaceName("");
  };

  let index = 0;
  let Key = 0;

  const deleteRouteInfo = (index) => {
    const new_list = [...routeInfo];
    new_list.splice(index, 1);
    setRouteInfo(new_list);
  };

  const handleSaveRoute = (event) => {
    const db = getDatabase();
    //console.log(routeInfo)

    routeInfo.map((val, key) =>
      onValue(
        ref(db, `bus/${val.busName}/${val.busStartTime}/`),
        (snapshot) => {
          snapshot.forEach((childSnapshot) => {
            Key = childSnapshot.size;
            //console.log(sem);
          });
        }
      )
    );

    routeInfo.map((val, key) =>
      update(
        ref(db, `bus/${val.busName}/${val.busStartTime}/routes/${Key++}`),
        {
          estTime: val.estTime,
          latitude: val.latitude,
          longitude: val.longitude,
          mapPlaceName: val.mapExactPlaceName,
          place: val.placeName,
        },
        { merge: true }
      )
    );

    setRouteInfo([]);
  };

  return (
    <>
      <Appbar />
      <h2 style={{ textAlign: "center", marginTop: "2%" }}>CREATE BUS</h2>

      <Paper elevation={7} className={classes.paperdiv}>
        <FormControl className={classes.busInput} onChange={handlebusName}>
          <OutlinedInput
            value={busName}
            style={{ paddingLeft: "5%" }}
            placeholder="ENTER BUS NAME"
          />
        </FormControl>
        <FormControl className={classes.busInput} onChange={handlebusStartTime}>
          <OutlinedInput
            value={busStartTime}
            style={{ paddingLeft: "5%" }}
            placeholder="ENTER BUS START TIMING"
          />
        </FormControl>

        <hr style={{ width: "90%", margin: "2% auto" }}></hr>

        <div className={classes.busRoutediv}>
          <h2>ROUTES</h2>
          <div className={classes.addRouteInfo}>
            <FormControl
              className={classes.busInput}
              onChange={handlemapExactPlaceName}
            >
              <OutlinedInput
                value={mapExactPlaceName}
                style={{ paddingLeft: "2.5%" }}
                placeholder="ENTER MAP EXACT PLACE NAME"
              />
            </FormControl>

            <FormControl
              className={classes.busInput}
              onChange={handleplaceName}
            >
              <OutlinedInput
                value={placeName}
                style={{ paddingLeft: "2.5%" }}
                placeholder="ENTER PLACE NAME"
              />
            </FormControl>

            <FormControl className={classes.busInput} onChange={handleestTime}>
              <OutlinedInput
                value={estTime}
                style={{ paddingLeft: "2.5%" }}
                placeholder="ENTER EST. TIME"
              />
            </FormControl>

            <div className={classes.buslocationInfo}>
              <FormControl
                className={classes.busInput}
                onChange={handlelatitude}
                style={{ marginRight: "0.5%" }}
              >
                <OutlinedInput
                  value={latitude}
                  style={{ paddingLeft: "5%" }}
                  placeholder="ENTER LATITUDE"
                />
              </FormControl>
              <FormControl
                className={classes.busInput}
                onChange={handlelongitude}
                style={{ marginLeft: "0.5%" }}
              >
                <OutlinedInput
                  value={longitude}
                  style={{ paddingLeft: "5%" }}
                  placeholder="ENTER LONGITUDE"
                />
              </FormControl>
            </div>

            <Button
              variant="contained"
              onClick={handleclick}
              className={classes.button_addRoute}
            >
              ADD ROUTE
            </Button>
          </div>
        </div>

        <hr style={{ width: "90%", margin: "2% auto" }}></hr>
        {routeInfo.map((val, key) => (
          <CardComponent
            CardNo={CardNo++}
            name={val.busName}
            time={val.busStartTime}
            Estimated_time={val.estTime}
            Latitude={val.latitude}
            Longitude={val.longitude}
            Map_Place_Name={val.mapExactPlaceName}
            Place_Name={val.placeName}
            deleteRouteInfo={deleteRouteInfo}
            id={key}
            key={key}
          />
        ))}

        <Button
          variant="contained"
          onClick={handleSaveRoute}
          className={classes.savebutton}
        >
          SAVE ROUTE
        </Button>
      </Paper>
    </>
  );
};

export default CreateBusPage;
