package com.mrpowergamerbr.powersuperantiproxy.utils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RetroUtils {

    public static Method getOP;

    static {
        try {
            getOP = Bukkit.getServer().getClass().getMethod("getOnlinePlayers");
        } catch (NoSuchMethodException | SecurityException e) {
            // Qual versão você está usando?
            e.printStackTrace();
        }
    }

    public static Collection<? extends Player> getOnlinePlayers() {
        try {
            Object object = getOP.invoke(Bukkit.getServer());

            if (object instanceof Collection) {
                return (Collection<? extends Player>) object;
            } else {
                // Ah não... isto é um Player[]! Maldita *insira versão antes da 1.7.9-R0.3-SNAPSHOT!*
                Player[] oldList = (Player[]) object;

                return Arrays.asList(oldList);
            }
        } catch (Exception e) {
            // Oh no! Deu problema! Vamos usar o Bukkit.getOnlinePlayers() e torcer para que dê certo...
            return Bukkit.getOnlinePlayers();
        }
    }
}
