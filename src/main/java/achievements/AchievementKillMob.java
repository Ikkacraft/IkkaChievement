package achievements;

// Achievement de type KillMob
public class AchievementKillMob extends Achievement {
    // id du mob � tuer et le nombre
    private String mobId;
    private int mobNumber;

    public AchievementKillMob(String name, String description, String mobId, int mobNumber, String urlPicture, int badgeID) {
        super(name, description, urlPicture, badgeID);
        this.type = 3;
        this.mobId = mobId;
        this.mobNumber = mobNumber;
    }

    // R�cup�rer l'id du mob
    public String getMobId() {
        return mobId;
    }
    // R�cup�rer le nombre de mobs � tuer
    public int getMobNumber() {return this.mobNumber; }
}
