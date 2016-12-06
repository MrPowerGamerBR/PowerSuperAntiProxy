package com.mrpowergamerbr.powersuperantiproxy.listeners;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

import com.mrpowergamerbr.powersuperantiproxy.PowerSuperAntiProxy;
import com.mrpowergamerbr.powersuperantiproxy.SubnetUtils;
import com.mrpowergamerbr.powersuperantiproxy.utils.RetroUtils;

public class AsyncJoinListener implements Listener {
    PowerSuperAntiProxy m;

    public AsyncJoinListener(PowerSuperAntiProxy m) {
        Bukkit.getPluginManager().registerEvents(this, m);
        this.m = m;
    }

    @EventHandler
    public void onJoin(final AsyncPlayerPreLoginEvent apple) {
        final InetAddress inet = apple.getAddress();

        if (m.getCnf().getSafeIPs().contains(inet.getHostAddress())) {
            return;
        }

        if (m.getCnf().getProxyIPs().contains(inet.getHostAddress())) {
            apple.disallow(Result.KICK_OTHER, m.getCnf().getProxyUse());
            m.getCnf().logToFile("[" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR) + " " + Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + "] " + apple.getAddress().getHostName() + "///" + apple.getAddress().getHostAddress() + " (" + apple.getName() + ") " + ": Proxy do DeathBot!");
            return;
        }

        if (m.getConfig().getInt("BloquearContasPorIP") != -1) {
            int max = m.getConfig().getInt("BloquearContasPorIP");

            int current = 0;

            for (Player p : RetroUtils.getOnlinePlayers()) {
                if (inet.getHostAddress().equals(p.getAddress().getAddress().getHostAddress())) {
                    current++;
                }
            }

            if (current > max) {
                apple.disallow(Result.KICK_OTHER, m.getCnf().getProxyUse());
                return;
            }
        }

        if (m.getCnf().getDeathBotIPs().contains(inet.getHostAddress())) {
            apple.disallow(Result.KICK_OTHER, m.getCnf().getProxyUse());
            m.getCnf().getProxyIPs().add(inet.getHostAddress());
            m.getCnf().logToFile("[" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR) + " " + Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + "] " + apple.getAddress().getHostName() + "///" + apple.getAddress().getHostAddress() + " (" + apple.getName() + ") " + ": Proxy do DeathBot!");
            return;
        }

        if (m.getCnf().getPowerHateListIPs().contains(inet.getHostAddress())) {
            m.getCnf().getProxyIPs().add(inet.getHostAddress());
            apple.disallow(Result.KICK_OTHER, m.getCnf().getProxyUse());
            m.getCnf().logToFile("[" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR) + " " + Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + "] " + apple.getAddress().getHostName() + "///" + apple.getAddress().getHostAddress() + " (" + apple.getName() + ") " + ": Proxy no hate.txt!");
            return;
        }

        try {
            for (String s : m.getCnf().getCidr()) {
                String[] split = inet.getHostAddress().split("\\.");
                if (s.startsWith(split[0] + ".")) {
                    String[] addresses = new SubnetUtils(s).getInfo().getAllAddresses();

                    if (Arrays.asList(addresses).contains(s)) {
                        apple.disallow(Result.KICK_OTHER, m.getCnf().getProxyUse());
                        m.getCnf().logToFile("[" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR) + " " + Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + "] " + apple.getAddress().getHostName() + "///" + apple.getAddress().getHostAddress() + " (" + apple.getName() + ") " + ": Proxy ScriptKiddie!");
                        m.getCnf().writeIPs(inet.getHostAddress());
                        return;
                    }
                }
            }
        } catch (Exception e) {

        }

        /*
         * Bloquear Hostnames Feios
         */
        if ((boolean) m.getConfig().get("BloquearHostnamesFeios")) {
            ArrayList<String> hostnames = (ArrayList<String>) m.getConfig().get("HostnamesBloqueados");
            for (String hostname : hostnames) {
                if (inet.getHostName().toLowerCase().contains("hostname")) {
                    apple.disallow(Result.KICK_OTHER, m.getCnf().getProxyUse());
                    m.getCnf().logToFile("[" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR) + " " + Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + "] " + apple.getAddress().getHostName() + "///" + apple.getAddress().getHostAddress() + " (" + apple.getName() + ") " + ": IP contém " + hostname + "!");
                    m.getCnf().getProxyIPs().add(inet.getHostAddress());
                    return;
                }
            }
        }

        /*
         * Bloquear IPs da OVH
         */
        if ((boolean) m.getConfig().get("BloquearIPsDaOVH") && inet.getHostName().toLowerCase().matches(".*ns[0-9]+.*")) {
            apple.disallow(Result.KICK_OTHER, m.getCnf().getProxyUse());
            m.getCnf().logToFile("[" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR) + " " + Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + "] " + apple.getAddress().getHostName() + "///" + apple.getAddress().getHostAddress() + " (" + apple.getName() + ") " + ": IP da OVH!");
            m.getCnf().getProxyIPs().add(inet.getHostAddress());
            return;
        }

        if ((boolean) m.getConfig().get("UsarStopForumSpam")) {
            // http://api.stopforumspam.org/api?ip=91.186.18.61
            if (m.getCnf().searchAndDestroy("http://api.stopforumspam.org/api?ip=" + inet.getHostAddress(), inet.getHostAddress(), "<appears>yes")) {
                apple.disallow(Result.KICK_OTHER, m.getCnf().getProxyUse());
                m.getCnf().logToFile("[" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR) + " " + Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + "] " + apple.getAddress().getHostName() + "///" + apple.getAddress().getHostAddress() + " (" + apple.getName() + ") " + ": Marcado como Proxy pelo StopForumSpam!");
                m.getCnf().writeIPs(inet.getHostAddress());
                return;
            }
        }

        m.getCnf().getSafeIPs().add(inet.getHostAddress());
    }
}
