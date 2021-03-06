package com.mrpowergamerbr.powersuperantiproxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import lombok.*;

@Getter
@Setter
// https://youtu.be/Kv2TnMowhh8
public class EntaoBabyEsperaUmPouco {

    HashSet<String> safeIPs = new HashSet<>();
    HashSet<String> proxyIPs = new HashSet<>();

    String proxyUse = "�cUso de Proxy!\n\n�cDesative o Proxy antes de conectar!";

    HashSet<String> deathBotIPs = new HashSet<>();
    HashSet<String> powerHateListIPs = new HashSet<>();

    HashSet<String> cidr = new HashSet<>();

    File data;

    public void init() {
        safeIPs.clear();
        proxyIPs.clear();
        deathBotIPs.clear();
        powerHateListIPs.clear();
        cidr.clear();

        proxyUse = PowerSuperAntiProxy.getInstance().getConfig().getString("MensagemDeKick").replace("&", "§");

        data = PowerSuperAntiProxy.getInstance().getDataFolder();

        /*
         * IPs em formato CIDR do ASkidban
         */
        if (PowerSuperAntiProxy.getInstance().getConfig().getBoolean("UsarASkidban")) {
            System.out.println("[PowerSuperAntiProxy] Pegando os IPs CIDR...");

            try {
                Scanner ipChecker = new Scanner((new URL("https://raw.githubusercontent.com/pisto/ASkidban/master/compiled/ipv4")).openStream());
                while (ipChecker.hasNextLine()) {
                    String line = ipChecker.nextLine();
                    cidr.add(line);
                }
                System.out.println("[PowerSuperAntiProxy] IPs em formato CIDR foram pegos! Yay!");
                ipChecker.close();
            } catch (IOException e) {
                System.out.println("[PowerSuperAntiProxy] Um erro ocorreu ao tentar pegar os IPs em formado CIDR!");
                e.printStackTrace();
            }
        }

        Scanner ipChecker;

        ArrayList<String> websitesToFind = (ArrayList<String>) PowerSuperAntiProxy.getInstance().getConfig().getStringList("SitesBuscados");

        for (String web : websitesToFind) {
            try {
                pastebinSearch(web, powerHateListIPs);
            } catch (Exception e) {
                Bukkit.getLogger().log(Level.WARNING, "[PowerSuperAntiProxy] Um problema ocorreu ao tentar carregar os IPs do site: {0}", web);
            }
        }

        if (PowerSuperAntiProxy.getInstance().getConfig().getBoolean("BloquearIPsDoDeathBot")) {
            /*
             * DeathBot antigo
             */
            /*
             * HTTP Proxy
             */
            try {
                ipChecker = new Scanner((new URL("http://pastebin.com/raw.php?i=H9v1wFJB")).openStream());
                while (ipChecker.hasNextLine()) {
                    String line = ipChecker.nextLine();
                    String[] split = line.split("\\:");
                    deathBotIPs.add(split[0]);
                }
                ipChecker.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            /*
             * SOCKS5 Proxy
             */
            try {
                ipChecker = new Scanner((new URL("http://pastebin.com/raw.php?i=83uAYrLT")).openStream());
                while (ipChecker.hasNextLine()) {
                    String line = ipChecker.nextLine();
                    String[] split = line.split("\\:");
                    deathBotIPs.add(split[0]);
                }
                ipChecker.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            /*
             * SOCKS4 Proxy
             */
            try {
                ipChecker = new Scanner((new URL("http://pastebin.com/raw.php?i=H8RJWN6f")).openStream());
                while (ipChecker.hasNextLine()) {
                    String line = ipChecker.nextLine();
                    String[] split = line.split("\\:");
                    deathBotIPs.add(split[0]);
                }
                ipChecker.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            /*
             * DeathBot novo
             */
            /*
             * SOCKS4 Proxy
             */
            try {
                ipChecker = new Scanner((new URL("http://tjfmc.de/DeathBot/Socks4.txt")).openStream());
                while (ipChecker.hasNextLine()) {
                    String line = ipChecker.nextLine();
                    String[] split = line.split("\\:");
                    deathBotIPs.add(split[0]);
                }
                ipChecker.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            /*
             * SOCKS5 Proxy
             */
            try {
                ipChecker = new Scanner((new URL("http://tjfmc.de/DeathBot/Socks5.txt")).openStream());
                while (ipChecker.hasNextLine()) {
                    String line = ipChecker.nextLine();
                    String[] split = line.split("\\:");
                    deathBotIPs.add(split[0]);
                }
                ipChecker.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            File blockThis = new File(data, "blockthis.txt");
            if (blockThis.exists()) {
                try (BufferedReader br = new BufferedReader(new FileReader(blockThis))) {
                    for (String line; (line = br.readLine()) != null;) {
                        proxyIPs.add(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean searchAndDestroy(String web, String ip, String search) {
        if (ip.equals("127.0.0.1") || ip.startsWith("192.168")) {
            return false;
        }
        StringBuilder res = new StringBuilder();

        Scanner ProxyChecker;
        try {
            for (ProxyChecker = new Scanner((new URL(web)).openStream()); ProxyChecker.hasNextLine(); res.append(ProxyChecker.nextLine())) {
            }
            ProxyChecker.close();
            if (res.toString().contains(search)) {
                proxyIPs.add(ip);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();    
        }
        return false;
    }

    public void logToFile(final String message) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!data.exists()) {
                        data.mkdir();
                    }
                    File saveTo = new File(data, "markedproxies.log");
                    if (!saveTo.exists()) {
                        saveTo.createNewFile();
                    }
                    FileWriter fw = new FileWriter(saveTo, true);
                    PrintWriter pw = new PrintWriter(fw);
                    pw.println(message);
                    pw.flush();
                    pw.close();
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    public void writeIPs(final String message) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!data.exists()) {
                        data.mkdir();
                    }
                    File saveTo = new File(data, "blockthis.txt");
                    if (!saveTo.exists()) {
                        saveTo.createNewFile();
                    }
                    FileWriter fw = new FileWriter(saveTo, true);
                    PrintWriter pw = new PrintWriter(fw);
                    pw.println(message);
                    pw.flush();
                    pw.close();
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    public void pastebinSearch(String url, HashSet<String> hash) throws MalformedURLException, IOException {
        int ipsLoaded = 0;
        java.net.URLConnection c = (new URL(url)).openConnection();
        c.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");

        Scanner ipChecker = new Scanner(c.getInputStream());

        boolean title = false;

        while (ipChecker.hasNextLine()) {
            String line = ipChecker.nextLine();
            if (line.startsWith("-> ")) {
                pastebinSearch(line.replace("-> ", ""), hash);
            }
            if (line.startsWith("#")) {
                if (!title) {
                    Bukkit.getLogger().log(Level.INFO, "Descri��o do {0}", url);
                    title = true;
                }
                Bukkit.getLogger().log(Level.INFO, line.replace("# ", ""));
            } else if (line.contains(":")) {
                String[] split = line.split("\\:");
                try {
                    hash.add(split[0]);
                    ipsLoaded++;
                } catch (Exception e) {

                }
            } else {
                hash.add(line);
                ipsLoaded++;
            }
        }
        ipChecker.close();

        Bukkit.getLogger().log(Level.INFO, "[PowerSuperAntiProxy] IPs carregados do site {0} - {1}", new Object[] { url, ipsLoaded } );
    }
}
