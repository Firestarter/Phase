package xyz.nkomarn.Phase.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.nkomarn.Phase.Phase;
import xyz.nkomarn.Phase.util.Config;
import xyz.nkomarn.Phase.util.Search;
import xyz.nkomarn.Phase.util.WarpUtil;

public class SignListener implements Listener {
    @EventHandler
    public void onSignPlace(SignChangeEvent event) {
        if (isWarpSign(event.getLines())) {
            if (Search.getWarpByName(event.getLine(2)).isPresent()) {
                event.setLine(1, ChatColor.translateAlternateColorCodes('&', "&3&l- Warp -"));
                event.setLine(2, ChatColor.WHITE + event.getLine(2));
            } else {
                event.getBlock().breakNaturally();
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                        "%s: A warp with that name doesn't exist.", Config.getPrefix())));
            }
        }
    }

    @EventHandler
    public void onSignInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null) {
            if (event.getClickedBlock().getType().toString().contains("SIGN")) {
                Sign sign = (Sign) event.getClickedBlock().getState();
                if (isWarpSign(sign.getLines())) {
                    Search.getWarpByName(ChatColor.stripColor(sign.getLine(2))).ifPresent(warp ->
                            WarpUtil.warpPlayer(event.getPlayer(), warp));
                }
            }
        }
    }

    private boolean isWarpSign(String[] lines) {
        return lines[1] != null && ChatColor.stripColor(lines[1]).equalsIgnoreCase("- Warp -");
    }
}
