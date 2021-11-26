import { Navigate, useRoutes } from "react-router-dom";
import Users from "./pages/Users";
import HomePage from "./pages/HomePage";
import ViewBusPage from "./pages/SelectBus";
import Volunteers from "pages/Volunteers";
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
    {
      path: "/volunteers",
      element: <Volunteers />,
    },
    {
      path: "/",
      element: <HomePage />,
    },
    {
      path: "/viewbus",
      element: <ViewBusPage />,
    },

    { path: "*", element: <Navigate to="/404" replace /> },
  ]);
}
