import { useMemo, useState } from "react";

const shuffleArray = (arr) => {
  const copy = [...arr];
  for (let i = copy.length - 1; i > 0; i -= 1) {
    const j = Math.floor(Math.random() * (i + 1));
    [copy[i], copy[j]] = [copy[j], copy[i]];
  }
  return copy;
};

const MAX_QUIZ_OPTIONS = 4;

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

export default function QuizMode({ items }) {
  const [quizOrder, setQuizOrder] = useState(() =>
    shuffleArray(items.map((_, index) => index))
  );
  const [quizIndex, setQuizIndex] = useState(0);
  const [quizOptions, setQuizOptions] = useState(() => {
    const firstItem = items[quizOrder[0]];
    return buildQuizOptions(firstItem, items);
  });
  const [quizSelectedAnswer, setQuizSelectedAnswer] = useState("");
  const [quizFeedback, setQuizFeedback] = useState(null);
  const [quizScore, setQuizScore] = useState(0);
  const [isQuizComplete, setIsQuizComplete] = useState(false);

  const currentQuizItem = useMemo(() => {
    if (!items.length || !quizOrder.length) {
      return null;
    }
    const itemIndex = quizOrder[quizIndex] ?? quizOrder[0];
    return items[itemIndex] || null;
  }, [items, quizOrder, quizIndex]);

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

  return (
    <div className="border rounded-3 p-3 bg-white">
      {!currentQuizItem ? (
        <p className="text-muted m-0">Không có dữ liệu để tạo câu hỏi.</p>
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
            <button type="button" className="btn btn-primary" onClick={resetQuizGame}>
              Làm lại
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
  );
}
