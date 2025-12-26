
--
-- Database: `b9g3nn8ghmeiozxexg2b`
--
CREATE DATABASE IF NOT EXISTS `b9g3nn8ghmeiozxexg2b` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `b9g3nn8ghmeiozxexg2b`;

-- --------------------------------------------------------

--
-- Table structure for table `DienNuocHangThang`
--

CREATE TABLE `DienNuocHangThang` (
  `MaDienNuoc` int NOT NULL,
  `MaPhong` int NOT NULL,
  `Thang` int NOT NULL,
  `Nam` int NOT NULL,
  `ChiSoDienCu` int DEFAULT NULL,
  `ChiSoDienMoi` int DEFAULT NULL,
  `SoDienTieuThu` int GENERATED ALWAYS AS ((`ChiSoDienMoi` - `ChiSoDienCu`)) VIRTUAL,
  `ChiSoNuocCu` int DEFAULT NULL,
  `ChiSoNuocMoi` int DEFAULT NULL,
  `SoNuocTieuThu` int GENERATED ALWAYS AS ((`ChiSoNuocMoi` - `ChiSoNuocCu`)) VIRTUAL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `DienNuocHangThang`
--

INSERT INTO `DienNuocHangThang` (`MaDienNuoc`, `MaPhong`, `Thang`, `Nam`, `ChiSoDienCu`, `ChiSoDienMoi`, `ChiSoNuocCu`, `ChiSoNuocMoi`) VALUES
(1, 1, 11, 2025, 0, 120, 0, 60),
(2, 2, 11, 2025, 0, 250, 0, 90),
(3, 5, 11, 2025, 0, 180, 0, 70),
(4, 6, 11, 2025, 0, 300, 0, 200),
(5, 8, 11, 2025, 0, 10564, 0, 36),
(6, 1, 12, 2025, 120, 360, 60, 110),
(7, 2, 12, 2025, 250, 420, 90, 150),
(8, 5, 12, 2025, 180, 350, 70, 130),
(9, 6, 12, 2025, 300, 520, 120, 200),
(10, 8, 12, 2025, 200, 380, 85, 150),
(11, 10, 12, 2025, 0, 777, 0, 36),
(12, 4, 12, 2025, 0, 20000, 0, 0),
(13, 7, 12, 2025, 0, 0, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `HoaDon`
--

CREATE TABLE `HoaDon` (
  `MaThanhToan` int NOT NULL,
  `MaHopDong` int NOT NULL,
  `MaDienNuoc` int DEFAULT NULL,
  `TienPhong` decimal(15,2) DEFAULT '0.00',
  `TienChiPhiPhu` decimal(15,2) DEFAULT '0.00',
  `TongTien` decimal(15,2) DEFAULT '0.00',
  `NgayThanhToan` datetime DEFAULT CURRENT_TIMESTAMP,
  `TrangThaiThanhToan` tinyint DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `HoaDon`
--

INSERT INTO `HoaDon` (`MaThanhToan`, `MaHopDong`, `MaDienNuoc`, `TienPhong`, `TienChiPhiPhu`, `TongTien`, `NgayThanhToan`, `TrangThaiThanhToan`) VALUES
(1, 1, 1, 5000000.00, 300000.00, 5300000.00, '2025-12-23 14:27:54', 1),
(2, 2, 2, 5000000.00, 350000.00, 5350000.00, '2025-12-23 14:27:54', 0),
(3, 3, 3, 5500000.00, 400000.00, 5900000.00, '2025-12-23 14:27:54', 1),
(4, 4, NULL, 1500000.00, 0.00, 1500000.00, '2025-12-23 14:27:54', 1),
(5, 5, NULL, 600000.00, 0.00, 600000.00, '2025-12-23 14:27:54', 1),
(12, 18, NULL, 5000000.00, 20000.00, 5020000.00, '2025-12-25 15:55:27', 1);

-- --------------------------------------------------------

--
-- Table structure for table `HopDongThue`
--

CREATE TABLE `HopDongThue` (
  `MaHopDong` int NOT NULL,
  `MaKhachHang` int NOT NULL,
  `MaPhong` int NOT NULL,
  `MaNhanVien` int DEFAULT NULL,
  `LoaiHinhThue` enum('Ngày','Tháng') NOT NULL,
  `NgayBatDau` date NOT NULL,
  `NgayKetThuc` date NOT NULL,
  `TrangThaiHopDong` enum('Active','Completed','Cancelled') DEFAULT 'Active'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `HopDongThue`
--

INSERT INTO `HopDongThue` (`MaHopDong`, `MaKhachHang`, `MaPhong`, `MaNhanVien`, `LoaiHinhThue`, `NgayBatDau`, `NgayKetThuc`, `TrangThaiHopDong`) VALUES
(1, 1, 1, 2, 'Tháng', '2025-10-01', '2025-12-31', 'Cancelled'),
(2, 2, 2, 2, 'Tháng', '2025-11-15', '2026-05-15', 'Cancelled'),
(3, 3, 5, 3, 'Tháng', '2025-11-01', '2026-01-01', 'Cancelled'),
(4, 4, 8, 3, 'Ngày', '2025-12-20', '2025-12-25', 'Cancelled'),
(5, 5, 3, 2, 'Ngày', '2025-11-10', '2025-11-12', 'Cancelled'),
(6, 6, 6, 1, 'Tháng', '2025-12-01', '2026-06-01', 'Cancelled'),
(7, 7, 7, 3, 'Tháng', '2025-09-01', '2025-11-01', 'Cancelled'),
(8, 8, 10, 2, 'Ngày', '2025-12-22', '2025-12-24', 'Cancelled'),
(9, 9, 9, 4, 'Tháng', '2026-01-01', '2026-06-30', 'Cancelled'),
(10, 10, 4, 5, 'Ngày', '2026-01-10', '2026-01-15', 'Cancelled'),
(11, 11, 12, 1, 'Tháng', '2025-12-23', '2026-01-22', 'Cancelled'),
(14, 14, 14, 1, 'Tháng', '2025-12-25', '2026-01-24', 'Cancelled'),
(15, 15, 10, 1, 'Ngày', '2025-12-25', '2025-12-26', 'Cancelled'),
(16, 14, 13, 1, 'Ngày', '2025-12-25', '2025-12-25', 'Cancelled'),
(17, 14, 15, 1, 'Ngày', '2025-12-25', '2025-12-25', 'Cancelled'),
(18, 16, 1, 1, 'Tháng', '2025-12-25', '2026-01-24', 'Active');

-- --------------------------------------------------------

--
-- Table structure for table `KhachHang`
--

CREATE TABLE `KhachHang` (
  `MaKH` int NOT NULL,
  `HoTen` varchar(100) NOT NULL,
  `SoDienThoai` varchar(15) NOT NULL,
  `CCCD` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `KhachHang`
--

INSERT INTO `KhachHang` (`MaKH`, `HoTen`, `SoDienThoai`, `CCCD`) VALUES
(1, 'Vua Da Nang', '0999999999', '001090000001'),
(2, 'Phạm Thị Bình', '0909333444', '001090000002'),
(3, 'Nguyễn Văn Cường', '0909555666', '001090000003'),
(4, 'Trần Trung Dũng', '0909777888', '001090000004'),
(5, 'Hoàng Mỹ Hạnh', '0909999000', '001090000005'),
(6, 'Đặng Văn Giang', '0912111222', '001090000006'),
(7, 'Bùi Minh Nhật', '0912333444', '001090000007'),
(8, 'Vũ Thu Thảo', '0912555666', '001090000008'),
(9, 'Đỗ Duy Mạnh', '0912777888', '001090000009'),
(10, 'Lý Nhã Kỳ', '0912999000', '001090000010'),
(11, 'Toi day', '0123456789', '012464834830'),
(14, 'Hehe', '0123456789', '012345678911'),
(15, 'Anh LengKa', '0841234568', '043204007889'),
(16, 'Thong day', '0848200159', '012345678910');

-- --------------------------------------------------------

--
-- Table structure for table `NhanVien`
--

CREATE TABLE `NhanVien` (
  `MaNV` int NOT NULL,
  `Username` varchar(50) NOT NULL,
  `Password` varchar(255) NOT NULL,
  `HoTen` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `NhanVien`
--

INSERT INTO `NhanVien` (`MaNV`, `Username`, `Password`, `HoTen`) VALUES
(1, 'admin', '123456', 'Nguyễn Quản Trị'),
(2, 'letan01', '123456', 'Trần Thị Vui'),
(3, 'letan02', '123456', 'Lê Văn Hòa'),
(4, 'quanly01', '123456', 'Phạm Minh Đức'),
(5, 'baotri01', '123456', 'Hoàng Văn Sửa'),
(6, 'nv06', '123456', 'Nguyễn Văn A'),
(7, 'nv07', '123456', 'Trần Văn B'),
(8, 'nv08', '123456', 'Lê Thị C'),
(9, 'nv09', '123456', 'Phạm Văn D'),
(10, 'nv10', '123456', 'Hoàng Thị E');

-- --------------------------------------------------------

--
-- Table structure for table `Phong`
--

CREATE TABLE `Phong` (
  `MaPhong` int NOT NULL,
  `TenPhong` varchar(50) NOT NULL,
  `MaTrangThai` int DEFAULT '1',
  `SoDienHienTai` int DEFAULT '0',
  `SoNuocHienTai` int DEFAULT '0',
  `GiaThueNgay` decimal(15,2) DEFAULT NULL,
  `GiaThueThang` decimal(15,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `Phong`
--

INSERT INTO `Phong` (`MaPhong`, `TenPhong`, `MaTrangThai`, `SoDienHienTai`, `SoNuocHienTai`, `GiaThueNgay`, `GiaThueThang`) VALUES
(1, 'Phòng 306', 2, 360, 110, 300000.00, 5000000.00),
(2, 'Phòng 102', 1, 250, 90, 300000.00, 5000000.00),
(3, 'Phòng 103', 1, 0, 0, 300000.00, 5000000.00),
(4, 'Phòng 104', 4, 20000, 0, 300000.00, 5000000.00),
(5, 'Phòng 105', 5, 180, 70, 350000.00, 5500000.00),
(6, 'Phòng 201', 1, 300, 200, 500000.00, 8000000.00),
(7, 'Phòng 202', 1, 0, 0, 500000.00, 8000000.00),
(8, 'Phòng 203', 1, 10564, 36, 500000.00, 8500000.00),
(9, 'Phòng 204', 1, 0, 0, 500000.00, 8000000.00),
(10, 'Phòng thai', 1, 777, 36, 500000.00, 8000000.00),
(11, 'Phòng khong', 6, 0, 0, 500000.00, 3600000.00),
(12, 'Phòng chống ma tóe', 6, 0, 0, 300.00, 3000.00),
(13, 'Phòng chong ma toe', 1, 0, 0, 50000.00, 1000000.00),
(14, 'Phòng mới ken', 1, 0, 0, 0.00, 0.00),
(15, 'Phòng mới vo cung', 1, 0, 0, 0.00, 0.00),
(16, 'Phòng mới ne', 6, 0, 0, 123.00, 123123123.00);

-- --------------------------------------------------------

--
-- Table structure for table `TrangThaiPhong`
--

CREATE TABLE `TrangThaiPhong` (
  `MaTrangThai` int NOT NULL,
  `TenTrangThai` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `TrangThaiPhong`
--

INSERT INTO `TrangThaiPhong` (`MaTrangThai`, `TenTrangThai`) VALUES
(1, 'Trống'),
(2, 'Đang sử dụng'),
(3, 'Đã đặt'),
(4, 'Bảo trì'),
(5, 'Ngừng sử dụng'),
(6, 'Đã xóa');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `DienNuocHangThang`
--
ALTER TABLE `DienNuocHangThang`
  ADD PRIMARY KEY (`MaDienNuoc`),
  ADD UNIQUE KEY `uq_phong_thang` (`MaPhong`,`Thang`,`Nam`);

--
-- Indexes for table `HoaDon`
--
ALTER TABLE `HoaDon`
  ADD PRIMARY KEY (`MaThanhToan`),
  ADD UNIQUE KEY `uq_hd_hopdong` (`MaHopDong`),
  ADD KEY `fk_hd_diennuoc` (`MaDienNuoc`);

--
-- Indexes for table `HopDongThue`
--
ALTER TABLE `HopDongThue`
  ADD PRIMARY KEY (`MaHopDong`),
  ADD KEY `fk_hd_khach` (`MaKhachHang`),
  ADD KEY `fk_hd_phong` (`MaPhong`),
  ADD KEY `fk_hd_nv` (`MaNhanVien`);

--
-- Indexes for table `KhachHang`
--
ALTER TABLE `KhachHang`
  ADD PRIMARY KEY (`MaKH`),
  ADD UNIQUE KEY `uq_cccd` (`CCCD`);

--
-- Indexes for table `NhanVien`
--
ALTER TABLE `NhanVien`
  ADD PRIMARY KEY (`MaNV`),
  ADD UNIQUE KEY `uq_username` (`Username`);

--
-- Indexes for table `Phong`
--
ALTER TABLE `Phong`
  ADD PRIMARY KEY (`MaPhong`),
  ADD KEY `fk_phong_trangthai` (`MaTrangThai`);

--
-- Indexes for table `TrangThaiPhong`
--
ALTER TABLE `TrangThaiPhong`
  ADD PRIMARY KEY (`MaTrangThai`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `DienNuocHangThang`
--
ALTER TABLE `DienNuocHangThang`
  MODIFY `MaDienNuoc` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `HoaDon`
--
ALTER TABLE `HoaDon`
  MODIFY `MaThanhToan` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `HopDongThue`
--
ALTER TABLE `HopDongThue`
  MODIFY `MaHopDong` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT for table `KhachHang`
--
ALTER TABLE `KhachHang`
  MODIFY `MaKH` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `NhanVien`
--
ALTER TABLE `NhanVien`
  MODIFY `MaNV` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `Phong`
--
ALTER TABLE `Phong`
  MODIFY `MaPhong` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `TrangThaiPhong`
--
ALTER TABLE `TrangThaiPhong`
  MODIFY `MaTrangThai` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `DienNuocHangThang`
--
ALTER TABLE `DienNuocHangThang`
  ADD CONSTRAINT `fk_dn_phong` FOREIGN KEY (`MaPhong`) REFERENCES `Phong` (`MaPhong`);

--
-- Constraints for table `HoaDon`
--
ALTER TABLE `HoaDon`
  ADD CONSTRAINT `fk_hd_diennuoc` FOREIGN KEY (`MaDienNuoc`) REFERENCES `DienNuocHangThang` (`MaDienNuoc`),
  ADD CONSTRAINT `fk_hd_hopdong` FOREIGN KEY (`MaHopDong`) REFERENCES `HopDongThue` (`MaHopDong`);

--
-- Constraints for table `HopDongThue`
--
ALTER TABLE `HopDongThue`
  ADD CONSTRAINT `fk_hd_khach` FOREIGN KEY (`MaKhachHang`) REFERENCES `KhachHang` (`MaKH`),
  ADD CONSTRAINT `fk_hd_nv` FOREIGN KEY (`MaNhanVien`) REFERENCES `NhanVien` (`MaNV`),
  ADD CONSTRAINT `fk_hd_phong` FOREIGN KEY (`MaPhong`) REFERENCES `Phong` (`MaPhong`);

--
-- Constraints for table `Phong`
--
ALTER TABLE `Phong`
  ADD CONSTRAINT `fk_phong_trangthai` FOREIGN KEY (`MaTrangThai`) REFERENCES `TrangThaiPhong` (`MaTrangThai`);
COMMIT;

