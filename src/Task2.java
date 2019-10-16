import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Task2 {

    private static List<Flight> flights = new ArrayList<>();
    private static List<Flight> departFlights;

    private static void printToFile(List<List> combinations)
    {
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("AllFlights.txt"));

            if(!flights.isEmpty() && !departFlights.isEmpty()) {
                writer.write("All the departure flights:\n");

                for (Flight flight : departFlights) {
                    writer.write(flight.toString());
                }

                writer.write("All the return flights:\n");

                for (Flight flight : flights) {
                    writer.write(flight.toString());
                }

                writer.write("All the cheapest flights and their info:\n");

                for (List<Flight> element : combinations) {
                    int i = 0;
                    for (Flight flight : element) {
                        if(i%2==0)
                            writer.write("Departure flight:\n" + flight.toString());
                        else
                            writer.write("Return flight:\n" + flight.toString() + "---------- \n");
                        i++;
                    }
                }

            }
            else
                writer.write("No flights were found\n");

            writer.close();
        }
        catch (Exception e) {
            System.out.println("Exception in writing function: " + e);
        }
    }

    private static void calculatePrices(){
        List<List> combinations = new ArrayList<>();
        List<Flight> cheapest = new ArrayList<>();
        double lowestPrice = -2;

        for (Flight depFlight: departFlights) {
            for (Flight arrFlight : flights) {
                if (lowestPrice == -2){
                    lowestPrice = depFlight.getPrice() + arrFlight.getPrice();
                    cheapest.add(depFlight);
                    cheapest.add(arrFlight);
                    combinations.add(cheapest);
                    cheapest = new ArrayList<>();
                    continue;
                }
                if(lowestPrice>depFlight.getPrice() + arrFlight.getPrice())
                {
                    combinations = new ArrayList<>();
                    lowestPrice = depFlight.getPrice() + arrFlight.getPrice();
                    cheapest.add(depFlight);
                    cheapest.add(arrFlight);
                    combinations.add(cheapest);
                    cheapest = new ArrayList<>();
                }
                else if(lowestPrice==depFlight.getPrice() + arrFlight.getPrice()){
                    cheapest.add(depFlight);
                    cheapest.add(arrFlight);
                    combinations.add(cheapest);
                    cheapest = new ArrayList<>();
                }
            } //for arrival flights
        } //for departure flights
        printToFile(combinations);
    }

    private static void parseTable(Element table, int day){
        Elements rows = table.select("tr");

        double price = -1;
        String departureTime = "", departureAirport = "", arrivalTime = "", arrivalAirport = "", interArrivalTime = "",
                interDepartTime = "", interArrivalAirport = "";
        boolean indirect = false;

        for (Element row : rows)
        {
            String prices = row.select(".price").text();
            if(!prices.equals("")){
                if(price!=-1){
                    if (indirect) {
                        if(arrivalAirport.contains("Oslo")){
                            flights.add(new Flight(day, price, departureAirport, departureTime,
                                    arrivalAirport, interArrivalTime, interDepartTime, interArrivalAirport, arrivalTime));
                        }
                    }
                    else {
                        flights.add(new Flight(day, price,departureAirport,departureTime,arrivalAirport,arrivalTime));
                    }
                    indirect = false;
                }

                price = -2;
                departureTime ="";
                departureAirport ="";
                arrivalTime ="";
                arrivalAirport ="";
                interArrivalTime = "";

                for (String p: prices.replace("€", "").replace(",", ".").split(" ")) {
                    if(!p.equals("-")){
                        price = Double.parseDouble(p);
                        break;
                    }
                }
                String time = row.select(".time").text();
                departureTime = time.split(" ")[0];
                arrivalTime = time.split(" ")[2];
                continue;
            } // if prices
            if(!row.text().contains("Stop over")) {
                String outAir = row.select("span.route:nth-of-type(1)").text();
                if (!outAir.equals("") && departureAirport.equals("")) {
                    departureAirport = outAir;
                }
                String inAir = row.select("span.route:nth-of-type(3)").text();
                if (!inAir.equals("") && arrivalAirport.equals("")) {
                    arrivalAirport = inAir;
                }
            }
            else {
                indirect = true;
                String fullRow = row.text();
                if(!fullRow.equals("") && interArrivalTime.equals("")){
                    interArrivalTime = fullRow.split(" ")[2];
                    String[] placeholder = fullRow.split("Stop over at:")[1].split(arrivalTime)[0].split(" ");
                    interDepartTime = placeholder[placeholder.length -2];
                    placeholder = fullRow.split("Stop over at:")[1].split(" Operated by:")[0].split("-");
                    interArrivalAirport = placeholder[placeholder.length -1];
                }
            }

        }//for rows

    }

    public static void main(String[] args) {

        try{

            FileInputStream fis = new FileInputStream("resources/html.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            String response = "";
            for (String line; (line = reader.readLine()) != null; response += line);

            Document document;
            document = Jsoup.parse(response);

            Element table = document.select("table").get(3); //select the first table 3 next 25.
            parseTable(table, 4);

            departFlights = flights;
            flights = new ArrayList<>();

            table = document.select("table").get(25); //select the first table 3 next 25.
            parseTable(table, 10);

            table = document.select("table").get(44); //select the first table 3 next 25 next 44.
            parseTable(table, 11);

            calculatePrices();
        }
        catch (Exception e)
        {
            System.out.println("Exception was thrown in main method: " + e);
        }

    }
}
