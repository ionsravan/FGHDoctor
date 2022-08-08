package com.ambitious.fghdoctor.Model;

public class HistoryModel {
    String wallet_txn_id,user_id,amount,nature,remark,datetime,entrydt;

    public String getWallet_txn_id() {
        return wallet_txn_id;
    }

    public void setWallet_txn_id(String wallet_txn_id) {
        this.wallet_txn_id = wallet_txn_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getEntrydt() {
        return entrydt;
    }

    public void setEntrydt(String entrydt) {
        this.entrydt = entrydt;
    }
}
