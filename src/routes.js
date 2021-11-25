import { Navigate, useRoutes } from "react-router-dom";
import Users from "./pages/Users";
// layouts
// import DashboardLayout from './layouts/dashboard';
// import LogoOnlyLayout from './layouts/LogoOnlyLayout';
// //
// import Login from './pages/Login';
// import Register from './pages/Register';
// import DashboardApp from './pages/DashboardApp';
// import Products from './pages/Products';
// import Blog from './pages/Blog';
// import User from './pages/User';
// import NotFound from './pages/Page404';

// ----------------------------------------------------------------------

export default function Router() {
  return useRoutes([
    {
      path: "/users",
      element: <Users />,
    },
    // {
    //   path: "/",
    //   element: <LogoOnlyLayout />,
    //   children: [
    //     { path: "login", element: <Login /> },
    //     { path: "register", element: <Register /> },
    //     { path: "404", element: <NotFound /> },
    //     { path: "/", element: <Navigate to="/dashboard" /> },
    //     { path: "*", element: <Navigate to="/404" /> },
    //   ],
    // },
    // { path: "*", element: <Navigate to="/404" replace /> },
  ]);
}
