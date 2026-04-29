import { useEffect, useMemo, useRef, useState } from "react";
import NavBar from "../NavBar";
import fetchData from "../fetch/fetchData";
import "./index.css";

const emptyItem = () => ({ term: "", meaning: "", note: "", example: "" });
const MAX_MATCH_ITEMS = 6;
const MAX_QUIZ_OPTIONS = 4;

const shuffleArray = (arr) => {
  const copy = [...arr];
  for (let i = copy.length - 1; i > 0; i -= 1) {
    const j = Math.floor(Math.random() * (i + 1));
    [copy[i], copy[j]] = [copy[j], copy[i]];
  }
  return copy;
};

const isSameOrder = (a, b) => {
  if (a.length !== b.length) {
    return false;
  }
  return a.every((value, index) => value === b[index]);
};

const buildMatchingCards = (sourceItems) => {
  const chosenItems = shuffleArray(sourceItems).slice(0, MAX_MATCH_ITEMS);
  const cards = chosenItems.flatMap((item) => [
    {
      id: `${item.itemId}-term`,
      pairId: item.itemId,
      type: "term",
      text: item.term,
    },
    {
      id: `${item.itemId}-meaning`,
      pairId: item.itemId,
      type: "meaning",
      text: item.meaning,
    },
  ]);
  return shuffleArray(cards);
};

const normalizeMeaning = (meaning) => (meaning || "").trim().toLowerCase();

