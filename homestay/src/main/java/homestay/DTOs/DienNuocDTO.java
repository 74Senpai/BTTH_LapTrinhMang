package homestay.DTOs;

import java.util.List;

public class DienNuocDTO {

    public record View(
            int maDienNuoc,
            int maPhong,
            int thang,
            int nam,
            int chiSoDienCu,
            int chiSoDienMoi,
            int soDienTieuThu,
            int chiSoNuocCu,
            int chiSoNuocMoi,
            int soNuocTieuThu
            ) {

    }

    public record Create(int maPhong) {

    }

    public record Update(
            int maDienNuoc,
            int chiSoDienMoi,
            int chiSoNuocMoi
            ) {

    }

    public static class ListDienNuoc {

        private List<DienNuocDTO.View> records;

        public ListDienNuoc(List<DienNuocDTO.View> records) {
            this.records = records;
        }

        public List<DienNuocDTO.View> getRecords() {
            return records;
        }

        public void addRecord(DienNuocDTO.View record) {
            records.add(record);
        }
    }
}
