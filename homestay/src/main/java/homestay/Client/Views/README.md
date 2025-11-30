### Thư mục chứa các phần liên quan tới giao diện

### Tổ chức code
`HomeView` là nơi chứa logic điều hướng chính, là khung chứa các View khác khác.
- Khi muốn tạo thêm `View` tiến hành tạo File mới và tiến hành khởi tạo View sau đó 
gán vào `HomeView`. ***Ví dụ*** : `DashboardView.java`
- Quy tắc thêm `View` mới vào `HomeView`:
    + Khởi tạo `View` mới ở phía trên đảm bảo có khoảng trắng giữa các `View`
    + Logic hiển thị `View` ở giữa hàm và tất cả hiển thị liên quan ở cuối
    + Đảm bảo căn lề, clean code, comments rõ ràng

- Các hàm dùng nhiều lần phải phân tách và đưa vào lớp Components hoặc tạo lớp Utils riêng
- Các hình ảnh phải bỏ vào resources hoặc folders liên quan, không để trộn lẫn với mã nguồn
