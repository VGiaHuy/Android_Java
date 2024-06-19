package com.example.de05.Model;

public class Sach{
    int maSach;
    String tenSach;
    double giaSach;

    public Sach(int maSach, String tenSach, double giaSach) {
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.giaSach = giaSach;
    }

    public int getMaSach() {
        return maSach;
    }

    public void setMaSach(int maSach) {
        this.maSach = maSach;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public double getGiaSach() {
        return giaSach;
    }

    public void setGiaSach(double giaSach) {
        this.giaSach = giaSach;
    }
}
