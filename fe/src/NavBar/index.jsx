import { useState } from 'react';
import { Link, NavLink } from 'react-router-dom';
import { useAuth } from '../AuthProvider';

export default function NavBar() {
  const {user} = useAuth();
  const [isOpen, setIsOpen] = useState(false);
  const [drop,setDrop] = useState(false);
  const toggleMenu = () => setIsOpen(!isOpen);

  return (
    <nav className="navbar navbar-expand-lg bg-primary sticky-top" data-bs-theme="dark">
      <div className="container">
        <Link className="navbar-brand fw-bold" to="/"></Link>

        <button 
          className="navbar-toggler" 
          type="button" 
          onClick={toggleMenu}
        >
          <span className="navbar-toggler-icon"></span>
        </button>

        <div className={`collapse navbar-collapse ${isOpen ? 'show' : ''}`}>
          <ul className="navbar-nav me-auto">
            <li className="nav-item">
              <NavLink className="nav-link" to="/exams">Kỳ thi</NavLink>
            </li>
            <li className="nav-item">
              <NavLink className="nav-link" to="/results">Kết quả</NavLink>
            </li>
            <li className="nav-item dropdown">
              <a 
                className="nav-link dropdown-toggle" 
                href="#" 
                role="button" 
                onClick = {()=>setDrop(!drop)}
                aria-expanded="false"
              >
                Quản lý Bài thi
              </a>

              <ul className={`dropdown-menu bg-primary border border-0 ${drop?`show`:``}`} >
                <li><a className="dropdown-item" href="#">Danh sách bài thi</a></li>
                <li><a className="dropdown-item" href="#">Tạo bài thi mới</a></li>
              </ul>
            </li>
            </ul>

          <div className="d-flex align-items-center gap-3"> 
          {user ? (
            <>
              <span className="text-light me-2">Chào, {user.email}</span>
              <img 
                src={user.picture || `avt.jpg`} 
                alt="Profile"
                className="rounded-circle border border-1 border-light" 
                style={{ width: '35px', height: '35px', objectFit: 'cover' }} 
              />
            </>
          ) : (
            <>
              <button className="btn btn-outline-light">Đăng nhập</button>
              <button className="btn btn-light text-primary">Đăng ký</button>
            </>
          )}
          </div>
        </div>
      </div>
    </nav>
  );
}