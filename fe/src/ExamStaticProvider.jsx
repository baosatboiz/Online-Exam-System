import { createContext, useContext, useMemo, useState } from "react";

export const ExamStaticContext = createContext();

export function ExamStaticProvider({exam,children}){
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
    const value = useMemo(()=>({
        flatSequence,
        title:exam.title,
        total:flatSequence.length,
        duration:exam.durationMinutes*60
    }),[exam,flatSequence])
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