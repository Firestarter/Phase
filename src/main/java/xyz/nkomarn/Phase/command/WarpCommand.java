package xyz.nkomarn.Phase.command;

import com.firestartermc.kerosene.command.BrigadierExecutor;
import com.firestartermc.kerosene.util.AdvancementUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.Phase.gui.menu.MainMenu;
import xyz.nkomarn.Phase.util.Config;
import xyz.nkomarn.Phase.util.Search;
import xyz.nkomarn.Phase.util.WarpUtil;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WarpCommand implements BrigadierExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        if (args.length < 1) {
            new MainMenu().open(player);
            player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);
            AdvancementUtils.grant(player, "warp-menu");
            return true;
        }

        var warpName = WarpUtil.argsToString(args);

        CompletableFuture.supplyAsync(() -> Search.getWarpByName(warpName)).thenAccept(warp -> {
            if (warp.isEmpty()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                        "%sWarp '%s' doesn't exist.", Config.getPrefix(), warpName
                )));

                return;
            }

            warp.get().teleport(player);
        });

        return true;
    }

    @Override
    @NotNull
    public List<LiteralArgumentBuilder<?>> getCompletions() {
        var argument = RequiredArgumentBuilder.argument("name", StringArgumentType.greedyString()).suggests((context, suggestionsBuilder) -> {
            var remaining = suggestionsBuilder.getRemaining().toLowerCase();

            Search.getPublicWarps().stream()
                    .map(warp -> ChatColor.stripColor(warp.getName()))
                    .filter(name -> name.toLowerCase().contains(remaining)) // todo startsWith??
                    .forEach(suggestionsBuilder::suggest);

            return suggestionsBuilder.buildFuture();
        });

        return Arrays.asList(
                LiteralArgumentBuilder.literal("warp").then(argument),
                LiteralArgumentBuilder.literal("warps").then(argument)
        );
    }
}
