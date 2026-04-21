import { createContext, useContext, useMemo, useState } from "react";

export const ExamStaticContext = createContext();

export function ExamStaticProvider({exam,children,isReview}){
    const flatSequence = useMemo(()=>{
        if(!exam) return [];
        let sequence =[];
        exam.parts.forEach(p=>
            p.questionGroups.forEach(
                q=>sequence.push(
                    {...q,partType:p.partType}))
        )
        return sequence;
    },[exam])
    console.log(flatSequence);
    const value = useMemo(()=>({
        flatSequence,
        title:exam.title,
        total:flatSequence.length,
        duration:exam.durationMinutes * 60,
        isReview
    }),[exam,flatSequence,isReview])
       return(
        <ExamStaticContext.Provider value={value}>
            {children}
        </ExamStaticContext.Provider>
       ) 
}
export function useExam(){
    const context = useContext(ExamStaticContext);
    if(!context) throw new Error("Hook need using in ExamProvider");
    return context;
}