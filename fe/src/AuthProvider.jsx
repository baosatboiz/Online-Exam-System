import { createContext, useContext, useMemo, useState } from "react";

export const AuthContext = createContext();
export function AuthProvider({children}){
    const [user,setUser] = useState(()=>{
        const currentUser = localStorage.getItem('user');
        return currentUser ? JSON.parse(currentUser) : null;
    });
    const login = (userData) => {
        if (userData && userData.authToken) {
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
        logout
    }),[user])
    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    )
}
export const useAuth = () => useContext(AuthContext);
