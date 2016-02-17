package events;

import pluginCore.PluginCore;
import achievements.Achievement;
import ws.Badge;
import ws.WebService;

import java.io.IOException;

// Class Event. Contient les arguments et m�thodes utilis�s par tous les types d'�v�nements
public abstract class Event {
    protected final PluginCore core;

    // Constructeur
    protected Event(PluginCore core) {
        this.core = core;
    }

    // Permet de valider un achievement
    protected void validAchievement(Achievement achievement, String cause) throws IOException {
        String playerName = getPlayerFromCause(cause);
        String playerUUID = core.getGame().getServer().getPlayer(playerName).get().getUniqueId().toString();
        if (!playerHasAchievment(playerUUID, achievement.getName())) {
            WebService ws = new WebService(core);
            ws.validAchievement(playerUUID, achievement.getBadgeID());
        }
    }

    protected boolean playerHasAchievment(String playerUUID, String achievementName) throws IOException {
        WebService ws = new WebService(core);
        String userBadges = ws.getUnlockUserBadges(playerUUID);
        return userBadges.contains(achievementName);
    }

    // R�cup�re le nom du joueur gr�ce � la cause de l'�v�nement g�n�r�e par Sponge
    public String getPlayerFromCause(String cause) {
        int pos1 = cause.indexOf("['");
        int pos2 = cause.indexOf("'/");
        return cause.substring(pos1 + 2, pos2);
    }
}
