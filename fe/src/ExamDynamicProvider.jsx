import { createContext, useCallback, useContext, useMemo, useState } from "react";
import { useExam } from "./ExamStaticProvider";
import fetchData from "./fetch/fetchData";
import { useNavigate } from "react-router-dom";

export const SessionContext = createContext();

export const ExamDynamicProvider = ({attemptId,children})=>{
        const {flatSequence,total} = useExam();
        const [currentIndex,setCurrentIndex] = useState(0);
        const [answer,setAnswer] = useState(()=>{
                let res={};
                flatSequence.map(group=>
                    group.questions.map(question=>res={...res,[question.questionId]:question.userChoice})
                )  
                return res;  
            });
        const navigate = useNavigate();
        const chooseAnswer = useCallback(async (choice)=>{
            const data = await fetchData(`/api/exam-attempts/${attemptId}/answer`,{
                method:'POST',
                body: JSON.stringify(choice)
            })
            console.log(data);
                    
        },[attemptId])
        const submit = useCallback(async()=>{
            const data = await fetchData(`/api/exam-attempts/${attemptId}/submit`,{
                method:'PUT'
            })
            console.log(data);
            navigate(`/result/${attemptId}`,{state:{data:data}});
        },[attemptId])
        const jumpTo = useCallback((questionId)=>{
            const index = flatSequence.findIndex(group=>{
                return group.questions.some(q=>q.questionId===questionId);
            })
            console.log(`index ${index}`);
            setCurrentIndex(index);
        },[flatSequence])
        const next = useCallback(()=>setCurrentIndex(i=>i+1),[]);
        const prev = useCallback(()=>setCurrentIndex(i=>i-1),[]);

        const value = useMemo(()=>({
            currentIndex:currentIndex+1,
            currentItem:flatSequence[currentIndex],
            next,
            prev,
            answer,
            setAnswer,
            chooseAnswer,
            jumpTo,
            onSubmit:submit
        }),[currentIndex,flatSequence,answer,chooseAnswer]);
        return <SessionContext.Provider value={value}>
            {children}
        </SessionContext.Provider>
}
export const useSession = ()=>useContext(SessionContext);
