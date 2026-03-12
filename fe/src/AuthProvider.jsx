import { createContext, useContext, useEffect, useMemo, useState } from "react";
import fetchData from "./fetch/fetchData";

export const AuthContext = createContext();
export function AuthProvider({children}){
    const [user,setUser] = useState(()=>{
        const currentUser = localStorage.getItem('user');
        return currentUser ? JSON.parse(currentUser) : null;
    });
    const [loading,setLoading] = useState(true);
    useEffect(()=>{
       const handle = async()=>{
       try{
        const user = await fetchData('/me');
        console.log(user);
        login(user);}
       catch(e){logout();}
       finally{setLoading(false);}
       }
       handle();
    },[])
    console.log(loading)
    const login = (userData) => {
        if (userData) {
            setUser(userData);
            localStorage.setItem('user', JSON.stringify(userData));
        }
    };

    const logout = () => {
        setUser(null);
        localStorage.removeItem('user');
    };
    const value = useMemo(()=>({
        user:user,
        login,
        logout,
        loading
    }),[user,loading])
    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    )
}
export const useAuth = () => useContext(AuthContext);
