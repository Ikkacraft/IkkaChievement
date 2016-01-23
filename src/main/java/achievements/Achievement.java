//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package achievements;

import com.flowpowered.math.vector.Vector3i;

public abstract class Achievement {
    protected String type;
    protected String name;
    protected String description;
    protected Vector3i position;

    public Achievement(String name, String description, Vector3i position) {
        this.name = name;
        this.position = position;
        this.description = description;
        this.type = "Aucun";
    }

    public Achievement(String name, Vector3i position) {
        this.name = name;
        this.position = position;
        this.description = "Aucune description";
        this.type = "Aucun";
    }

    public String getType() {
        return this.type;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        if(description != null && description != "") {
            this.description = description;
        }

    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if(name != null && name != "") {
            this.name = name;
        }

    }

    public Vector3i getPosition() {
        return this.position;
    }

    public void setPosition(Vector3i position) {
        if(position != null) {
            this.position = position;
        }

    }

    public String getWorld() {
        return null;
    }

    public void setWorld(String world) {
    }
}
