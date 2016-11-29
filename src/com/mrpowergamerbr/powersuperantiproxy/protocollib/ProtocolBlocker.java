package com.mrpowergamerbr.powersuperantiproxy.protocollib;

import java.net.InetAddress;
import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.mrpowergamerbr.powersuperantiproxy.PowerSuperAntiProxy;

public class ProtocolBlocker extends PacketAdapter {
    PowerSuperAntiProxy m;
    
    public ProtocolBlocker(PowerSuperAntiProxy plugin, PacketType[] types) {
        super(plugin, types);
        this.m = plugin;
    }

    @Override
    public void onPacketReceiving(final PacketEvent event) {
        InetAddress inet = event.getPlayer().getAddress().getAddress();

        if (m.getCnf().getSafeIPs().contains(inet.getHostAddress())) {
            return;
        }

        if (m.getCnf().getProxyIPs().contains(inet.getHostAddress())) {
            event.setCancelled(true);
            return;
        }

        if (m.getConfig().getInt("BloquearContasPorIP") != -1) {
            int max = m.getConfig().getInt("BloquearContasPorIP");
            
            int current = 0;
            
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (inet.getHostAddress().equals(p.getAddress().getAddress().getHostAddress())) {
                    current++;
                }
            }
            
            if (current > max) {
                event.setCancelled(true);
                return;
            }
        }
        
        if (m.getCnf().getDeathBotIPs().contains(inet.getHostAddress())) {
            event.setCancelled(true);

            m.getCnf().getProxyIPs().add(inet.getHostAddress());

            m.getCnf().logToFile("[" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR) + " " + Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + "] " + inet.getHostAddress() + ": Bloqueado em Protocol Level! (DeathBot IPs)");
            return;
        }
    }
    
}
