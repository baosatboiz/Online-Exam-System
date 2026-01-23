Application Layer Architecture
Tầng Application đóng vai trò là bộ não điều phối (Orchestrator), kết nối giữa giao diện người dùng (Web) và nghiệp vụ cốt lõi (Domain). Tầng này được thiết kế theo nguyên lý Clean Architecture và tách biệt giữa các hành động thay đổi trạng thái và truy vấn dữ liệu.

📂 Cấu trúc thư mục (Advanced Lean Structure)
Plaintext

application/
├── command/    # Command DTOs - Ý định thay đổi trạng thái (Create/Update/Delete)
├── query/      # Query DTOs - Yêu cầu truy xuất dữ liệu (Read-only)
├── result/     # Result DTOs (Dữ liệu đầu ra) - Kết quả tinh gọn trả về cho Frontend
├── service/    # Application Services (Implementations) - Điều phối thực thi
└── usecase/    # Input Ports (Interfaces) - Định nghĩa kịch bản sử dụng (Use Cases)
🧩 Các thành phần chính
1. UseCase (Input Ports)
   Bản chất: Là các Java Interface định nghĩa "Cái gì cần làm".

Vai trò: Định nghĩa ranh giới giữa tầng Web và Application. Tầng Web (Controller) chỉ tương tác với Interface này.

Tác dụng: Đảm bảo tính trừu tượng, giúp việc thay đổi logic thực thi hoặc Mocking khi Unit Test trở nên dễ dàng mà không làm ảnh hưởng đến Controller.

2. Service (Implementations)
   Bản chất: Các Class thực thi Interface UseCase (thường được đánh dấu @Service trong Spring).

Nhiệm vụ:

Triệu hồi thực thể thông qua Repository Interface.

Điều phối các logic nghiệp vụ từ các Aggregate Root như Exam, ExamSchedule hay ExamAttempt.

Quản lý giao dịch (@Transactional) để đảm bảo tính toàn vẹn dữ liệu.

Nguyên tắc: Thin Service – Service chỉ điều phối, không chứa logic chấm điểm hay tính toán thời gian; toàn bộ "chất xám" đó phải nằm ở tầng Domain.

3. Command & Query (Input Data Models)
   Command: Chứa dữ liệu cho các hành động làm thay đổi dữ liệu (ví dụ: StartExamCommand, SubmitAnswerCommand).

Query: Chứa các tham số lọc, tìm kiếm để truy xuất dữ liệu (ví dụ: GetExamResultQuery, SearchExamQuery).

Ưu điểm: Tách biệt rõ ràng mục đích của Request, giúp hệ thống dễ mở rộng và bảo trì theo hướng CQRS.

4. Result (Output Data Models)
   Bản chất: Thay thế cho khái niệm DTO truyền thống, tập trung vào kết quả trả về.

Vai trò: Chế biến dữ liệu từ Domain thành định dạng mà Frontend (Next.js) cần.

Bảo mật: Loại bỏ các thông tin nhạy cảm (như đáp án đúng hoặc logic chấm điểm nội bộ) trước khi gửi ra ngoài.

Lưu ý quan trọng: Tầng Application tuyệt đối không được chứa logic nghiệp vụ.