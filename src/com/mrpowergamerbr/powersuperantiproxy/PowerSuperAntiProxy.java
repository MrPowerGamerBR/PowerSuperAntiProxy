package com.mrpowergamerbr.powersuperantiproxy;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.mrpowergamerbr.powersuperantiproxy.listeners.AsyncJoinListener;
import com.mrpowergamerbr.powersuperantiproxy.protocollib.ProtocolBlocker;
import com.mrpowergamerbr.powersuperantiproxy.utils.ReloadCommand;

import lombok.*;

@Getter
@Setter
public class PowerSuperAntiProxy extends JavaPlugin implements Listener {

    EntaoBabyEsperaUmPouco cnf;

    public static PowerSuperAntiProxy getInstance() {
        return (PowerSuperAntiProxy) Bukkit.getPluginManager().getPlugin("PowerSuperAntiProxy");
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        cnf = new EntaoBabyEsperaUmPouco();
        cnf.init();

        new AsyncJoinListener(this);

        Bukkit.getPluginManager().registerEvents(this, this);

        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
            if (getConfig().getBoolean("UsarProtocolLib")) {
                ProtoLibGambiarra.register(this);
            }
        }

        new ReloadCommand("powersuperantiproxy", this).register();
    }

    @Override
    public void onDisable() {
    }

    public PowerSuperAntiProxy getMe() {
        return this;
    }
}

class ProtoLibGambiarra {

    public static void register(PowerSuperAntiProxy m) {
        ProtocolLibrary.getProtocolManager().addPacketListener(new ProtocolBlocker(m, new PacketType[]{PacketType.Handshake.Client.SET_PROTOCOL}));
    }
}
