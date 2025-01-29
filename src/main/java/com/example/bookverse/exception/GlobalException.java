package com.example.bookverse.exception;

import com.example.bookverse.domain.response.RestResponse;
import com.example.bookverse.exception.book.ExistTitleException;
import com.example.bookverse.exception.category.ExistCategoryNameException;
import com.example.bookverse.exception.role.ExistRoleNameException;
import com.example.bookverse.exception.user.ExistUsernameException;
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

    // Handle User Exception
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

    // Handle Role Exception
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

    // Handle Category Exception
    @ExceptionHandler(value = {
            ExistCategoryNameException.class,
    })
    public ResponseEntity<RestResponse<Object>> handleExistCategoryNameException(Exception ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        res.setMessage(ex.getMessage());
        res.setError("CategoryName already exist");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    // Handle BookException
    @ExceptionHandler(value = {
            ExistTitleException.class,
    })
    public ResponseEntity<RestResponse<Object>> handleExistTitleException(Exception ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        res.setMessage(ex.getMessage());
        res.setError("Title already exist");
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
        res.setStatus(HttpStatus.UNAUTHORIZED.value());
        res.setMessage(ex.getMessage());
        res.setError("Username or password invalid");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
    }
}