const buildQuizOptions = (currentItem, sourceItems) => {
  if (!currentItem) {
    return [];
  }

  const correctMeaning = currentItem.meaning;
  const seen = new Set([normalizeMeaning(correctMeaning)]);
  const wrongMeanings = shuffleArray(
    sourceItems
      .filter((item) => item.itemId !== currentItem.itemId)
      .map((item) => item.meaning)
      .filter((meaning) => {
        const normalized = normalizeMeaning(meaning);
        if (!normalized || seen.has(normalized)) {
          return false;
        }
        seen.add(normalized);
        return true;
      })
  );

  const options = [correctMeaning, ...wrongMeanings].slice(0, MAX_QUIZ_OPTIONS);
  return shuffleArray(options);
};

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
  const [flashMode, setFlashMode] = useState("en");
  const [flashOrder, setFlashOrder] = useState([]);
  const [flashIndex, setFlashIndex] = useState(0);
  const [isFlipped, setIsFlipped] = useState(false);
  const [matchingCards, setMatchingCards] = useState([]);
  const [selectedMatchingCards, setSelectedMatchingCards] = useState([]);
  const [matchedMatchingCards, setMatchedMatchingCards] = useState([]);
  const [matchingBusy, setMatchingBusy] = useState(false);
  const [quizOrder, setQuizOrder] = useState([]);
  const [quizIndex, setQuizIndex] = useState(0);
  const [quizOptions, setQuizOptions] = useState([]);
  const [quizSelectedAnswer, setQuizSelectedAnswer] = useState("");
  const [quizFeedback, setQuizFeedback] = useState(null);
  const [quizScore, setQuizScore] = useState(0);
  const [isQuizComplete, setIsQuizComplete] = useState(false);
  const [showEditItemModal, setShowEditItemModal] = useState(false);
  const [editingItemId, setEditingItemId] = useState("");
  const [editItemForm, setEditItemForm] = useState({ term: "", meaning: "", note: "", example: "" });
  const [editItemLoading, setEditItemLoading] = useState(false);
  const matchingTimerRef = useRef(null);

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

  const currentFlashItem = useMemo(() => {
    if (!items.length || !flashOrder.length) {
      return null;
    }
    const itemIndex = flashOrder[flashIndex] ?? flashOrder[0];
    return items[itemIndex] || null;
  }, [items, flashOrder, flashIndex]);

  const currentQuizItem = useMemo(() => {
    if (!items.length || !quizOrder.length) {
      return null;
    }
    const itemIndex = quizOrder[quizIndex] ?? quizOrder[0];
    return items[itemIndex] || null;
  }, [items, quizOrder, quizIndex]);

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
  }, []);

  useEffect(() => {
    loadItems(selectedSetId);
  }, [selectedSetId]);

  useEffect(() => {
    const defaultOrder = items.map((_, index) => index);
    setFlashOrder(defaultOrder);
    setFlashIndex(0);
    setIsFlipped(false);
    setMatchingCards(buildMatchingCards(items));
    setSelectedMatchingCards([]);
    setMatchedMatchingCards([]);
    setMatchingBusy(false);
    const newQuizOrder = shuffleArray(defaultOrder);
    setQuizOrder(newQuizOrder);
    setQuizIndex(0);
    setQuizSelectedAnswer("");
    setQuizFeedback(null);
    setQuizScore(0);
    setIsQuizComplete(false);
    const firstQuizItem = items[newQuizOrder[0]];
    setQuizOptions(buildQuizOptions(firstQuizItem, items));
  }, [items]);

  useEffect(() => {
    return () => {
      if (matchingTimerRef.current) {
        clearTimeout(matchingTimerRef.current);
      }
    };
  }, []);

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

  const moveToNextFlashcard = () => {
    if (!flashOrder.length) {
      return;
    }
    setFlashIndex((prev) => (prev + 1) % flashOrder.length);
    setIsFlipped(false);
  };

  const moveToPrevFlashcard = () => {
    if (!flashOrder.length) {
      return;
    }
    setFlashIndex((prev) => (prev - 1 + flashOrder.length) % flashOrder.length);
    setIsFlipped(false);
  };

  const shuffleFlashcards = () => {
    if (items.length <= 1) {
      return;
    }

    const original = items.map((_, index) => index);
    let shuffled = shuffleArray(original);
    let retries = 0;

    while (isSameOrder(original, shuffled) && retries < 5) {
      shuffled = shuffleArray(original);
      retries += 1;
    }

    setFlashOrder(shuffled);
    setFlashIndex(0);
    setIsFlipped(false);
  };

  const resetFlashOrder = () => {
    setFlashOrder(items.map((_, index) => index));
    setFlashIndex(0);
    setIsFlipped(false);
  };

  const resetMatchingGame = () => {
    if (matchingTimerRef.current) {
      clearTimeout(matchingTimerRef.current);
    }
    setMatchingCards(buildMatchingCards(items));
    setSelectedMatchingCards([]);
    setMatchedMatchingCards([]);
    setMatchingBusy(false);
  };

  const resetQuizGame = () => {
    const order = shuffleArray(items.map((_, index) => index));
    setQuizOrder(order);
    setQuizIndex(0);
    setQuizSelectedAnswer("");
    setQuizFeedback(null);
    setQuizScore(0);
    setIsQuizComplete(false);
    const firstQuizItem = items[order[0]];
    setQuizOptions(buildQuizOptions(firstQuizItem, items));
  };

  const answerQuizQuestion = (answer) => {
    if (!currentQuizItem || quizSelectedAnswer) {
      return;
    }

    setQuizSelectedAnswer(answer);

    if (answer === currentQuizItem.meaning) {
      setQuizScore((prev) => prev + 1);
      setQuizFeedback({
        type: "success",
        message: "Đáp án chính xác",
      });
      return;
    }

    setQuizFeedback({
      type: "danger",
      message: `Trả lời sai. Đáp án đúng: ${currentQuizItem.meaning}`,
    });
  };

  const nextQuizQuestion = () => {
    if (!quizOrder.length) {
      return;
    }

    // Check if this is the last question
    if (quizIndex === quizOrder.length - 1) {
      setIsQuizComplete(true);
      return;
    }

    const nextIndex = quizIndex + 1;
    const nextQuizItem = items[quizOrder[nextIndex]];
    setQuizIndex(nextIndex);
    setQuizSelectedAnswer("");
    setQuizFeedback(null);
    setQuizOptions(buildQuizOptions(nextQuizItem, items));
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

  const handleMatchingCardClick = (cardId) => {
    if (matchingBusy || matchedMatchingCards.includes(cardId) || selectedMatchingCards.includes(cardId)) {
      return;
    }

    if (selectedMatchingCards.length === 0) {
      setSelectedMatchingCards([cardId]);
      return;
    }

    const firstCardId = selectedMatchingCards[0];
    const secondCardId = cardId;
    const firstCard = matchingCards.find((card) => card.id === firstCardId);
    const secondCard = matchingCards.find((card) => card.id === secondCardId);

    if (!firstCard || !secondCard) {
      setSelectedMatchingCards([]);
      return;
    }

    const isMatched =
      firstCard.pairId === secondCard.pairId && firstCard.type !== secondCard.type;

    setSelectedMatchingCards([firstCardId, secondCardId]);
    setMatchingBusy(true);

    if (matchingTimerRef.current) {
      clearTimeout(matchingTimerRef.current);
    }

    matchingTimerRef.current = setTimeout(() => {
      if (isMatched) {
        setMatchedMatchingCards((prev) => [...prev, firstCardId, secondCardId]);
      }
      setSelectedMatchingCards([]);
      setMatchingBusy(false);
    }, isMatched ? 260 : 700);
  };

  const matchingCompleted =
    matchingCards.length > 0 && matchedMatchingCards.length === matchingCards.length;

  const frontText =
    flashMode === "en" ? currentFlashItem?.term || "" : currentFlashItem?.meaning || "";
  const backText =
    flashMode === "en" ? currentFlashItem?.meaning || "" : currentFlashItem?.term || "";

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
          <div className={selectedSetId ? "col-lg-3" : "col-12"}>
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

                      {learningMode === "flashcard" && (
                        <div className="flashcard-shell border rounded-3 p-3">
                          <div className="d-flex flex-wrap gap-2 justify-content-between align-items-center mb-3">
                            <div className="btn-group" role="group" aria-label="Chọn mặt trước flashcard">
                              <button
                                type="button"
                                className={`btn btn-sm ${flashMode === "en" ? "btn-primary" : "btn-outline-primary"}`}
                                onClick={() => {
                                  setFlashMode("en");
                                  setIsFlipped(false);
                                }}
                              >
                                Mặt trước English
                              </button>
                              <button
                                type="button"
                                className={`btn btn-sm ${flashMode === "vi" ? "btn-primary" : "btn-outline-primary"}`}
                                onClick={() => {
                                  setFlashMode("vi");
                                  setIsFlipped(false);
                                }}
                              >
                                Mặt trước Vietnamese
                              </button>
                            </div>
                            <div className="d-flex gap-2">
                              <button
                                type="button"
                                className="btn btn-sm btn-outline-dark"
                                onClick={shuffleFlashcards}
                              >
                                Đảo thứ tự
                              </button>
                              <button
                                type="button"
                                className="btn btn-sm btn-outline-secondary"
                                onClick={resetFlashOrder}
                              >
                                Về thứ tự gốc
                              </button>
                            </div>
                          </div>

                          <div className="flashcard-meta text-muted small mb-2">
                            <span>
                              Thẻ {flashOrder.length ? flashIndex + 1 : 0}/{flashOrder.length}
                            </span>
                          </div>

                          <button
                            type="button"
                            className={`flashcard-card ${isFlipped ? "is-flipped" : ""}`}
                            onClick={() => setIsFlipped((prev) => !prev)}
                          >
                            <div className="flashcard-face flashcard-front">
                              <h4 className="m-0">{frontText}</h4>
                            </div>
                            <div className="flashcard-face flashcard-back">
                              <h4 className="m-0">{backText}</h4>
                            </div>
                          </button>

                          {(currentFlashItem?.note || currentFlashItem?.example) && (
                            <div className="flashcard-note mt-3 small text-muted">
                              {currentFlashItem?.note && <div>Note: {currentFlashItem.note}</div>}
                              {currentFlashItem?.example && <div>Example: {currentFlashItem.example}</div>}
                            </div>
                          )}

                          <div className="d-flex justify-content-between mt-3">
                            <button
                              type="button"
                              className="btn btn-outline-primary"
                              onClick={moveToPrevFlashcard}
                            >
                              Trước
                            </button>
                            <button
                              type="button"
                              className="btn btn-primary"
                              onClick={moveToNextFlashcard}
                            >
                              Tiếp theo
                            </button>
                          </div>
                        </div>
                      )}

                      {learningMode === "matching" && (
                        <div className="matching-shell border rounded-3 p-3">
                          <div className="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
                            <h6 className="fw-bold m-0">Chế độ ghép thẻ</h6>
                            <button
                              type="button"
                              className="btn btn-sm btn-outline-secondary"
                              onClick={resetMatchingGame}
                            >
                              Chơi lại
                            </button>
                          </div>

                          <div className="small text-muted mb-2">
                            Đã ghép: {matchedMatchingCards.length / 2}/{matchingCards.length / 2}
                          </div>

                          {matchingCompleted && (
                            <div className="alert alert-success py-2">
                              Hoàn thành. Bạn đã ghép đúng tất cả thẻ.
                            </div>
                          )}

                          <div className="matching-grid">
                            {matchingCards
                              .filter((card) => !matchedMatchingCards.includes(card.id))
                              .map((card) => {
                                const isSelected = selectedMatchingCards.includes(card.id);
                                const cardTypeLabel = card.type === "term" ? "EN" : "VI";
                                return (
                                  <button
                                    key={card.id}
                                    type="button"
                                    className={`matching-card ${isSelected ? "is-selected" : ""}`}
                                    onClick={() => handleMatchingCardClick(card.id)}
                                    disabled={matchingBusy}
                                  >
                                    <span className="matching-tag">{cardTypeLabel}</span>
                                    <span>{card.text}</span>
                                  </button>
                                );
                              })}
                          </div>
                        </div>
                      )}

                      {learningMode === "quiz" && (
                        <div className="border rounded-3 p-3 bg-white">
                          {!currentQuizItem ? (
                            <p className="text-muted m-0">Khong du du lieu de tao cau hoi.</p>
                          ) : isQuizComplete ? (
                            <div className="d-grid gap-3">
                              <div className="alert alert-info py-3 mb-0">
                                <h5 className="fw-bold">Hoàn thành bài trắc nghiệm</h5>
                              </div>
                              <div className="text-center py-4">
                                <div className="display-4 fw-bold text-primary mb-2">
                                  {quizScore}/{quizOrder.length}
                                </div>
                                <div className="fs-5 text-muted">
                                  Bạn trả lời đúng {quizScore} trên {quizOrder.length} câu hỏi
                                </div>
                                <div className="text-muted mt-2">
                                  Tỷ lệ: {Math.round((quizScore / quizOrder.length) * 100)}%
                                </div>
                              </div>
                              <div className="d-flex gap-2 justify-content-center">
                                <button
                                  type="button"
                                  className="btn btn-primary"
                                  onClick={resetQuizGame}
                                >
                                  Làm lại
                                </button>
                                <button
                                  type="button"
                                  className="btn btn-outline-secondary"
                                  onClick={() => setLearningMode("list")}
                                >
                                  Quay lại danh sách
                                </button>
                              </div>
                            </div>
                          ) : (
                            <div className="d-grid gap-3">
                              <div className="d-flex justify-content-between align-items-center">
                                <h6 className="fw-bold m-0">
                                  Câu hỏi {quizIndex + 1}/{quizOrder.length} - Điểm: {quizScore}
                                </h6>
                                <button
                                  type="button"
                                  className="btn btn-sm btn-outline-secondary"
                                  onClick={resetQuizGame}
                                >
                                  Trộn lại bộ câu hỏi
                                </button>
                              </div>

                              <div className="quiz-question-box">
                                <div className="text-muted small">Từ tiếng Anh</div>
                                <div className="fs-5 fw-semibold">{currentQuizItem.term}</div>
                              </div>

                              <div className="d-grid gap-2">
                                {quizOptions.map((option, optionIndex) => {
                                  const selected = quizSelectedAnswer === option;
                                  const correct = option === currentQuizItem.meaning;
                                  let optionClass = "btn-outline-primary";

                                  if (quizSelectedAnswer) {
                                    if (correct) {
                                      optionClass = "btn-success";
                                    } else if (selected) {
                                      optionClass = "btn-danger";
                                    } else {
                                      optionClass = "btn-outline-secondary";
                                    }
                                  }

                                  return (
                                    <button
                                      key={`${option}-${optionIndex}`}
                                      type="button"
                                      className={`btn text-start ${optionClass}`}
                                      onClick={() => answerQuizQuestion(option)}
                                      disabled={Boolean(quizSelectedAnswer)}
                                    >
                                      {option}
                                    </button>
                                  );
                                })}
                              </div>

                              {quizFeedback && (
                                <div className={`alert alert-${quizFeedback.type} py-2 m-0`}>
                                  {quizFeedback.message}
                                </div>
                              )}

                              <div className="d-flex justify-content-end">
                                <button
                                  type="button"
                                  className="btn btn-primary"
                                  onClick={nextQuizQuestion}
                                  disabled={!quizSelectedAnswer}
                                >
                                  Câu tiếp theo
                                </button>
                              </div>
                            </div>
                          )}
                        </div>
                      )}
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
