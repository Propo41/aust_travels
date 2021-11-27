import React from "react";
import Toolbar from "@mui/material/Toolbar";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";
import { Link } from "react-router-dom";
import { useTheme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";
import { signOut } from "auth/firebaseAuth";

const useStyles = makeStyles((theme) => ({
  root: {
    flexGrow: 1,
    paddingTop: theme.spacing(1),
    paddingBottom: theme.spacing(1),
    backgroundColor: theme.palette.warning.main,
  },
  title: {
    marginLeft: 60,
    [theme.breakpoints.down("720")]: {
      flexGrow: 1,
    },
  },
  button: {
    marginLeft: 60,
    fontSize: "1.5rem",
    color: "red",
  },
  logoText: {
    color: theme.palette.admin.black,
    textDecoration: "none",
  },
}));

const Appbar = () => {
  const classes = useStyles();

  const onLogoutClick = () => {
    signOut();
    console.log("logging out!");
    window.location.href = "/";
  };

  return (
    <div className={classes.root}>
      <Toolbar style={{ paddingLeft: 0, paddingRight: 0 }}>
        {/* Place brand logo here */}
        <Typography variant="h4" className={classes.title}>
          <Link to="/" className={classes.logoText}>
            Home
          </Link>
        </Typography>

        <Button variant="h4" className={classes.button} onClick={onLogoutClick}>
          Logout
        </Button>
      </Toolbar>
    </div>
  );
};

export default Appbar;
