package org.tcathebluecreper.playitplus;

import dev.nanosync.mcsrvstat4j.McsrvStat4J;

import java.io.*;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    static String ip = null;
    static Process p;
    public static void main(String[] args) throws IOException, InterruptedException {
        run();

        while(ip == null) Thread.sleep(10);

        System.out.println("Booted on ip: " + ip.split(":")[0]);

        while(true) {
            while(ip == null) Thread.sleep(10);

            String[] s = ip.split(":");
            URL c = new URL("https://api.mcsrvstat.us/3/" + s[0]);

            while(true) {
                Thread.sleep(1000);
                /*HttpURLConnection con = (HttpURLConnection) c.openConnection();
                con.setRequestMethod("GET");
                con.connect();

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                String out = content.toString();
                in.close();

                JAPIResult result = new Gson().fromJson(out, JAPIResult.class);*/

                if(Boolean.valueOf(new McsrvStat4J().getStatus(s[0]).getOnline())) {
                    System.out.println("Invalid, rebooting on ip: " + s[0]);
                    p.destroy();
                    run();
                }
                System.out.println(new McsrvStat4J().getStatus(s[0]).getOnline());
            }
        }
    }
    public static void run() throws IOException {
        p = Runtime.getRuntime().exec("C:\\Program Files\\playit_gg\\bin\\playit.exe");

        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
        String line;

        new Thread(() -> {
            try {
                while(true) {
                    if(input.ready()) {
                        StringBuilder s = new StringBuilder();
                        Thread.sleep(10);
                        while(input.ready()) {
                            s.append(input.readLine());
                        }

                        Pattern r = Pattern.compile("(?<=(TUNNELS))[a-z-.:0-9]*");
                        Matcher m = r.matcher(s.toString());
                        //System.out.println(s.toString().matches("(?<=(TUNNELS))[a-z-.:0-9]*") + " " + m.matches() + " " + s.toString());
                        if(m.find()) {
                            ip = m.group(0);
                        }

                    }
                    Thread.sleep(1);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        BufferedReader user = new BufferedReader(new InputStreamReader(System.in));
        new Thread(() -> {
            try {
                while(true) {
                    if(user.ready()) {
                        output.write(user.readLine());
                    }
                    Thread.sleep(1);
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
