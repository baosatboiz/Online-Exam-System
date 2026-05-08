import { useState, useEffect } from "react";
import fetchData from "../fetch/fetchData";

export default function AiConfigModal({ show, onClose, onConfigSaved }) {
  const [loading, setLoading] = useState(false);
  const [testingGroq, setTestingGroq] = useState(false);
  const [testingOpenRouter, setTestingOpenRouter] = useState(false);
  const [deletingGroq, setDeletingGroq] = useState(false);
  const [deletingOpenRouter, setDeletingOpenRouter] = useState(false);
  const [groqKey, setGroqKey] = useState("");
  const [openRouterKey, setOpenRouterKey] = useState("");
  const [configs, setConfigs] = useState({});
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  useEffect(() => {
    if (show) {
      loadConfigs();
    }
  }, [show]);

  const loadConfigs = async () => {
    try {
      const groqConfig = await fetchData("/api/ai-config/groq").catch(
        (err) => {
          // Handle 404 errors gracefully (config doesn't exist yet)
          const errorMsg = err?.message || err?.toString() || "";
          if (errorMsg.includes("404") || errorMsg.includes("Not Found")) return null;
          throw err;
        }
      );
      const openrouterConfig = await fetchData("/api/ai-config/openrouter").catch(
        (err) => {
          // Handle 404 errors gracefully (config doesn't exist yet)
          const errorMsg = err?.message || err?.toString() || "";
          if (errorMsg.includes("404") || errorMsg.includes("Not Found")) return null;
          throw err;
        }
      );

      console.log("Loaded configs - Groq:", groqConfig, "OpenRouter:", openrouterConfig);
      setConfigs({
        groq: groqConfig,
        openrouter: openrouterConfig,
      });
    } catch (err) {
      console.log("Error loading configs:", err);
      setError("Lỗi khi tải cấu hình. Vui lòng tải lại trang.");
    }
  };

  const handleSaveGroq = async () => {
    if (!groqKey.trim()) {
      setError("Groq API key không được để trống");
      return;
    }

    setTestingGroq(true);
    setError("");
    setSuccess("");

    try {
      await fetchData("/api/ai-config", {
        method: "POST",
        body: JSON.stringify({
          provider: "groq",
          apiKey: groqKey,
        }),
      });

      setGroqKey("");
      setSuccess("Cấu hình Groq thành công!");
      // Immediately update state to show delete button
      setConfigs((prev) => ({ ...prev, groq: { provider: "groq" } }));
      // Reload configs after a short delay to ensure server has updated
      setTimeout(() => loadConfigs(), 500);
      if (onConfigSaved) onConfigSaved();
    } catch (err) {
      const errorMsg = err?.message || err?.toString() || "Lỗi khi cấu hình Groq. Vui lòng kiểm tra API key.";
      console.error("Groq config error:", err);
      setError(errorMsg);
    } finally {
      setTestingGroq(false);
    }
  };

  const handleSaveOpenRouter = async () => {
    if (!openRouterKey.trim()) {
      setError("OpenRouter API key không được để trống");
      return;
    }

    setTestingOpenRouter(true);
    setError("");
    setSuccess("");

    try {
      await fetchData("/api/ai-config", {
        method: "POST",
        body: JSON.stringify({
          provider: "openrouter",
          apiKey: openRouterKey,
        }),
      });

      setOpenRouterKey("");
      setSuccess("Cấu hình OpenRouter thành công!");
      // Immediately update state to show delete button
      setConfigs((prev) => ({ ...prev, openrouter: { provider: "openrouter" } }));
      // Reload configs after a short delay to ensure server has updated
      setTimeout(() => loadConfigs(), 500);
      if (onConfigSaved) onConfigSaved();
    } catch (err) {
      const errorMsg = err?.message || err?.toString() || "Lỗi khi cấu hình OpenRouter. Vui lòng kiểm tra API key.";
      console.error("OpenRouter config error:", err);
      setError(errorMsg);
    } finally {
      setTestingOpenRouter(false);
    }
  };

  const handleDeleteConfig = async (provider) => {
    if (!window.confirm(`Bạn có chắc muốn xóa cấu hình ${provider}?`)) {
      return;
    }

    // Set state xóa riêng cho mỗi provider
    if (provider === "groq") {
      setDeletingGroq(true);
    } else {
      setDeletingOpenRouter(true);
    }

    setError("");
    setSuccess("");

    try {
      await fetchData(`/api/ai-config/${provider}`, {
        method: "DELETE",
      });

      setSuccess(`Đã xóa cấu hình ${provider}`);
      await loadConfigs();
      if (onConfigSaved) onConfigSaved();
    } catch (err) {
      setError(err.message || `Lỗi khi xóa cấu hình ${provider}`);
    } finally {
      if (provider === "groq") {
        setDeletingGroq(false);
      } else {
        setDeletingOpenRouter(false);
      }
    }
  };

  if (!show) return null;

  return (
    <div className="modal d-block" style={{ backgroundColor: "rgba(0, 0, 0, 0.5)" }}>
      <div className="modal-dialog modal-dialog-centered">
        <div className="modal-content">
          <div className="modal-header">
            <h5 className="modal-title">Cấu hình AI Models</h5>
            <button
              type="button"
              className="btn-close"
              onClick={onClose}
              disabled={loading || testingGroq || testingOpenRouter}
            ></button>
          </div>
          <div className="modal-body d-grid gap-4">
            {error && (
              <div className="alert alert-danger mb-0" style={{ whiteSpace: "pre-line" }}>
                {error}
              </div>
            )}
            {success && <div className="alert alert-success mb-0">{success}</div>}

            {/* Groq Config */}
            <div className="border rounded p-3">
              <h6 className="fw-bold mb-3">Groq API</h6>
              {configs.groq ? (
                <div className="d-grid gap-2">
                  <div className="alert alert-info mb-0 py-2">
                    <small>✓ Groq đã được cấu hình thành công</small>
                  </div>
                  <button
                    type="button"
                    className="btn btn-sm btn-outline-danger"
                    onClick={() => handleDeleteConfig("groq")}
                    disabled={deletingGroq}
                  >
                    {deletingGroq ? "Đang xóa..." : "Xóa cấu hình"}
                  </button>
                </div>
              ) : (
                <div className="d-grid gap-2">
                  <small className="text-muted fw-bold">Hướng dẫn:</small>
                  <small className="text-muted">
                    1. Vào{" "}
                    <a href="https://console.groq.com/keys" target="_blank" rel="noreferrer">
                      Groq Console
                    </a>{" "}
                    đăng nhập/tạo tài khoản<br />
                    2. Tạo API key mới<br />
                    3. Copy toàn bộ khóa (kể cả phần "gsk_...")<br />
                    4. Dán vào ô bên dưới
                  </small>
                  <input
                    type="password"
                    className="form-control form-control-sm"
                    placeholder="gsk_..."
                    value={groqKey}
                    onChange={(e) => setGroqKey(e.target.value)}
                    disabled={testingGroq}
                  />
                  <button
                    type="button"
                    className="btn btn-sm btn-primary"
                    onClick={handleSaveGroq}
                    disabled={testingGroq || testingOpenRouter || loading}
                  >
                    {testingGroq ? "Đang kiểm tra..." : "Lưu & Kiểm tra"}
                  </button>
                </div>
              )}
            </div>

            {/* OpenRouter Config */}
            <div className="border rounded p-3">
              <h6 className="fw-bold mb-3">OpenRouter API</h6>
              {configs.openrouter ? (
                <div className="d-grid gap-2">
                  <div className="alert alert-info mb-0 py-2">
                    <small>✓ OpenRouter đã được cấu hình thành công</small>
                  </div>
                  <button
                    type="button"
                    className="btn btn-sm btn-outline-danger"
                    onClick={() => handleDeleteConfig("openrouter")}
                    disabled={deletingOpenRouter}
                  >
                    {deletingOpenRouter ? "Đang xóa..." : "Xóa cấu hình"}
                  </button>
                </div>
              ) : (
                <div className="d-grid gap-2">
                  <small className="text-muted fw-bold">Hướng dẫn:</small>
                  <small className="text-muted">
                    1. Vào{" "}
                    <a href="https://openrouter.ai/keys" target="_blank" rel="noreferrer">
                      OpenRouter
                    </a>{" "}
                    đăng nhập/tạo tài khoản<br />
                    2. Tạo API key mới<br />
                    3. Nạp credit vào tài khoản (miễn phí $5)<br />
                    4. Copy toàn bộ khóa<br />
                    5. Dán vào ô bên dưới
                  </small>
                  <input
                    type="password"
                    className="form-control form-control-sm"
                    placeholder="sk-..."
                    value={openRouterKey}
                    onChange={(e) => setOpenRouterKey(e.target.value)}
                    disabled={testingOpenRouter}
                  />
                  <button
                    type="button"
                    className="btn btn-sm btn-primary"
                    onClick={handleSaveOpenRouter}
                    disabled={testingOpenRouter || testingGroq || loading}
                  >
                    {testingOpenRouter ? "Đang kiểm tra..." : "Lưu & Kiểm tra"}
                  </button>
                </div>
              )}
            </div>
          </div>
          <div className="modal-footer">
            <button
              type="button"
              className="btn btn-secondary"
              onClick={onClose}
              disabled={loading || testingGroq || testingOpenRouter}
            >
              Đóng
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
