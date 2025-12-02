package homestay.Client.Views;

import java.awt.*;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class CustomerView extends Panel implements Components.IViewCheck{
    Panel pnlCustomer = new Panel();

    JTable tb;
    Button btnHuy = new Button("Hủy thay đổi");
    Button btnLuu = new Button("Lưu cập nhật");
    Button btnXoa = new Button("Xóa khách");
    Button btnThem = new Button("Thêm khách");
    
    // Biến lưu trữ dữ liệu gốc
    String originalData[][];
    public boolean isTableChanged = false;

    public void showCustomerView() {
        pnlCustomer.setLayout(new GridBagLayout());
        pnlCustomer.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        // Setup Layout
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Tiêu đề 
        Label lblWelcome = new Label("welcome Quản lý Khách Thuê");
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 18));
        pnlCustomer.add(lblWelcome, gbc);

        gbc.gridy = 1;

        // Controller lấy dữ liệu từ server
        String data[][] = { { "Quay vat dau tien", "0363636363","049363636"}};
        String column[] = { "Họ Tên", "Số điện thoại", "CCCD" };
        
        // Sao lưu dữ liệu vào bộ nhớ tạm
        originalData = new String[data.length][data[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                originalData[i][j] = data[i][j];
            }
        }

        // --- TẠO BẢNG ---
        DefaultTableModel model = new DefaultTableModel(data, column){
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
        tb.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        tb.setFont(new Font("Arial", Font.PLAIN, 14));
        tb.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane sp = new JScrollPane(tb);

        // --- CẤU HÌNH VALIDATION ---
        // Cột SĐT (index 2) và CCCD (index 3) chỉ được nhập số
        TableColumn colSDT = tb.getColumnModel().getColumn(1);
        TableColumn colCCCD = tb.getColumnModel().getColumn(2);
        
        // Sử dụng NumericCellEditor (Class bạn đã có ở phần RoomView)
        colSDT.setCellEditor(new Components.PhoneNumberCellEditor());
        
        colCCCD.setCellEditor(new Components.CCCDCellEditor());

        pnlCustomer.add(sp, gbc);

        // Panel nút bấm
        gbc.gridy = 2;
        Panel pnlButtons = new Panel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        // --- XỬ LÝ SỰ KIỆN ---

        // Nút Thêm
        btnThem.addActionListener(e -> {
            if (model.getRowCount() > 0){
                int lastRowIndex = model.getRowCount() - 1;
                Object lastRowName = tb.getValueAt(lastRowIndex, 0);
                Object lastRowPhone = tb.getValueAt(lastRowIndex, 1);
                Object lastRowCCCD = tb.getValueAt(lastRowIndex, 2);
                if (lastRowName.toString().isEmpty() || lastRowName == null
                    || lastRowPhone.toString().isEmpty() || lastRowPhone == null
                    || lastRowCCCD.toString().isEmpty()|| lastRowCCCD == null){
                    
                    JOptionPane.showMessageDialog(
                        null,
                        "Vui lòng nhập đầy đủ thông tin ở dòng vừa thêm hoặc xóa dòng nếu không muốn thêm khách hàng.",
                        "Nhắc nhở",
                        JOptionPane.INFORMATION_MESSAGE);
                        tb.changeSelection(lastRowIndex, 1, false, false);
                        tb.editCellAt(lastRowIndex, 0);
                        tb.getEditorComponent().requestFocus();;
                        return;
                }

                
            }
            isTableChanged = true;
            // Thêm dòng mới: Mã tự sinh hoặc để trống, các ô khác trống
            String[] newRow = { "", "", "" };
            model.addRow(newRow);
            
            btnHuy.setEnabled(true);
            btnLuu.setEnabled(true);
        });

        // Nút Hủy (Reset về dữ liệu gốc)
        btnHuy.setEnabled(false);
        btnHuy.addActionListener(e -> {
            isTableChanged = false;
            model.setRowCount(0); // Xóa sạch bảng hiện tại
            // Đổ lại dữ liệu từ backup
            for (int i = 0; i < originalData.length; i++) {
                model.addRow(originalData[i]);
            }

            btnHuy.setEnabled(false);
            btnLuu.setEnabled(false);
        });

        // Nút Lưu
        btnLuu.setEnabled(false);
        btnLuu.addActionListener(e -> {
            // Dừng việc edit nếu đang gõ dở 
            if (tb.isEditing()) tb.getCellEditor().stopCellEditing();

            // Kiểm tra dữ liệu rỗng
            for (int i = 0; i < model.getRowCount(); i++) {
                String name = model.getValueAt(i, 0).toString();
                String phone = model.getValueAt(i, 1).toString();
                String cccd = model.getValueAt(i, 2).toString();
                if (name.trim().isEmpty() || name == null){
                    JOptionPane.showMessageDialog(null, "Dòng thứ " + (i + 1) + ": Họ tên không được để trống!");
                    // Focus vào dòng lỗi
                    tb.setRowSelectionInterval(i, i);
                    return;
                }
                if (phone.trim().isEmpty() || phone == null){
                    JOptionPane.showMessageDialog(null, "Dòng thứ " + (i + 1) + ": Số điện thoại không được để trống!");
                    tb.setRowSelectionInterval(i, i);
                    return;
                }
                if (cccd.trim().isEmpty() || cccd == null){
                    JOptionPane.showMessageDialog(null, "Dòng thứ " + (i + 1) + ": CCCD không được để trống!");
                    tb.setRowSelectionInterval(i, i);
                    return;
                }
            }
            // Hộp thoại xác nhận    
            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Bạn có chắc chắn muốn lưu thay đổi không?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm != JOptionPane.YES_OPTION) return;

            // Cập nhật lại dữ liệu gốc (Backup lại cái mới)
            originalData = new String[model.getRowCount()][model.getColumnCount()];
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    Object val = model.getValueAt(i, j);
                    originalData[i][j] = (val != null) ? val.toString() : "";
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
                    "Bạn có chắc muốn xóa khách hàng này?",
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
                btnXoa.setEnabled(tb.getSelectedRow() != -1);
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

        pnlButtons.add(btnThem);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnHuy);
        pnlButtons.add(btnLuu);
        pnlCustomer.add(pnlButtons, gbc);
    }

    // Hàm check trước khi chuyển tab
    @Override
    public boolean confirmBeforeSwitch() {
        if (!isTableChanged) return true;
        JOptionPane.showMessageDialog(
                null,
                "Dữ liệu trong bảng đã thay đổi! Hãy cập nhật hoặc hủy thay đổi trước khi rời khỏi.",
                "Cảnh báo",
                JOptionPane.WARNING_MESSAGE);
        return false;
    }

}
