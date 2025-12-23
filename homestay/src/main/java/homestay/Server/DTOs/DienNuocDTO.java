package homestay.Server.DTOs;

public class DienNuocDTO {

    // View: Hiển thị tất cả thông tin, chỉ dùng Constructor để đổ dữ liệu
    public static class View {
        public int maDienNuoc;
        public int maPhong;
        public int thang;
        public int nam;
        public int chiSoDienCu;
        public int chiSoDienMoi;
        public int soDienTieuThu;
        public int chiSoNuocCu;
        public int chiSoNuocMoi;
        public int soNuocTieuThu;

        public View(int maDienNuoc, int maPhong, int thang, int nam, 
                    int chiSoDienCu, int chiSoDienMoi, int soDienTieuThu, 
                    int chiSoNuocCu, int chiSoNuocMoi, int soNuocTieuThu) {
            this.maDienNuoc = maDienNuoc;
            this.maPhong = maPhong;
            this.thang = thang;
            this.nam = nam;
            this.chiSoDienCu = chiSoDienCu;
            this.chiSoDienMoi = chiSoDienMoi;
            this.soDienTieuThu = soDienTieuThu;
            this.chiSoNuocCu = chiSoNuocCu;
            this.chiSoNuocMoi = chiSoNuocMoi;
            this.soNuocTieuThu = soNuocTieuThu;
        }
    }

    // Create: Chỉ dùng để tạo mới, chỉ chứa mã phòng và Getter
    public static class Create {
        private int maPhong;

        public Create(int maPhong) {
            this.maPhong = maPhong;
        }

        public int getMaPhong() {
            return maPhong;
        }
    }

    // Update: Dùng để cập nhật chỉ số, chỉ chứa ID và 2 chỉ số mới cùng Getter
    public static class Update {
        private int maDienNuoc;
        private int chiSoDienMoi;
        private int chiSoNuocMoi;

        public Update(int maDienNuoc, int chiSoDienMoi, int chiSoNuocMoi) {
            this.maDienNuoc = maDienNuoc;
            this.chiSoDienMoi = chiSoDienMoi;
            this.chiSoNuocMoi = chiSoNuocMoi;
        }

        public int getMaDienNuoc() {
            return maDienNuoc;
        }

        public int getChiSoDienMoi() {
            return chiSoDienMoi;
        }

        public int getChiSoNuocMoi() {
            return chiSoNuocMoi;
        }
    }
}