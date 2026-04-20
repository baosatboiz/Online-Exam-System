import './App.css'
import 'bootstrap/dist/css/bootstrap.min.css';
import ResultPage from './ResultPage/index.jsx'
import { Route, BrowserRouter as Router, Routes } from 'react-router-dom'
import ExamPageWrapper from './ExamPageWrapper.jsx'
import Home from './Home/index.jsx';
import AuthWrapperPage from './AuthWrapperPage.jsx';
import ProtectedRoute from './ProtectedRoute.jsx'
import { AuthProvider } from './AuthProvider.jsx';
import ManageSchedule from './ManageSchedule/index.jsx';
import MyVocabulary from './MyVocabulary/index.jsx';
function App() {
  return (
    <div className="">
      <Router>
        <AuthProvider>
        <Routes>
      <Route path="/auth" element={<AuthWrapperPage/>}></Route>
      <Route element={<ProtectedRoute/>}>
        <Route path='/' element={<Home/>}/>      
        <Route path="/exam/:attemptId" element={<ExamPageWrapper isReview={false}/>}></Route>
        <Route path="/review/:attemptId" element={<ExamPageWrapper isReview={true}/>}></Route>
        <Route path="/result/:attemptId" element={<ResultPage/>}></Route>
        <Route path="/manage-schedule" element={<ManageSchedule/>}></Route>
        <Route path="/my-vocabulary" element={<MyVocabulary/>}></Route>
      </Route>
      </Routes>
      </AuthProvider>
      </Router>
    </div>
  )
}

export default App
