package com.mrpowergamerbr.powersuperantiproxy.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mrpowergamerbr.powersuperantiproxy.PowerSuperAntiProxy;

public class ReloadCommand extends AbstractCommand {
    PowerSuperAntiProxy m;
    
    public ReloadCommand(String command, PowerSuperAntiProxy m) {
        super(command);
        this.m = m;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("SparklySuperAntiProxy.ReloadConfig")) {
            m.getCnf().init();
            sender.sendMessage("§aConfig recarregada!");
            return true;
        }
        sender.sendMessage("§6§lPowerSuperAntiProxy §8- §7Criado por §b§lMrPowerGamerBR");
        sender.sendMessage("§bMrPowerGamerBR Website:§3 http://mrpowergamerbr.com");
        sender.sendMessage("§4§lSparkly§b§lPower§b:§3 http://sparklypower.net");
        return true;
    }

}
