import { useEffect, useState } from "react";
import fetchData from "../fetch/fetchData";

export default function ManageSchedule() {
    const [exams, setExams] = useState([]);
    const [selectedExam, setSelectedExam] = useState("");
    const [openAt, setOpenAt] = useState("");
    const [endAt, setEndAt] = useState("");
    const [mode,setMode]=useState("PRACTICE");
    const [schedule,setSchedule] = useState([]);
    const fTime = (s) => s ? new Date(s).toLocaleTimeString('vi-VN',{ hour: '2-digit', minute: '2-digit' }):'';
    const fDate = (s) => s ? new Date(s).toLocaleDateString('vi-VN') : '';
    

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
            "body":JSON.stringify({examId:selectedExam,openAt:openAt,endAt:endAt,examMode:mode})
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
        const response = await fetchData(`/api/exam-schedule/${id}`, {
            method: "DELETE"
        });
        setSchedule(prev => prev.filter(item => item.scheduleId !== id));
        alert("Xóa lịch thi thành công!");
    } catch (error) {
        console.error("Delete error:", error);
        alert("Không thể xóa lịch thi này.");
    }
    };
    return (
        <div className="min-vh-100 bg-white p-3">
            <div className="container">
                <div className="row bg-light p-4 m-2">
                <h3>Create Exam Schedule</h3>

                <form onSubmit={handleSubmit} >
                    <div className="mb-3">
                        <label>Exam</label>
                        <select
                            className="form-control"
                            value={selectedExam}
                            onChange={(e) => {setSelectedExam(e.target.value); console.log(e.target.value);}}
                        >
                            <option value="">-- Choose exam --</option>
                            {exams.map((e) => (
                                <option key={e.businessId} value={e.businessId}>
                                    {e.title}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="mb-3">
                        <label>Open at</label>
                        <input
                            type="datetime-local"
                            className="form-control"
                            value={openAt}
                            onChange={(e) => setOpenAt(e.target.value)}
                        />
                    </div>

                    <div className="mb-3">
                        <label>End at</label>
                        <input
                            type="datetime-local"
                            className="form-control"
                            value={endAt}
                            onChange={(e) => setEndAt(e.target.value)}
                        />
                    </div>
                    <div className="mb-3">
                        <label>Mode</label>
                    <select className="form-control" value={mode} onChange={(e)=>setMode(e.target.value)}>
                        <option value="PRACTICE">PRACTICE</option>
                        <option value="REAL">REAL</option>
                    </select>
                    </div>

                    <button className="btn btn-primary">Create</button>
                </form>
                <div className="mt-3">
                        <h3>Available Schedule</h3>
                        <table className="table table-bordered">
                            <thead>
                                <tr>
                                    <th>Title</th>
                                    <th>Open at</th>
                                    <th>End At</th>
                                    <th>Mode</th>
                                    <th>Action</th>
                                </tr>
                                {schedule.map(s=>
                                    <tr key={s.scheduleId}>
                                        <td>{s.title}</td>
                                        <td>{fTime(s.openAt)} {fDate(s.openAt)}</td>
                                        <td>{fTime(s.closeAt)} {fDate(s.closeAt)}</td>
                                        <td>{s.examMode}</td>
                                        <td>
                                            <button className="btn btn-danger"
                                                onClick={()=>handleDelete(s.scheduleId)}
                                            ><i className="bi bi-trash"></i></button></td>
                                    </tr>
                                )}
                            </thead>
                        </table>
                    </div>
            </div>
        </div>
        </div>
    );
}