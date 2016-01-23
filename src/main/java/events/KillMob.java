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
import org.spongepowered.api.event.entity.player.PlayerInteractBlockEvent;
import org.spongepowered.api.text.Texts;

public class KillMob {
    private final PluginCore core;

    public KillMob(PluginCore core) {
        this.core = core;
    }

    public void OnKillMob(PlayerInteractBlockEvent event) {
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
