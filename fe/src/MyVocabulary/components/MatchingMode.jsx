import { useEffect, useRef, useState } from "react";

const shuffleArray = (arr) => {
  const copy = [...arr];
  for (let i = copy.length - 1; i > 0; i -= 1) {
    const j = Math.floor(Math.random() * (i + 1));
    [copy[i], copy[j]] = [copy[j], copy[i]];
  }
  return copy;
};

const MAX_MATCH_ITEMS = 6;

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

export default function MatchingMode({ items }) {
  const [matchingCards, setMatchingCards] = useState(() => buildMatchingCards(items));
  const [selectedMatchingCards, setSelectedMatchingCards] = useState([]);
  const [matchedMatchingCards, setMatchedMatchingCards] = useState([]);
  const [matchingBusy, setMatchingBusy] = useState(false);
  const matchingTimerRef = useRef(null);

  useEffect(() => {
    return () => {
      if (matchingTimerRef.current) {
        clearTimeout(matchingTimerRef.current);
      }
    };
  }, []);

  const resetMatchingGame = () => {
    if (matchingTimerRef.current) {
      clearTimeout(matchingTimerRef.current);
    }
    setMatchingCards(buildMatchingCards(items));
    setSelectedMatchingCards([]);
    setMatchedMatchingCards([]);
    setMatchingBusy(false);
  };

  const matchingCompleted =
    matchingCards.length > 0 && matchedMatchingCards.length === matchingCards.length;

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

  return (
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
  );
}
