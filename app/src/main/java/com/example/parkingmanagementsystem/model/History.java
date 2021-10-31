package com.example.parkingmanagementsystem.model;

public class History {
    private String rcd_id;
    private String area_id;
    private String area_name;
    private String slot_no;
    private String reg_no;
    private String st_time;
    private String et_time;
    private String reg_time;
    private String cl_time;
    private String sts;

    @Override
    public String toString() {
        return "History{" +
                "rcd_id='" + rcd_id + '\'' +
                ", area_id='" + area_id + '\'' +
                ", area_name='" + area_name + '\'' +
                ", slot_no='" + slot_no + '\'' +
                ", reg_no='" + reg_no + '\'' +
                ", st_time='" + st_time + '\'' +
                ", et_time='" + et_time + '\'' +
                ", reg_time='" + reg_time + '\'' +
                ", cl_time='" + cl_time + '\'' +
                ", sts='" + sts + '\'' +
                '}';
    }

    public String getRcd_id() {
        return rcd_id;
    }

    public void setRcd_id(String rcd_id) {
        this.rcd_id = rcd_id;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getSlot_no() {
        return slot_no;
    }

    public void setSlot_no(String slot_no) {
        this.slot_no = slot_no;
    }

    public String getReg_no() {
        return reg_no;
    }

    public void setReg_no(String reg_no) {
        this.reg_no = reg_no;
    }

    public String getSt_time() {
        return st_time;
    }

    public void setSt_time(String st_time) {
        this.st_time = st_time;
    }

    public String getEt_time() {
        return et_time;
    }

    public void setEt_time(String et_time) {
        this.et_time = et_time;
    }

    public String getReg_time() {
        return reg_time;
    }

    public void setReg_time(String reg_time) {
        this.reg_time = reg_time;
    }

    public String getCl_time() {
        return cl_time;
    }

    public void setCl_time(String cl_time) {
        this.cl_time = cl_time;
    }

    public String getSts() {
        return sts;
    }

    public void setSts(String sts) {
        this.sts = sts;
    }
}
