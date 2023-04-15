package me.hobbie.compactar;

import me.hobbie.compactar.commands.CommandCompactar;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Main extends JavaPlugin {
    public static Main plugin;
    public static Economy econ = null;

    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        this.getCommand("compactardrop").setExecutor(new CommandCompactar());
        Bukkit.getConsoleSender().sendMessage("§aO plugin §2\"hbCompactarDrop\" §ado repositório §2\"Outside Projects\" §afoi habilitado.");

        if (!setupEconomy())
        {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }

    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§cO plugin §4\"hbCompactarDrop\" §cdo repositório §4\"Outside Projects\" cafoi desabilitado.");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null) {
            return false;
        }

        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEco() {
        return econ;
    }
}
