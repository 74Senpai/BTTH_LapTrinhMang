package homestay.Client.Views;

import java.awt.*;

public class DashboardView extends Panel implements Components.IViewCheck{

    final Color COLOR_CARD_BLUE = new Color(100, 120, 140);
    final Color COLOR_TEXT_HEADER = new Color(50, 50, 50);
    Panel pnlMain = new Panel();
    public void showDashboard(){
        pnlMain.setLayout(new GridBagLayout());
        pnlMain.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.weighty = 1.0;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        // --- A. Header Welcome ---
        gbc.gridy = 0;
        Label lblWelcome = new Label("Welcome to your dashboard!");
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 18));
        pnlMain.add(lblWelcome, gbc);

        // --- B. Section: Overview of Rooms ---
        gbc.gridy = 1;
        Label lblOverview = new Label("Overview of Rooms");
        lblOverview.setFont(new Font("Arial", Font.BOLD, 14));
        pnlMain.add(lblOverview, gbc);

        gbc.gridy = 2;
        Panel pnlOverviewCards = new Panel(new GridLayout(1, 4, 15, 0)); // 1 hàng, 4 cột, khoảng cách 15
        pnlOverviewCards.add(Components.createCard("Total Rooms", "50 Total", COLOR_CARD_BLUE));
        pnlOverviewCards.add(Components.createCard("Occupied Rooms", "Occupied: 30", COLOR_CARD_BLUE));
        pnlOverviewCards.add(Components.createCard("Current Tenants", "Active: 20", COLOR_CARD_BLUE));
        pnlOverviewCards.add(Components.createCard("Monthly Revenue", "$5000", COLOR_CARD_BLUE));
        pnlMain.add(pnlOverviewCards, gbc);

        // --- C. Section: Quick Actions ---
        gbc.gridy = 3;
        Label lblActions = new Label("Quick Actions");
        lblActions.setFont(new Font("Arial", Font.BOLD, 14));
        pnlMain.add(lblActions, gbc);

        gbc.gridy = 4;
        Panel pnlActionCards = new Panel(new GridLayout(1, 3, 15, 0));
        pnlActionCards.add(Components.createCard("Room Stats", "Tenant Stats", new Color(150, 160, 170)));
        pnlActionCards.add(Components.createCard("Reports", "Settings", new Color(180, 190, 200)));
        pnlActionCards.add(Components.createCard("User Overview", "2023", new Color(120, 130, 140)));
        // chỉnh chiều cao cho panel action một chút
        pnlActionCards.setPreferredSize(new Dimension(0, 100));
        pnlMain.add(pnlActionCards, gbc);

        // --- D. Section: Dashboard Insights (List) ---
        gbc.gridy = 5;
        Label lblInsights = new Label("Dashboard Insights");
        lblInsights.setFont(new Font("Arial", Font.BOLD, 14));
        pnlMain.add(lblInsights, gbc);

        gbc.gridy = 6;
        Panel pnlList = new Panel(new GridLayout(3, 1, 0, 10));
        pnlList.add(Components.createListItem("Your Performance", "Admin"));
        pnlList.add(Components.createListItem("Manage", "Team"));
        pnlList.add(Components.createListItem("Stay", "Client Updates"));
        pnlMain.add(pnlList, gbc);
    }
    
    // Hàm check trước khi chuyển tab
    @Override
    public boolean confirmBeforeSwitch() {
        return true; 
    }
}
