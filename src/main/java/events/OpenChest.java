package events;

import pluginCore.PluginCore;
import java.util.Iterator;
import achievements.LocatedAchievement;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.block.InteractBlockEvent;

// Class OpenChest r�agit lorsqu'un joueur effectue un clic droit sur un bloc
public class OpenChest extends Event implements EventListener<InteractBlockEvent.Secondary> {

    // Constructeur de la class OpenChest
    // Permet de r�cup�rer PluginCore
    public OpenChest(PluginCore core) {
        super(core);
    }

    // R�cup�ration de l'�v�nement lorsqu'un joueur effectue un clic droit sur un bloc
    public void handle(InteractBlockEvent.Secondary event) throws Exception {
        // On r�cup�re le type du bloc
        BlockSnapshot blockSnap = event.getTargetBlock();
        BlockType block = blockSnap.getState().getType();
        // Si le bloc est un coffre
        if(block == BlockTypes.CHEST) {
            // Achievement de type coffre cach�
            Iterator iterator = this.core.getChests().iterator();
            // On parcoure tous les �v�nements de type coffre cach� et l'on v�rifie si le joueur est dans le bon monde
            // et � la bonne position. Si oui, on valide l'achievement.
            while(iterator.hasNext()) {
                LocatedAchievement achiev = (LocatedAchievement)iterator.next();
                if (achiev.getWorld().equals(blockSnap.getWorldUniqueId())) {
                    if(achiev.getPosition().equals(blockSnap.getPosition())) {
                        validAchievement(achiev, event.getCause().toString());
                        break;
                    }
                }
            }
        }

    }
}
