package test_db_entities;

import java.util.Date;

public class Pet {
    private int id;
    private String name;
    private String owner;
    private String species;
    private char sex;
    private Date birth;
    private Date death;

    public Pet getPet() {
        return this;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public String getSpecies() {
        return species;
    }

    public Date getBirth() {
        return birth;
    }

    public Date getDeath() {
        return death;
    }

    public char getSex() {
        return sex;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public void setDeath(Date death) {
        this.death = death;
    }
}
