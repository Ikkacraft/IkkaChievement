package achievements;

import java.util.UUID;
import com.flowpowered.math.vector.Vector3i;

// Achievement de type Panorama
public class AchievementPanorama extends LocatedAchievement {
    public AchievementPanorama(String name, String description, Vector3i position, UUID world) {
        super(name, description, world, position);
        this.type = "Panorama";
    }
}
