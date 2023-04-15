package me.hobbie.compactar.commands;

import me.hobbie.compactar.Main;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CommandCompactar implements CommandExecutor {
    private Main plugin = Main.plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        Player player = (Player) sender;
        if (player.hasPermission("hbplugin.compactar")) {
            if (verificarQuantidadeItem(player, Material.RAW_BEEF) >= 9) {
                compactarItem(player, Material.RAW_BEEF, (String) configMessage(1, "names.raw_beef"));
            } else if (verificarQuantidadeItem(player, Material.LEATHER) >= 9) {
                compactarItem(player, Material.LEATHER, (String) configMessage(1, "names.leather"));
            } else if (verificarQuantidadeItem(player, Material.STRING) >= 9) {
                compactarItem(player, Material.STRING, (String) configMessage(1, "names.string"));
            } else if (verificarQuantidadeItem(player, Material.SPIDER_EYE) >= 9) {
                compactarItem(player, Material.SPIDER_EYE, (String) configMessage(1, "names.spider_eye"));
            } else if (verificarQuantidadeItem(player, Material.ROTTEN_FLESH) >= 9) {
                compactarItem(player, Material.ROTTEN_FLESH, (String) configMessage(1, "names.rotten_flesh"));
            } else if (verificarQuantidadeItem(player, Material.BONE) >= 9) {
                compactarItem(player, Material.BONE, (String) configMessage(1, "names.bone"));
            } else if (verificarQuantidadeItem(player, Material.ARROW) >= 9) {
                compactarItem(player, Material.ARROW, (String) configMessage(1, "names.arrow"));
            } else if (verificarQuantidadeItem(player, Material.BLAZE_ROD) >= 9) {
                compactarItem(player, Material.BLAZE_ROD, (String) configMessage(1, "names.blaze_rod"));
            } else {
                player.sendMessage((String) configMessage(1, "messages.insufficient_items"));
            }
        } else {
            player.sendMessage((String) configMessage(1, "messages.dont_permission"));
        }
        return true;
    }

    public void compactarItem(Player player, Material material, String nome) {
        if (plugin.getConfig().getBoolean("pay_to_use") == true) {
            if (Main.getEco().has(player, plugin.getConfig().getInt("coust"))) {
                Main.getEco().withdrawPlayer(player, plugin.getConfig().getInt("coust"));
            } else {
                player.sendMessage((String) configMessage(1, "messages.insufficient_money"));
                return;
            }
        }

        final int quantidadeItem = verificarQuantidadeItem(player, material);

        final int quantidadeCompactada = quantidadeItem / 9;

        removerItem(player, material, quantidadeCompactada * 9);

        ItemStack itemCompactado = new ItemStack(material);
        ItemMeta meta = itemCompactado.getItemMeta();
        meta.setDisplayName(nome);
        meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemCompactado.setItemMeta(meta);
        itemCompactado.setAmount(quantidadeCompactada);

        HashMap<Integer, ItemStack> itemsNaoAdicionados = player.getInventory().addItem(itemCompactado);

        if (!itemsNaoAdicionados.isEmpty()) {
            for (ItemStack item : itemsNaoAdicionados.values()) {
                player.getWorld().dropItem(player.getLocation(), item);
            }
            player.sendMessage((String) configMessage(1, "messages.drop_items"));
        } else {
            player.sendMessage((String) configMessage(1, "messages.success_compact"));
        }
    }

    public boolean removerItem(Player jogador, Material material, int quantidade) {
        Inventory inventario = jogador.getInventory();
        int quantRemover = quantidade;

        for (int i = 0; i < inventario.getSize(); i++) {
            ItemStack item = inventario.getItem(i);

            if (item != null && item.getType() == material) {
                int quantItem = item.getAmount();

                if (quantItem <= quantRemover) {
                    inventario.clear(i);
                    quantRemover -= quantItem;

                    if (quantRemover == 0) {
                        return true;
                    }
                } else {
                    item.setAmount(quantItem - quantRemover);
                    inventario.setItem(i, item);
                    return true;
                }
            }
        }

        return false;
    }

    public int verificarQuantidadeItem(Player jogador, Material material) {
        int quantidade = 0;
        Inventory inventario = jogador.getInventory();

        for (ItemStack item : inventario.getContents()) {
            if (item != null && item.getType() == material) {
                quantidade += item.getAmount();
            }
        }

        return quantidade;
    }

    public Object configMessage(int type, String config) {
        Object result = null;
        switch (type) {
            case 1:
                result = plugin.getConfig().getString(config).replace("&", "§");
                break;
            case 2:
                result = plugin.getConfig().getInt(config);
                break;
            case 3:
                result = plugin.getConfig().getBoolean(config);
                break;
            default:
                break;
        }
        return result;
    }
}