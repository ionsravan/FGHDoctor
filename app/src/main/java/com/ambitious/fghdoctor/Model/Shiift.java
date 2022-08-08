package com.ambitious.fghdoctor.Model;

public class Shiift {

    String doctor_shift_id,shift_name,doctor_id,from_time,to_time,no_of_token,time_per_token,fees;

    public Shiift() {
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

    public String getNo_of_token() {
        return no_of_token;
    }

    public void setNo_of_token(String no_of_token) {
        this.no_of_token = no_of_token;
    }

    public String getTime_per_token() {
        return time_per_token;
    }

    public void setTime_per_token(String time_per_token) {
        this.time_per_token = time_per_token;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }
}
