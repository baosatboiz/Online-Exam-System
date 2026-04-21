import React from "react";
import { useExam } from "../ExamStaticProvider";
import { useSession } from "../ExamDynamicProvider";
const Square = React.memo(({isCurrent,isDone,num,onClick,isCorrect,isEmpty})=>{
    let btnClass = "btn-outline-secondary";
    if(isDone) btnClass = "btn-success";
    if(isCorrect!==undefined) {
      if(isCorrect) btnClass="btn-success";
       else if(isEmpty)  btnClass = "btn-outline-secondary";
        else btnClass="btn-danger";
    }
    const activeStyle = isCurrent 
        ? { border: "1px solid #4a4b4d" } 
        : { border: "1px solid #dee2e6" };
    return (
    <button onClick={onClick} className={`text-center w-100 btn ${btnClass}`} style={{aspectRatio: "1/1",...activeStyle}}>
      {num}
    </button>
    )
})
export default function Palete(){
    const {flatSequence,isReview} = useExam();
    const {answer,currentItem,jumpTo} = useSession();
    const question = React.useMemo(()=>{
      let res=[];
      flatSequence.forEach(group=>group.questions.forEach(q=>res.push(q)));
      return res;
    },[flatSequence]);
    const listId = React.useMemo(()=>
      new Set(currentItem.questions.map(q=>q.questionId)),
    [currentItem]
    )
    return (
        <div className="container mt-2 py-2 border border-secondary bg-white shadow-sm" style={{maxWidth:"340px"}}>
            <div className="row row-cols-5 g-2 overflow-auto" style={{maxHeight:"400px"}}>
           {question.map((q,num)=>{
            const isCurrent = !!listId.has(q.questionId);
            const isDone = !!answer[q.questionId];
            const isCorrect = q?.isCorrect;
            const isEmpty = q?.userChoice===null;
            return (
            <div className="col" key={q.questionId} >
                <Square 
                num={num+1} 
                isCurrent={isCurrent}
                isDone={isDone}
                isCorrect = {isCorrect}
                isEmpty={isEmpty}
                onClick={()=>jumpTo(q.questionId)}
                ></Square>

                </div>)
})}
        </div>
        <div className="mt-3 pt-2 border-top d-flex gap-3 small text-muted">
        <div className="d-flex align-items-center gap-1">
          <span className="badge bg-success" style={{ width: "12px", height: "12px", padding: 0 }}> </span> 
          <span>{isReview?'Correct':'Done'}</span>
        </div>
        <div className="d-flex align-items-center gap-1">
          <span className="badge bg-white border border-secondary" style={{ width: "12px", height: "12px", padding: 0 }}> </span> 
          <span>Empty</span>
        </div>
        {isReview&&<div className="d-flex align-items-center gap-1">
          <span className="badge bg-danger border border-secondary" style={{ width: "12px", height: "12px", padding: 0 }}> </span> 
          <span>Incorrect</span>
        </div>}
      </div>
        </div>
    )
}