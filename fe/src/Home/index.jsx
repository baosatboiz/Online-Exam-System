import { useEffect, useState } from 'react';
import ExamSchedule from '../ExamSchedule/index.jsx'
import fetchData from '../fetch/fetchData.js';
export default function Home(){
    const [schedule,setSchedule] = useState([]);
    const [loading,setLoading] = useState(true);
    useEffect(()=> {
        fetchData(`/api/exam-schedule`)
            .then(data=>{setSchedule(data); setLoading(false);console.log(data);})
            .catch(error=>console.log(error))
    },[])
    if(loading) return <div className="d-flex align-items-center justify-content-center h-100">Loading</div>
    return (
    <div className="min-vh-100 bg-light">
    <div className="container py-3">
          <h2 className="fw-bold text-center">Danh sách bài thi</h2>
           <div className="d-flex justify-content-center flex-wrap">
             {schedule.map(s=>
               <ExamSchedule key={s.scheduleId} data={s}/>
             )}
             </div>
       </div>
  </div>
)
}