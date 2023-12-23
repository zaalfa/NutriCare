package com.example.FinalProject;

public class Makanan {
    private String hari;
    private String nama;
    private int kalori;
    private int berat;

    public Makanan() {

    }

    public Makanan(String nama, int kalori, int berat) {
        this.hari = hari;
        this.nama = nama;
        this.kalori = kalori;
        this.berat = berat;
    }

    public String getHari() {

        return hari;
    }

    public void getHari(String hari) {

        this.hari = hari;
    }
    public String getNama() {

        return nama;
    }

    public void setNama(String nama) {

        this.nama = nama;
    }

    public int getKalori() {

        return kalori;
    }

    public void setKalori(int kalori) {

        this.kalori = kalori;
    }

    public int getBerat() {

        return berat;
    }

    public void setBerat(int berat) {

        this.berat = berat;
    }
}
