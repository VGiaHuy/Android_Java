package com.nguyenthanhluan.models;

public class Food {
    // Tạo các thuộc tính của Food
    int maMonAn;
    String tenMonAn;
    String moTaMonAn;
    double giaMonAn;
    String danhMucMonAn;
    byte[] hinhAnhMonAn;

    public Food(int maMonAn, String tenMonAn, String moTaMonAn, double giaMonAn, String danhMucMonAn, byte[] hinhAnhMonAn) {
        this.maMonAn = maMonAn;
        this.tenMonAn = tenMonAn;
        this.moTaMonAn = moTaMonAn;
        this.giaMonAn = giaMonAn;
        this.danhMucMonAn = danhMucMonAn;
        this.hinhAnhMonAn = hinhAnhMonAn;
    }

    public int getMaMonAn() {
        return maMonAn;
    }

    public void setMaMonAn(int maMonAn) {
        this.maMonAn = maMonAn;
    }

    public String getTenMonAn() {
        return tenMonAn;
    }

    public void setTenMonAn(String tenMonAn) {
        this.tenMonAn = tenMonAn;
    }

    public String getMoTaMonAn() {
        return moTaMonAn;
    }

    public void setMoTaMonAn(String moTaMonAn) {
        this.moTaMonAn = moTaMonAn;
    }

    public double getGiaMonAn() {
        return giaMonAn;
    }

    public void setGiaMonAn(double giaMonAn) {
        this.giaMonAn = giaMonAn;
    }

    public String getDanhMucMonAn() {
        return danhMucMonAn;
    }

    public void setDanhMucMonAn(String danhMucMonAn) {
        this.danhMucMonAn = danhMucMonAn;
    }

    public byte[] getHinhAnhMonAn() {
        return hinhAnhMonAn;
    }

    public void setHinhAnhMonAn(byte[] hinhAnhMonAn) {
        this.hinhAnhMonAn = hinhAnhMonAn;
    }
}
