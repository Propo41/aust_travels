import Router from "./routes";
import GlobalStyles from "./theme/globalStyles";
import firebaseApp from "auth";
import { getDatabase, ref, onValue } from "firebase/database";
import { AuthProvider } from "auth/AuthContext";

const { default: ThemeConfig } = require("./theme");

function App() {
  // const db = getDatabase(app);
  // const starCountRef = ref(db, "users");
  // onValue(starCountRef, (snapshot) => {
  //   const data = snapshot.val();
  //   console.log(data);
  // });

  return (
    <ThemeConfig>
      <GlobalStyles />
      {/* injecting the context into the Router component so 
      that all child components can access firebaseApp */}
      <AuthProvider value={firebaseApp}>
        <Router />
      </AuthProvider>
    </ThemeConfig>
  );
}

export default App;
