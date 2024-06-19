package com.example.model;

public class Sanphammodel {
    int masp;
    String tensp;
    double giasp;
    byte[] anhsp;


    public Sanphammodel(int masp, String tensp, double giasp, byte[] anhsp) {
        this.masp = masp;
        this.tensp = tensp;
        this.giasp = giasp;
        this.anhsp = anhsp;
    }

    public int getMasp() {
        return masp;
    }

    public void setMasp(int masp) {
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

    public byte[] getAnhsp() {
        return anhsp;
    }

    public void setAnhsp(byte[] anhsp) {
        this.anhsp = anhsp;
    }
}
