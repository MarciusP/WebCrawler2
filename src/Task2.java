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

    //Prints all the flights and cheapest round flight combinations to file
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

    //Calculates prices of round flights and sorts out the cheapest ones
    private static void calculatePrices(){
        List<List> combinations = new ArrayList<>();
        List<Flight> cheapest = new ArrayList<>();
        double lowestPrice = -2; //price is set to -2 to get the first combination of flights

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

    //Parses provided table and writes all the fitting flights to list
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

                //All the values below are zeroed from previous flight
                price = -2;
                departureTime ="";
                departureAirport ="";
                arrivalTime ="";
                arrivalAirport ="";
                interArrivalTime = "";

                //selects cheapest price in prices row and converts it to double
                for (String p: prices.replace("€", "").replace(".","")
                        .replace(",", ".").split(" ")){
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

            //Reads web page html from a file, because it wasn't accessible otherwise due to captcha
            FileInputStream fis = new FileInputStream("resources/html.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            //Puts all the read lines into single string
            String response = "";
            for (String line; (line = reader.readLine()) != null; response += line);

            Document document;
            //Parses the single sting of html to Jsoup document
            document = Jsoup.parse(response);

            //select the first table with departure flights
            Element table = document.select("table").get(3);
            parseTable(table, 4);

            departFlights = flights;
            flights = new ArrayList<>();

            //select the second table with return flights for day 10
            table = document.select("table").get(25);
            parseTable(table, 10);

            //select the second table with return flights for day 11
            table = document.select("table").get(44);
            parseTable(table, 11);

            calculatePrices();
        }
        catch (Exception e)
        {
            System.out.println("Exception was thrown in main method: " + e);
        }

    }
}
