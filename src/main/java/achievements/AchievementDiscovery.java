package achievements;

// Achievement de type d�couverte
public class AchievementDiscovery extends Achievement {
    // nombre de pas � effectuer
    private int stepNumber;

    public AchievementDiscovery(String name, String description, int stepNumber, String urlPicture, int badgeID) {
        super(name, description, urlPicture, badgeID);
        this.type = 4;
        this.stepNumber = stepNumber;
    }

    // R�cup�rer le nombre de pas � faire
    public int getStepNumber() { return stepNumber; }
}
