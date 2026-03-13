import React, { useEffect, useState } from "react";
import { useExam } from "../ExamStaticProvider";
import { useTime } from "../TimeProvider";
import { useSession } from "../ExamDynamicProvider";
import Palete from "../Palete";
import { createPortal } from "react-dom";
const TimeDisplay = React.memo(()=>{
    const {timeLeft} = useTime();
    const beautify = ()=>{
        const hours = Math.floor(timeLeft/3600);
        const minute = Math.floor((timeLeft%3600)/60);
        const second = timeLeft%60;
        return `${hours.toString().padStart(2,'0')}:${minute.toString().padStart(2,'0')}:${second.toString().padStart(2,'0')}`;
    }
    return (
        <span className="text-white">Remaining Time: {beautify()}</span>
    )
})
export default function ExamHeader({}){
    const{currentItem,onSubmit} = useSession();
    const part = currentItem.partType;
    const {title,isReview} = useExam();
    const[palete,setPalete] = useState(false);
    return(
        <header className="fixed-top bg-primary d-flex justify-content-between px-sm-3 px-1 py-1">
            <div className="d-flex gap-1 gap-sm-3 align-items-center p-sm-3 p-sm-1 fs-4 me-2">
                <h5 className="m-0 text-white fw-bold d-none d-sm-inline">{title}</h5>     
                <div className="d-flex flex-column flex-sm-row gap-2 align-items-center">
                <span className="badge rounded-pill bg-white text-primary">{part}</span> 
                <div className="position-relative">
                     <button
                    className="btn btn-sm btn-light text-primary rounded-circle d-flex align-items-center justify-content-center"
                    style={{ width: '32px', height: '32px', transition: 'all 0.2s' }} onClick={() => setPalete(prev => !prev)}>
                    <i className="bi bi-list-ul fs-5"></i>
                    </button>
                    {palete && createPortal(
                        <div style={{ position: 'fixed',top: '65px',left: '50px', zIndex: 9999, }}>
                    <Palete /></div>,
                document.getElementById("portal-root")
            )}
                </div>
                </div>
            </div>
            <div className="d-flex gap-0 gap-sm-4 align-items-center">
                {!isReview?
                <><TimeDisplay/>
                <button onClick={onSubmit} className="btn btn-success pe-0 pe-sm-3">Submit</button>
                </>:
                <p className="fw-bold m-0">Review Mode</p>}
            </div>
        </header>
    )
}