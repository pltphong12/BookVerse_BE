package com.example.bookverse.exception;

import com.example.bookverse.domain.response.RestResponse;
import com.example.bookverse.exception.global.ExistDataException;
import com.example.bookverse.exception.global.IdInvalidException;
import com.example.bookverse.exception.global.InvalidUsernameOrPassword;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalException {
    // Handle IdInvalid
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

    // Handle ExistDataException
    @ExceptionHandler(value = {
            ExistDataException.class
    })
    public ResponseEntity<RestResponse<Object>> handleExistDataException(Exception ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        res.setMessage(ex.getMessage());
        res.setError("Data already exist");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    // Handle Validation Exception when Login
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> validationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();

        RestResponse<Object> res = new RestResponse<>();
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getBody().getDetail());

        List<String> errors = fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        res.setMessage(errors.size() > 1 ? String.valueOf(errors) : errors.get(0));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    // Handle Invalid Username or Password
    @ExceptionHandler(value = {
            InvalidUsernameOrPassword.class
    })
    public ResponseEntity<RestResponse<Object>> handleInvalidUsernameOrPasswordException(Exception ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        res.setMessage(ex.getMessage());
        res.setError("Username or password invalid");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
}
