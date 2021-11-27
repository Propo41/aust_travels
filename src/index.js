import React from "react";
import ReactDOM from "react-dom";
import App from "./App";
import { HelmetProvider } from "react-helmet-async";
import { BrowserRouter } from "react-router-dom";
import { AuthContextProvider } from "auth/firebaseAuth";
import config from "auth/config";
import { initializeApp } from "@firebase/app";

initializeApp(config);

ReactDOM.render(
  <HelmetProvider>
    <BrowserRouter>
      <AuthContextProvider>
        <App />
      </AuthContextProvider>
    </BrowserRouter>
  </HelmetProvider>,
  document.getElementById("root")
);
