//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package events;

import achievements.Achievement;
import com.flowpowered.math.vector.Vector3i;
import core.PluginCore;
import java.util.Iterator;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.entity.player.PlayerMoveEvent;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.world.Location;

public class PlayerMove {
    private final PluginCore core;

    public PlayerMove(PluginCore core) {
        this.core = core;
    }

    @Subscribe
    public void OnPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Achievement achievement = null;
        Location pos = null;
        Iterator var5 = this.core.getPanoramas().iterator();

        while(var5.hasNext()) {
            Achievement achiev = (Achievement)var5.next();
            if(achiev.getWorld() == player.getWorld().getName()) {
                pos = player.getLocation();
                if(achiev.getPosition().equals(pos.getBlockPosition())) {
                    achievement = achiev;
                    break;
                }
            }
        }

        if(achievement != null) {
            event.getGame().getServer().broadcastMessage(Texts.of(player.getName() + " a obtenu le " + achievement.getType() + " " + achievement.getName() + " !"));
        }

    }

    private boolean isGreatPlayer(Vector3i pressurePlatePosition, Player player) {
        byte tolerance = 1;
        Location location = player.getLocation();
        Vector3i playerPosition = new Vector3i(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        Vector3i[] positions = new Vector3i[]{new Vector3i(0, 0, 0), new Vector3i(tolerance, 0, 0), new Vector3i(0, tolerance, 0), new Vector3i(0, 0, tolerance), new Vector3i(-tolerance, 0, 0), new Vector3i(0, -tolerance, 0), new Vector3i(0, 0, -tolerance)};
        Vector3i[] var7 = positions;
        int var8 = positions.length;

        for(int var9 = 0; var9 < var8; ++var9) {
            Vector3i position = var7[var9];
            if(pressurePlatePosition.equals(playerPosition.add(position))) {
                return true;
            }
        }

        return false;
    }
}
