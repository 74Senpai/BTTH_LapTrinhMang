### 📝 Mô tả dự án
Ứng dụng quản lý homestay được thiết kế theo kiến trúc **Client-Server** sử dụng giao thức **TCP/IP Socket**. Dự án áp dụng mô hình **MVC** để tách biệt giao diện, xử lý nghiệp vụ và truy xuất dữ liệu.

- **Server:** Quản lý cơ sở dữ liệu, tiếp nhận yêu cầu từ Client, xử lý tính toán và phản hồi dữ liệu.
- **Client:** Cung cấp giao diện đồ họa (Swing) giúp nhân viên tương tác: quản lý phòng, đặt phòng, chốt điện nước và xem báo cáo.

### 🛠 Công nghệ sử dụng
- **Ngôn ngữ:** Java (JDK 17+)
- **Giao tiếp:** TCP/IP Socket (Object Serialization)
- **Giao diện:** Java Swing, AWT
- **Quản lý dự án:** Maven
- **Cơ sở dữ liệu:** 
    - **Quan hệ:** MySQL (Lưu trữ dữ liệu nghiệp vụ).
    - **Phi quan hệ:** File Log (`server-log.txt`) lưu trữ nhật ký hành vi.

---

### 🚀 Hướng dẫn cài đặt và khởi chạy (via Bash)

#### 1. Yêu cầu hệ thống
*   Đã cài đặt **JDK 17** hoặc cao hơn.
*   Đã cài đặt **Maven**.

#### 2. Tải mã nguồn
```bash
git clone https://github.com/74Senpai/BTTH_LapTrinhMang.git
cd BTTH_LapTrinhMang/homestay
```

#### 3. Cấu hình Cơ sở dữ liệu
Dự án đã được cấu hình sẵn **Cloud MySQL**. Giảng viên có thể chạy trực tiếp mà không cần cài đặt SQL cục bộ.

> [!WARNING]
> **Lưu ý về tốc độ:** Do sử dụng Cloud Database (Free Plan) đặt tại máy chủ nước ngoài, mỗi truy vấn (như đăng nhập, tải danh sách phòng) sẽ có độ trễ vật lý từ **1 - 3 giây**. Vui lòng đợi trong giây lát để dữ liệu được phản hồi.

*Nếu muốn chạy Database local để có tốc độ cao hơn:* Import file SQL trong thư mục `database/` và cập nhật thông tin tại file `homestay/.env`.

#### 4. Build và Chạy ứng dụng
Để đảm bảo tính ổn định (tránh race condition), ứng dụng khởi chạy đồng thời 1 Server và 1 Client thông qua lớp Main tổng hợp:

```bash
# Cài đặt thư viện và build dự án
mvn clean install

# Khởi chạy chế độ 1 Server - 1 Client
mvn exec:java -Dexec.mainClass="homestay.Main"
```

---

### 🔐 Thông tin tài khoản
- **Tên đăng nhập:** `admin`
- **Mật khẩu:** `123456`

---

### 📁 Cấu trúc mã nguồn
```text
BTTH_LapTrinhMang
├───database               # Chứa file SQL tạo database
└───homestay
    └───src/main/java/homestay
        ├───Client         # Giao diện (Views) và Điều hướng (Controllers)
        ├───Server         # Xử lý Socket, Dịch vụ (Services) và Truy vấn (DAO)
        ├───DTOs           # Các đối tượng truyền tải dữ liệu giữa Client - Server
        └───Main.java      # File khởi tạo tích hợp hệ thống
```

### ✅ Danh sách chức năng (Checklist)
- [x] **Kết nối Socket:** Trao đổi dữ liệu qua giao thức TCP/IP.
- [x] **Xác thực:** Đăng nhập và quản lý phiên làm việc cơ bản.
- [x] **Quản lý phòng:** Xem trạng thái, thêm/sửa/xóa phòng (CRUD).
- [x] **Quản lý khách:** Lưu trữ thông tin khách hàng đang lưu trú.
- [x] **Dịch vụ:** Tính tiền điện, nước, internet theo chỉ số.
- [x] **Thống kê:** Báo cáo tình trạng phòng và tổng thu doanh thu.
- [x] **Logging:** Lưu vết hành vi người dùng vào tệp `server-log.txt` (Dạng NoSQL file).
- [x] **Database:** Kết nối MySQL linh hoạt (Local/Cloud).

---
### 👥 Tác giả
- **Nhóm 67 - Đề tài 7:** Quản lý homestay.

