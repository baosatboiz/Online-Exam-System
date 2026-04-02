import { useEffect, useState } from "react";
import fetchData from "../fetch/fetchData";
import "./index.css";

export default function ManageSchedule() {
    const [exams, setExams] = useState([]);
    const [selectedExam, setSelectedExam] = useState("");
    const [openAt, setOpenAt] = useState("");
    const [endAt, setEndAt] = useState("");
    const [mode,setMode]=useState("PRACTICE");
    const [schedule,setSchedule] = useState([]);
    const [activeMode, setActiveMode] = useState("ALL");
    const fTime = (s) => s ? new Date(s).toLocaleTimeString('vi-VN',{ hour: '2-digit', minute: '2-digit' }):'';
    const fDate = (s) => s ? new Date(s).toLocaleDateString('vi-VN') : '';
    const normalizeMode = (value) => (value || "PRACTICE").toUpperCase();
    const modeOptions = ["PRACTICE", "REAL"];
    

    useEffect(() => {
        const fetchExam = async () => {
            const data = await fetchData("/api/exams");
            setExams(data);
        };
        fetchExam();
    }, []);
    useEffect(()=>{
        const fetchSchedule = async ()=>{
            const data = await fetchData("/api/exam-schedule");
            setSchedule(data);
        }
        fetchSchedule();
    },[])
    const handleSubmit = async (e)=>{
        e.preventDefault();
        const data=await fetchData("/api/exam-schedule",{
            "method":"POST",
            "body":JSON.stringify({examId:selectedExam,openAt:new Date(openAt).toISOString(),endAt:new Date(endAt).toISOString(),examMode:mode})
        })
        const newId = data.scheduleId;

        const chosenExam = exams.find(ex => ex.businessId === selectedExam);

        const newScheduleEntry = {
            scheduleId: newId,                 
            title: chosenExam ? chosenExam.title : "N/A", 
            openAt: openAt,                    
            closeAt: endAt,                  
            examMode: mode
        }                  

        setSchedule(prev => [...prev, newScheduleEntry]);

        setSelectedExam("");
        setOpenAt("");
        setEndAt("");
        
        alert("Create schedule successfully")
    }
    const handleDelete = async (id) => {
    if (!window.confirm("Bạn có chắc chắn muốn xóa lịch thi này?")) return;
    try {
        await fetchData(`/api/exam-schedule/${id}`, {
            method: "DELETE"
        });
        setSchedule(prev => prev.filter(item => item.scheduleId !== id));
        alert("Xóa lịch thi thành công!");
    } catch (error) {
        console.error("Delete error:", error);
        alert("Không thể xóa lịch thi này.");
    }
    };

    const groupedSchedule = modeOptions.reduce((acc, currentMode) => {
        acc[currentMode] = schedule.filter((item) => normalizeMode(item.examMode) === currentMode);
        return acc;
    }, {});

    const modesToRender = activeMode === "ALL" ? modeOptions : [activeMode];

    return (
        <div className="manage-schedule-page min-vh-100 p-3 p-md-4">
            <div className="container">
                <div className="manage-card p-4 p-md-5">
                    <div className="d-flex flex-column flex-md-row justify-content-between gap-3 align-items-md-center mb-4">
                        <div>
                            <p className="schedule-eyebrow mb-1">Exam Control Center</p>
                            <h3 className="mb-1">Create Exam Schedule</h3>
                        </div>
                        <div className="d-flex gap-2 flex-wrap">
                            <span className="badge text-bg-light p-2 px-3">All: {schedule.length}</span>
                            <span className="badge text-bg-primary p-2 px-3">Practice: {groupedSchedule.PRACTICE.length}</span>
                            <span className="badge text-bg-warning p-2 px-3">Real: {groupedSchedule.REAL.length}</span>
                        </div>
                    </div>

                    <form onSubmit={handleSubmit} className="row g-3 align-items-end">
                        <div className="col-12 col-lg-6">
                            <label className="form-label">Exam</label>
                            <select
                                className="form-control"
                                value={selectedExam}
                                onChange={(e) => setSelectedExam(e.target.value)}
                                required
                            >
                                <option value="">-- Choose exam --</option>
                                {exams.map((e) => (
                                    <option key={e.businessId} value={e.businessId}>
                                        {e.title}
                                    </option>
                                ))}
                            </select>
                        </div>

                        <div className="col-12 col-md-6 col-lg-3">
                            <label className="form-label">Open at</label>
                            <input
                                type="datetime-local"
                                className="form-control"
                                value={openAt}
                                onChange={(e) => setOpenAt(e.target.value)}
                                required
                            />
                        </div>

                        <div className="col-12 col-md-6 col-lg-3">
                            <label className="form-label">End at</label>
                            <input
                                type="datetime-local"
                                className="form-control"
                                value={endAt}
                                onChange={(e) => setEndAt(e.target.value)}
                                required
                            />
                        </div>

                        <div className="col-12 col-md-6 col-lg-3">
                            <label className="form-label">Mode</label>
                            <select className="form-control" value={mode} onChange={(e)=>setMode(e.target.value)}>
                                <option value="PRACTICE">PRACTICE</option>
                                <option value="REAL">REAL</option>
                            </select>
                        </div>

                        <div className="col-12 col-md-6 col-lg-3">
                            <button className="btn btn-primary w-100">Create</button>
                        </div>
                    </form>

                    <div className="mt-5">
                        <div className="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3 mb-3">
                            <h4 className="mb-0">Available Schedule</h4>
                            <div className="mode-filter d-flex gap-2 flex-wrap">
                                <button
                                    type="button"
                                    className={`btn btn-sm ${activeMode === "ALL" ? "btn-dark" : "btn-outline-dark"}`}
                                    onClick={() => setActiveMode("ALL")}
                                >
                                    ALL
                                </button>
                                {modeOptions.map((itemMode) => (
                                    <button
                                        key={itemMode}
                                        type="button"
                                        className={`btn btn-sm ${activeMode === itemMode ? "btn-dark" : "btn-outline-dark"}`}
                                        onClick={() => setActiveMode(itemMode)}
                                    >
                                        {itemMode}
                                    </button>
                                ))}
                            </div>
                        </div>

                        <div className="mode-groups d-grid gap-4">
                            {modesToRender.map((itemMode) => (
                                <section key={itemMode} className="mode-section p-3 p-md-4">
                                    <div className="d-flex justify-content-between align-items-center mb-3">
                                        <h5 className="mb-0">{itemMode} Mode</h5>
                                        <span className="badge text-bg-secondary">{groupedSchedule[itemMode].length} schedule(s)</span>
                                    </div>

                                    {groupedSchedule[itemMode].length === 0 ? (
                                        <div className="empty-group text-muted">No schedule in this mode yet.</div>
                                    ) : (
                                        <div className="schedule-grid">
                                            {groupedSchedule[itemMode].map((s) => (
                                                <article key={s.scheduleId} className="schedule-item p-3">
                                                    <div className="d-flex justify-content-between gap-3">
                                                        <h6 className="mb-2 text-truncate">{s.title}</h6>
                                                        <span className="badge text-bg-light border">{normalizeMode(s.examMode)}</span>
                                                    </div>

                                                    <p className="mb-2 small text-muted">
                                                        <i className="bi bi-clock me-2"></i>
                                                        {fTime(s.openAt)} {fDate(s.openAt)}
                                                    </p>

                                                    <p className="mb-3 small text-muted">
                                                        <i className="bi bi-stopwatch me-2"></i>
                                                        {fTime(s.closeAt)} {fDate(s.closeAt)}
                                                    </p>

                                                    <button
                                                        type="button"
                                                        className="btn btn-outline-danger btn-sm"
                                                        onClick={() => handleDelete(s.scheduleId)}
                                                    >
                                                        <i className="bi bi-trash me-1"></i>Delete
                                                    </button>
                                                </article>
                                            ))}
                                        </div>
                                    )}
                                </section>
                            ))}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}