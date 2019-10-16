import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Task2 {

    private static List<Flight> departFlights = new ArrayList<>();;

    private static void addDepartFlights(double price, String departureAirport, String departureTime,
                                         String arrivalAirport, String arrivalTime){
        departFlights.add(new Flight(4, price,departureAirport,departureTime,arrivalAirport,arrivalTime));
    }

    public static void main(String[] args) {

        try{

            FileInputStream fis = new FileInputStream("resources/html.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            String response = "";
            for (String line; (line = reader.readLine()) != null; response += line);

            //System.out.println(response);

            Document document;
            document = Jsoup.parse(response);

            //System.out.println("text received: " + reader.lines().collect(Collectors.joining()));

            //if(!document.hasText())
             //   System.out.println("NICHUJA BLED");
            //else
             //   System.out.println(document.text());

            int i=0;

            Element table = document.select("table").get(25); //select the first table 3 next 25.
            System.out.println(table.text());
            System.out.println("table size: " + document.select("table").size());
            Elements rows = table.select("tr");

            double price = -1;
            String departureTime = "", departureAirport = "", arrivalTime = "", arrivalAirport = "", interArrivalTime = "",
                    interDepartTime = "", interArrivalAirport = "";
            boolean indirect = false;

            for (Element row : rows)
            {
                //System.out.println("atejo i row: " + i);
                i++;
                //System.out.println(row.text());


                String prices = row.select(".price").text();
                if(!prices.equals("")){
                    //System.out.println(prices);
                    if(price!=-1){
                        if (indirect) {
                            if(arrivalAirport.contains("Oslo")){
                                departFlights.add(new Flight(4, price, departureAirport, departureTime,
                                        arrivalAirport, interArrivalTime, interDepartTime, interArrivalAirport, arrivalTime));
                                System.out.println(departFlights.get(departFlights.size() - 1).toString());
                            }

                        }
                        else {
                            departFlights.add(new Flight(4, price,departureAirport,departureTime,arrivalAirport,arrivalTime));
                            System.out.println(departFlights.get(departFlights.size() - 1).toString());
                        }
                        indirect = false;
                    }


                    //cia IRASYTI PRAEITA SKRYDY

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
                    //System.out.println("LAIKAI: " + time);
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
                    //System.out.println("SU STOPU ________:" + row.select("span.route:nth-of-type(3)").text());
            }

        }
        catch (Exception e)
        {
            System.out.println("Exception was thrown in main method: " + e);
        }

    }
}
