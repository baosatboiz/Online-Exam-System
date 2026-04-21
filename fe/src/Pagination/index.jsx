import { useSession } from "../ExamDynamicProvider";
import { useExam } from "../ExamStaticProvider";

export default function Pagination() {
  const {currentIndex:current,next:onNext,prev:onPrev} = useSession();
  const {total} = useExam();
  const progress = (current / total) * 100;

  return (
    <div className="fixed-bottom bg-white border-top shadow-lg" >
      <div className="progress rounded-0" style={{ height: "4px" }}>
        <div 
          className="progress-bar bg-primary transition-all" 
          style={{ width: `${progress}%`, transition: "width 0.3s ease" }}
        ></div>
      </div>

      <div className="container d-flex justify-content-between align-items-center py-3 px-4" style={{ maxWidth: "800px" }}>
        
        <button 
          onClick={onPrev}
          disabled={current === 1}
          className="btn btn-outline-secondary rounded-pill px-4 d-flex align-items-center gap-2 border-0"
        >
          <i className="bi bi-arrow-left"></i>
          <span className="d-none d-sm-inline">Previous</span>
        </button>

        <div className="text-center">
          <span className="fw-bold text-primary" style={{ fontSize: "1.1rem" }}>{current}</span>
          <span className="text-muted small"> / {total}</span>
        </div>

        <button 
          onClick={onNext}
          className="btn btn-primary rounded-pill px-4 shadow-sm d-flex align-items-center gap-2"
          disabled={current===total}  
        >
          <span className="fw-bold">Next</span>
          <i className="bi bi-arrow-right"></i>
        </button>

      </div>
    </div>
  );
}