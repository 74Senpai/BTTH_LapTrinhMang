package homestay.Client.Views;

import java.awt.*;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class RoomView extends javax.swing.JPanel implements Components.IViewCheck {
    Panel pnlRoom = new Panel();

    JTable tb;
    Button btnHuy = new Button("Hủy thay đổi");
    Button btnLuu = new Button("Lưu cập nhật");
    Button btnXoa = new Button("Xóa phòng");
    Button btnThem = new Button("Thêm phòng");
    // Biến lưu trữ dữ liệu gốc
    String originalData[][];
    public boolean isTableChanged = false;

    public void showRoomView() {
        pnlRoom.setLayout(new GridBagLayout());
        pnlRoom.setBackground(new Color(248, 249, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        // Setup Layout
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridx = 0;

        // Tiêu đề
        gbc.gridy = 0;
        gbc.weighty = 0; 
        Label lblWelcome = new Label("welcome Quản lý phòng");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblWelcome.setForeground(new Color(33, 37, 41));
        pnlRoom.add(lblWelcome, gbc);
        
        // Controller lấy dữ liệu từ server
        // Thêm STT để dễ quản lý ........
        String data[][] = {
            { "tribeti", "Trống", "360", "670", "123", "123" },
            { "huyenthien", "Trống", "360", "670", "123", "123" },
            { "cter dam", "Trống", "360", "670", "123", "123" } };
            String column[] = { 
                "Tên phòng", 
                "Trạng thái", 
                "Giá/ngày (₫)", 
                "Giá/tháng (₫)", 
                "Điện (kWh)", 
                "Nước (m³)"};

                // Lưu dữ liệu vào bộ nhớ tạm
        if (data.length > 0) {
            originalData = new String[data.length][data[0].length];
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[i].length; j++) {
                    originalData[i][j] = data[i][j];
                }
            }
        } else {
            originalData = new String[0][0];
        }
        
        // --- TẠO BẢNG ---
        gbc.gridy = 1;
        gbc.weighty = 0.1; 
        gbc.fill = GridBagConstraints.BOTH; 

        DefaultTableModel model = new DefaultTableModel(data, column) {
            // ghi đè phương thức để kiểm tra sự thay đổi dữ liệu
            @Override
            public void setValueAt(Object aValue, int row, int column) {
                // Lấy giá trị cũ đang có trong ô
                Object oldValue = getValueAt(row, column);
                
                // So sánh giá trị cũ và mới
                if (oldValue != null && oldValue.toString().equals(aValue.toString())) {
                    // Nếu giống hệt nhau
                    return;
                }
                // Nếu khác nhau -> Mới gọi hàm gốc để cập nhật và bắn sự kiện UPDATE
                super.setValueAt(aValue, row, column);
            }
        };
        tb = new JTable(model);
        tb.setRowHeight(36);
        Components.CenterTable(tb); // Căn giữa toàn bảng
        tb.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        tb.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JScrollPane sp = new JScrollPane(tb);

        // --- CẤU HÌNH VALIDATION ---
        TableColumn colNgay = tb.getColumn("Giá/ngày (₫)");
        TableColumn colThang = tb.getColumn("Giá/tháng (₫)");
        TableColumn colSoDien = tb.getColumn("Điện (kWh)"); 
        TableColumn colSoNuoc = tb.getColumn("Nước (m³)");
        // Kiểm tra number
        colNgay.setCellEditor(new Components.NumericCellEditor());
        colThang.setCellEditor(new Components.NumericCellEditor());
        colSoDien.setCellEditor(new Components.NumericCellEditor());
        colSoNuoc.setCellEditor(new Components.NumericCellEditor());
        // Combobox để chọn trạng thái của phòng
        String[] statusList = { "Trống", "Đang sử dụng", "Đã đặt" , "Quá hạn" };
        TableColumn statusColumn = tb.getColumnModel().getColumn(1);
        JComboBox<String> comboBox = new JComboBox<>(statusList);
        statusColumn.setCellEditor(new DefaultCellEditor(comboBox));

        pnlRoom.add(sp, gbc);

        // Panel nút bấm
        gbc.gridy = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        Panel pnlButtons = new Panel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        
        // --- XỬ LÝ SỰ KIỆN ---
        // Nút Thêm xử lý logic thêm phòng
        btnThem.addActionListener(e -> {

            // Nếu ô tên phòng mới được để trống thì yêu cầu nhập hoặc xóa
            if (model.getRowCount() > 0) {
                int lastRowIndex = model.getRowCount() - 1;
                Object lastRowData = tb.getValueAt(lastRowIndex, 0);
                if (lastRowData == null || lastRowData.toString().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Vui lòng nhập tên phòng mới vào ô vừa thêm hoặc xóa dòng nếu không muốn thêm phòng.",
                            "Nhắc nhở",
                            JOptionPane.INFORMATION_MESSAGE);
                    tb.editCellAt(lastRowIndex, 0);
                    tb.getEditorComponent().requestFocus();
                    return;
                }
            }
            isTableChanged = true;
            String[] newRow = { "", "Trống", "0", "0", "0", "0" };
            model.addRow(newRow);

            btnHuy.setEnabled(true);
            btnLuu.setEnabled(true);

        });

        // Nút hủy Xử lý logic hủy cập nhật trả về dữ liệu ban đầu
        btnHuy.setEnabled(false);
        btnHuy.addActionListener(e -> {
            isTableChanged = false;
            // Xử lý logic hủy cập nhật trả về dữ liệu ban đầu
            model.setRowCount(0);
            // Khôi phục dữ liệu từ RAM
            for (int i = 0; i < originalData.length; i++) {
                model.addRow(originalData[i]);
            }
            btnHuy.setEnabled(false);
            btnLuu.setEnabled(false);
        });

        // Nút Xóa
        btnXoa.setEnabled(false);
        btnXoa.addActionListener(e -> {
            int selectedRow = tb.getSelectedRow();
            if (selectedRow == -1)
                return;

            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Bạn có chắc chắn muốn xóa dòng này không?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                model.removeRow(selectedRow);
                isTableChanged = true;
                btnHuy.setEnabled(true);
                btnLuu.setEnabled(true);

            }

        });

        // Nút Lưu Xử lý logic lưu cập nhật dữ liệu
        btnLuu.setEnabled(false);
        btnLuu.addActionListener(e -> {
            // Dừng việc edit nếu đang gõ dở
            if (tb.isEditing())
                tb.getCellEditor().stopCellEditing();

            // Kiểm tra dữ liệu rỗng
            for (int i = 0; i < model.getRowCount(); i++) {
                Object roomName = model.getValueAt(i, 0);
                if (roomName == null || roomName.toString().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Dòng thứ " + (i + 1) + ": Tên phòng không được để trống!");
                    tb.setRowSelectionInterval(i, i);
                    return;
                }
            }

            // Hộp thoại xác nhận
            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Bạn có chắc chắn muốn cập nhật thay đổi này không?(Hãy xem kỹ lại trước khi cập nhật)",
                    "Xác nhận Cập nhật",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            // Cập nhật lại dữ liệu original
            originalData = new String[model.getRowCount()][model.getColumnCount()];
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    originalData[i][j] = model.getValueAt(i, j).toString();
                }
            }

            isTableChanged = false;
            btnHuy.setEnabled(false);
            btnLuu.setEnabled(false);

            // Gọi Controller gửi về Server

            JOptionPane.showMessageDialog(null, "Đã lưu thành công!");

        });

        
        // Nút Xóa
        btnXoa.setEnabled(false);
        btnXoa.addActionListener(e -> {
            int selectedRow = tb.getSelectedRow();
            if (selectedRow == -1) return;
            
            int confirm = JOptionPane.showConfirmDialog(
                null,
                "Bạn có chắc chắn muốn xóa dòng này không?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    model.removeRow(selectedRow);
                    isTableChanged = true;
                    btnHuy.setEnabled(true);
                    btnLuu.setEnabled(true);
                    
                }
                
            });
            
        // Sự kiện chọn dòng để bật nút Xóa
        tb.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tb.getSelectedRow();
                if (selectedRow != -1) {
                    btnXoa.setEnabled(true);

                } else {
                    btnXoa.setEnabled(false);

                }
            }
        });
        // Lắng nghe thay đổi trên ô dữ liệu
        tb.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                isTableChanged = true;
                btnHuy.setEnabled(true);
                btnLuu.setEnabled(true);
            }
        });

        // Khi edit một ô thì disable nút Lưu và Hủy để tránh lỗi
        tb.addPropertyChangeListener("tableCellEditor", evt -> {
            if (tb.isEditing()) {
                btnLuu.setEnabled(false);
                btnHuy.setEnabled(false);
                btnXoa.setEnabled(false);
                btnThem.setEnabled(false);
            } else {
                if (isTableChanged) {
                    btnLuu.setEnabled(true);
                    btnHuy.setEnabled(true);
                    btnXoa.setEnabled(true);
                    btnThem.setEnabled(true);
                }

            }
        });

        // Thêm nút vào panel
        pnlButtons.add(btnThem);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnHuy);
        pnlButtons.add(btnLuu);
        pnlRoom.add(pnlButtons, gbc);
    }

    // Hàm cảnh báo trước khi chuyển trang
    @Override
    public boolean confirmBeforeSwitch() {

        if (!isTableChanged)
            return true;

        JOptionPane.showMessageDialog(
                null,
                "Dữ liệu trong bảng đã thay đổi! Hãy cập nhật hoặc hủy thay đổi trước khi rời khỏi.",
                "Cảnh báo",
                JOptionPane.WARNING_MESSAGE);
        return false;
    }
}
