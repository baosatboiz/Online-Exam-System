import { Navigate, Outlet } from "react-router-dom";

export default function ProtectedRoute(){
    const token = localStorage.getItem('user');
    if(!token) return <Navigate to="/auth" replace></Navigate>
    return (
        <Outlet/>
    )
}