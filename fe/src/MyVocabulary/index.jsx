import { useEffect, useMemo, useState } from "react";
import NavBar from "../NavBar";
import fetchData from "../fetch/fetchData";

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
                      <button
                        key={set.setId}
                        type="button"
                        className={`list-group-item list-group-item-action ${selectedSetId === set.setId ? "active" : ""}`}
                        onClick={() => setSelectedSetId(set.setId)}
                      >
                        <div className="d-flex justify-content-between align-items-center">
                          <span className="fw-bold">{set.name}</span>
                          <span className="badge bg-secondary">{set.itemCount}</span>
                        </div>
                        {set.description && <small>{set.description}</small>}
                      </button>
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
