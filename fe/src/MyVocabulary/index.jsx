import { useEffect, useMemo, useState } from "react";
import NavBar from "../NavBar";
import fetchData from "../fetch/fetchData";
import "./index.css";

const emptyItem = () => ({ term: "", meaning: "", note: "", example: "" });

export default function MyVocabulary() {
  const [sets, setSets] = useState([]);
  const [selectedSetId, setSelectedSetId] = useState("");
  const [items, setItems] = useState([]);
  const [loadingSets, setLoadingSets] = useState(true);
  const [loadingItems, setLoadingItems] = useState(false);
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [form, setForm] = useState({
    name: "",
    description: "",
    items: [emptyItem(), emptyItem()],
  });
  const [submitMessage, setSubmitMessage] = useState("");
  const [submitError, setSubmitError] = useState("");
  const [showEditModal, setShowEditModal] = useState(false);
  const [editingSetId, setEditingSetId] = useState("");
  const [editForm, setEditForm] = useState({ name: "", description: "", items: [] });
  const [editLoading, setEditLoading] = useState(false);
  const [deleteConfirm, setDeleteConfirm] = useState(null);
  const [deleteLoading, setDeleteLoading] = useState(false);

  const selectedSet = useMemo(
    () => sets.find((set) => set.setId === selectedSetId),
    [sets, selectedSetId]
  );

  const loadSets = async () => {
    setLoadingSets(true);
    try {
      const data = await fetchData("/api/vocabulary/sets");
      setSets(data || []);
      if ((data || []).length > 0 && !selectedSetId) {
        setSelectedSetId(data[0].setId);
      }
    } catch (error) {
      console.log(error);
      setSets([]);
    } finally {
      setLoadingSets(false);
    }
  };

  const loadItems = async (setId) => {
    if (!setId) {
      setItems([]);
      return;
    }
    setLoadingItems(true);
    try {
      const data = await fetchData(`/api/vocabulary/sets/${setId}/items`);
      setItems(data || []);
    } catch (error) {
      console.log(error);
      setItems([]);
    } finally {
      setLoadingItems(false);
    }
  };

  useEffect(() => {
    loadSets();
  }, []);

  useEffect(() => {
    loadItems(selectedSetId);
  }, [selectedSetId]);

  const updateItemField = (index, field, value) => {
    setForm((prev) => ({
      ...prev,
      items: prev.items.map((item, itemIndex) =>
        itemIndex === index ? { ...item, [field]: value } : item
      ),
    }));
  };

  const addInputRow = () => {
    setForm((prev) => ({ ...prev, items: [...prev.items, emptyItem()] }));
  };

  const removeInputRow = (index) => {
    setForm((prev) => ({
      ...prev,
      items: prev.items.length <= 2
        ? prev.items
        : prev.items.filter((_, itemIndex) => itemIndex !== index),
    }));
  };

  const resetForm = () => {
    setForm({
      name: "",
      description: "",
      items: [emptyItem(), emptyItem()],
    });
  };

  const handleCreateSet = async (event) => {
    event.preventDefault();
    setSubmitMessage("");
    setSubmitError("");
    try {
      const payload = {
        name: form.name,
        description: form.description,
        items: form.items,
      };
      const result = await fetchData("/api/vocabulary/sets/with-items", {
        method: "POST",
        body: JSON.stringify(payload),
      });
      await loadSets();
      setSelectedSetId(result.setId);
      await loadItems(result.setId);
      setSubmitMessage(
        result.skippedTerms?.length
          ? `Tạo thành công. Bỏ qua từ trùng: ${result.skippedTerms.join(", ")}`
          : "Tạo bộ từ thành công"
      );
      resetForm();
      setShowCreateForm(false);
    } catch (error) {
      setSubmitError(error.message || "Tạo bộ từ thất bại");
    }
  };

  const openEditModal = async (set) => {
    setEditingSetId(set.setId);
    setEditLoading(true);
    try {
      const itemsData = await fetchData(`/api/vocabulary/sets/${set.setId}/items`);
      const formattedItems = (itemsData || []).map((item) => ({
        itemId: item.itemId,
        term: item.term,
        meaning: item.meaning,
        note: item.note || "",
        example: item.example || "",
        isNew: false,
      }));
      setEditForm({
        name: set.name,
        description: set.description || "",
        items: formattedItems,
      });
    } catch (error) {
      console.log(error);
      setEditForm({
        name: set.name,
        description: set.description || "",
        items: [],
      });
    } finally {
      setEditLoading(false);
      setShowEditModal(true);
    }
  };

  const updateEditItemField = (index, field, value) => {
    setEditForm((prev) => ({
      ...prev,
      items: prev.items.map((item, itemIndex) =>
        itemIndex === index ? { ...item, [field]: value } : item
      ),
    }));
  };

  const addEditItemRow = () => {
    setEditForm((prev) => ({
      ...prev,
      items: [
        ...prev.items,
        { term: "", meaning: "", note: "", example: "", isNew: true },
      ],
    }));
  };

  const removeEditItemRow = (index) => {
    setEditForm((prev) => ({
      ...prev,
      items: prev.items.filter((_, itemIndex) => itemIndex !== index),
    }));
  };

  const handleUpdateSet = async (event) => {
    event.preventDefault();
    if (!editForm.name.trim()) {
      setSubmitError("Tên bộ từ không được để trống");
      return;
    }

    setEditLoading(true);
    try {
      const payload = {
        name: editForm.name,
        description: editForm.description,
        items: editForm.items || [],
      };
      await fetchData(`/api/vocabulary/sets/${editingSetId}/with-items`, {
        method: "PUT",
        body: JSON.stringify(payload),
      });
      await loadSets();
      await loadItems(editingSetId);
      setShowEditModal(false);
      setSubmitMessage("Cập nhật bộ từ thành công");
    } catch (error) {
      setSubmitError(error.message || "Cập nhật bộ từ thất bại");
    } finally {
      setEditLoading(false);
    }
  };

  const handleDeleteSet = async (setId) => {
    setDeleteLoading(true);
    try {
      await fetchData(`/api/vocabulary/sets/${setId}`, {
        method: "DELETE",
      });
      if (selectedSetId === setId) {
        setSelectedSetId("");
        setItems([]);
      }
      await loadSets();
      setDeleteConfirm(null);
      setSubmitMessage("Xóa bộ từ thành công");
    } catch (error) {
      setSubmitError(error.message || "Xóa bộ từ thất bại");
    } finally {
      setDeleteLoading(false);
    }
  };

  return (
    <div className="min-vh-100 bg-light">
      <NavBar />
      <div className="container py-4">
        <div className="d-flex justify-content-between align-items-center mb-3">
          <h2 className="fw-bold m-0">My Vocabulary</h2>
          <button
            className="btn btn-primary"
            onClick={() => {
              setShowCreateForm((prev) => !prev);
              setSubmitError("");
              setSubmitMessage("");
            }}
          >
            {showCreateForm ? "Đóng" : "Tạo bộ từ mới"}
          </button>
        </div>

        {submitMessage && <div className="alert alert-success">{submitMessage}</div>}
        {submitError && <div className="alert alert-danger">{submitError}</div>}

        {showEditModal && (
          <div className="modal d-block" style={{ backgroundColor: "rgba(0, 0, 0, 0.5)" }}>
            <div className="modal-dialog modal-dialog-centered modal-xl">
              <div className="modal-content">
                <div className="modal-header">
                  <h5 className="modal-title">Sửa bộ từ</h5>
                  <button
                    type="button"
                    className="btn-close"
                    onClick={() => setShowEditModal(false)}
                  ></button>
                </div>
                <form onSubmit={handleUpdateSet}>
                  <div className="modal-body" style={{ maxHeight: "70vh", overflowY: "auto" }}>
                    <div className="mb-3">
                      <label className="form-label">Tên bộ từ</label>
                      <input
                        className="form-control"
                        value={editForm.name}
                        onChange={(e) =>
                          setEditForm((prev) => ({ ...prev, name: e.target.value }))
                        }
                        required
                      />
                    </div>
                    <div className="mb-3">
                      <label className="form-label">Mô tả</label>
                      <textarea
                        className="form-control"
                        value={editForm.description}
                        onChange={(e) =>
                          setEditForm((prev) => ({ ...prev, description: e.target.value }))
                        }
                        rows={2}
                      />
                    </div>
                    <div className="mb-3">
                      <div className="d-flex justify-content-between align-items-center mb-2">
                        <label className="form-label m-0">Từ vựng ({editForm.items?.length || 0})</label>
                        <button
                          type="button"
                          className="btn btn-sm btn-success"
                          onClick={addEditItemRow}
                        >
                          + Thêm từ
                        </button>
                      </div>
                      <div style={{ overflowX: "auto" }}>
                        <table className="table table-sm table-bordered">
                          <thead className="table-light">
                            <tr>
                              <th style={{ width: "25%" }}>Từ vựng</th>
                              <th style={{ width: "25%" }}>Nghĩa</th>
                              <th style={{ width: "20%" }}>Ghi chú</th>
                              <th style={{ width: "20%" }}>Ví dụ</th>
                              <th style={{ width: "10%" }}>Hành động</th>
                            </tr>
                          </thead>
                          <tbody>
                            {(editForm.items || []).map((item, index) => (
                              <tr key={index}>
                                <td>
                                  <input
                                    type="text"
                                    className="form-control form-control-sm"
                                    value={item.term}
                                    onChange={(e) =>
                                      updateEditItemField(index, "term", e.target.value)
                                    }
                                    placeholder="Nhập từ"
                                    required
                                  />
                                </td>
                                <td>
                                  <input
                                    type="text"
                                    className="form-control form-control-sm"
                                    value={item.meaning}
                                    onChange={(e) =>
                                      updateEditItemField(index, "meaning", e.target.value)
                                    }
                                    placeholder="Nhập nghĩa"
                                    required
                                  />
                                </td>
                                <td>
                                  <input
                                    type="text"
                                    className="form-control form-control-sm"
                                    value={item.note}
                                    onChange={(e) =>
                                      updateEditItemField(index, "note", e.target.value)
                                    }
                                    placeholder="Ghi chú"
                                  />
                                </td>
                                <td>
                                  <input
                                    type="text"
                                    className="form-control form-control-sm"
                                    value={item.example}
                                    onChange={(e) =>
                                      updateEditItemField(index, "example", e.target.value)
                                    }
                                    placeholder="Ví dụ"
                                  />
                                </td>
                                <td>
                                  <button
                                    type="button"
                                    className="btn btn-sm btn-danger"
                                    onClick={() => removeEditItemRow(index)}
                                  >
                                    Xóa
                                  </button>
                                </td>
                              </tr>
                            ))}
                          </tbody>
                        </table>
                      </div>
                    </div>
                  </div>
                  <div className="modal-footer">
                    <button
                      type="button"
                      className="btn btn-secondary"
                      onClick={() => setShowEditModal(false)}
                      disabled={editLoading}
                    >
                      Hủy
                    </button>
                    <button
                      type="submit"
                      className="btn btn-primary"
                      disabled={editLoading}
                    >
                      {editLoading ? "Đang lưu..." : "Lưu"}
                    </button>
                  </div>
                </form>
              </div>
            </div>
          </div>
        )}

        {deleteConfirm && (
          <div className="modal d-block" style={{ backgroundColor: "rgba(0, 0, 0, 0.5)" }}>
            <div className="modal-dialog modal-dialog-centered">
              <div className="modal-content">
                <div className="modal-header">
                  <h5 className="modal-title">Xác nhận xóa</h5>
                  <button
                    type="button"
                    className="btn-close"
                    onClick={() => setDeleteConfirm(null)}
                  ></button>
                </div>
                <div className="modal-body">
                  <p>Bạn có chắc muốn xóa bộ từ "{deleteConfirm.name}"? Hành động này không thể hoàn tác.</p>
                </div>
                <div className="modal-footer">
                  <button
                    type="button"
                    className="btn btn-secondary"
                    onClick={() => setDeleteConfirm(null)}
                    disabled={deleteLoading}
                  >
                    Hủy
                  </button>
                  <button
                    type="button"
                    className="btn btn-danger"
                    onClick={() => handleDeleteSet(deleteConfirm.setId)}
                    disabled={deleteLoading}
                  >
                    {deleteLoading ? "Đang xóa..." : "Xóa"}
                  </button>
                </div>
              </div>
            </div>
          </div>
        )}

        {showCreateForm && (
          <div className="card shadow-sm border-0 mb-4">
            <div className="card-body">
              <h5 className="fw-bold mb-3">Tạo bộ từ</h5>
              <form onSubmit={handleCreateSet} className="d-grid gap-3">
                <div>
                  <label className="form-label">Tên bộ từ</label>
                  <input
                    className="form-control"
                    value={form.name}
                    onChange={(event) => setForm((prev) => ({ ...prev, name: event.target.value }))}
                    required
                  />
                </div>
                <div>
                  <label className="form-label">Mô tả</label>
                  <textarea
                    className="form-control"
                    value={form.description}
                    onChange={(event) => setForm((prev) => ({ ...prev, description: event.target.value }))}
                    rows={2}
                  />
                </div>
                <div className="d-grid gap-3">
                  {form.items.map((item, index) => (
                    <div key={`item-row-${index}`} className="border rounded-3 p-3 bg-white">
                      <div className="row g-2">
                        <div className="col-md-6">
                          <label className="form-label">Term</label>
                          <input
                            className="form-control"
                            value={item.term}
                            onChange={(event) => updateItemField(index, "term", event.target.value)}
                          />
                        </div>
                        <div className="col-md-6">
                          <label className="form-label">Meaning</label>
                          <input
                            className="form-control"
                            value={item.meaning}
                            onChange={(event) => updateItemField(index, "meaning", event.target.value)}
                          />
                        </div>
                        <div className="col-md-6">
                          <label className="form-label">Note</label>
                          <input
                            className="form-control"
                            value={item.note}
                            onChange={(event) => updateItemField(index, "note", event.target.value)}
                          />
                        </div>
                        <div className="col-md-6">
                          <label className="form-label">Example</label>
                          <input
                            className="form-control"
                            value={item.example}
                            onChange={(event) => updateItemField(index, "example", event.target.value)}
                          />
                        </div>
                      </div>
                      {form.items.length > 2 && (
                        <button
                          type="button"
                          className="btn btn-sm btn-outline-danger mt-2"
                          onClick={() => removeInputRow(index)}
                        >
                          Xóa ô này
                        </button>
                      )}
                    </div>
                  ))}
                </div>
                <div className="d-flex gap-2">
                  <button type="button" className="btn btn-outline-primary" onClick={addInputRow}>
                    + Thêm ô nhập từ
                  </button>
                  <button type="submit" className="btn btn-primary">
                    Tạo bộ từ
                  </button>
                </div>
              </form>
            </div>
          </div>
        )}

        <div className="row g-3">
          <div className="col-lg-4">
            <div className="card shadow-sm border-0 h-100">
              <div className="card-body">
                <h5 className="fw-bold">Bộ từ của bạn</h5>
                {loadingSets ? (
                  <p className="text-muted">Đang tải...</p>
                ) : sets.length === 0 ? (
                  <p className="text-muted">Chưa có bộ từ nào.</p>
                ) : (
                  <div className="list-group">
                    {sets.map((set) => (
                      <div
                        key={set.setId}
                        className={`list-group-item ${selectedSetId === set.setId ? "active" : ""}`}
                      >
                        <button
                          type="button"
                          className={`w-100 text-start border-0 bg-transparent ${selectedSetId === set.setId ? "" : "text-dark"}`}
                          onClick={() => setSelectedSetId(set.setId)}
                        >
                          <div className="d-flex justify-content-between align-items-center">
                            <span className="fw-bold">{set.name}</span>
                            <span className="badge bg-secondary">{set.itemCount}</span>
                          </div>
                          {set.description && <small>{set.description}</small>}
                        </button>
                        <div className="mt-2 d-flex gap-2">
                          <button
                            type="button"
                            className="btn btn-sm btn-outline-primary"
                            onClick={() => openEditModal(set)}
                          >
                            Sửa
                          </button>
                          <button
                            type="button"
                            className="btn btn-sm btn-outline-danger"
                            onClick={() => setDeleteConfirm(set)}
                          >
                            Xóa
                          </button>
                        </div>
                      </div>
                    ))}
                  </div>
                )}
              </div>
            </div>
          </div>

          <div className="col-lg-8">
            <div className="card shadow-sm border-0 h-100">
              <div className="card-body">
                <h5 className="fw-bold mb-3">{selectedSet ? selectedSet.name : "Danh sách từ"}</h5>
                {loadingItems ? (
                  <p className="text-muted">Đang tải từ vựng...</p>
                ) : !selectedSetId ? (
                  <p className="text-muted">Hãy chọn một bộ từ.</p>
                ) : items.length === 0 ? (
                  <p className="text-muted">Bộ từ này chưa có item.</p>
                ) : (
                  <div className="table-responsive">
                    <table className="table table-striped">
                      <thead>
                        <tr>
                          <th>Term</th>
                          <th>Meaning</th>
                          <th>Note</th>
                          <th>Example</th>
                        </tr>
                      </thead>
                      <tbody>
                        {items.map((item) => (
                          <tr key={item.itemId}>
                            <td className="fw-semibold">{item.term}</td>
                            <td>{item.meaning}</td>
                            <td>{item.note || "-"}</td>
                            <td>{item.example || "-"}</td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
