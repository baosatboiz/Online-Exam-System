1. Mục tiêu
Hệ thống hỗ trợ 2 hình thức đăng nhập:
- Email + Password (LOCAL)
- OAuth2 Google Login

Mục tiêu:
- Cho phép user login nhanh bằng Google
- Liên kết Google account với User trong hệ thống

2. Database Design
Bảng user_provider:
- id: PK
- user_business_id: UUID của User
- provider: Enum (GOOGLE, LOCAL)
- provider_id: googleSub (là ID duy nhất của user trên Google)

3. Google Cloud Configuration

- Truy cập Google Cloud Console
- Tạo Project mới
- Enable API: Google Identity Services
- Tạo OAuth 2.0 Client ID
    - Application type: Web application
    - Authorized redirect URI:
      http://localhost:8080/login/oauth2/code/google
- Lấy:
    - Client ID
    - Client Secret
- Cấu hình trong application.yml

4. Luồng tổng quát
   OAuth2LoginImpl.execute(command)
   │
   ├── Tìm UserProvider theo (GOOGLE, googleSub)
   │     │
   │     ├── ✅ TÌM THẤY
   │     │     └── User đã login Google trước đây
   │     │         → Lấy User theo userBusinessId
   │     │         → Generate JWT → trả về
   │     │
   │     └── ❌ KHÔNG TÌM THẤY
   │           │
   │           ├── Tìm User theo email
   │           │     │
   │           │     ├── ✅ EMAIL TỒN TẠI (đã đăng ký LOCAL)
   │           │     │     └── Link thêm GOOGLE provider vào user này
   │           │     │         → Save UserProvider {GOOGLE, googleSub}
   │           │     │         → Generate JWT → trả về
   │           │     │
   │           │     └── ❌ EMAIL KHÔNG TỒN TẠI (user hoàn toàn mới)
   │           │           └── Tạo User mới (password = null)
   │           │               → Save User
   │           │               → Save UserProvider {GOOGLE, googleSub}
   │           │               → Generate JWT → trả về
5. JwtFilter — Thứ tự đọc token
   Request đến
   │
   ├── Đọc Authorization header
   │     └── "Bearer eyJ..." → lấy token
   │
   ├── Nếu không có header → đọc Cookie "access_token"
   │     └── Cookie value → lấy token
   │
   ├── Nếu không có cả 2 → bỏ qua, tiếp tục filter chain
   │
   └── Nếu có token
   → JwtUtils.parseToken() → verify HMAC signature
   → Lấy "sub" (email)
   → Load SecurityUser
   → Set SecurityContextHolder
   → tiếp tục filter chain
6. vai trò của các class 
OAuth2UserService       → nhận thông tin từ Google, gọi use case  
OAuth2UserPrincipal     → wrapper result → OAuth2User             
OAuth2SuccessHandler    → set cookie, redirect
SecurityConfig          → cấu hình permit URL, oauth2Login
JwtFilter               → đọc JWT từ header hoặc cookie