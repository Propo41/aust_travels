import { useAuth } from "auth/firebaseAuth";
import { PrivateRouter, PublicRouter } from "routes";
import GlobalStyles from "./theme/globalStyles";

const { default: ThemeConfig } = require("./theme");

function App() {
  const user = useAuth();

  return (
    <ThemeConfig>
      <GlobalStyles />
      {user ? <PrivateRouter /> : <PublicRouter />}
    </ThemeConfig>
  );
}

export default App;
