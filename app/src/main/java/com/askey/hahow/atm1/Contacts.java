package com.askey.hahow.atm1;

import java.util.ArrayList;
import java.util.List;

public class Contacts {
    String name;
    int id;
    List<String> phone = new ArrayList<String>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getPhone() {
        return phone;
    }

    public void setPhone(List<String> phone) {
        this.phone = phone;
    }

    public Contacts(int id, String name) {
        this.name = name;
        this.id = id;
        this.phone = phone;
    }


}
