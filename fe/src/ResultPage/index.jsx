import { useLocation } from "react-router-dom"

const data={
    score:770,
    correct:150,
    false:50,
    skip:10,
    time:115,
    listening:370,
    reading:400
}
export default function ResultPage(){
    const location = useLocation();
    const {data} = location.state;
    const beautify = (x)=>{
        const hours = Math.floor(x/3600);
        const minute = Math.floor((x%3600)/60);
        const second = x%60;
        return `${hours.toString().padStart(2,'0')}:${minute.toString().padStart(2,'0')}:${second.toString().padStart(2,'0')}`;
    }
    return (
        <div className="bg-light">
        <div className="container py-4">
            <div className="text-center fw-bold">
                <h2>Attempt Score</h2>
            <div className="d-flex align-items-center justify-content-center flex-column border-top border-primary border-5 rounded-circle shadow-lg mx-auto my-4" style={{width:"180px",height:"180px"}}>
                <span className="fs-1 text-primary">{data.totalScore}</span>
                <span className="text-muted">/ 990</span>
            </div>
                <p className="ps-4 text-success">Try hard more, your goal is comming!</p>
        </div>
        <div className="row">
            <div className="col-6 col-md-3">
                <div className="bg-white card p-3 text-center fw-bold rounded-3">
                     <i className="bi bi-check-circle-fill text-success fs-3"></i>
                     <span className="mt-2">{data.totalCorrect}</span>
                     <span className="mt-2">Correct</span>
                     </div>
            </div>

            <div className="col-6 col-md-3">
                <div className="bg-white card p-3 text-center fw-bold rounded-3">
                     <i className="bi bi-x-circle-fill text-danger fs-3"></i>
                     <span className="mt-2">{data.totalWrong}</span>
                     <span className="mt-2">False</span>
                     </div>
            </div>

            <div className="col-6 col-md-3">
                <div className="bg-white card p-3 text-center fw-bold rounded-3">
                    <i className="bi bi-circle fs-3"></i>
                     <span className="mt-2">{data.totalUnanswered}</span>
                     <span className="mt-2">Skipped</span>
                     </div>
            </div>

            <div className="col-6 col-md-3">
                <div className="bg-white card p-3 text-center fw-bold rounded-3">
                     <i class="bi bi-clock-history text-primary fs-3"></i>
                     <span className="mt-2">{beautify(data.totalTimeSeconds)}</span>
                     <span className="mt-2">Time</span>
                     </div>
            </div>
        </div>
        <div className="card mt-3 p-4">
            <h5 className="fw-bold mb-4">Skill Analysis</h5>
            <div className="d-flex fw-bold fs-5 justify-content-between">
                <span>Listening</span>
                <span>{data.listeningCorrect}/495</span>
            </div>
            <div className="progress" style={{height:"12px"}}>
                <div className="progress-bar bg-primary" style={{ width: `${(data.listeningCorrect / 495) * 100}%` }}></div>
            </div>
            <div className="d-flex fw-bold fs-5 justify-content-between mt-4">
                <span>Reading</span>
                <span>{data.readingCorrect}/495</span>
            </div>
            <div className="progress" style={{height:"12px"}}>
                <div className="progress-bar bg-primary" style={{ width: `${(data.readingCorrect / 495) * 100}%` }}></div>
            </div>
        </div>
        <div className="d-flex gap-3 mt-5">
            <button className="btn btn-outline-primary flex-grow-1 rounded-pill fw-bold p-3"><i class="bi bi-arrow-left"></i> Back Home</button>
            <button className="btn btn-primary flex-grow-1 rounded-pill fw-bold p-3">View detail explanation</button>
        </div>
        </div>
        </div>
    )
}