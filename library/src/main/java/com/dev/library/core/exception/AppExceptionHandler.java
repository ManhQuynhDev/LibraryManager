package com.dev.library.core.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.dev.library.core.AppError;
import com.dev.library.core.AppError.ErrorCode;
import com.dev.library.core.ResponseObject;

import jakarta.servlet.http.HttpServletRequest;

@SuppressWarnings({ "rawtypes", "unchecked" })
@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = { UserAccountNotFoundException.class, UserAccountExitsException.class})
    public ResponseEntity<ResponseObject> handleCustomExceptions(RuntimeException ex, HttpServletRequest request) {
        ResponseObject response = new ResponseObject();
        response.setMessage("Data is invalid.");
        response.setStatus(false);

        AppError.ErrorCode errorCode;

        switch (ex.getClass().getSimpleName()) {
            case "UserAccountExistingException":
                errorCode = AppError.ErrorCode.ACCOUNT_EXIST;
                break;
            case "UserAccountNotFoundException":
                errorCode = AppError.ErrorCode.ACCOUNT_NOT_FOUND;
                break;
            default:
                errorCode = AppError.ErrorCode.UNKNOWN_ERROR;
                break;
        }

        response.setError(new AppError(errorCode, ex.getMessage()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> invalid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<AppError> errors = new ArrayList<>();
        ex.getAllErrors().forEach(err -> {
            AppError error = new AppError(AppError.ErrorCode.DATA_INVALID,
                    err.getDefaultMessage());
            errors.add(error);
        });
        ResponseObject response = new ResponseObject();
        response.setMessage("Data is invalid.");
        response.setStatus(false);
        response.setErrors(errors);
        return new ResponseEntity<ResponseObject>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {

        AppError error = new AppError(AppError.ErrorCode.METHOD_NOT_ALLOWED,
                ex.getMessage());
        ResponseObject<Boolean> responseObject = new ResponseObject<>();
        responseObject.setStatus(false);
        responseObject.setError(error);
        responseObject.setMessage("Method not supported");

        return new ResponseEntity<>(responseObject, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(value = { UnknownException.class })
    public ResponseEntity<?> unknown(Exception ex, HttpServletRequest request) {
        ResponseObject response = new ResponseObject();
        response.setStatus(false);
        response.setMessage("Something went wrong!.");
        response.setError(new AppError(ErrorCode.UNKNOWN_ERROR, ex.getMessage()));
        return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = { BadRequestException.class })
    public ResponseEntity<?> badRequest(Exception ex, HttpServletRequest request) {
        ResponseObject response = new ResponseObject();
        response.setStatus(false);
        response.setError(new AppError(ErrorCode.UNKNOWN_ERROR, ex.getMessage()));
        return new ResponseEntity<ResponseObject>(response, HttpStatus.BAD_REQUEST);
    }
}
