package homestay.Client.Views.Room.Dialog;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import homestay.Client.Controllers.BaseDataController;
import homestay.Client.Controllers.RoomController;
import homestay.Client.DTOs.ListRoomStateDTO;
import homestay.Client.DTOs.RoomDTO;
import homestay.Client.Helper.TableMapper;

public class AddRoomDialog extends JDialog {

    private JTextField txtRoomName = new JTextField();
    private JComboBox<String> cbStatus = new JComboBox<>();
    ;
    private JTextField txtPriceDay = new JTextField();
    private JTextField txtPriceMonth = new JTextField();
    private JTextField txtElectric = new JTextField();
    private JTextField txtWater = new JTextField();

    private ListRoomStateDTO listRoomState;

    private boolean confirmed = false;

    public AddRoomDialog(JFrame parent) {
        // Panel chính với padding
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // padding xung quanh
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // padding giữa các component
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // ===== Thêm label + field =====
        panel.add(new JLabel("Tên phòng:"), gbc);
        gbc.gridx = 1;
        panel.add(txtRoomName, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Trạng thái:"), gbc);
        gbc.gridx = 1;
        panel.add(cbStatus, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Giá/ngày:"), gbc);
        gbc.gridx = 1;
        panel.add(txtPriceDay, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Giá/tháng:"), gbc);
        gbc.gridx = 1;
        panel.add(txtPriceMonth, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Số điện:"), gbc);
        gbc.gridx = 1;
        panel.add(txtElectric, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Số nước:"), gbc);
        gbc.gridx = 1;
        panel.add(txtWater, gbc);

        loadRoomStatesFromServer();

        // ===== Buttons =====
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnOk = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");
        pnlButtons.add(btnOk);
        pnlButtons.add(btnCancel);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(pnlButtons, gbc);

        setContentPane(panel);
        pack();
        if (parent != null) {
            setLocationRelativeTo(parent);
        } else {
            setLocationRelativeTo(null); // center trên màn hình
        }

        // ===== Action =====
        btnOk.addActionListener(e -> {
            confirmed = true;
            setVisible(false);
        });

        btnCancel.addActionListener(e -> setVisible(false));
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Object[] getRoomData() {
        Object selected = cbStatus.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn trạng thái!");
            return null;
        }

        int maTrangThai = BaseDataController.getCachedRoomStates()
                .getMaTrangThai(selected.toString());

        RoomDTO.ViewRoomDTO room = new RoomController().addRoom(
                txtRoomName.getText().trim(),
                maTrangThai,
                Double.parseDouble(txtPriceDay.getText().trim()),
                Double.parseDouble(txtPriceMonth.getText().trim())
        );

        if (room == null) {
            JOptionPane.showMessageDialog(this, "Tạo phòng thất bại");
        }
        return TableMapper.mapRoomToRow(room);
    }

    private void loadRoomStatesFromServer() {
        try {
            this.listRoomState = new BaseDataController().getRoomStates();
            cbStatus.removeAllItems();
            for (String name : listRoomState.getAllStateNames()) {
                cbStatus.addItem(name);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Không thể lấy trạng thái phòng từ server:\n" + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
