import { useAuth } from "auth/firebaseAuth";
import { PrivateRouter, PublicRouter } from "routes";
import GlobalStyles from "./theme/globalStyles";

const { default: ThemeConfig } = require("./theme");

function App() {
  const user = useAuth();
  console.log("App.js user:", user);

  return (
    <ThemeConfig>
      <GlobalStyles />
      {user ? <PrivateRouter /> : <PublicRouter />}
    </ThemeConfig>
  );
}

export default App;
