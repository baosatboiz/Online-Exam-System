import { useState } from "react";
import { AuthProvider } from "./AuthProvider";
import LoginForm from "./LoginForm";
import Register from "./Register";

 export default function AuthWrapperPage(){
  const [isLogin,setIsLogin] = useState(true);
  return (
    <div className={`form-wrap ${isLogin?'':'register'}`}>
        <AuthProvider>
        {isLogin?
            <LoginForm setIsLogin={setIsLogin}></LoginForm>
            :<Register  setIsLogin={setIsLogin}/>}
        </AuthProvider>
  </div>
  )
}