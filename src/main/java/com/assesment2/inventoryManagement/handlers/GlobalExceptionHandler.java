package com.assesment2.inventoryManagement.handlers;

import com.assesment2.inventoryManagement.response.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.NoSuchElementException;

@ControllerAdvice
public class  GlobalExceptionHandler {
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ResponseMessage> handleNoSuchElementException(NoSuchElementException ex) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage(ex.getMessage());
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseMessage> handleIllegalArgumentException(IllegalArgumentException ex) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage(ex.getMessage());
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseMessage> handleGenericException(Exception ex) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage("An unexpected error occurred: " + ex.getMessage());
        return new ResponseEntity<>(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseMessage> handleArgumentMismatchExceptions(MethodArgumentTypeMismatchException ex) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage("Input cannot be empty");
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }
}