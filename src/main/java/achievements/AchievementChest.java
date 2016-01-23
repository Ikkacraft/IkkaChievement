//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package achievements;

import achievements.Achievement;
import com.flowpowered.math.vector.Vector3i;

public class AchievementChest extends Achievement {
    private String world;

    public AchievementChest(String name, String description, Vector3i position, String world) {
        super(name, description, position);
        this.type = "Coffre perdu";
        this.world = world;
    }

    public AchievementChest(String name, Vector3i position, String world) {
        super(name, position);
        this.type = "Coffre perdu";
        this.world = world;
    }

    public String getWorld() {
        return this.world;
    }

    public void setWorld(String world) {
        if(world != null && world != "") {
            this.world = world;
        }

    }
}