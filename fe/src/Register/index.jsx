import { Navigate } from "react-router-dom";
import { useAuth } from "../AuthProvider";
import { useState } from "react";
import fetchData from "../fetch/fetchData";

export default function Register({setIsLogin}) {
  const {user,login} = useAuth();
  const [email,setEmail] = useState("");
  const [password,setPassword] = useState("");
  const [confirm,setConfirm] = useState("");
  const handleClick = async (e)=>{
    e.preventDefault();
    if(password!==confirm) {alert("Confirm password do not match"); return;}
    const user = await fetchData("/register",{
      method:"POST",
      body:JSON.stringify({email,password,confirmPassword:confirm})
    })
    login(user);
  }
  if(user) return <Navigate to="/" replace></Navigate>
  return (
    <div className="background d-flex justify-content-center align-items-center vh-100 vw-100">
      <form onSubmit={handleClick}className="login-container p-4 rounded-3 shadow-lg w-100 m-2" style={{maxWidth:"400px"}}>
        <h3 className="text-primary text-center mb-4">
          ONLINE EXAM SYSTEM
        </h3>

        <div className="mb-3">
          <label className="form-label">Email</label>
          <input
            required 
            value={email}
            autoComplete="email"
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
            autoComplete="new-password"
            value={password}
            type="password"
            className="form-control"
            placeholder="Enter your password"
            onChange={(e)=>setPassword(e.target.value)}
          />
        </div>
        <div className="mb-3">
          <label className="form-label">Confirm Password</label>
          <input
            required
            autoComplete="new-password"
            value={confirm}
            type="password"
            className="form-control"
            placeholder="Enter password again"
            onChange={(e)=>setConfirm(e.target.value)}
          />
        </div>

        <button type="submit" className="btn btn-primary w-100 mb-2">
          Register
        </button>

        <p className="text-center mt-3 mb-0">
          Have an account yet?{" "}
          <a onClick={(e)=>{
            e.preventDefault();
            setIsLogin(true);
          }} className="text-decoration-none">
            Login
          </a>
        </p>
      </form>
    </div>
  );
}