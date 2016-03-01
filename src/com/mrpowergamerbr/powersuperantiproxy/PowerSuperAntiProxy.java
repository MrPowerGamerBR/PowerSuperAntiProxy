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
import java.util.logging.Level;

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
import com.mrpowergamerbr.powersuperantiproxy.utils.AsrielConfig;
import com.mrpowergamerbr.powersuperantiproxy.utils.PowerCommandUtils;
import com.mrpowergamerbr.powersuperantiproxy.utils.TemmieUpdater;

public class PowerSuperAntiProxy extends JavaPlugin implements Listener {
	ArrayList<String> safeIPs = new ArrayList<String>();
	ArrayList<String> proxyIPs = new ArrayList<String>();

	String proxyUse = "§cUso de Proxy!\n\n§cDesative o Proxy antes de conectar!";

	ArrayList<String> deathBotIPs = new ArrayList<String>();
	ArrayList<String> powerHateListIPs = new ArrayList<String>();

	static ArrayList<String> cidr = new ArrayList<String>();

	public AsrielConfig asriel;

	public static final String pluginName = "PowerSuperAntiProxy";

	public static final String pluginVersion = "v1.0.1";

	@Override
	public void onEnable() {
		saveDefaultConfig();
		
		/*
		 * Iniciar o AsrielConfig
		 */
		asriel = new AsrielConfig(this);

		proxyUse = asriel.getChanged("MensagemDeKick");

		new PowerCommandUtils(this, "powerantiproxy");

		/*
		 * IPs em formato CIDR do ASkidban
		 */
		if ((boolean) asriel.get("UsarASkidban")) {
			Thread t = new Thread(new Runnable() {
				public void run() {
					System.out.println("[" + pluginName + "] Pegando os IPs CIDR...");

					try {
						Scanner ipChecker = new Scanner((new URL("https://raw.githubusercontent.com/pisto/ASkidban/master/compiled/ipv4")).openStream());
						while (ipChecker.hasNextLine()) {
							String line = ipChecker.nextLine();
							cidr.add(line);
						}
						System.out.println("[" + pluginName + "] IPs em formato CIDR foram pegos! Yay!");
						ipChecker.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("[" + pluginName + "] Um erro ocorreu ao tentar pegar os IPs em formado CIDR!");
						return;
					}
				}
			});
			t.start();
		}


		Scanner ipChecker;
		/*
		 * PowerHateList
		 * HAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAATEEEEEEEEEEEEEEEEEEEEEEEEE
		 */

		if ((boolean) asriel.get("BuscarDeSites")) {
			ArrayList<String> websitesToFind = (ArrayList<String>) asriel.get("SitesBuscados");

			for (String web : websitesToFind) {
				int ipsLoaded = 0;
				try {
					ipChecker = new Scanner((new URL(web)).openStream());


					while (ipChecker.hasNextLine()) {
						String line = ipChecker.nextLine();
						if (line.contains("HATE") || line.equals("")) {
							continue;
						}
						ipsLoaded++;
						powerHateListIPs.add(line);
					}
					// System.out.println("HATE. " + powerHateListIPs.toString());
					ipChecker.close();

					Bukkit.getLogger().log(Level.INFO, "[" + pluginName + "] IPs carregados do site " + web + " - " + ipsLoaded);
				} catch (Exception e) {
					Bukkit.getLogger().log(Level.INFO, "[" + pluginName + "] Um problema ocorreu ao tentar carregar os IPs do site: " + web);
					continue;
				}
			}
		}

		if ((boolean) asriel.get("BloquearIPsDoDeathBot")) {
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

			File blockThis = new File(getDataFolder(), "blockthis.txt");
			if (blockThis.exists()) {
				try(BufferedReader br = new BufferedReader(new FileReader(blockThis))) {
					for(String line; (line = br.readLine()) != null; ) {
						proxyIPs.add(line);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		Bukkit.getPluginManager().registerEvents(this, this);

		if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
			if ((boolean) asriel.get("UsarProtocolLib")) {
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

							logToFile("[" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR) + " " + Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + "] " + inet.getHostAddress() + ": Bloqueado em Protocol Level! (DeathBot IPs)");
							return;
						}

						/* if (powerHateListIPs.contains(inet.getHostAddress())) {
							event.setCancelled(true);

							proxyIPs.add(inet.getHostAddress());

							logToFile("[" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR) + " " + Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + "] " + inet.getHostAddress() + ": Bloqueado em Protocol Level! (HateList IPs)");
							return;
						} */
					}
				});
			}
		}

		if ((boolean) asriel.get("TemmieUpdater.VerificarUpdates")) {
			new TemmieUpdater(this);
		}
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
		 * Bloquear Hostnames Feios
		 */
		if ((boolean) asriel.get("BloquearHostnamesFeios")) {
			ArrayList<String> hostnames = (ArrayList<String>) asriel.get("HostnamesBloqueados");
			for (String hostname : hostnames) {
				if (inet.getHostName().toLowerCase().contains("hostname")) {
					apple.disallow(Result.KICK_OTHER, proxyUse);
					logToFile("[" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR) + " " + Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + "] " + apple.getAddress().getHostName() + "///" + apple.getAddress().getHostAddress() + " (" + apple.getName() + ") " + ": IP contém " + hostname + "!");
					proxyIPs.add(inet.getHostAddress());
					return;
				}
			}
		}

		/*
		 * Bloquear IPs da OVH
		 */
		if ((boolean) asriel.get("BloquearIPsDaOVH") && inet.getHostName().toLowerCase().matches(".*ns[0-9]+.*")) {
			apple.disallow(Result.KICK_OTHER, proxyUse);
			logToFile("[" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR) + " " + Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + "] " + apple.getAddress().getHostName() + "///" + apple.getAddress().getHostAddress() + " (" + apple.getName() + ") " + ": IP da OVH!");
			proxyIPs.add(inet.getHostAddress());
			return;
		}

		if ((boolean) asriel.get("UsarStopForumSpam")) {
			// http://api.stopforumspam.org/api?ip=91.186.18.61
			if (searchAndDestroy("http://api.stopforumspam.org/api?ip=" + inet.getHostAddress(), inet.getHostAddress(), "<appears>yes")) {
				apple.disallow(Result.KICK_OTHER, proxyUse);
				logToFile("[" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR) + " " + Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + "] " + apple.getAddress().getHostName() + "///" + apple.getAddress().getHostAddress() + " (" + apple.getName() + ") " + ": Marcado como Proxy pelo StopForumSpam!");
				writeIPs(inet.getHostAddress());
				return;
			}
		}

		safeIPs.add(inet.getHostAddress());
	}

	public PowerSuperAntiProxy getMe() {
		return this;
	}

	public boolean searchAndDestroy(String web, String ip, String search) {
		if (ip.equals("127.0.0.1")) {
			return false;
		}
		if (ip.startsWith("192.168")) {
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