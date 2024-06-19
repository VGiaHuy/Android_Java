package com.example.model;

public class Sach {

    String masp;
    String tensp;
    double giasp;

    public Sach(String masp, String tensp, double giasp) {
        this.masp = masp;
        this.tensp = tensp;
        this.giasp = giasp;
    }

    public String getMasp() {
        return masp;
    }

    public void setMasp(String masp) {
        this.masp = masp;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public double getGiasp() {
        return giasp;
    }

    public void setGiasp(double giasp) {
        this.giasp = giasp;
    }
}
