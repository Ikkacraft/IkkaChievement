package achievements;

import java.util.UUID;
import com.flowpowered.math.vector.Vector3i;

// La class LocatedAchievement offre les méthodes pour des achievements localisés dans le monde
public abstract class LocatedAchievement extends Achievement{
    // Monde et position de l'achievement
    private UUID world;
    private Vector3i position;

    // Constructeur
    public LocatedAchievement(String name, String description, UUID world, Vector3i position, String urlPicture, int badgeID) {
        super(name, description, urlPicture, badgeID);
        this.world = world;
        this.position = position;
    }

    // Enregistrer l'UUID du monde
    public UUID getWorld() {
        return this.world;
    }
    // Récupérer la position
    public Vector3i getPosition() {
        return this.position;
    }
}
