public class Flight {

    private final int day;

    private final double price;
    private final String departureAirport, departureTime, arrivalAirport, arrivalTime;
    private String interAirport, interArrivalTime, interDepartureTime;

    public Flight(int day, double price, String departureAirport, String departureTime, String arrivalAirport, String arrivalTime)
    {
        this.day = day;
        this.price = price;
        this.departureAirport = departureAirport;
        this.departureTime = departureTime;
        this.arrivalAirport = arrivalAirport;
        this.arrivalTime = arrivalTime;
    }

    public Flight(int day, double price, String departureAirport, String departureTime, String interAirport, String interArrivalTime, String interDepartureTime, String arrivalAirport, String arrivalTime)
    {
        this.day = day;
        this.price = price;
        this.departureAirport = departureAirport;
        this.departureTime = departureTime;
        this.arrivalAirport = arrivalAirport;
        this.arrivalTime = arrivalTime;
        this.interAirport = interAirport;
        this.interArrivalTime = interArrivalTime;
        this.interDepartureTime = interDepartureTime;
    }

    public double getPrice() {
        return price;
    }

    public String toString(){
        if(interAirport == null)
            return "Departure day: " + day + "\n" + "Price: " + price + " departure airport: " + departureAirport
                    + " departure time: " + departureTime + " arrival airport: " + arrivalAirport + " arrival time: "
                    + arrivalTime + "\n";
        else
            return "Departure day: " + day + "\n" + "Price: " + price + " departure airport: " + departureAirport
                    + " departure time: " + departureTime + " Stop over arrival time: " + interArrivalTime
                    + " Stop over airport: " + interAirport + " Stop over departure: " + interDepartureTime
                    +  " arrival airport: " + arrivalAirport + " arrival time: " + arrivalTime + "\n";
    }
}
