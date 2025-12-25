package homestay.Client.Controllers;

import com.google.gson.Gson;

import homestay.Client.Helper.TableMapper;
import homestay.DTOs.BaseDTO;
import homestay.DTOs.HopDongDTO;

public class ContractController {

    private final Gson gson = new Gson();
    private final String dir = "HOP_DONG";

    public ContractController() {
    }

    /**
     * THÊM HỢP ĐỒNG
     */
    public void handleAddContract(Object[] rowData) throws Exception {
        // 1. Chuyển đổi dữ liệu từ dòng của bảng
        HopDongDTO.Create dto = TableMapper.mapRowToContractCreate(rowData);

        if (dto == null) {
            throw new Exception("Dữ liệu dòng không hợp lệ hoặc bị thiếu thông tin.");
        }

        // 2. Kiểm tra tính hợp lệ của dữ liệu (Validate)
        validateContract(dto.tenKhachHang(), dto.soDienThoai());

        try {
            // 3. Kết nối và gửi yêu cầu
            ClientSocketController.ensureConnected();
            BaseDTO.Response response = ClientSocketController.sendRequest(dir, "CREATE_CONTRACT", dto, true);

            if (response == null) {
                throw new Exception("Máy chủ không phản hồi.");
            }

            // 4. Nếu Server báo lỗi (Ví dụ: Phòng đã có người thuê)
            if (response.statusCode() != 200) {
                throw new Exception(response.message());
            }
        } catch (Exception e) {
            throw new Exception("Lỗi khi thêm hợp đồng: " + e.getMessage());
        }
    }

    /**
     * CẬP NHẬT HỢP ĐỒNG
     */
    public void handleUpdateContract(int contractId, Object[] rowData) throws Exception {
        HopDongDTO.Update dto = TableMapper.mapRowToContractUpdate(contractId, rowData);

        if (dto == null) {
            throw new Exception("Dữ liệu cập nhật không hợp lệ.");
        }

        try {
            ClientSocketController.ensureConnected();
            BaseDTO.Response response = ClientSocketController.sendRequest(dir, "UPDATE_CONTRACT", dto, true);

            if (response == null) {
                throw new Exception("Máy chủ không phản hồi.");
            }

            if (response.statusCode() != 200) {
                throw new Exception(response.message());
            }
        } catch (Exception e) {
            throw new Exception("Lỗi khi cập nhật hợp đồng: " + e.getMessage());
        }
    }

    /**
     * XÓA HỢP ĐỒNG
     */
    public void handleDeleteContract(int contractId) throws Exception {
        if (contractId <= 0) {
            throw new Exception("Mã hợp đồng không hợp lệ.");
        }

        HopDongDTO.Delete deleteDto = new HopDongDTO.Delete(contractId);

        try {
            ClientSocketController.ensureConnected();
            BaseDTO.Response response = ClientSocketController.sendRequest(dir, "DELETE_CONTRACT", deleteDto, true);

            if (response == null) {
                throw new Exception("Máy chủ không phản hồi.");
            }

            if (response.statusCode() != 200) {
                throw new Exception(response.message());
            }
        } catch (Exception e) {
            throw new Exception("Lỗi khi xóa hợp đồng: " + e.getMessage());
        }
    }

    /**
     * LẤY DANH SÁCH HỢP ĐỒNG
     */
    public HopDongDTO.ListHopDong getContracts() throws Exception {
        try {
            ClientSocketController.ensureConnected();
            BaseDTO.Response response = ClientSocketController.sendRequest(dir, "GET_CONTRACTS", null, true);

            if (response != null && response.statusCode() == 200 && response.data() != null) {
                return gson.fromJson(response.data(), HopDongDTO.ListHopDong.class);
            }

            String errorMsg = (response != null) ? response.message() : "Không có phản hồi từ Server";
            throw new Exception(errorMsg);
        } catch (Exception e) {
            throw new Exception("Không thể lấy danh sách hợp đồng: " + e.getMessage());
        }
    }

    /**
     * HỦY HỢP ĐỒNG (Cập nhật trạng thái thành Cancelled)
     */
    public void handleCancelContract(int contractId) throws Exception {
        try {
            ClientSocketController.ensureConnected();
            // Bạn có thể dùng chung DTO Delete hoặc tạo DTO Cancel riêng tùy Server
            BaseDTO.Response response = ClientSocketController.sendRequest(dir, "CANCEL_CONTRACT", contractId, true);

            if (response == null) {
                throw new Exception("Máy chủ không phản hồi.");
            }
            if (response.statusCode() != 200) {
                throw new Exception(response.message());
            }

        } catch (Exception e) {
            throw new Exception("Lỗi khi hủy hợp đồng: " + e.getMessage());
        }
    }

    private void validateContract(String ten, String sdt) throws Exception {
        if (ten == null || ten.trim().isEmpty()) {
            throw new Exception("Tên khách hàng không được để trống.");
        }
        if (sdt == null || !sdt.matches("\\d{10,11}")) {
            throw new Exception("Số điện thoại không hợp lệ (phải từ 10-11 chữ số).");
        }
    }
}
