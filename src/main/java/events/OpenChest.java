//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package events;

import achievements.Achievement;
import core.PluginCore;
import java.util.Iterator;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.entity.player.PlayerInteractBlockEvent;
import org.spongepowered.api.text.Texts;

public class OpenChest {
    private final PluginCore core;

    public OpenChest(PluginCore core) {
        this.core = core;
    }

    @Subscribe
    public void OnPlayerMove(PlayerInteractBlockEvent event) {
        BlockType block = event.getBlock().getType();
        if(block == BlockTypes.CHEST) {
            Achievement achievement = null;
            Iterator var4 = this.core.getChests().iterator();

            while(var4.hasNext()) {
                Achievement achiev = (Achievement)var4.next();
                if(achiev.getPosition().equals(event.getBlock().getBlockPosition())) {
                    achievement = achiev;
                    break;
                }
            }

            if(achievement != null) {
                event.getGame().getServer().broadcastMessage(Texts.of(event.getPlayer().getName() + " a obtenu le " + achievement.getType() + " " + achievement.getName() + " !"));
            }
        }

    }
}
