import { useState } from "react";
import QuestionChoice from "../QuestionChoice";
import { useSession } from "../ExamDynamicProvider";
import { useExam } from "../ExamStaticProvider";
export default function QuestionGroup (){
    const {currentItem,answer,setAnswer,chooseAnswer} = useSession();
    const {isReview} = useExam();
    const questionData = currentItem.questions;
    const handleClick = ({questionId,key})=>{
        setAnswer(prev=>({...prev,[questionId]:key}))
        chooseAnswer({questionId,choiceKey:key})
    }
    return (
        <div>
        {questionData&&questionData.map(({content,choices,questionId,correctChoice,explanation},index)=>(
            <div key={questionId}>
                <p className="fw-bold p-3">{content}</p>
                {choices.map(({key,content},i)=>(
                    <QuestionChoice 
                    key={key}
                    label={key} 
                    content={content} 
                    isSelected={answer[questionId]===key}
                    isTrue={isReview&&correctChoice===key}
                    isReview={isReview}
                    onClick={()=>handleClick({questionId,key})}></QuestionChoice>
                ))}
                {isReview && explanation && (
                <div className="mt-4 shadow-sm border rounded">
                    <div className="bg-info text-white px-3 py-2 rounded-top small fw-bold">
                        HƯỚNG DẪN GIẢI CHI TIẾT
                    </div>
                    <div 
                        className="p-3 bg-white text-dark" 
                        style={{ 
                            whiteSpace: 'pre-wrap', 
                            lineHeight: '1.6',
                            fontFamily: 'inherit'
                        }}
                    >
                        {explanation}
                    </div>
                </div>
            )}
            </div>
        ))}
        </div>
    )
}