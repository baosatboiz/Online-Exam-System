import React, { useEffect, useState } from "react";
import { useExam } from "../ExamStaticProvider";
import { useTime } from "../TimeProvider";
import { useSession } from "../ExamDynamicProvider";
import Palete from "../Palete";
import { createPortal } from "react-dom";
import fetchData from "../fetch/fetchData";
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
    const [showVocabModal, setShowVocabModal] = useState(false);
    const [sets, setSets] = useState([]);
    const [loadingSets, setLoadingSets] = useState(false);
    const [saving, setSaving] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");
    const [successMessage, setSuccessMessage] = useState("");
    const [form, setForm] = useState({
        term: "",
        meaning: "",
        example: "",
        note: "",
        targetSetId: "",
        newSetName: "",
        newSetDescription: "",
    });

    const loadSets = async () => {
        setLoadingSets(true);
        try {
            const data = await fetchData("/api/vocabulary/sets");
            setSets(data || []);
            setForm((prev) => ({
                ...prev,
                targetSetId: (data || []).length > 0 ? data[0].setId : "__new__",
            }));
        } catch (error) {
            console.log(error);
            setSets([]);
            setForm((prev) => ({ ...prev, targetSetId: "__new__" }));
        } finally {
            setLoadingSets(false);
        }
    };

    const openVocabModal = async () => {
        setShowVocabModal(true);
        setErrorMessage("");
        setSuccessMessage("");
        await loadSets();
    };

    const closeVocabModal = () => {
        setShowVocabModal(false);
        setErrorMessage("");
        setSuccessMessage("");
    };

    const handleSaveVocab = async () => {
        setErrorMessage("");
        setSuccessMessage("");
        if (!form.term.trim() || !form.meaning.trim()) {
            setErrorMessage("Term và Meaning là bắt buộc");
            return;
        }
        if (form.targetSetId === "__new__" && !form.newSetName.trim()) {
            setErrorMessage("Vui lòng nhập tên bộ từ mới");
            return;
        }

        setSaving(true);
        try {
            let targetSetId = form.targetSetId;
            if (form.targetSetId === "__new__") {
                const createdSet = await fetchData("/api/vocabulary/sets", {
                    method: "POST",
                    body: JSON.stringify({
                        name: form.newSetName,
                        description: form.newSetDescription,
                    }),
                });
                targetSetId = createdSet.setId;
            }

            await fetchData(`/api/vocabulary/sets/${targetSetId}/items`, {
                method: "POST",
                body: JSON.stringify({
                    term: form.term,
                    meaning: form.meaning,
                    note: form.note,
                    example: form.example,
                }),
            });

            setSuccessMessage("Lưu từ thành công");
            setForm((prev) => ({
                ...prev,
                term: "",
                meaning: "",
                note: "",
                example: "",
            }));
        } catch (error) {
            setErrorMessage(error.message || "Lưu từ thất bại");
        } finally {
            setSaving(false);
        }
    };

    return(
        <>
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
                <div className="d-flex align-items-center gap-2">
                    <p className="fw-bold m-0 text-white">Review Mode</p>
                    <button className="btn btn-light btn-sm" onClick={openVocabModal}>Lưu từ</button>
                </div>}
            </div>
        </header>
        {showVocabModal && (
            <div
                className="position-fixed top-0 start-0 w-100 h-100 d-flex align-items-center justify-content-center"
                style={{ backgroundColor: "rgba(0,0,0,0.5)", zIndex: 11000 }}
            >
                <div className="bg-white rounded-3 p-4" style={{ width: "min(680px, 95vw)" }}>
                    <h5 className="fw-bold mb-3">Lưu từ mới</h5>
                    {errorMessage && <div className="alert alert-danger py-2">{errorMessage}</div>}
                    {successMessage && <div className="alert alert-success py-2">{successMessage}</div>}

                    <div className="row g-3">
                        <div className="col-md-6">
                            <label className="form-label">Từ/Cụm từ</label>
                            <input className="form-control" value={form.term} onChange={(e) => setForm((prev) => ({ ...prev, term: e.target.value }))} />
                        </div>
                        <div className="col-md-6">
                            <label className="form-label">Nghĩa</label>
                            <input className="form-control" value={form.meaning} onChange={(e) => setForm((prev) => ({ ...prev, meaning: e.target.value }))} />
                        </div>
                        <div className="col-md-6">
                            <label className="form-label">Ví dụ</label>
                            <input className="form-control" value={form.example} onChange={(e) => setForm((prev) => ({ ...prev, example: e.target.value }))} />
                        </div>
                        <div className="col-md-6">
                            <label className="form-label">Note</label>
                            <input className="form-control" value={form.note} onChange={(e) => setForm((prev) => ({ ...prev, note: e.target.value }))} />
                        </div>
                        <div className="col-12">
                            <label className="form-label">Bộ từ đích</label>
                            <select
                                className="form-select"
                                value={form.targetSetId}
                                onChange={(e) => setForm((prev) => ({ ...prev, targetSetId: e.target.value }))}
                                disabled={loadingSets}
                            >
                                {sets.map((set) => (
                                    <option key={set.setId} value={set.setId}>{set.name}</option>
                                ))}
                                <option value="__new__">+ Tạo bộ từ mới</option>
                            </select>
                        </div>

                        {form.targetSetId === "__new__" && (
                            <>
                                <div className="col-md-6">
                                    <label className="form-label">Tên bộ từ mới</label>
                                    <input className="form-control" value={form.newSetName} onChange={(e) => setForm((prev) => ({ ...prev, newSetName: e.target.value }))} />
                                </div>
                                <div className="col-md-6">
                                    <label className="form-label">Mô tả bộ từ</label>
                                    <input className="form-control" value={form.newSetDescription} onChange={(e) => setForm((prev) => ({ ...prev, newSetDescription: e.target.value }))} />
                                </div>
                            </>
                        )}
                    </div>

                    <div className="d-flex justify-content-end gap-2 mt-4">
                        <button className="btn btn-outline-secondary" onClick={closeVocabModal}>Đóng</button>
                        <button className="btn btn-primary" onClick={handleSaveVocab} disabled={saving}>
                            {saving ? "Đang lưu..." : "Lưu từ"}
                        </button>
                    </div>
                </div>
            </div>
        )}
        </>
    )
}