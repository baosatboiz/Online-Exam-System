import { useEffect, useState } from "react";
import { useLocation, useParams, useSearchParams } from "react-router-dom"
import fetchData from "./fetch/fetchData";
import { ExamStaticProvider } from "./ExamStaticProvider";
import { ExamDynamicProvider } from "./ExamDynamicProvider";
import AppLayout from "./AppLayout";
import LoadingPage from "./Loading/LoadingPage.jsx";
export default function ExamPageWrapper({isReview}){
    const {attemptId} = useParams();
    const location = useLocation();
    const [searchParams] = useSearchParams();
    const [exam,setExam] = useState({});
    const [loading,setLoading] = useState(true);
    const stateContext = location.state?.miniTestContext || null;
    const storedContext = (() => {
        try {
            return JSON.parse(sessionStorage.getItem(`exam-attempt-context:${attemptId}`) || 'null');
        } catch {
            return null;
        }
    })();
    const fallbackMode = (searchParams.get('mode') || '').toUpperCase();
    const fallbackRawPart = Number(searchParams.get('partNumber'));
    const mode = (stateContext?.mode || storedContext?.mode || fallbackMode).toUpperCase();
    const rawPart = Number(stateContext?.partNumber ?? storedContext?.partNumber ?? fallbackRawPart);
    const partNumber = Number.isInteger(rawPart) && rawPart > 0 ? rawPart : 1;

    const filterMiniTestReview = (payload) => {
        if (!payload?.parts || mode !== 'MINI_TEST') return payload;
        const part = payload.parts.find((item) => String(item.partType || '').includes(`PART_${partNumber}`));
        return {
            ...payload,
            parts: part ? [part] : [],
        };
    };

    useEffect(()=>{
        setLoading(true);
        const endpoint = isReview
            ? `/api/exam-attempts/${attemptId}/review`
            : mode === 'MINI_TEST'
                ? `/api/exam-attempts/${attemptId}/part-questions?partNumber=${partNumber}`
                : `/api/exam-attempts/${attemptId}/questions`;
        fetchData(endpoint)
        .then(data=>{setExam(isReview ? filterMiniTestReview(data) : data); setLoading(false);})
        .catch(err=>console.log(err))
    },[attemptId,isReview,mode,partNumber])
    if(loading) return <LoadingPage></LoadingPage>
    return(
        <ExamStaticProvider exam={exam} isReview={isReview}>
            <ExamDynamicProvider attemptId={attemptId} mode={mode} partNumber={partNumber}>
        <AppLayout/>
        </ExamDynamicProvider>
        </ExamStaticProvider> 
    )
} 