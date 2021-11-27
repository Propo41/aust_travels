import React, { useEffect, useState, useContext } from "react";
import { getAuth, onAuthStateChanged } from "firebase/auth";

const AuthContext = React.createContext(null);

const AuthContextProvider = ({ children }) => {
  const [user, setUser] = useState(() => {
    const user = getAuth().currentUser;

    return user;
  });

  useEffect(() => {
    const auth = getAuth();
    onAuthStateChanged(auth, (user) => {
      if (user) {
        setUser(user);
      } else {
        setUser(null);
        console.log("user is null");
      }
    });
  }, []);

  return <AuthContext.Provider value={user}>{children}</AuthContext.Provider>;
};

const useAuth = () => {
  const user = useContext(AuthContext);
  return user;
};

const signOut = () => {
  return getAuth().signOut();
};

export { signOut, AuthContextProvider, useAuth };
