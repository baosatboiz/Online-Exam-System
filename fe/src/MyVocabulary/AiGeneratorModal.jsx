import { useState, useEffect } from "react";
import fetchData from "../fetch/fetchData";

export default function AiGeneratorModal({
  show,
  onClose,
  term,
  onGenerated,
  availableProviders,
}) {
  const [loading, setLoading] = useState(false);
  const [selectedProvider, setSelectedProvider] = useState(availableProviders?.[0] || "groq");
  const [generatedData, setGeneratedData] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    if (availableProviders?.length) {
      setSelectedProvider(availableProviders[0]);
    }
  }, [availableProviders]);

  const handleGenerate = async () => {
    if (!term || !term.trim()) {
      setError("Vui lòng nhập từ trước");
      return;
    }

    setLoading(true);
    setError("");
    setGeneratedData(null);

    try {
      // Call to backend to generate vocabulary
      const response = await fetchData("/api/ai/generate-vocabulary", {
        method: "POST",
        body: JSON.stringify({
          word: term,
          provider: selectedProvider,
        }),
      });

      if (response) {
        setGeneratedData(response);
      } else {
        setError("Không thể tạo từ vựng. Vui lòng thử lại.");
      }
    } catch (err) {
      setError(err.message || "Lỗi khi tạo từ vựng từ AI");
    } finally {
      setLoading(false);
    }
  };

  const handleApply = () => {
    if (generatedData && onGenerated) {
      onGenerated(generatedData);
      onClose();
    }
  };

  if (!show) return null;

  return (
    <div className="modal d-block" style={{ backgroundColor: "rgba(0, 0, 0, 0.5)", zIndex: 12000 }}>
      <div className="modal-dialog modal-dialog-centered">
        <div className="modal-content">
          <div className="modal-header">
            <h5 className="modal-title">Tạo từ vựng với AI</h5>
            <button
              type="button"
              className="btn-close"
              onClick={onClose}
              disabled={loading}
            ></button>
          </div>
          <div className="modal-body d-grid gap-3">
            {error && <div className="alert alert-danger mb-0">{error}</div>}

            <div>
              <label className="form-label">Từ cần tạo</label>
              <input
                type="text"
                className="form-control"
                value={term}
                disabled
              />
            </div>

            {availableProviders?.length > 1 && (
              <div>
                <label className="form-label">Chọn AI Model</label>
                <select
                  className="form-select"
                  value={selectedProvider}
                  onChange={(e) => setSelectedProvider(e.target.value)}
                  disabled={loading}
                >
                  {availableProviders.map((provider) => (
                    <option key={provider} value={provider}>
                      {provider === "groq" ? "Groq" : "OpenRouter"}
                    </option>
                  ))}
                </select>
              </div>
            )}

            {!generatedData && (
              <button
                type="button"
                className="btn btn-primary"
                onClick={handleGenerate}
                disabled={loading || !availableProviders?.length}
              >
                {loading ? "Đang tạo..." : "Tạo từ vựng"}
              </button>
            )}

            {generatedData && (
              <div className="border rounded p-3 bg-light">
                <h6 className="fw-bold mb-2">Kết quả tạo:</h6>
                <div className="d-grid gap-2 small">
                  {generatedData.meaning && (
                    <div>
                      <strong>Meaning:</strong>
                      <div className="text-muted">{generatedData.meaning}</div>
                    </div>
                  )}
                  {generatedData.note && (
                    <div>
                      <strong>Note:</strong>
                      <div className="text-muted">{generatedData.note}</div>
                    </div>
                  )}
                  {generatedData.example && (
                    <div>
                      <strong>Example:</strong>
                      <div className="text-muted">{generatedData.example}</div>
                    </div>
                  )}
                </div>
              </div>
            )}
          </div>
          <div className="modal-footer">
            {generatedData ? (
              <>
                <button
                  type="button"
                  className="btn btn-secondary"
                  onClick={() => setGeneratedData(null)}
                  disabled={loading}
                >
                  Tạo lại
                </button>
                <button
                  type="button"
                  className="btn btn-primary"
                  onClick={handleApply}
                >
                  Áp dụng
                </button>
              </>
            ) : (
              <button
                type="button"
                className="btn btn-secondary"
                onClick={onClose}
                disabled={loading}
              >
                Hủy
              </button>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
