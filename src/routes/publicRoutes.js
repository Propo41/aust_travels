import { Navigate, useRoutes } from "react-router-dom";
import SignIn from "pages/SignIn";

export default function Router() {
  return useRoutes([
    {
      path: "/",
      element: <SignIn />,
    },
    { path: "*", element: <Navigate to="/404" replace /> },
  ]);
}
