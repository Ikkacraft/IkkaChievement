package achievements;

import java.util.UUID;
import com.flowpowered.math.vector.Vector3i;

// Achievement de type coffre caché
public class AchievementChest extends LocatedAchievement {

    public AchievementChest(String name, String description, Vector3i position, UUID world, String urlPicture, int badgeID) {
        super(name, description, world, position, urlPicture, badgeID);
        this.type = 1;
    }
}
