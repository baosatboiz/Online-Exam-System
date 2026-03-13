import './index.css'

export default function QuestionChoice({ label, content, isSelected, onClick, isTrue, isReview }) {
  
  const getButtonClass = () => {
    if (!isReview) {
      return isSelected ? 'btn-primary active' : 'btn-light shadow-sm';
    }

    if (isTrue) return 'btn-success text-white'; 
    if (isSelected && !isTrue) return 'btn-danger text-white'; 
    
    return 'btn-light shadow-sm'; 
  
  };

  const isHighLighted = (isSelected && !isReview) || isTrue || (isReview && isSelected);

  return (
    <div>
      <button 
        disabled={isReview} 
        onClick={onClick} 
        className={`choice-btn rounded-4 btn w-100 d-flex align-items-center mb-2 p-3 gap-3 ${getButtonClass()}`}
      >
        <div 
          className={`d-flex align-items-center justify-content-center rounded-circle ${
            isHighLighted ? 'bg-white text-dark' : 'bg-primary text-white'
          }`} 
          style={{ width: "32px", height: "32px", minWidth: "32px" }}
        >
          {label}
        </div>
        <div className={`text-start ${isHighLighted ? 'text-white' : 'text-dark'}`}>
          {content}
        </div>
      </button>
    </div>
  );
}