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

    public boolean handleAddContract(Object[] rowData) {
        HopDongDTO.Create dto = TableMapper.mapRowToContractCreate(rowData);

        if (dto == null) {
            return false;
        }

        try {
            validateContract(dto.tenKhachHang(), dto.soDienThoai());
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return false;
        }

        final String action = "CREATE_CONTRACT";
        ClientSocketController.ensureConnected();
        BaseDTO.Response response
                = ClientSocketController.sendRequest(dir, action, dto, true);

        return response != null && response.statusCode() == 200;
    }

    public boolean handleUpdateContract(int contractId, Object[] rowData) {
        HopDongDTO.Update dto = TableMapper.mapRowToContractUpdate(contractId, rowData);

        if (dto == null) {
            return false;
        }

        final String action = "UPDATE_CONTRACT";
        ClientSocketController.ensureConnected();
        BaseDTO.Response response
                = ClientSocketController.sendRequest(dir, action, dto, true);

        return response != null && response.statusCode() == 200;
    }

    public boolean handleDeleteContract(int contractId) throws Exception {
        HopDongDTO.Delete deleteDto = new HopDongDTO.Delete(contractId);

        final String action = "DELETE_CONTRACT";
        ClientSocketController.ensureConnected();
        BaseDTO.Response response
                = ClientSocketController.sendRequest(dir, action, deleteDto, true);

        if (response != null && response.statusCode() == 200) {
            return true;
        } else {
            throw new Exception(response != null ? response.message() : "Lỗi không xác định");
        }
    }

    public HopDongDTO.ListHopDong getContracts() throws Exception {
        final String action = "GET_CONTRACTS";
        ClientSocketController.ensureConnected();
        BaseDTO.Response response
                = ClientSocketController.sendRequest(dir, action, null, true);

        if (response != null && response.statusCode() == 200 && response.data() != null) {
            return gson.fromJson(response.data(), HopDongDTO.ListHopDong.class);
        }
        throw new Exception("Không thể lấy danh sách hợp đồng: " + (response != null ? response.message() : "No response"));
    }

    private void validateContract(String ten, String sdt) {
        if (ten == null || ten.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên khách hàng không được để trống");
        }
        if (sdt == null || !sdt.matches("\\d{10,11}")) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ (10-11 số)");
        }
    }
}
