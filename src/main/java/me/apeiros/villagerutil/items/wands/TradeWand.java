package me.apeiros.villagerutil.items.wands;

import java.lang.ref.WeakReference;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.EntityInteractHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;

import me.apeiros.villagerutil.Setup;
import me.apeiros.villagerutil.VillagerUtil;
import me.apeiros.villagerutil.util.Utils;

public class TradeWand extends SlimefunItem implements Listener {

    // Weak Reference to a villager
    private static WeakReference<Villager> villagerRef;

    // Creates Villager Trade Wand
    public TradeWand(ItemGroup ig) {
        super(ig, Setup.TRADE_WAND, "VU_TRADE_WAND", RecipeType.ANCIENT_ALTAR, new ItemStack[] {
            SlimefunItems.VILLAGER_RUNE, SlimefunItems.STRANGE_NETHER_GOO, Setup.TOKEN,
            SlimefunItems.FILLED_FLASK_OF_KNOWLEDGE, new ItemStack(Material.END_ROD), new ItemStack(Material.EMERALD_BLOCK),
            Setup.TOKEN, new ItemStack(Material.EMERALD_BLOCK), SlimefunItems.STAFF_ELEMENTAL
        });
    }

    // Creates and returns handler
    private EntityInteractHandler getEntityInteractHandler() {
        return (e, i, offhand) -> {
            // Cancel event
            e.setCancelled(true);
            
            // Check if the clicked entity is a villager
            Entity en = e.getRightClicked();
            if (en instanceof Villager) {
                // Store villager, profession, player, and inventory
                Villager v = (Villager) en;
                Profession prof = v.getProfession();
                Player p = e.getPlayer();
                Inventory inv = p.getInventory();

                // Check for permission
                if (!Slimefun.getProtectionManager().hasPermission(p, p.getLocation(), Interaction.INTERACT_ENTITY)) {
                    p.sendMessage(ChatColors.color("&c你沒有權限!"));
                    v.shakeHead();
                    return;
                }

                // Check if the villager has no job or is a nitwit
                if (prof == Profession.NONE || prof == Profession.NITWIT) {
                    p.sendMessage(ChatColors.color("&c這個村民沒有職業!"));
                    v.shakeHead();
                    return;
                }

                // Check for villager tokens
                if (!Utils.hasToken(p, inv)) {
                    p.sendMessage(ChatColors.color("&c村民代幣不足!"));
                    v.shakeHead();
                    return;
                }
                
                // Check if the villager's trades are locked
                if (Utils.villagerTradesLocked(v)) {
                    // Reference the villager and its profession
                    villagerRef = new WeakReference<>(v);
                    
                    // Ask user for confirmation to reset trades
                    sendWarning(p);
                } else {
                    // Remove profession
                    Utils.removeProfession(v);

                    // Add back profession
                    v.setProfession(prof);

                    // Play sounds
                    World w = v.getWorld();
                    Location l = v.getLocation();
                    w.playSound(l, Sound.ITEM_LODESTONE_COMPASS_LOCK, 1F, 1F);
                    w.playSound(l, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1F, 1F);

                    // Consume villager token
                    Utils.removeToken(p, inv);
                }
            }
        };
    }

    // Registers handler
    public void preRegister() {
        this.addItemHandler(getEntityInteractHandler());
    }

    // Sends warning to players trying to reset a locked villager
    private void sendWarning(Player p) {
        // Send normal messages
        p.sendMessage(ChatColors.color("&c該村民的交易已被鎖定."));
        p.sendMessage(ChatColors.color("&c你確定要重置其交易?"));

        // Create component
        BaseComponent message = new TextComponent(ChatColors.color("&6點擊這裡 &7重置該村民的交易"));
        message.setClickEvent(new ClickEvent(Action.RUN_COMMAND, 
                "/resetvillager " + VillagerUtil.getCommandNumber()));

        // Send component
        p.spigot().sendMessage(message);
    }

    // Resets locked trades of the villager
    public static void resetLockedTrades(Player p) {
        // Get the villager from the reference
        Villager v = villagerRef.get();

        // Check if the villager exists
        if (v == null) {
            p.sendMessage(ChatColors.color("&c確認時間過長!"));
            p.sendMessage(ChatColors.color("&c再次右鍵點擊村民,然後點擊聊天消息."));
        } else {
            // Store the profession and the player's inventory
            Inventory inv = p.getInventory();
            Profession prof = v.getProfession();

            // Whether or not tokens are used
            boolean useTokens = VillagerUtil.useTokens();

            // Check for permission
            if (!Slimefun.getProtectionManager().hasPermission(p, p.getLocation(), Interaction.INTERACT_ENTITY)) {
                p.sendMessage(ChatColors.color("&c你沒有權限!"));
                v.shakeHead();
                return;
            }

            // Check if the villager has no job or is a nitwit
            if (prof == Profession.NONE || prof == Profession.NITWIT) {
                p.sendMessage(ChatColors.color("&c這個村民沒有職業!"));
                v.shakeHead();
                return;
            }

            // Check for villager tokens
            if (!Utils.hasToken(p, inv)) {
                p.sendMessage(ChatColors.color("&c村民代幣不足!"));
                v.shakeHead();
                return;
            }

            // Reset trades and villager exp
            Utils.removeProfessionAndExp(v);

            // Set back the profession
            v.setProfession(prof);

            // Play sounds
            World w = v.getWorld();
            Location l = v.getLocation();
            w.playSound(l, Sound.BLOCK_BEACON_ACTIVATE, 1F, 1F);
            w.playSound(l, Sound.BLOCK_BEACON_POWER_SELECT, 1F, 1F);

            // Consume villager token
            Utils.removeToken(p, inv);

            // Clean up the reference
            villagerRef.clear();
        }
    }
    
}
