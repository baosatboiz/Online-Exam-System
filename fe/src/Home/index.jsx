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
  const [selectedMiniPart, setSelectedMiniPart] = useState(1);

  const normalizeMode = (value) => (value || 'PRACTICE').toUpperCase();
  const modeOptions = ['PRACTICE', 'REAL', 'MINI_TEST'];
  const modeLabel = (mode) => mode.replace('_', ' ');

  const unwrapScheduleItems = (payload) => {
    if (Array.isArray(payload)) return payload;
    if (Array.isArray(payload?.content)) return payload.content;
    if (Array.isArray(payload?.data)) return payload.data;
    return [];
  };

    useEffect(()=> {
      const fetchSchedulesByMode = async () => {
        try {
          const modeResponses = await Promise.all(
            modeOptions.map((mode) => fetchData(`/api/exam-schedule?mode=${mode}`))
          );

          const mergedSchedule = modeResponses.flatMap((res, index) =>
            unwrapScheduleItems(res).map((item) => ({
              ...item,
              examMode: normalizeMode(item.examMode || modeOptions[index]),
            }))
          );

          setSchedule(mergedSchedule);
        } catch (error) {
          console.log(error);
          setSchedule([]);
        } finally {
          setLoading(false);
        }
      };

      fetchSchedulesByMode();
    },[])

  const groupedSchedule = modeOptions.reduce((acc, mode) => {
    acc[mode] = schedule.filter((item) => normalizeMode(item.examMode) === mode);
    return acc;
  }, {});

  const miniPartCards = Array.from({ length: 7 }, (_, index) => {
    const partNumber = index + 1;
    const count = groupedSchedule.MINI_TEST.filter(
      (item) => Number(item.partNumber) === partNumber
    ).length;
    return { partNumber, count };
  });

  const selectedMiniSchedules = groupedSchedule.MINI_TEST
    .filter((item) => Number(item.partNumber) === selectedMiniPart);

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
              {modeLabel(mode)} ({groupedSchedule[mode].length})
            </button>
          ))}
        </div>
      </div>

      <div className="mode-section-wrap d-grid gap-4 pb-4">
        {modesToRender.map((mode) => (
          <section key={mode} className={`mode-section mode-${mode.toLowerCase()} p-3 p-md-4`}>
            <div className="d-flex justify-content-between align-items-center mb-3">
              <h4 className="mb-0">{modeLabel(mode)} MODE</h4>
              <span className="badge rounded-pill text-bg-light border px-3 py-2">
                {mode === 'MINI_TEST' ? groupedSchedule[mode].length : groupedSchedule[mode].length} bài thi
              </span>
            </div>

            {groupedSchedule[mode].length === 0 ? (
              <div className="empty-mode">Chưa có lịch thi cho mode này.</div>
            ) : mode === 'MINI_TEST' ? (
              <div className="d-grid gap-4">
                <div>
                  <h5 className="fw-bold mb-3">Chọn Part</h5>
                  <div className="row row-cols-2 row-cols-md-4 row-cols-xl-7 g-2">
                    {miniPartCards.map((part) => (
                      <div key={`mini-part-card-${part.partNumber}`} className="col">
                        <button
                          type="button"
                          className={`btn w-100 py-3 fw-bold ${selectedMiniPart === part.partNumber ? 'btn-dark' : 'btn-outline-dark'}`}
                          onClick={() => setSelectedMiniPart(part.partNumber)}
                        >
                          <div>Part {part.partNumber}</div>
                          <small>{part.count} bài</small>
                        </button>
                      </div>
                    ))}
                  </div>
                </div>

                <div>
                  <div className="d-flex justify-content-between align-items-center mb-3">
                    <h5 className="mb-0 fw-bold">Danh sách bài Part {selectedMiniPart}</h5>
                    <span className="badge rounded-pill text-bg-light border px-3 py-2">
                      {selectedMiniSchedules.length} bài thi
                    </span>
                  </div>

                  {selectedMiniSchedules.length === 0 ? (
                    <div className="empty-mode">Part {selectedMiniPart} chưa có bài thi.</div>
                  ) : (
                    <div className="d-flex justify-content-center flex-wrap">
                      {selectedMiniSchedules.map((s) => (
                        <ExamSchedule key={s.scheduleId} data={s}/>
                      ))}
                    </div>
                  )}
                </div>
              </div>
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