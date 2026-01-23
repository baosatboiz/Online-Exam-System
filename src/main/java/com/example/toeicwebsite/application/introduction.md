Application Layer Architecture
Tầng Application đóng vai trò là bộ não điều phối (Orchestrator), kết nối giữa giao diện người dùng (Web) và nghiệp vụ cốt lõi (Domain). Tầng này được thiết kế theo nguyên lý Clean Architecture để đảm bảo tính độc lập và dễ kiểm thử.

📂 Cấu trúc thư mục (Lean Structure)
Plaintext

application/
├── usecase/    # Input Ports (Interfaces) - Định nghĩa "Cái gì cần làm"
├── service/    # Application Services (Implementations) - Điều phối thực thi
├── command/    # Request DTOs (Dữ liệu đầu vào) - Ý định thay đổi trạng thái
└── dto/        # Response DTOs (Dữ liệu đầu ra) - Kết quả trả về cho Web
🧩 Các thành phần chính
1. UseCase (Input Ports)
   Bản chất: Là các Java Interface.

Vai trò: Định nghĩa ranh giới giữa tầng Web và Application. Tầng Web (Controller) chỉ được biết đến Interface này.

Tác dụng: Giúp Mocking cực nhanh khi Unit Test, tiết kiệm RAM CPU cho IDE.

2. Service (Implementations)
   Bản chất: Các Class thực thi Interface UseCase.

Nhiệm vụ: * Triệu hồi thực thể từ Database thông qua Output Port (Repository Interface).

Gọi các logic nghiệp vụ dày đặc từ Domain Model (ví dụ: calculateScore, isReal).

Thực hiện giao dịch (Transactions).

Nguyên tắc: Service phải "mỏng" (Thin Service) – không chứa logic chấm điểm hay tính toán thời gian, toàn bộ logic đó nằm ở Domain.

3. Command (Input Data)
   Bản chất: Thường dùng Java record để đạt được tính bất biến (Immutable).

Vai trò: Chứa mọi dữ liệu cần thiết để thực hiện một hành động (ví dụ: examId, questionId, choiceKey).

Ưu điểm: Loại bỏ boilerplate code (Getter/Setter), cực nhẹ bộ nhớ.

4. DTO (Output Data)
   Bản chất: Các đối tượng vận chuyển dữ liệu trả về cho Frontend.

Vai trò: Che giấu các thông tin nhạy cảm của Domain (như đáp án đúng hoặc ID hệ thống không cần thiết).