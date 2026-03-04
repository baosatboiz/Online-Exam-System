import './index.css'
export default function QuestionChoice({label,content,isSelected,onClick}) {
  return (
    <div>
      <button onClick ={onClick} className={`choice-btn rounded-4 btn w-100 d-flex align-items-center mb-2 p-3 gap-3 ${isSelected? 'btn-primary active' : 'btn-light shadow-sm'}`}>
        <div className={`d-flex align-items-center justify-content-center rounded-circle ${isSelected?'bg-white text-primary':'bg-primary text-white'}`} style={{width:"32px",height:"32px"}}>{label}</div>
        <div className={`${isSelected?'text-white':'text-dark'}`}>{content}</div>
      </button>
    </div>
)}