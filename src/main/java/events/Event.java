package events;

import core.PluginCore;
import achievements.Achievement;

// Class Event. Contient les arguments et m�thodes utilis�s par tous les types d'�v�nements
public abstract class Event {
    protected final PluginCore core;

    // Constructeur
    protected Event(PluginCore core) {
        this.core = core;
    }

    // Permet de valider un achievement
    protected void validAchievement(Achievement achievement, String cause) {
        core.broadcastText(getPlayerFromCause(cause) + " a obtenu le " + achievement.getType() + " " + achievement.getName());
    }

    // R�cup�re le nom du joueur gr�ce � la cause de l'�v�nement g�n�r�e par Sponge
    public String getPlayerFromCause(String cause) {
        int pos1 = cause.indexOf("['");
        int pos2 = cause.indexOf("'/");
        return cause.substring(pos1 + 2, pos2);
    }
}
