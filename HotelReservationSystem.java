package HotelReservationSystem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HotelReservationSystem {

    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    private JPanel homePanel;
    private JPanel searchPanel;
    private JPanel reservePanel;

    private JTextArea searchResultArea;
    private JComboBox<String> roomNumberDropdown;
    private List<Room> availableRooms;
    private List<Room> reservedRooms;
    private List<String> customerReservations;

    public HotelReservationSystem() {
        frame = new JFrame("Hotel Reservation System");
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        availableRooms = new ArrayList<>();
        reservedRooms = new ArrayList<>();
        customerReservations = new ArrayList<>();

        createHomePanel();
        createSearchPanel();
        createReservePanel();

        mainPanel.add(homePanel, "Home");
        mainPanel.add(searchPanel, "Search");
        mainPanel.add(reservePanel, "Reserve");

        frame.add(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 450);
        frame.setVisible(true);
    }

    private void createHomePanel() {
        homePanel = new JPanel(new BorderLayout());
        homePanel.setBackground(new Color(240, 240, 240)); // Light grey background

        JLabel welcomeLabel = new JLabel("Welcome to Hotel Reservation System", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        homePanel.add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(240, 240, 240)); // Matching background

        JButton searchButton = new JButton("Search Rooms");
        JButton reserveButton = new JButton("Reserve Room");

        styleButton(searchButton);
        styleButton(reserveButton);

        searchButton.addActionListener(e -> cardLayout.show(mainPanel, "Search"));
        reserveButton.addActionListener(e -> cardLayout.show(mainPanel, "Reserve"));

        buttonPanel.add(searchButton);
        buttonPanel.add(reserveButton);

        homePanel.add(buttonPanel, BorderLayout.CENTER);

        JTextArea customerListArea = new JTextArea(10, 30);
        customerListArea.setEditable(false);
        customerListArea.setBackground(new Color(255, 255, 255)); // White background
        customerListArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200))); // Light grey border
        updateCustomerListArea(customerListArea);
        homePanel.add(new JScrollPane(customerListArea), BorderLayout.SOUTH);
    }

    private void createSearchPanel() {
        searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(new Color(240, 240, 240)); // Light grey background

        JLabel searchLabel = new JLabel("Search for Available Rooms", JLabel.CENTER);
        searchLabel.setFont(new Font("Arial", Font.BOLD, 20));
        searchPanel.add(searchLabel, BorderLayout.NORTH);

        searchResultArea = new JTextArea(10, 30);
        searchResultArea.setEditable(false);
        searchResultArea.setBackground(new Color(255, 255, 255)); // White background
        searchResultArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200))); // Light grey border
        searchPanel.add(new JScrollPane(searchResultArea), BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        controlPanel.setBackground(new Color(240, 240, 240)); // Matching background

        JButton searchButton = new JButton("Search");
        JButton backButton = new JButton("Back");

        styleButton(searchButton);
        styleButton(backButton);

        searchButton.addActionListener(e -> updateSearchResults());

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Home"));

        controlPanel.add(searchButton);
        controlPanel.add(backButton);

        searchPanel.add(controlPanel, BorderLayout.SOUTH);
    }

    private void createReservePanel() {
        reservePanel = new JPanel(new BorderLayout());
        reservePanel.setBackground(new Color(240, 240, 240)); // Light grey background

        JLabel reserveLabel = new JLabel("Reserve a Room", JLabel.CENTER);
        reserveLabel.setFont(new Font("Arial", Font.BOLD, 20));
        reservePanel.add(reserveLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBackground(new Color(240, 240, 240)); // Matching background
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.add(new JLabel("Room Number:"));
        roomNumberDropdown = new JComboBox<>();
        formPanel.add(roomNumberDropdown);

        formPanel.add(new JLabel("Customer Name:"));
        JTextField customerNameField = new JTextField();
        formPanel.add(customerNameField);

        formPanel.add(new JLabel("Check-in Date:"));
        JTextField checkinDateField = new JTextField();
        checkinDateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        checkinDateField.setEditable(false);
        formPanel.add(checkinDateField);

        formPanel.add(new JLabel("Check-out Date:"));
        JSpinner checkoutDateSpinner = createDateSpinner();
        formPanel.add(checkoutDateSpinner);

        reservePanel.add(formPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        controlPanel.setBackground(new Color(240, 240, 240)); // Matching background

        JButton reserveButton = new JButton("Reserve");
        JButton backButton = new JButton("Back");

        styleButton(reserveButton);
        styleButton(backButton);

        reserveButton.addActionListener(e -> {
            String selectedRoom = (String) roomNumberDropdown.getSelectedItem();
            String customerName = customerNameField.getText();
            Date checkinDate = new Date();
            Date checkoutDate = (Date) checkoutDateSpinner.getValue();

            if (selectedRoom != null && customerName != null && !customerName.isEmpty() && checkoutDate != null) {
                reserveRoom(selectedRoom, customerName, checkinDate, checkoutDate);
                JOptionPane.showMessageDialog(frame, "Room reserved successfully!");
                updateCustomerListArea((JTextArea) ((JScrollPane) homePanel.getComponent(2)).getViewport().getView());
            } else {
                JOptionPane.showMessageDialog(frame, "Please fill all the fields.");
            }
        });

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Home"));

        controlPanel.add(reserveButton);
        controlPanel.add(backButton);

        reservePanel.add(controlPanel, BorderLayout.SOUTH);
    }

    private JSpinner createDateSpinner() {
        SpinnerDateModel model = new SpinnerDateModel();
        JSpinner dateSpinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(editor);
        return dateSpinner;
    }

    private void updateSearchResults() {
        availableRooms.clear();
        availableRooms.add(new Room("101", "Single", 100));
        availableRooms.add(new Room("102", "Double", 150));
        availableRooms.add(new Room("103", "Suite", 200));

        searchResultArea.setText("Available Rooms:\n");
        roomNumberDropdown.removeAllItems();
        for (Room room : availableRooms) {
            if (!reservedRooms.contains(room)) {
                searchResultArea.append(room + "\n");
                roomNumberDropdown.addItem(room.getRoomNumber());
            }
        }
    }

    private void reserveRoom(String roomNumber, String customerName, Date checkinDate, Date checkoutDate) {
        for (Room room : availableRooms) {
            if (room.getRoomNumber().equals(roomNumber)) {
                reservedRooms.add(room);
                customerReservations.add(customerName + " - Room " + roomNumber + " (Check-in: " + new SimpleDateFormat("yyyy-MM-dd").format(checkinDate) + ", Check-out: " + new SimpleDateFormat("yyyy-MM-dd").format(checkoutDate) + ")");
                break;
            }
        }
        updateSearchResults();
    }

    private void updateCustomerListArea(JTextArea customerListArea) {
        customerListArea.setText("Customer Reservations:\n");
        for (String reservation : customerReservations) {
            customerListArea.append(reservation + "\n");
        }
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(169, 169, 169)); // Dark grey (metallic look)
        button.setForeground(Color.WHITE); // White text
        button.setBorder(BorderFactory.createLineBorder(new Color(128, 128, 128))); // Grey border
        button.setPreferredSize(new Dimension(150, 40)); // Button size
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HotelReservationSystem::new);
    }
}
