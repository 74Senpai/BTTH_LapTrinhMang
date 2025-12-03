package homestay.Client.Views;

import java.awt.Panel;


public class DashboardView extends Panel implements Components.IViewCheck{

    Panel pnlMain = new Panel();
    public void showDashboard(){
        pnlMain.setLayout(new GridBagLayout());
        pnlMain.setBackground(new Color(248, 249, 250));
        // Setup Layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;


        // --- Header Welcome ---
        gbc.gridy = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        Label lblWelcome = new Label("Welcome to your dashboard!");
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 18));
        pnlMain.add(lblWelcome, gbc);

        
        // --- Overview Cards ---
        gbc.gridy = 1;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        Panel pnlOverviewCards = new Panel(new GridLayout(1, 4, 15, 0));
        pnlOverviewCards.add(Components.createCard("Total Rooms", "50 Total", COLOR_CARD_BLUE));
        pnlOverviewCards.add(Components.createCard("Occupied Rooms", "30", new Color(40, 167, 69))); 
        pnlOverviewCards.add(Components.createCard("Empty Rooms", "20", new Color(255, 193, 7))); 
        pnlOverviewCards.add(Components.createCard("Revenue", "$5,000", new Color(220, 53, 69)));
        pnlMain.add(pnlOverviewCards, gbc);

        // Thêm filler để đẩy nội dung lên trên
        gbc.gridy = 2;
        gbc.weighty = 1.0;

        Panel pnlFiller = new Panel();
        pnlFiller.setBackground(new Color(248, 249, 250));
        pnlMain.add(pnlFiller, gbc);

    }
    
    // Hàm check trước khi chuyển tab
    @Override
    public boolean confirmBeforeSwitch() {
        return true; 
    }
}
