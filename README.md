# Installation

Follow the following steps to install the app:

1. Clone the repository to your local machine, using `git clone git@github.com:Propo41/aust_travels.git`
2. Install dependencies using `yarn install`
3. Run the app using `yarn start`

Contribution Guidelines
------
1. To change the color of items, use the colors defined in `theme.palatte.admin.*` which can be found in the path: `src/theme/palette.js`
2. Only use the material UI library in the app. Refer to: [docs](https://mui.com/)
3. Do not use any .css or module css files. Only use the convention used in material UI docs that uses JSS based styling system. To more about the different method of styling, refer to: [here](https://www.smashingmagazine.com/2020/05/styling-components-react)
4. To create your own custom styles, follow the following steps in the component file:

```js
import { useTheme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";

const useStyles = makeStyles((theme) => ({
  root: {
    flexGrow: 1,
    paddingTop: theme.spacing(1),
    paddingBottom: theme.spacing(1),
    backgroundColor: theme.palette.warning.main,
  },
}));

const Appbar = () => {
  const theme = useTheme();
  const classes = useStyles();

  return <div className={classes.root}>Dummy content</div>;
};
```

## Available Scripts

In the project directory, you can run:

### `yarn start`

Runs the app in the development mode.\
Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

The page will reload if you make edits.\
You will also see any lint errors in the console.

### `yarn test`

Launches the test runner in the interactive watch mode.\
See the section about [running tests](https://facebook.github.io/create-react-app/docs/running-tests) for more information.

### `yarn build`

Builds the app for production to the `build` folder.\
It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.\
Your app is ready to be deployed!

See the section about [deployment](https://facebook.github.io/create-react-app/docs/deployment) for more information.
