import './App.css'
import 'bootstrap/dist/css/bootstrap.min.css';
import ResultPage from './ResultPage/index.jsx'
import { Route, BrowserRouter as Router, Routes } from 'react-router-dom'
import ExamPageWrapper from './ExamPageWrapper.jsx'
import Home from './Home/index.jsx';
import AuthWrapperPage from './AuthWrapperPage.jsx';
import ProtectedRoute from './ProtectedRoute.jsx'
function App() {
  return (
    <div className="">
      <Router>
        <Routes>
      <Route path="/auth" element={<AuthWrapperPage/>}></Route>
      <Route element={<ProtectedRoute/>}>
        <Route path='/' element={<Home/>}/>      
        <Route path="/exam/:attemptId" element={<ExamPageWrapper/>}></Route>
        <Route path="/result/:attemptId" element={<ResultPage/>}></Route>
      </Route>
      </Routes>
      </Router>
    </div>
  )
}

export default App
