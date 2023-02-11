package org.gullivigne.foursolaire;

public class PairedDevice {
    private int id;
    private String name;
    private String adress;

    public PairedDevice(int id, String name, String adress) {
        this.id = id;
        this.name = name;
        this.adress = adress;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAdress() {
        return adress;
    }
}
