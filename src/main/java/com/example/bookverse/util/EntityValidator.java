package com.example.bookverse.util;

import com.example.bookverse.exception.global.IdInvalidException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EntityValidator {
    // Phương thức kiểm tra danh sách ID có tồn tại không
    public static <T, ID> void validateIdsExist(List<ID> ids, JpaRepository<T, ID> repository, String entityName) throws IdInvalidException {
        if (ids == null || ids.isEmpty()) {
            return; // Không cần kiểm tra nếu danh sách rỗng
        }

        // Lấy danh sách ID thực tế có trong database
        Set<ID> existingIds = (Set<ID>) repository.findAllById(ids).stream()
                .map(entity -> getId(entity))
                .collect(Collectors.toSet());

        // Lọc ra các ID không tồn tại
        List<ID> notFoundIds = ids.stream()
                .filter(id -> !existingIds.contains(id))
                .collect(Collectors.toList());

        // Nếu có ID không tồn tại, ném Exception
        if (!notFoundIds.isEmpty()) {
            throw new IdInvalidException("Các " + entityName + " ID sau không tồn tại: " + notFoundIds);
        }
    }

    // Hàm lấy ID từ entity (cần bổ sung nếu entity không có phương thức getId())
    private static <T, ID> ID getId(T entity) {
        try {
            return (ID) entity.getClass().getMethod("getId").invoke(entity);
        } catch (Exception e) {
            throw new RuntimeException("Không thể lấy ID từ entity: " + entity.getClass().getSimpleName());
        }
    }
}
