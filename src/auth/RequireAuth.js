const { Navigate } = require("react-router");
const { useAuth } = require("./firebaseAuth");

function RequireAuth({ children }) {
  const user = useAuth();

  return user !== null ? children : <Navigate to="/" replace />;
}

export default RequireAuth;
