package homestay.Client.Views;

import java.awt.*;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;


public class RoomView extends Panel{
    Panel pnlRoom = new Panel();
    JTable tb;
    Button btnHuy = new Button("Huy cap nhat");
    Button btnLuu = new Button("Luu cap nhat");
    Button btnXoa = new Button("Xoa phong");
    Button btnThem = new Button("Them phong");
    String originalData[][];
    public void showRoomView(){
        pnlRoom.setLayout(new GridBagLayout());
        pnlRoom.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20 , 10, 20);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridx = 0;


        gbc.gridy = 0;
        Label lblWelcome = new Label("welcome qlroom");
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 18));
        pnlRoom.add(lblWelcome, gbc);

        gbc.gridy = 1;
        // Controller lấy dữ liệu từ server
        String data[][] = { { "101", "tribeti", "Trống" ,"100000","10000000"}, 
                { "102", "huyenthien", "Trống","100000","10000000" }, 
                { "101", "cter dam", "Trống" ,"100000","10000000" } };
        String column[] = { "Ma phong", "Ten Phong", "Trang Thai" ,"Gia thue theo ngay","Gia thue theo thang"};
        // Lưu dữ liệu vào bộ nhớ tạm 
        originalData = new String[data.length][data[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                originalData[i][j] = data[i][j];
            }
        }

        DefaultTableModel model = new DefaultTableModel(data, column);
        tb = new JTable(model);
        tb.setRowHeight(36);
        // Nếu hiện mã phòng còn không thì thôi
        // {
        //     @Override
        //     public boolean isCellEditable(int row, int column) {
        //         // không cho sửa mã phòng
        //         return column != 0;
        //     }
        // };
        // Kiểm tra giá theo ngày và tháng
        TableColumn colNgay = tb.getColumn("Gia thue theo ngay");
        TableColumn colThang = tb.getColumn("Gia thue theo thang");
        colNgay.setCellEditor(new Components.NumericCellEditor());
        colThang.setCellEditor(new Components.NumericCellEditor());

        // Combobox để chọn trạng thái của phòng
        String[] statusList = {"Trống", "Đang thuê", "Bảo trì"};
        // Nếu không có mã phòng thì sửa lại getColumn 1
        TableColumn statusColumn = tb.getColumnModel().getColumn(2);
        JComboBox<String> comboBox = new JComboBox<>(statusList);
        statusColumn.setCellEditor(new DefaultCellEditor(comboBox));
        
        JScrollPane sp = new JScrollPane(tb);
        pnlRoom.add(sp, gbc);
        
        gbc.gridy = 2;
        Panel pnlButtons = new Panel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnThem.addActionListener(e -> {
            // Xử lý logic thêm phòng
            // Thêm một hàng mới vào bảng
            String[] newRow = {"Id tang", "", "Trống", "0", "0"};
            model.addRow(newRow);
            //Controller thêm mới vào bảng  
        });
        btnHuy.setEnabled(false);
        btnHuy.addActionListener(e -> {
            // Xử lý logic hủy cập nhật trả về dữ liệu ban đầu
            model.setRowCount(0);
            // Khôi phục dữ liệu từ RAM
            for (int i = 0; i < originalData.length; i++) {
                model.addRow(originalData[i]);
            }
            btnHuy.setEnabled(false);
            btnLuu.setEnabled(false);   

        });
        btnLuu.setEnabled(false);
        btnLuu.addActionListener(e -> {
            // Xử lý logic lưu cập nhật dữ liệu
            btnHuy.setEnabled(false);
            // Controller lưu dữ liệu
            
            
        });


        tb.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                // lấy dữ liệu từ bảng nếu đã thay đổi
                btnHuy.setEnabled(true);
                btnLuu.setEnabled(true);
                int row = e.getFirstRow();
                int columnValue = e.getColumn();
                Object data1 = tb.getValueAt(row, columnValue);
                String columnName = tb.getColumnName(columnValue);
                System.out.println("Data at row " + row + ", column " + columnName + " changed to: " + data1);
            }
        });
        pnlButtons.add(btnThem);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnHuy);
        pnlButtons.add(btnLuu);
        pnlRoom.add(pnlButtons, gbc);

    }
}
