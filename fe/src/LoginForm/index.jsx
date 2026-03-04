import { useState } from "react";
import { useAuth } from "../AuthProvider";
import "./index.css";
import fetchData from "../fetch/fetchData";
import { Navigate } from "react-router-dom";

export default function LoginForm({setIsLogin}) {
  const {user,login} = useAuth();
  const [email,setEmail] = useState("");
  const [password,setPassword] = useState("");
  const handleClick = async (e)=>{
    e.preventDefault();
    const user = await fetchData('/login',{
      method:"POST",
      body: JSON.stringify({email,password})
    });
    login(user);
  }
  if(user) return <Navigate to="/" replace></Navigate>
  return (
    <div className="background d-flex justify-content-center align-items-center vh-100 vw-100">
      <form onSubmit={handleClick} className="login-container p-4 rounded-3 shadow-lg w-100 m-2" style={{maxWidth:"400px"}}>
        <h3 className="text-primary text-center mb-4">
          ONLINE EXAM SYSTEM
        </h3>

        <div className="mb-3">
          <label className="form-label">Email</label>
          <input
            required
            autoComplete="email"
            value ={email}
            type="email"
            className="form-control"
            placeholder="Enter your email"
            onChange={(e)=>setEmail(e.target.value)}
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Password</label>
          <input
            required
            autoComplete="current-password"
            value ={password}
            type="password"
            className="form-control"
            placeholder="Enter your password"
            onChange={(e)=>setPassword(e.target.value)}
          />
        </div>

        <button type="submit" className="btn btn-primary w-100 mb-3">
          Login
        </button>

        <div className="text-center">
          <a href="#" className="text-decoration-none">
            Forgot password?
          </a>
        </div>

        <p className="text-center mt-3 mb-0">
          Don't have an account?{" "}
          <a onClick={(e)=>{e.preventDefault(); setIsLogin(false)}} className="text-decoration-none">
            Register
          </a>
        </p>
      </form>
    </div>
  );
}