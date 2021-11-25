import Router from "./routes";
import GlobalStyles from "./theme/globalStyles";
const { default: ThemeConfig } = require("./theme");

function App() {
  return (
    <ThemeConfig>
      <GlobalStyles />
      <Router />
    </ThemeConfig>
  );
}

export default App;
