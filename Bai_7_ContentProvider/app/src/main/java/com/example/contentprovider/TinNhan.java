package com.example.contentprovider;

public class TinNhan {
    Long id;
    String message;
    String address;

    public TinNhan(Long id, String message, String address) {
        this.id = id;
        this.message = message;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public TinNhan() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
