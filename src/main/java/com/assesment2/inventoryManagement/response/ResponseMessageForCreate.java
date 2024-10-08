package com.assesment2.inventoryManagement.response;

public class ResponseMessageForCreate {
    String message;
    Integer id;

    public ResponseMessageForCreate(String message, Integer id) {
        this.message = message;
        this.id = id;
    }

    public String getMessage() {
        return message;
    }


    public Integer getId() {
        return id;
    }


}
