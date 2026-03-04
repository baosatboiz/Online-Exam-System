import { useState } from "react";
import QuestionChoice from "../QuestionChoice";
import { useSession } from "../ExamDynamicProvider";
export default function QuestionGroup (){
    const {currentItem,answer,setAnswer,chooseAnswer} = useSession();
    const questionData = currentItem.questions;
    const handleClick = ({questionId,key})=>{
        setAnswer(prev=>({...prev,[questionId]:key}))
        chooseAnswer({questionId,choiceKey:key})
    }
    return (
        <div>
        {questionData&&questionData.map(({content,choices,questionId},index)=>(
            <div key={questionId}>
                <p className="fw-bold p-3">{content}</p>
                {choices.map(({key,content},i)=>(
                    <QuestionChoice 
                    key={key}
                    label={key} 
                    content={content} 
                    isSelected={answer[questionId]===key}
                    onClick={()=>handleClick({questionId,key})}></QuestionChoice>
                ))}
            </div>
        ))}
        </div>
    )
}