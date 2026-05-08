import { useEffect, useMemo, useRef, useState } from "react";
import NavBar from "../NavBar";
import fetchData from "../fetch/fetchData";
import AiConfigModal from "./AiConfigModal";
import AiGeneratorModal from "./AiGeneratorModal";
import FlashcardMode from "./components/FlashcardMode";
import MatchingMode from "./components/MatchingMode";
import QuizMode from "./components/QuizMode";
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
  const [setSearchKeyword, setSetSearchKeyword] = useState("");
  const [learningMode, setLearningMode] = useState("list");
  const [showEditItemModal, setShowEditItemModal] = useState(false);
  const [editingItemId, setEditingItemId] = useState("");
  const [editItemForm, setEditItemForm] = useState({ term: "", meaning: "", note: "", example: "" });
  const [editItemLoading, setEditItemLoading] = useState(false);
  const [showAiConfigModal, setShowAiConfigModal] = useState(false);
  const [showAiGeneratorModal, setShowAiGeneratorModal] = useState(false);
  const [aiProviders, setAiProviders] = useState([]);
  const [createFormAiItemIndex, setCreateFormAiItemIndex] = useState(-1);
  const [aiConfigDetails, setAiConfigDetails] = useState({});
  const [createFormSubmitting, setCreateFormSubmitting] = useState(false);
  const [editFormAiItemIndex, setEditFormAiItemIndex] = useState(-1);

  const selectedSet = useMemo(
    () => sets.find((set) => set.setId === selectedSetId),
    [sets, selectedSetId]
  );

  const filteredSets = useMemo(() => {
    const keyword = setSearchKeyword.trim().toLowerCase();
    if (!keyword) {
      return sets;
    }
    return sets.filter((set) => (set.name || "").toLowerCase().includes(keyword));
  }, [sets, setSearchKeyword]);

  const loadSets = async () => {
    setLoadingSets(true);
    try {
      const data = await fetchData("/api/vocabulary/sets");
      setSets(data || []);
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
    loadAiProviders();
  }, []);

  const loadAiProviders = async () => {
    const providers = [];
    const details = {};
    try {
      const groqConfig = await fetchData("/api/ai-config/groq").catch((err) => {
        if (err?.message?.includes("404") || err?.message?.includes("Lỗi 404")) return null;
        throw err;
      });
      if (groqConfig) {
        providers.push("groq");
        details.groq = groqConfig;
      }
    } catch (err) {
      console.log("Error checking Groq config:", err);
    }

    try {
      const openrouterConfig = await fetchData("/api/ai-config/openrouter").catch((err) => {
        if (err?.message?.includes("404") || err?.message?.includes("Lỗi 404")) return null;
        throw err;
      });
      if (openrouterConfig) {
        providers.push("openrouter");
        details.openrouter = openrouterConfig;
      }
    } catch (err) {
      console.log("Error checking OpenRouter config:", err);
    }

    setAiProviders(providers);
    setAiConfigDetails(details);
  };

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
    setCreateFormSubmitting(true);
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
      setSelectedSetId("");
      setSubmitMessage(
        result.skippedTerms?.length
          ? `Tạo thành công. Bỏ qua từ trùng: ${result.skippedTerms.join(", ")}`
          : "Tạo bộ từ thành công"
      );
      resetForm();
      setShowCreateForm(false);
    } catch (error) {
      setSubmitError(error.message || "Tạo bộ từ thất bại");
    } finally {
      setCreateFormSubmitting(false);
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

  const handleAiConfigSaved = () => {
    loadAiProviders();
  };

  const handleAiGenerated = (generatedData) => {
    if (generatedData) {
      setEditItemForm((prev) => ({
        ...prev,
        meaning: generatedData.meaning || prev.meaning,
        note: generatedData.note || prev.note,
        example: generatedData.example || prev.example,
      }));
    }
  };

  const handleCreateFormAiGenerated = (generatedData) => {
    if (generatedData && createFormAiItemIndex >= 0) {
      setForm((prev) => ({
        ...prev,
        items: prev.items.map((item, index) =>
          index === createFormAiItemIndex
            ? {
                ...item,
                meaning: generatedData.meaning || item.meaning,
                note: generatedData.note || item.note,
                example: generatedData.example || item.example,
              }
            : item
        ),
      }));
      setCreateFormAiItemIndex(-1);
    }
  };

  const handleEditFormAiGenerated = (generatedData) => {
    if (generatedData && editFormAiItemIndex >= 0) {
      setEditForm((prev) => ({
        ...prev,
        items: prev.items.map((item, index) =>
          index === editFormAiItemIndex
            ? {
                ...item,
                meaning: generatedData.meaning || item.meaning,
                note: generatedData.note || item.note,
                example: generatedData.example || item.example,
              }
            : item
        ),
      }));
      setEditFormAiItemIndex(-1);
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

  const openEditItemModal = (item) => {
    setEditingItemId(item.itemId);
    setEditItemForm({
      term: item.term || "",
      meaning: item.meaning || "",
      note: item.note || "",
      example: item.example || "",
    });
    setShowEditItemModal(true);
  };

  const handleUpdateSingleItem = async (event) => {
    event.preventDefault();
    if (!selectedSetId || !selectedSet) {
      return;
    }
    if (!editItemForm.term.trim() || !editItemForm.meaning.trim()) {
      setSubmitError("Term và Meaning không được để trống");
      return;
    }

    setEditItemLoading(true);
    setSubmitMessage("");
    setSubmitError("");

    try {
      const result = await fetchData(`/api/vocabulary/sets/${selectedSetId}/items/${editingItemId}`, {
        method: "PUT",
        body: JSON.stringify(editItemForm),
      });

      await loadItems(selectedSetId);
      await loadSets();
      setShowEditItemModal(false);
      setEditingItemId("");
      setSubmitMessage(result?.term ? `Cập nhật từ vựng thành công: ${result.term}` : "Cập nhật từ vựng thành công");
    } catch (error) {
      setSubmitError(error.message || "Cập nhật từ vựng thất bại");
    } finally {
      setEditItemLoading(false);
    }
  };

  const playAudio = (audioUrl) => {
    if (!audioUrl) {
      return;
    }
    const audio = new Audio(audioUrl);
    audio.play().catch((err) => {
      console.error("Error playing audio:", err);
    });
  };

  return (
    <div className="min-vh-100 bg-light">
      <NavBar />
      <div className="container py-4">
      <div className="d-flex justify-content-between align-items-center mb-3">
          <h2 className="fw-bold m-0">My Vocabulary</h2>
          <div className="d-flex gap-2">
            <button
              className="btn btn-outline-secondary btn-sm"
              onClick={() => setShowAiConfigModal(true)}
              title="Cấu hình AI Models"
            >
              ⚙️ AI Config
            </button>
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
        </div>

        {submitMessage && <div className="alert alert-success">{submitMessage}</div>}
        {submitError && <div className="alert alert-danger">{submitError}</div>}

        <AiConfigModal
          show={showAiConfigModal}
          onClose={() => setShowAiConfigModal(false)}
          onConfigSaved={handleAiConfigSaved}
        />

        <AiGeneratorModal
          show={showAiGeneratorModal}
          onClose={() => setShowAiGeneratorModal(false)}
          term={editItemForm.term}
          onGenerated={handleAiGenerated}
          availableProviders={aiProviders}
        />

        {createFormAiItemIndex >= 0 && (
          <AiGeneratorModal
            show={true}
            onClose={() => setCreateFormAiItemIndex(-1)}
            term={form.items[createFormAiItemIndex]?.term || ""}
            onGenerated={handleCreateFormAiGenerated}
            availableProviders={aiProviders}
          />
        )}

        {editFormAiItemIndex >= 0 && (
          <AiGeneratorModal
            show={true}
            onClose={() => setEditFormAiItemIndex(-1)}
            term={editForm.items[editFormAiItemIndex]?.term || ""}
            onGenerated={handleEditFormAiGenerated}
            availableProviders={aiProviders}
          />
        )}

        {showEditModal && (
          <div className="modal d-block" style={{ backgroundColor: "rgba(0, 0, 0, 0.5)", zIndex: 11000 }}>
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
                    {aiProviders.length > 0 && (
                      <div className="alert alert-info mb-3 py-2">
                        <small>
                          💡 <strong>Mẹo AI:</strong> Khi cập nhật từ, bạn có thể dùng nút "Tạo từ AI" 
                          để tự động điền Meaning, Note, Example!
                        </small>
                      </div>
                    )}
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
                                  <div className="d-flex flex-column gap-1">
                                    {aiProviders.length > 0 && item.term.trim() && (
                                      <button
                                        type="button"
                                        className="btn btn-sm btn-outline-info"
                                        onClick={() => setEditFormAiItemIndex(index)}
                                      >
                                        🤖 AI
                                      </button>
                                    )}
                                    <button
                                      type="button"
                                      className="btn btn-sm btn-danger"
                                      onClick={() => removeEditItemRow(index)}
                                    >
                                      Xóa
                                    </button>
                                  </div>
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

        {showEditItemModal && (
          <div className="modal d-block" style={{ backgroundColor: "rgba(0, 0, 0, 0.5)" }}>
            <div className="modal-dialog modal-dialog-centered">
              <div className="modal-content">
                <div className="modal-header">
                  <h5 className="modal-title">Sửa từ vựng</h5>
                  <button
                    type="button"
                    className="btn-close"
                    onClick={() => setShowEditItemModal(false)}
                  ></button>
                </div>
                <form onSubmit={handleUpdateSingleItem}>
                  <div className="modal-body d-grid gap-3">
                    <div>
                      <label className="form-label">Term</label>
                      <input
                        type="text"
                        className="form-control"
                        value={editItemForm.term}
                        onChange={(event) =>
                          setEditItemForm((prev) => ({ ...prev, term: event.target.value }))
                        }
                        required
                      />
                    </div>
                    <div>
                      <label className="form-label">Meaning</label>
                      <input
                        type="text"
                        className="form-control"
                        value={editItemForm.meaning}
                        onChange={(event) =>
                          setEditItemForm((prev) => ({ ...prev, meaning: event.target.value }))
                        }
                        required
                      />
                    </div>
                    <div>
                      <label className="form-label">Note</label>
                      <input
                        type="text"
                        className="form-control"
                        value={editItemForm.note}
                        onChange={(event) =>
                          setEditItemForm((prev) => ({ ...prev, note: event.target.value }))
                        }
                      />
                    </div>
                    <div>
                      <label className="form-label">Example</label>
                      <input
                        type="text"
                        className="form-control"
                        value={editItemForm.example}
                        onChange={(event) =>
                          setEditItemForm((prev) => ({ ...prev, example: event.target.value }))
                        }
                      />
                    </div>
                    {aiProviders.length > 0 && editItemForm.term.trim() && (
                      <div>
                        <button
                          type="button"
                          className="btn btn-outline-info w-100"
                          onClick={() => setShowAiGeneratorModal(true)}
                          disabled={editItemLoading}
                        >
                          🤖 Tạo từ Meaning, Note, Example bằng AI
                        </button>
                      </div>
                    )}
                  </div>
                  <div className="modal-footer">
                    <button
                      type="button"
                      className="btn btn-secondary"
                      onClick={() => setShowEditItemModal(false)}
                      disabled={editItemLoading}
                    >
                      Hủy
                    </button>
                    <button
                      type="submit"
                      className="btn btn-primary"
                      disabled={editItemLoading}
                    >
                      {editItemLoading ? "Đang lưu..." : "Lưu"}
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
              {aiProviders.length > 0 && (
                <div className="alert alert-info mb-3 py-2">
                  <small>
                    💡 <strong>Mẹo AI:</strong> Bạn có thể chỉ nhập từ (Term) và để trống Meaning, Note, Example. 
                    AI ({aiProviders.join("/").toUpperCase()}) sẽ tự động điền các trường này cho bạn!
                  </small>
                </div>
              )}
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
                        {aiProviders.length > 0 && item.term.trim() && (
                          <div className="col-12">
                            <button
                              type="button"
                              className="btn btn-sm btn-outline-info w-100"
                              onClick={() => setCreateFormAiItemIndex(index)}
                            >
                              🤖 Tạo Meaning, Note, Example bằng AI
                            </button>
                          </div>
                        )}
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
                  <button type="submit" className="btn btn-primary" disabled={createFormSubmitting}>
                    {createFormSubmitting ? "Đang tạo..." : "Tạo bộ từ"}
                  </button>
                </div>
              </form>
            </div>
          </div>
        )}

        <div className="row g-3">
          <div className={selectedSetId ? "col-lg-3" : "col-12"}>
            {/* AI Configs Section */}
            {aiProviders.length > 0 && (
              <div className="card shadow-sm border-0 mb-3">
                <div className="card-body">
                  <h5 className="fw-bold mb-3">Cấu hình AI đã lưu</h5>
                  <div className="d-grid gap-2">
                    {aiProviders.map((provider) => (
                      <div key={provider} className="alert alert-info py-2 mb-0">
                        <div className="d-flex justify-content-between align-items-center">
                          <span className="fw-bold">
                            {provider === "groq" ? "🚀 Groq" : "🔄 OpenRouter"}
                          </span>
                          <span className="badge bg-success">✓ Hoạt động</span>
                        </div>
                        {aiConfigDetails[provider]?.createdAt && (
                          <small className="text-muted">
                            Lưu: {new Date(aiConfigDetails[provider].createdAt).toLocaleDateString("vi-VN")}
                          </small>
                        )}
                      </div>
                    ))}
                  </div>
                </div>
              </div>
            )}

            <div className="card shadow-sm border-0 h-100">
              <div className="card-body">
                <h5 className="fw-bold">Bộ từ của bạn</h5>
                <div className="mb-3">
                  <input
                    type="text"
                    className="form-control"
                    placeholder="Tìm theo tên bộ từ..."
                    value={setSearchKeyword}
                    onChange={(event) => setSetSearchKeyword(event.target.value)}
                  />
                </div>
                {loadingSets ? (
                  <p className="text-muted">Đang tải...</p>
                ) : sets.length === 0 ? (
                  <p className="text-muted">Chưa có bộ từ nào.</p>
                ) : filteredSets.length === 0 ? (
                  <p className="text-muted">Không tìm thấy bộ từ phù hợp.</p>
                ) : (
                  <div className="list-group">
                    {filteredSets.map((set) => (
                      <div
                        key={set.setId}
                        className={`list-group-item ${selectedSetId === set.setId ? "active" : ""}`}
                      >
                        <button
                          type="button"
                          className={`w-100 text-start border-0 bg-transparent ${selectedSetId === set.setId ? "" : "text-dark"}`}
                          onClick={() => {
                            setSelectedSetId(set.setId);
                            setLearningMode("list");
                          }}
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

          {selectedSetId && (
            <div className="col-lg-9">
              <div className="card shadow-sm border-0 h-100">
                <div className="card-body">
                  <div className="d-flex justify-content-between align-items-center mb-3">
                    <h5 className="fw-bold m-0">{selectedSet ? selectedSet.name : "Chi tiết bộ từ"}</h5>
                    <button
                      type="button"
                      className="btn btn-sm btn-outline-secondary"
                      onClick={() => {
                        setSelectedSetId("");
                        setItems([]);
                      }}
                    >
                      Quay lại danh sách bộ từ
                    </button>
                  </div>

                  {loadingItems ? (
                    <p className="text-muted">Đang tải từ vựng...</p>
                  ) : items.length === 0 ? (
                    <p className="text-muted">Bộ từ này chưa có item.</p>
                  ) : (
                    <div className="d-grid gap-3">
                      <div className="d-flex flex-wrap gap-2">
                        <button
                          type="button"
                          className={`btn btn-sm ${learningMode === "list" ? "btn-primary" : "btn-outline-primary"}`}
                          onClick={() => setLearningMode("list")}
                        >
                          Danh sách từ
                        </button>
                        <button
                          type="button"
                          className={`btn btn-sm ${learningMode === "flashcard" ? "btn-primary" : "btn-outline-primary"}`}
                          onClick={() => setLearningMode("flashcard")}
                        >
                          Chế độ 1: Flash card
                        </button>
                        <button
                          type="button"
                          className={`btn btn-sm ${learningMode === "matching" ? "btn-primary" : "btn-outline-primary"}`}
                          onClick={() => {
                            setLearningMode("matching");
                            resetMatchingGame();
                          }}
                        >
                          Chế độ 2: Ghép thẻ
                        </button>
                        <button
                          type="button"
                          className={`btn btn-sm ${learningMode === "quiz" ? "btn-primary" : "btn-outline-primary"}`}
                          onClick={() => {
                            setLearningMode("quiz");
                            resetQuizGame();
                          }}
                        >
                          Chế độ 3: Trắc nghiệm
                        </button>
                      </div>

                      {learningMode === "list" && (
                        <div className="table-responsive">
                          <table className="table table-striped table-sm">
                            <thead>
                              <tr>
                                <th style={{ minWidth: "120px" }}>Term</th>
                                <th style={{ minWidth: "120px" }}>Meaning</th>
                                <th style={{ minWidth: "100px" }}>Pronunciation</th>
                                <th style={{ minWidth: "80px" }}>Audio</th>
                                <th style={{ minWidth: "80px" }}>Note</th>
                                <th style={{ minWidth: "100px" }}>Example</th>
                                <th style={{ minWidth: "80px" }}>Action</th>
                              </tr>
                            </thead>
                            <tbody>
                              {items.map((item) => (
                                <tr key={item.itemId}>
                                  <td className="fw-semibold">{item.term}</td>
                                  <td>{item.meaning}</td>
                                  <td className="small text-muted">{item.pronunciation || "-"}</td>
                                  <td>
                                    {item.audioUrl ? (
                                      <button
                                        type="button"
                                        className="btn btn-sm btn-outline-info p-1"
                                        onClick={() => playAudio(item.audioUrl)}
                                        title="Play pronunciation"
                                      >
                                        🔊
                                      </button>
                                    ) : (
                                      <span className="text-muted small">-</span>
                                    )}
                                  </td>
                                  <td className="small">{item.note || "-"}</td>
                                  <td className="small">{item.example || "-"}</td>
                                  <td>
                                    <button
                                      type="button"
                                      className="btn btn-sm btn-outline-primary"
                                      onClick={() => openEditItemModal(item)}
                                    >
                                      Sửa
                                    </button>
                                  </td>
                                </tr>
                              ))}
                            </tbody>
                          </table>
                        </div>
                      )}

                      {learningMode === "flashcard" && <FlashcardMode items={items} />}

                      {learningMode === "matching" && <MatchingMode items={items} />}

                      {learningMode === "quiz" && <QuizMode items={items} />}
                    </div>
                  )}
                </div>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
