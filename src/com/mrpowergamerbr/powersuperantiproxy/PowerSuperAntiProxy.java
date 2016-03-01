package com.mrpowergamerbr.powersuperantiproxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class PowerSuperAntiProxy extends JavaPlugin implements Listener {
	ArrayList<String> safeIPs = new ArrayList<String>();
	ArrayList<String> proxyIPs = new ArrayList<String>();

	final String proxyUse = "§cUso de Proxy!\n\n§cDesative o Proxy antes de conectar!";
	
	ArrayList<String> deathBotIPs = new ArrayList<String>();
	ArrayList<String> powerHateListIPs = new ArrayList<String>();
	
	static ArrayList<String> cidr = new ArrayList<String>();

	@Override
	public void onEnable() {
		Scanner ipChecker;
		/*
		 * PowerHateList
		 * HAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAATEEEEEEEEEEEEEEEEEEEEEEEEE
		 */
		Thread t = new Thread(new Runnable() {
			public void run() {
				System.out.println("CIDRs IPs...");

				try(BufferedReader br = new BufferedReader(new FileReader(getDataFolder() + "/scriptkiddies.txt"))) {
					for(String line; (line = br.readLine()) != null; ) {
						// process the line.
						cidr.add(line);
					}
					// line is not visible here.
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println("Finished gathering CIDR IP!");
			}
		});
		t.start();

		try {
			ipChecker = new Scanner((new URL("http://158.69.120.5/hate.txt")).openStream());
			System.out.println("HATE. LET ME TELL YOU HOW MUCH I'VE COME TO HATE YOU SINCE I BEGAN TO LIVE.");
			while (ipChecker.hasNextLine()) {
				String line = ipChecker.nextLine();
				if (line.contains("HATE") || line.equals("")) {
					continue;
				}
				powerHateListIPs.add(line);
			}
			// System.out.println("HATE. " + powerHateListIPs.toString());
			ipChecker.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		}

		try(BufferedReader br = new BufferedReader(new FileReader(new File(getDataFolder(), "blockthis.txt")))) {
			for(String line; (line = br.readLine()) != null; ) {
				// process the line.
				proxyIPs.add(line);
			}
			// line is not visible here.
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Bukkit.getPluginManager().registerEvents(this, this);

		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, new PacketType[] { PacketType.Handshake.Client.SET_PROTOCOL, PacketType.Login.Client.START }) {
			public void onPacketReceiving(final PacketEvent event) {
				InetAddress inet = event.getPlayer().getAddress().getAddress();
				
				if (safeIPs.contains(inet.getHostAddress())) {
					return;
				}

				if (proxyIPs.contains(inet.getHostAddress())) {
					event.setCancelled(true);
					return;
				}

				if (deathBotIPs.contains(inet.getHostAddress())) {
					event.setCancelled(true);

					proxyIPs.add(inet.getHostAddress());

					logToFile("[" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR) + " " + Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + "] " + inet.getHostAddress() + ": ProtocolPwned! (DeathBot IPs)");
					return;
				}

				if (powerHateListIPs.contains(inet.getHostAddress())) {
					event.setCancelled(true);

					proxyIPs.add(inet.getHostAddress());

					logToFile("[" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR) + " " + Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + "] " + inet.getHostAddress() + ": ProtocolPwned! (HateList IPs)");
					return;
				}
			}
		});
	}

	@Override
	public void onDisable() {
	}

	@EventHandler
	public void onJoin(final AsyncPlayerPreLoginEvent apple) {
		final InetAddress inet = apple.getAddress();

		if (safeIPs.contains(inet.getHostAddress())) {
			return;
		}

		if (proxyIPs.contains(inet.getHostAddress())) {
			apple.disallow(Result.KICK_OTHER, proxyUse);
			// logToFile("[" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR) + " " + Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + "] " + apple.getAddress().getHostName() + "///" + apple.getAddress().getHostAddress() + " (" + apple.getName() + ") " + ": Proxy do DeathBot!");
			return;
		}
		
		if (deathBotIPs.contains(inet.getHostAddress())) {
			apple.disallow(Result.KICK_OTHER, proxyUse);
			proxyIPs.add(inet.getHostAddress());
			logToFile("[" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR) + " " + Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + "] " + apple.getAddress().getHostName() + "///" + apple.getAddress().getHostAddress() + " (" + apple.getName() + ") " + ": Proxy do DeathBot!");
			return;
		}
		
		if (powerHateListIPs.contains(inet.getHostAddress())) {
			proxyIPs.add(inet.getHostAddress());
			apple.disallow(Result.KICK_OTHER, proxyUse);
			logToFile("[" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR) + " " + Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + "] " + apple.getAddress().getHostName() + "///" + apple.getAddress().getHostAddress() + " (" + apple.getName() + ") " + ": Proxy no hate.txt!");
			return;
		}
		
		for (String s : cidr) {
			String[] split = inet.getHostAddress().split("\\.");
			if (s.startsWith(split[0] + ".")) {
				String[] addresses = new SubnetUtils(s).getInfo().getAllAddresses();
				
				if (Arrays.asList(addresses).contains(s)) {
					apple.disallow(Result.KICK_OTHER, proxyUse);
					logToFile("[" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR) + " " + Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + "] " + apple.getAddress().getHostName() + "///" + apple.getAddress().getHostAddress() + " (" + apple.getName() + ") " + ": Proxy ScriptKiddie!");
					writeIPs(inet.getHostAddress());
					return;
				}
			}
		}

		for (String ip : powerHateListIPs) {
			if (inet.getHostAddress().contains(ip)) {
				apple.disallow(Result.KICK_OTHER, proxyUse);
				logToFile("[" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR) + " " + Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + "] " + apple.getAddress().getHostName() + "///" + apple.getAddress().getHostAddress() + " (" + apple.getName() + ") " + ": Proxy no hate.txt (Second Pass)!");
				writeIPs(inet.getHostAddress());
				return;
			}
			if (powerHateListIPs.contains(".")) {
				int i = 1;
				String[] moddedIP = ip.split(".");
				String finalIP = "";
				while (4 >= i) {
					if (100 > Integer.parseInt(moddedIP[i])) {
						finalIP = finalIP + "0";
						if (10 > Integer.parseInt(moddedIP[i])) {
							finalIP = finalIP + "0";
						}
					}
					finalIP = finalIP + moddedIP[i] + "-";
					i++;
				}
				if (inet.getHostName().contains(ip)) {
					apple.disallow(Result.KICK_OTHER, proxyUse);
					proxyIPs.add(inet.getHostAddress());
					logToFile("[" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR) + " " + Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + "] " + apple.getAddress().getHostName() + "///" + apple.getAddress().getHostAddress() + " (" + apple.getName() + ") " + ": Proxy no hate.txt (Third Pass)!");
					writeIPs(inet.getHostAddress());
					return;
				}
			}
		}

		/*
		 * Hotspot Shield
		 */
		if (inet.getHostName().toLowerCase().contains("anchorfree")) {
			apple.disallow(Result.KICK_OTHER, proxyUse);
			proxyIPs.add(inet.getHostAddress());
			logToFile("[" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR) + " " + Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + "] " + apple.getAddress().getHostName() + "///" + apple.getAddress().getHostAddress() + " (" + apple.getName() + ") " + ": Proxy utilizando o HotspotShield!");
			writeIPs(inet.getHostAddress());
			return;
		}

		/*
		 * IP Redator
		 */
		if (inet.getHostName().toLowerCase().contains("ipredator.se")) {
			apple.disallow(Result.KICK_OTHER, proxyUse);
			proxyIPs.add(inet.getHostAddress());
			logToFile("[" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR) + " " + Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + "] " + apple.getAddress().getHostName() + "///" + apple.getAddress().getHostAddress() + " (" + apple.getName() + ") " + ": Proxy utilizando o IP Redator!");
			writeIPs(inet.getHostAddress());
			return;
		}

		/*
		 * PixelFucker
		 */
		if (inet.getHostName().toLowerCase().contains("pixelfucker.org")) {
			apple.disallow(Result.KICK_OTHER, proxyUse);
			proxyIPs.add(inet.getHostAddress());
			logToFile("[" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR) + " " + Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + "] " + apple.getAddress().getHostName() + "///" + apple.getAddress().getHostAddress() + " (" + apple.getName() + ") " + ": Proxy utilizando o PixelFucker!");
			writeIPs(inet.getHostAddress());
			return;
		}

		/*
		 * TheRemailer
		 */
		if (inet.getHostName().toLowerCase().contains("theremailer.net")) {
			apple.disallow(Result.KICK_OTHER, proxyUse);
			proxyIPs.add(inet.getHostAddress());
			logToFile("[" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR) + " " + Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + "] " + apple.getAddress().getHostName() + "///" + apple.getAddress().getHostAddress() + " (" + apple.getName() + ") " + ": Proxy utilizando o TheRemailer!");
			writeIPs(inet.getHostAddress());
			return;
		}

		/*
		 * Tor Exit
		 */
		if (inet.getHostName().toLowerCase().contains("tor-exit") || inet.getHostName().toLowerCase().contains("torexit") || inet.getHostName().toLowerCase().contains("exitpoint")) {
			apple.disallow(Result.KICK_OTHER, proxyUse);
			proxyIPs.add(inet.getHostAddress());
			logToFile("[" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR) + " " + Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + "] " + apple.getAddress().getHostName() + "///" + apple.getAddress().getHostAddress() + " (" + apple.getName() + ") " + ": Proxy utilizando um Tor Exit Point!");
			writeIPs(inet.getHostAddress());
			return;
		}

		/*
		 * OVH IPs
		 */
		if (inet.getHostName().toLowerCase().matches("ns[0-9]+")) {
			apple.disallow(Result.KICK_OTHER, proxyUse);
			proxyIPs.add(inet.getHostAddress());
			logToFile("[" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR) + " " + Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + "] " + apple.getAddress().getHostName() + "///" + apple.getAddress().getHostAddress() + " (" + apple.getName() + ") " + ": IP da OVH!");
			writeIPs(inet.getHostAddress());
			return;
		}

		// http://api.stopforumspam.org/api?ip=91.186.18.61
		if (searchAndDestroy("http://api.stopforumspam.org/api?ip=" + inet.getHostAddress(), inet.getHostAddress(), "<appears>yes")) {
			apple.disallow(Result.KICK_OTHER, proxyUse);
			logToFile("[" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR) + " " + Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + "] " + apple.getAddress().getHostName() + "///" + apple.getAddress().getHostAddress() + " (" + apple.getName() + ") " + ": Marcado como Proxy pelo StopForumSpam!");
			writeIPs(inet.getHostAddress());
			return;
		}

		// http://www.shroomery.org/ythan/proxycheck.php?ip=177.138.98.29
		/* if (searchAndDestroy("http://www.shroomery.org/ythan/proxycheck.php?ip=" + inet.getHostAddress(), inet.getHostAddress(), "Y")) {
			apple.disallow(Result.KICK_OTHER, proxyUse);
			logToFile("[" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR) + " " + Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + "] " + apple.getAddress().getHostName() + "///" + apple.getAddress().getHostAddress() + " (" + apple.getName() + ") " + ": Marcado como Proxy pelo Shoomery!");
			writeIPs(inet.getHostAddress());
			return;
		} */

		// http://botscout.com/test/?ip=189.15.67.70
		//if (searchAndDestroy("http://botscout.com/test/?ip=" + inet.getHostAddress() + "&key=Xf3a2W8O6lcs5ND", inet.getHostAddress(), "Y")) {
		//	apple.disallow(Result.KICK_OTHER, proxyUse);
		//	logToFile("[" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR) + " " + Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + "] " + apple.getAddress().getHostName() + "///" + apple.getAddress().getHostAddress() + " (" + apple.getName() + ") " + ": Marcado como Proxy pelo BotScout!");
		//	writeIPs(inet.getHostAddress());
		//	return;
		//}

		safeIPs.add(inet.getHostAddress());
	}

	public PowerSuperAntiProxy getMe() {
		return this;
	}

	public boolean searchAndDestroy(String web, String ip, String search) {
		if (ip.equals("127.0.0.1")) {
			return false;
		}
		if (proxyIPs.contains(ip)) {
			return true;
		}
		if (safeIPs.contains(ip)) {
			return false;
		}
		String res = "";

		Scanner ProxyChecker;
		try {
			for (ProxyChecker = new Scanner((new URL(web)).openStream());
					ProxyChecker
					.hasNextLine(); res = res + ProxyChecker.nextLine()) {;
			}
			ProxyChecker.close();
			if (res.contains(search)) {
				proxyIPs.add(ip);
			}
			return res.contains(search) ? Boolean.valueOf(true) : Boolean
					.valueOf(false);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void logToFile(final String message)
	{
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try
				{
					File dataFolder = getDataFolder();
					if(!dataFolder.exists())
					{
						dataFolder.mkdir();
					}
					File saveTo = new File(getDataFolder(), "markedproxies.log");
					if (!saveTo.exists())
					{
						saveTo.createNewFile();
					}
					FileWriter fw = new FileWriter(saveTo, true);
					PrintWriter pw = new PrintWriter(fw);
					pw.println(message);
					pw.flush();
					pw.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		});
		t.start();
	}

	public void writeIPs(final String message)
	{
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try
				{
					File dataFolder = getDataFolder();
					if(!dataFolder.exists())
					{
						dataFolder.mkdir();
					}
					File saveTo = new File(getDataFolder(), "blockthis.txt");
					if (!saveTo.exists())
					{
						saveTo.createNewFile();
					}
					FileWriter fw = new FileWriter(saveTo, true);
					PrintWriter pw = new PrintWriter(fw);
					pw.println(message);
					pw.flush();
					pw.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		});
		t.start();
	}
}