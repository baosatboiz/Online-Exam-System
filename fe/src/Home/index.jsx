import { useEffect, useState } from 'react';
import ExamSchedule from '../ExamSchedule/index.jsx'
import fetchData from '../fetch/fetchData.js';
import LoadingPage from '../Loading/LoadingPage.jsx';
import NavBar from '../NavBar/index.jsx';
import './index.css';
export default function Home(){
    const [schedule,setSchedule] = useState([]);
    const [loading,setLoading] = useState(true);
  const [activeMode, setActiveMode] = useState('PRACTICE');

  const normalizeMode = (value) => (value || 'PRACTICE').toUpperCase();
  const modeOptions = ['PRACTICE', 'REAL'];

    useEffect(()=> {
        fetchData(`/api/exam-schedule`)
            .then(data=>{setSchedule(data); setLoading(false);console.log(data);})
            .catch(error=>console.log(error))
    },[])

  const groupedSchedule = modeOptions.reduce((acc, mode) => {
    acc[mode] = schedule.filter((item) => normalizeMode(item.examMode) === mode);
    return acc;
  }, {});

  const modesToRender = [activeMode];

    if(loading) return <LoadingPage></LoadingPage>
    return (
  <div className="home-dashboard min-vh-100">
     <NavBar></NavBar>
    <div className="container py-3">
        <h2 className="fw-bold mb-2">Danh sách bài thi</h2>

      <div className="mode-filter-panel d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3 mb-4">
        <div className="d-flex flex-wrap gap-2">
          {modeOptions.map((mode) => (
            <button
              key={mode}
              type="button"
              className={`btn btn-sm ${activeMode === mode ? 'btn-dark' : 'btn-outline-dark'}`}
              onClick={() => setActiveMode(mode)}
            >
              {mode} ({groupedSchedule[mode].length})
            </button>
          ))}
        </div>
      </div>

      <div className="mode-section-wrap d-grid gap-4 pb-4">
        {modesToRender.map((mode) => (
          <section key={mode} className={`mode-section mode-${mode.toLowerCase()} p-3 p-md-4`}>
            <div className="d-flex justify-content-between align-items-center mb-3">
              <h4 className="mb-0">{mode} MODE</h4>
              <span className="badge rounded-pill text-bg-light border px-3 py-2">
                {groupedSchedule[mode].length} bài thi
              </span>
            </div>

            {groupedSchedule[mode].length === 0 ? (
              <div className="empty-mode">Chưa có lịch thi cho mode này.</div>
            ) : (
              <div className="d-flex justify-content-center flex-wrap">
                {groupedSchedule[mode].map((s) => (
                  <ExamSchedule key={s.scheduleId} data={s}/>
                ))}
              </div>
            )}
          </section>
        ))}
      </div>
       </div>
       
  </div>
)
}