import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "./AuthProvider";
import LoadingPage from "./Loading/LoadingPage.jsx";
export default function ProtectedRoute(){
    const {user,loading} = useAuth();
    if(loading) return <LoadingPage/>
    if(!user) return <Navigate to="/auth" replace></Navigate>
    return (
        <Outlet/>
    )
}