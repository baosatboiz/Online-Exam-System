import React, { useEffect, useState } from 'react';
import fetchData from '../fetch/fetchData';

export default function PaymentModal({ show, onClose, scheduleId, paymentInfo, onSuccess }) {
    const [status, setStatus] = useState(paymentInfo?.paymentStatus || 'PENDING');

    useEffect(() => {
        if (!show) return;
        
        // Polling every 5 seconds
        const interval = setInterval(async () => {
            try {
                const data = await fetchData(`/api/exam-registration/payment-info/${scheduleId}`, { method: 'GET' });
                if (data.paymentStatus === 'PAID' || data.registrationStatus === 'CONFIRMED') {
                    setStatus('PAID');
                    clearInterval(interval);
                    if (onSuccess) onSuccess();
                }
            } catch (err) {
                console.error("Polling error", err);
            }
        }, 5000);

        return () => clearInterval(interval);
    }, [show, scheduleId, onSuccess]);

    if (!show || !paymentInfo) return null;

    return (
        <div className="modal show d-block" style={{ backgroundColor: 'rgba(0,0,0,0.5)' }} tabIndex="-1">
            <div className="modal-dialog modal-dialog-centered">
                <div className="modal-content border-0 shadow-lg" style={{ borderRadius: '1rem' }}>
                    <div className="modal-header border-0 bg-dark text-white" style={{ borderTopLeftRadius: '1rem', borderTopRightRadius: '1rem' }}>
                        <h5 className="modal-title fw-bold">
                            <i className="bi bi-qr-code-scan me-2"></i>
                            Thanh toán lệ phí thi
                        </h5>
                        <button type="button" className="btn-close btn-close-white" onClick={onClose} aria-label="Close"></button>
                    </div>
                    <div className="modal-body text-center p-4">
                        {status === 'PAID' ? (
                            <div className="py-4">
                                <i className="bi bi-check-circle-fill text-success" style={{ fontSize: '4rem' }}></i>
                                <h4 className="mt-3 fw-bold text-success">Thanh toán thành công!</h4>
                                <p className="text-muted">Bạn có thể bắt đầu làm bài thi ngay bây giờ.</p>
                                <button className="btn btn-dark px-4 py-2 mt-3 fw-bold rounded-pill" onClick={() => {
                                    onClose();
                                    if(onSuccess) onSuccess();
                                }}>
                                    Tiếp tục
                                </button>
                            </div>
                        ) : (
                            <>
                                <p className="mb-4 text-muted">Sử dụng ứng dụng ngân hàng hoặc Momo để quét mã QR bên dưới.</p>
                                <div className="bg-light p-3 rounded-4 mx-auto mb-4" style={{ width: 'fit-content', border: '2px dashed #dee2e6' }}>
                                    <img src={paymentInfo.qrCodeUrl} alt="Payment QR" style={{ width: '250px', height: '250px', objectFit: 'contain' }} />
                                </div>
                                <div className="bg-light rounded-3 p-3 text-start mx-auto" style={{ maxWidth: '300px' }}>
                                    <div className="d-flex justify-content-between mb-2">
                                        <span className="text-muted small">Số tiền:</span>
                                        <span className="fw-bold text-danger">{Number(paymentInfo.amount).toLocaleString('vi-VN')} đ</span>
                                    </div>
                                    <div className="d-flex justify-content-between mb-2">
                                        <span className="text-muted small">Mã đơn hàng:</span>
                                        <span className="fw-bold">{paymentInfo.paymentContent}</span>
                                    </div>
                                    <div className="text-center mt-3">
                                        <div className="spinner-border spinner-border-sm text-primary me-2" role="status">
                                            <span className="visually-hidden">Loading...</span>
                                        </div>
                                        <small className="text-muted">Đang chờ thanh toán...</small>
                                    </div>
                                </div>
                            </>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}
