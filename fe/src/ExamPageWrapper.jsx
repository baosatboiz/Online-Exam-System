import { useEffect, useState } from "react";
import { useParams } from "react-router-dom"
import fetchData from "./fetch/fetchData";
import { ExamStaticProvider } from "./ExamStaticProvider";
import { ExamDynamicProvider } from "./ExamDynamicProvider";
import AppLayout from "./AppLayout";
import LoadingPage from "./Loading/LoadingPage.jsx";
export default function ExamPageWrapper({isReview}){
    const {attemptId} = useParams();
    const [exam,setExam] = useState({});
    const [loading,setLoading] = useState(true);
    useEffect(()=>{
        setLoading(true);
        const endpoint = !isReview?`/api/exam-attempts/${attemptId}/questions`:`/api/exam-attempts/${attemptId}/review`;
        fetchData(endpoint)
        .then(data=>{setExam(data); setLoading(false);})
        .catch(err=>console.log(err))
    },[attemptId,isReview])
    if(loading) return <LoadingPage></LoadingPage>
    return(
        <ExamStaticProvider exam={exam} isReview={isReview}>
            <ExamDynamicProvider attemptId={attemptId}>
        <AppLayout/>
        </ExamDynamicProvider>
        </ExamStaticProvider> 
    )
} 