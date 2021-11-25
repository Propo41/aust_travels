import React from "react";
import Toolbar from "@mui/material/Toolbar";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";
import { Link } from "react-router-dom";
import { useTheme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";

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
  logoText: {
    color: theme.palette.admin.black,
    textDecoration: "none",
  },
}));

const Appbar = () => {
  const theme = useTheme();
  const classes = useStyles();
  // const mobileViewBreakpoint = useMediaQuery("(max-width: 599px)");

  return (
    <div className={classes.root}>
      <Toolbar style={{ paddingLeft: 0, paddingRight: 0 }}>
        {/* Place brand logo here */}
        <Typography variant="h4" className={classes.title}>
          <Link to="/" className={classes.logoText}>
            Home
          </Link>
        </Typography>
      </Toolbar>
    </div>
  );
};

export default Appbar;
