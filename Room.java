package HotelReservationSystem;

public class Room {
    private String roomNumber;
    private String category;
    private double price;

    public Room(String roomNumber, String category, double price) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.price = price;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    @Override
    public String toString() {
        return "Room Number: " + roomNumber + ", Category: " + category + ", Price: $" + price;
    }
}
