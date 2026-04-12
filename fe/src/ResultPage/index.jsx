import { useEffect, useMemo, useState } from "react"
import { useLocation, useNavigate, useParams, useSearchParams } from "react-router-dom"
import fetchData from "../fetch/fetchData"

export default function ResultPage(){
    const location = useLocation();
    const [searchParams] = useSearchParams();
    const stateContext = location.state?.miniTestContext || null;
    const attemptIdParam = useParams().attemptId;
    const storedContext = (() => {
        try {
            return JSON.parse(sessionStorage.getItem(`exam-attempt-context:${attemptIdParam}`) || 'null');
        } catch {
            return null;
        }
    })();
    const fallbackMode = (searchParams.get('mode') || '').toUpperCase();
    const fallbackRawPart = Number(searchParams.get('partNumber'));
    const isMiniTest = (stateContext?.mode || storedContext?.mode || fallbackMode).toUpperCase() === 'MINI_TEST';
    const rawPart = Number(stateContext?.partNumber ?? storedContext?.partNumber ?? fallbackRawPart);
    const partNumber = Number.isInteger(rawPart) && rawPart > 0 ? rawPart : 1;
    const [miniReview, setMiniReview] = useState(null);
    const [loading, setLoading] = useState(isMiniTest);
    const data = location.state?.data || {};
    const {attemptId} = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        if (!isMiniTest) return;
        let isActive = true;
        setLoading(true);
        fetchData(`/api/exam-attempts/${attemptId}/review`)
            .then((payload) => {
                if (!isActive) return;
                const part = payload?.parts?.find((item) => String(item.partType || '').includes(`PART_${partNumber}`));
                setMiniReview(part || null);
            })
            .catch((error) => console.log(error))
            .finally(() => {
                if (isActive) setLoading(false);
            });
        return () => {
            isActive = false;
        };
    }, [attemptId, isMiniTest, partNumber]);

    const miniSummary = useMemo(() => {
        if (!miniReview) return null;
        const questions = miniReview.questionGroups.flatMap((group) => group.questions);
        const totalQuestions = questions.length;
        const correct = questions.filter((question) => question.isCorrect ?? question.userChoice === question.correctChoice).length;
        const wrong = questions.filter((question) => question.userChoice && !(question.isCorrect ?? question.userChoice === question.correctChoice)).length;
        const unanswered = questions.filter((question) => question.userChoice == null).length;
        return { totalQuestions, correct, wrong, unanswered };
    }, [miniReview]);

    const beautify = (x)=>{
        const hours = Math.floor(x/3600);
        const minute = Math.floor((x%3600)/60);
        const second = x%60;
        return `${hours.toString().padStart(2,'0')}:${minute.toString().padStart(2,'0')}:${second.toString().padStart(2,'0')}`;
    }

    if (loading) {
        return <div className="container py-5">Loading...</div>;
    }

    if (isMiniTest && !miniSummary) {
        return <div className="container py-5">Không tải được dữ liệu part cho MINI_TEST.</div>;
    }

    return (
        <div className="bg-light">
        <div className="container py-4">
            <div className="text-center fw-bold">
                <h2>{isMiniTest ? `MINI TEST - Part ${partNumber}` : 'Attempt Score'}</h2>
            {!isMiniTest ? (
                <div className="d-flex align-items-center justify-content-center flex-column border-top border-primary border-5 rounded-circle shadow-lg mx-auto my-4" style={{width:"180px",height:"180px"}}>
                    <span className="fs-1 text-primary">{data.totalScore}</span>
                    <span className="text-muted">/ 990</span>
                </div>
            ) : (
                <div className="card shadow-sm rounded-4 mx-auto my-4 p-4" style={{maxWidth:'420px'}}>
                    <div className="d-flex justify-content-between fw-bold mb-2">
                        <span>Tổng số câu</span>
                        <span>{miniSummary.totalQuestions}</span>
                    </div>
                    <div className="d-flex justify-content-between fw-bold mb-2 text-success">
                        <span>Đúng</span>
                        <span>{miniSummary.correct}</span>
                    </div>
                    <div className="d-flex justify-content-between fw-bold mb-2 text-danger">
                        <span>Sai</span>
                        <span>{miniSummary.wrong}</span>
                    </div>
                    <div className="d-flex justify-content-between fw-bold">
                        <span>Skip</span>
                        <span>{miniSummary.unanswered}</span>
                    </div>
                </div>
            )}
                <p className="ps-4 text-success">{isMiniTest ? 'Kết quả mini test được tính theo part đang làm.' : 'Try hard more, your goal is comming!'}</p>
        </div>
        <div className="row">
            <div className="col-6 col-md-3">
                <div className="bg-white card p-3 text-center fw-bold rounded-3">
                     <i className="bi bi-check-circle-fill text-success fs-3"></i>
                     <span className="mt-2">{isMiniTest ? miniSummary.correct : data.totalCorrect}</span>
                     <span className="mt-2">Correct</span>
                     </div>
            </div>

            <div className="col-6 col-md-3">
                <div className="bg-white card p-3 text-center fw-bold rounded-3">
                     <i className="bi bi-x-circle-fill text-danger fs-3"></i>
                     <span className="mt-2">{isMiniTest ? miniSummary.wrong : data.totalWrong}</span>
                     <span className="mt-2">False</span>
                     </div>
            </div>

            <div className="col-6 col-md-3">
                <div className="bg-white card p-3 text-center fw-bold rounded-3">
                    <i className="bi bi-circle fs-3"></i>
                     <span className="mt-2">{isMiniTest ? miniSummary.unanswered : data.totalUnanswered}</span>
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
        {!isMiniTest && (
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
        )}
        <div className="d-flex gap-3 mt-5">
            <button className="btn btn-outline-primary flex-grow-1 rounded-pill fw-bold p-3" onClick={() => navigate('/')}><i className="bi bi-arrow-left"></i> Back Home</button>
            <button className="btn btn-primary flex-grow-1 rounded-pill fw-bold p-3 "
            onClick={()=>{
                const miniTestContext = isMiniTest ? { mode: 'MINI_TEST', partNumber } : null;
                if (miniTestContext) {
                    sessionStorage.setItem(`exam-attempt-context:${attemptId}`, JSON.stringify(miniTestContext));
                }
                navigate(`/review/${attemptId}`, { state: miniTestContext ? { miniTestContext } : undefined });
            }}>View detail explanation</button>
        </div>
        </div>
        </div>
    )
}