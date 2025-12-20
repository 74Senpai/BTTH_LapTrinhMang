package homestay.Client.Helper;

import java.util.List;

import homestay.Client.DTOs.RoomDTO;

public class TableMapper {

    public static Object[] mapRoomToRow(RoomDTO.ViewRoomDTO r) {
        if (r == null) {
            return null;
        }

        return new Object[]{
            r.getMaPhong(),
            r.getTenPhong(),
            r.getTenTrangThai(),
            r.getGiaThueNgay(),
            r.getGiaThueThang(),
            r.getSoDienHienTai(),
            r.getSoNuocHienTai()
        };
    }

    public static Object[][] mapRoomListToTableData(List<RoomDTO.ViewRoomDTO> rooms) {
        if (rooms == null || rooms.isEmpty()) {
            return new Object[0][0];
        }

        Object[][] data = new Object[rooms.size()][7];

        for (int i = 0; i < rooms.size(); i++) {
            RoomDTO.ViewRoomDTO r = rooms.get(i);

            data[i][0] = r.getMaPhong();
            data[i][1] = r.getTenPhong();
            data[i][2] = r.getTenTrangThai();
            data[i][3] = r.getGiaThueNgay();
            data[i][4] = r.getGiaThueThang();
            data[i][5] = r.getSoDienHienTai();
            data[i][6] = r.getSoNuocHienTai();
        }

        return data;
    }

}
