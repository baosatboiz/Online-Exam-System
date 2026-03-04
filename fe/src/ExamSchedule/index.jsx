import React, { useState } from 'react';
import './index.css'
import { useNavigate } from 'react-router-dom';
import fetchData from '../fetch/fetchData';
export default function ExamSchedule({ data = mockdata }) {
    const fTime = (s) => s ? new Date(s).toLocaleTimeString('vi-VN',{ hour: '2-digit', minute: '2-digit' }):'';
    const fDate = (s) => s ? new Date(s).toLocaleDateString('vi-VN') : '';
    const isContinuing = data.userStatus === "IN_PROGRESS";
    const navigate = useNavigate();
    const handleClick = async()=>{
        try{
        const {attemptId} = await fetchData(`/api/exam-attempts`,{
            method:'POST',
            body:JSON.stringify({examScheduleId:data.scheduleId})
        })
        navigate(`/exam/${attemptId}`);
    }
        catch(err){
            console.log(err);
        }
    }
    return (
        <div className="card free rounded-4 shadow-sm position-relative d-flex flex-column align-items-center p-4 m-2 border-0" 
             style={{ height: '350px', width: '320px' }}>
            
            <div className="position-absolute top-0 end-0 m-3">
                <span className="badge rounded-pill bg-white text-primary px-3 py-2 shadow-sm badge-shimmer fw-bold" style={{fontSize: '0.7rem'}}>
                    FREE
                </span>
            </div>

            <div className="text-center mt-3 w-100">
                <div className="mb-3">
                    <i className="bi bi-file-earmark-text display-4 text-white opacity-75"></i>
                </div>
                
                <h4 className="fw-bold text-white mb-3 text-truncate px-2">{data.title}</h4>

                <div className="d-flex justify-content-center gap-3 text-white mb-3 opacity-90">
                    <small><i className="bi bi-stopwatch me-1"></i>{data.duration}p</small>
                    <small><i className="bi bi-person-check me-1"></i>{data.totalAttempts}</small>
                    <small><i className="bi bi-patch-check me-1"></i>Giải thích</small>
                </div>

                {data.openAt && (
                    <div className="d-inline-flex align-items-center bg-black bg-opacity-10 rounded-3 p-2 border border-white border-opacity-10">
                        <div className="text-start">
                            <div style={{fontSize: '0.7rem'}} className="text-white-50 text-uppercase">Bắt đầu</div>
                            <div className="text-white fw-bold">{fTime(data.openAt)} <br/><small className="fw-normal opacity-75">{fDate(data.openAt)}</small></div>
                        </div>
                        <div className="text-white-50 mx-1"><i className="bi bi-arrow-right"></i></div>
                        <div className="text-end">
                            <div style={{fontSize: '0.7rem'}} className="text-white-50 text-uppercase">Kết thúc</div>
                            <div className="text-white fw-bold">{fTime(data.closeAt)} <br/><small className="fw-normal opacity-75">{fDate(data.closeAt)}</small></div>
                        </div>
                    </div>
                )}

                <button onClick={handleClick} className={`btn ${isContinuing ? 'btn-warning' : 'btn-light'} rounded-pill px-4 fw-bold shadow-sm mt-2`}>
                    {isContinuing ? (
                        <><i className="bi bi-arrow-right-circle me-2"></i>Tiếp tục</>
                    ) : (
                        'Luyện tập ngay'
                    )}
                </button>
            </div>
        </div>
    );
}