package com.assesment2.inventoryManagement.response;

public class ResponseMessageForUpdate {
    String message;

    public ResponseMessageForUpdate(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
