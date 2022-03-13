package me.apeiros.villagerutil;

import lombok.experimental.UtilityClass;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import me.apeiros.villagerutil.commands.ResetVillagerCommand;
import me.apeiros.villagerutil.items.TransportCharm;
import me.apeiros.villagerutil.items.wands.CureWand;
import me.apeiros.villagerutil.items.wands.NitwitWand;
import me.apeiros.villagerutil.items.wands.TradeWand;
import me.apeiros.villagerutil.items.wands.TransportWand;
import me.apeiros.villagerutil.util.Utils;

@UtilityClass
public class Setup {

    // Skull texture for Villager Transport Charm
    public static final String VILLAGER = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGNhOGVmMjQ1OGEyYjEwMjYwYjg3NTY1NThmNzY3OWJjYjdlZjY5MWQ0MWY1MzRlZmVhMmJhNzUxMDczMTVjYyJ9fX0=";

    // Item constants
    public static final SlimefunItemStack ESSENCE = new SlimefunItemStack(
            "VU_ESSENCE", Material.GLOWSTONE_DUST, "&d村民精華",
            "&7稀有而神秘的塵埃,是一把鑰匙",
            "&7村民魔法的部分組成",
            "",
            "&e材料 &9&o(村民工具包)");

    public static final SlimefunItemStack TOKEN = new SlimefunItemStack(
            "VU_TOKEN", Material.EMERALD, "&b村民代幣",
            "&7需要的特殊物品",
            "&7施放村民魔法",
            "",
            "&a消耗品 &9&o(村民工具包)");

    public static final SlimefunItemStack TRANSPORT_CHARM = new SlimefunItemStack(
            "VU_TRANSPORT_CHARM", VILLAGER, "&a&l村民護身符",
            "&7會傳送的魔法符咒",
            "&7連結村民到你所在地",
            "&e右鍵點擊 &7傳送村民",
            "",
            "&7沒有村民連結",
            "",
            "&b工具 &9&o(村民工具包)");

    public static final SlimefunItemStack TRANSPORT_WAND = new SlimefunItemStack(
            "VU_TRANSPORT_WAND", Material.BLAZE_ROD, "&c村民運輸魔杖",
            "&e右鍵點擊 &7在村民身上",
            "&7獲得與此村民有關",
            "&7的護身符",
            "",
            "&b工具 &9&o(村民工具包)");

    public static final SlimefunItemStack TRADE_WAND = new SlimefunItemStack(
            "VU_TRADE_WAND", Material.BLAZE_ROD, "&6村民貿易魔杖",
            "&e右鍵點擊 &7在村民身上",
            "&7循環村民交易",
            "",
            "&b工具 &9&o(村民工具包)");

    public static final SlimefunItemStack CURE_WAND = new SlimefunItemStack(
            "VU_CURE_WAND", Material.BLAZE_ROD, "&a村民治愈魔杖",
            "&e右鍵點擊 &7在殭屍村民身上",
            "&7來治療它的痛病",
            "",
            "&b工具 &9&o(村民工具包)");

    public static final SlimefunItemStack NITWIT_WAND = new SlimefunItemStack(
            "VU_NITWIT_WAND", Material.BLAZE_ROD, "&5村民反智者",
            "&e右鍵點擊 &7在傻子村民身上",
            "&7讓他找到工作",
            "",
            "&b工具 &9&o(村民工具包)");

    // Setup methods
    public static void setup(VillagerUtil p) {
        // Setup category and researches
        ItemGroup ig = new ItemGroup(Utils.key("villager_util"), new CustomItemStack(Material.EMERALD_BLOCK, "&a村民工具包"));
        ig.register(p);

        // Setup /resetvillager command
        new ResetVillagerCommand(p);

        // Setup items
        new SlimefunItem(ig, ESSENCE, RecipeType.ANCIENT_ALTAR, new ItemStack[] {
            SlimefunItems.MAGIC_LUMP_2, new ItemStack(Material.GLASS_PANE), SlimefunItems.ENDER_LUMP_2,
            new ItemStack(Material.EMERALD), SlimefunItems.VILLAGER_RUNE, SlimefunItems.FILLED_FLASK_OF_KNOWLEDGE,
            SlimefunItems.ENDER_LUMP_2, new ItemStack(Material.GLASS_PANE), SlimefunItems.MAGIC_LUMP_2
        }, new SlimefunItemStack(ESSENCE, 16)).register(p);

        new SlimefunItem(ig, TOKEN, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
            ESSENCE, SlimefunItems.STRANGE_NETHER_GOO, new ItemStack(Material.EMERALD),
            null, null, null,
            null, null, null
        }, new SlimefunItemStack(TOKEN, 2)).register(p);

        // Setup Villager Charm
        new TransportCharm(ig).register(p);

        // Setup wands
        new CureWand(ig).register(p);
        new NitwitWand(ig).register(p);
        new TradeWand(ig).register(p);
        new TransportWand(ig).register(p);
    }
}
