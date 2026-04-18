import React, { useState } from 'react';
import './index.css'
import { useNavigate } from 'react-router-dom';
import fetchData from '../fetch/fetchData';
import PaymentModal from './PaymentModal';

export default function ExamSchedule({ data }) {
    const fTime = (s) => s ? new Date(s).toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' }) : '';
    const fDate = (s) => s ? new Date(s).toLocaleDateString('vi-VN') : '';
    const isContinuing = data.userStatus === "IN_PROGRESS";
    const isMiniTest = (data.examMode || '').toUpperCase() === 'MINI_TEST';
    const isReal = (data.examMode || '').toUpperCase() === 'REAL';
    const resolvedPartNumber = Number.isInteger(Number(data.partNumber)) && Number(data.partNumber) > 0
        ? Number(data.partNumber)
        : 1;

    const [showPayment, setShowPayment] = useState(false);
    const [paymentInfo, setPaymentInfo] = useState(null);
    const [isProcessing, setIsProcessing] = useState(false);
    const navigate = useNavigate();

    const navigateToExam = (attemptId) => {
        const miniTestContext = isMiniTest ? { mode: 'MINI_TEST', partNumber: resolvedPartNumber } : null;
        if (miniTestContext) {
            sessionStorage.setItem(`exam-attempt-context:${attemptId}`, JSON.stringify(miniTestContext));
        }
        navigate(`/exam/${attemptId}`, {
            state: miniTestContext ? { miniTestContext } : undefined,
        });
    }

    const handleClick = async () => {
        setIsProcessing(true);
        try {
            const { attemptId } = await fetchData(`/api/exam-attempts`, {
                method: 'POST',
                body: JSON.stringify({ examScheduleId: data.scheduleId })
            });
            navigateToExam(attemptId);
        } catch (err) {
            if (isReal && !isContinuing) {
                try {
                    const regRes = await fetchData(`/api/exam-registration/register`, {
                        method: 'POST',
                        body: JSON.stringify({ scheduleId: data.scheduleId })
                    });
                    setPaymentInfo(regRes.paymentInfo);
                    setShowPayment(true);
                } catch (regErr) {
                    try {
                        const payInfo = await fetchData(`/api/exam-registration/payment-info/${data.scheduleId}`, { method: 'GET' });
                        if (payInfo.paymentStatus === 'PAID' || payInfo.registrationStatus === 'CONFIRMED') {
                            alert("Đã xác nhận thanh toán! Đang vào bài thi...");
                            const { attemptId } = await fetchData(`/api/exam-attempts`, {
                                method: 'POST',
                                body: JSON.stringify({ examScheduleId: data.scheduleId })
                            });
                            navigateToExam(attemptId);
                        } else {
                            setPaymentInfo(payInfo);
                            setShowPayment(true);
                        }
                    } catch (loadErr) {
                        alert(loadErr.message || "Không thể tải thông tin lịch thi, vui lòng thử lại.");
                    }
                }
            } else {
                alert(err.message || "Đã có lỗi xảy ra");
            }
        }
        setIsProcessing(false);
    }

    return (
        <>
            <div className={`card ${isReal ? 'bg-gradient-dark border-gold shadow-lg' : 'free shadow-sm border-0'} rounded-4 position-relative d-flex flex-column align-items-center p-4 m-2 overflow-hidden transition-all`}
                style={{ height: '370px', width: '320px', transition: 'transform 0.2s', cursor: 'pointer' }}
                onMouseEnter={e => e.currentTarget.style.transform = 'translateY(-5px)'}
                onMouseLeave={e => e.currentTarget.style.transform = 'translateY(0px)'}>

                <div className="position-absolute top-0 end-0 m-3">
                    {isReal ? (
                        <span className="badge rounded-pill bg-warning text-dark px-3 py-2 shadow-sm fw-bold border border-warning" style={{ fontSize: '0.75rem', letterSpacing: '1px' }}>
                            PREMIUM
                        </span>
                    ) : (
                        <span className="badge rounded-pill bg-white text-primary px-3 py-2 shadow-sm badge-shimmer fw-bold" style={{ fontSize: '0.7rem' }}>
                            FREE
                        </span>
                    )}
                </div>

                {isMiniTest && (
                    <div className="position-absolute top-0 start-0 m-3">
                        <span className="badge rounded-pill bg-dark text-white px-3 py-2 shadow-sm fw-bold" style={{ fontSize: '0.7rem' }}>
                            PART {resolvedPartNumber}
                        </span>
                    </div>
                )}

                <div className="text-center mt-4 w-100 position-relative z-1">
                    <div className="mb-3">
                        <i className={`bi bi-file-earmark-text display-4 ${isReal ? 'text-warning' : 'text-white'} opacity-75`}></i>
                    </div>

                    <h4 className={`fw-bold ${isReal ? 'text-warning' : 'text-white'} mb-3 text-truncate px-2`} style={{ textShadow: isReal ? '0 2px 4px rgba(0,0,0,0.5)' : 'none' }}>
                        {isMiniTest ? `Part ${resolvedPartNumber}` : data.title}
                    </h4>

                    <div className={`d-flex justify-content-center gap-3 ${isReal ? 'text-light' : 'text-white'} mb-3 opacity-90`}>
                        <small><i className="bi bi-stopwatch me-1"></i>{data.duration}p</small>
                        <small><i className="bi bi-person-check me-1"></i>{data.totalAttempts}</small>
                        <small><i className="bi bi-patch-check me-1"></i>Giải thích</small>
                    </div>

                    {data.openAt && (
                        <div className="d-inline-flex align-items-center bg-black bg-opacity-25 rounded-3 p-2 border border-white border-opacity-25 shadow-sm w-100">
                            <div className="text-start flex-fill">
                                <div style={{ fontSize: '0.65rem' }} className="text-white-50 text-uppercase fw-bold">Mở</div>
                                <div className="text-white small fw-bold">{fTime(data.openAt)} <br /><span className="fw-normal opacity-75">{fDate(data.openAt)}</span></div>
                            </div>
                            <div className="text-white-50 mx-2"><i className="bi bi-arrow-right"></i></div>
                            <div className="text-end flex-fill">
                                <div style={{ fontSize: '0.65rem' }} className="text-white-50 text-uppercase fw-bold">Đóng</div>
                                <div className="text-white small fw-bold">{fTime(data.closeAt)} <br /><span className="fw-normal opacity-75">{fDate(data.closeAt)}</span></div>
                            </div>
                        </div>
                    )}

                    <div className="mt-3">
                        <button onClick={handleClick} disabled={isProcessing} className={`btn ${isProcessing ? 'btn-secondary' : isContinuing ? 'btn-warning' : isReal ? 'btn-warning' : 'btn-light'} rounded-pill px-4 py-2 w-100 fw-bold shadow-sm transition-all`}>
                            {isProcessing ? (
                                <><span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>Đang xử lý</>
                            ) : isContinuing ? (
                                <><i className="bi bi-arrow-right-circle me-2"></i>Tiếp tục</>
                            ) : isReal ? (
                                <><i className="bi bi-shield-lock me-2"></i>Đăng ký thi</>
                            ) : (
                                'Luyện tập ngay'
                            )}
                        </button>
                    </div>
                </div>

                {isReal && (
                    <div className="position-absolute w-100 h-100 top-0 start-0 pointer-events-none" style={{
                        background: 'linear-gradient(135deg, rgba(255,193,7,0.1) 0%, rgba(0,0,0,0) 50%, rgba(255,193,7,0.05) 100%)',
                        zIndex: 0
                    }}></div>
                )}
            </div>

            <PaymentModal
                show={showPayment}
                onClose={() => setShowPayment(false)}
                scheduleId={data.scheduleId}
                paymentInfo={paymentInfo}
                onSuccess={() => {
                    // Payment succeeded, try start exam
                    handleClick();
                }}
            />
        </>
    );
}