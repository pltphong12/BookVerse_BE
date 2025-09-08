package com.example.bookverse.util;

import com.example.bookverse.exception.global.IdInvalidException;
import org.springframework.data.jpa.repository.JpaRepository;

public class FindObjectInDataBase {

    // Hàm generic để tìm theo ID
    public static <T, ID> T findByIdOrThrow(JpaRepository<T, ID> repository, ID id) throws IdInvalidException {
        return repository.findById(id)
                .orElseThrow(() -> new IdInvalidException(
                        "Id invalid: " + id
                ));
    }
}
