package com.example.bookverse.exception;

import com.example.bookverse.domain.response.RestResponse;
import com.example.bookverse.exception.role.ExistRoleNameException;
import com.example.bookverse.exception.user.ExistUsernameException;
import com.example.bookverse.exception.global.IdInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = {
            IdInvalidException.class,
    })
    public ResponseEntity<RestResponse<Object>> handleIdInvalidException(Exception ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        res.setMessage(ex.getMessage());
        res.setError("ID invalid");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    // Handle User
    @ExceptionHandler(value = {
            ExistUsernameException.class,
    })
    public ResponseEntity<RestResponse<Object>> handleExistUsernameException(Exception ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        res.setMessage(ex.getMessage());
        res.setError("Username already exist");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    // Handle Role
    @ExceptionHandler(value = {
            ExistRoleNameException.class,
    })
    public ResponseEntity<RestResponse<Object>> handleExistRoleNameException(Exception ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        res.setMessage(ex.getMessage());
        res.setError("RoleName already exist");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
}
