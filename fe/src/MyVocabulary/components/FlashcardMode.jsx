import { useMemo, useState } from "react";

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

export default function FlashcardMode({ items }) {
  const [flashMode, setFlashMode] = useState("en");
  const [flashOrder, setFlashOrder] = useState(items.map((_, index) => index));
  const [flashIndex, setFlashIndex] = useState(0);
  const [isFlipped, setIsFlipped] = useState(false);

  const currentFlashItem = useMemo(() => {
    if (!items.length || !flashOrder.length) {
      return null;
    }
    const itemIndex = flashOrder[flashIndex] ?? flashOrder[0];
    return items[itemIndex] || null;
  }, [items, flashOrder, flashIndex]);

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

  const frontText =
    flashMode === "en" ? currentFlashItem?.term || "" : currentFlashItem?.meaning || "";
  const backText =
    flashMode === "en" ? currentFlashItem?.meaning || "" : currentFlashItem?.term || "";

  return (
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
  );
}
