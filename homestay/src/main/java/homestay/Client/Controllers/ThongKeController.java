package homestay.Client.Controllers;

import com.google.gson.Gson;

import homestay.DTOs.BaseDTO;
import homestay.DTOs.ThongKeDTO;

public class ThongKeController {

    private final Gson gson = new Gson();
    private final String dir = "BASE";

    public ThongKeController() {
    }

    /**
     * Lấy báo cáo thống kê tổng hợp từ Server
     * @return ThongKeDTO.BaoCaoTongHop chứa doanh thu, trạng thái phòng và các chỉ số khác
     * @throws Exception khi có lỗi kết nối hoặc dữ liệu không hợp lệ
     */
    public ThongKeDTO.BaoCaoTongHop getBaoCaoTongHop() throws Exception {
        final String action = "GET_DASHBOARD_STATS";
        
        ClientSocketController.ensureConnected();
        
        BaseDTO.Response response = ClientSocketController.sendRequest(dir, action, null, true);

        if (response != null && response.statusCode() == 200 && response.data() != null) {
            return gson.fromJson(response.data(), ThongKeDTO.BaoCaoTongHop.class);
        } else {
            String errorMsg = (response != null) ? response.message() : "Không có phản hồi từ Server";
            throw new Exception("Lỗi khi lấy dữ liệu thống kê: " + errorMsg);
        }
    }

    /**
     * Phương thức bổ trợ nếu bạn muốn lấy riêng danh sách doanh thu tháng
     */
    public java.util.List<ThongKeDTO.DoanhThuThang> getDoanhThuTheoThang() throws Exception {
        ThongKeDTO.BaoCaoTongHop baoCao = getBaoCaoTongHop();
        return baoCao.dsDoanhThu;
    }

    /**
     * Phương thức bổ trợ nếu bạn muốn lấy riêng trạng thái phòng cho biểu đồ tròn (Pie Chart)
     */
    public java.util.List<ThongKeDTO.TrangThaiPhong> getTrangThaiPhong() throws Exception {
        ThongKeDTO.BaoCaoTongHop baoCao = getBaoCaoTongHop();
        return baoCao.dsTrangThai;
    }
}