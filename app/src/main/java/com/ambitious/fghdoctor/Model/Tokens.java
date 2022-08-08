package com.ambitious.fghdoctor.Model;

public class Tokens {

    String doctor_shift_id, shift_name, doctor_id, token, service_charge, from_time, to_time, booked, booked_by_me;
    boolean is_sel = false, is_enable = false;

    public Tokens() {
    }

    public String getDoctor_shift_id() {
        return doctor_shift_id;
    }

    public void setDoctor_shift_id(String doctor_shift_id) {
        this.doctor_shift_id = doctor_shift_id;
    }

    public String getShift_name() {
        return shift_name;
    }

    public void setShift_name(String shift_name) {
        this.shift_name = shift_name;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getService_charge() {
        return service_charge;
    }

    public void setService_charge(String service_charge) {
        this.service_charge = service_charge;
    }

    public String getFrom_time() {
        return from_time;
    }

    public void setFrom_time(String from_time) {
        this.from_time = from_time;
    }

    public String getTo_time() {
        return to_time;
    }

    public void setTo_time(String to_time) {
        this.to_time = to_time;
    }

    public String getBooked() {
        return booked;
    }

    public void setBooked(String booked) {
        this.booked = booked;
    }

    public String getBooked_by_me() {
        return booked_by_me;
    }

    public void setBooked_by_me(String booked_by_me) {
        this.booked_by_me = booked_by_me;
    }

    public boolean isIs_sel() {
        return is_sel;
    }

    public void setIs_sel(boolean is_sel) {
        this.is_sel = is_sel;
    }

    public boolean isIs_enable() {
        return is_enable;
    }

    public void setIs_enable(boolean is_enable) {
        this.is_enable = is_enable;
    }
}
