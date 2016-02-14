package achievements;

// Achievement de type KillMob
public class AchievementKillMob extends Achievement {
    // id du mob à tuer et le nombre
    private String mobId;
    private int mobNumber;

    public AchievementKillMob(String name, String description, String mobId, int mobNumber, String urlPicture, int badgeID) {
        super(name, description, urlPicture, badgeID);
        this.type = 3;
        this.mobId = mobId;
        this.mobNumber = mobNumber;
    }

    // Récupérer l'id du mob
    public String getMobId() {
        return mobId;
    }
    // Récupérer le nombre de mobs à tuer
    public int getMobNumber() {return this.mobNumber; }
}
