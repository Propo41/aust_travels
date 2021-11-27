import { Navigate, useRoutes } from "react-router-dom";
import Users from "pages/Users";
import HomePage from "pages/HomePage";
import ViewBusPage from "pages/SelectBus";
import Volunteers from "pages/Volunteers";
import SemesterPage from "./pages/Semester";
import DepartmentPage from "./pages/Department";
import CreateBusPage from "./pages/CreateBus";

export default function Router() {
  return useRoutes([
    {
      path: "/",
      element: <HomePage />,
    },
    {
      path: "/users",
      element: <Users />,
    },
    {
      path: "/volunteers",
      element: <Volunteers />,
    },
    {
      path: "/viewbus",
      element: <ViewBusPage />,
    },
    {
      path: "/createbus",
      element: <CreateBusPage />,
    },
    {
      path: "/semester",
      element: <SemesterPage />,
    },
    {
      path: "/department",
      element: <DepartmentPage />,
    },
    { path: "*", element: <Navigate to="/404" replace /> },
  ]);
}
