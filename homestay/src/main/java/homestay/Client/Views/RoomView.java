package homestay.Client.Views;

import java.awt.*;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;

public class RoomView extends Panel{
    Panel pnlRoom = new Panel();
    JTable tb;
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
        String data[][] = { { "101", "tribeti", "chua" ,"100000","10000000"}, 
                { "102", "huyenthien", "da","100000","10000000" }, 
                { "101", "cter dam", "chua" ,"100000","10000000" } };
        String column[] = { "Ma phong", "Ten Phong", "Trang Thai" ,"Gia thue theo ngay","Gia thue theo thang"};
        tb = new JTable(data,column);
        JScrollPane sp = new JScrollPane(tb);
        pnlRoom.add(sp, gbc);
        //Nếu data thay đổi thì thông báo lên bảng
        tb.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                //lấy dữ liệu từ bảng nếu đã thay đổi
                int row = e.getFirstRow();
                int columnValue = e.getColumn();
                Object data1 = tb.getValueAt(row, columnValue);
                System.out.println("Data at row " + row + ", column " + columnValue + " changed to: " + data1);
                
            }
        });

        gbc.gridy = 2;
        Button btnHuy = new Button("Huy cap nhat");
        btnHuy.addActionListener(e -> {
            // Xử lý logic hủy cập nhật trả về dữ liệu ban đầu
            
        });
        pnlRoom.add(btnHuy, gbc);
        

        
    }
}
