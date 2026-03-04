import { createContext, useContext, useEffect, useState } from "react";
import { useExam } from "./ExamStaticProvider";

export const TimeContext = createContext();

export const TimeProvider = ({onTimeUp,children})=>{
    const {duration} = useExam();
    const [timeLeft,setTimeLeft] = useState(duration);
    useEffect(()=>{
            if(!duration) return;
            const timer = setInterval(()=>
                setTimeLeft(prev=>{
                    if(prev<=1){
                        clearInterval(timer);
                        onTimeUp();
                        return 0;
                    }
                    return prev-1;
                })
            ,1000)
            return ()=>clearInterval(timer);
        },[onTimeUp])
        const value ={
            timeLeft
        }
        return <TimeContext.Provider value={value} >
            {children}
        </TimeContext.Provider>
}
export const useTime = ()=> useContext(TimeContext);