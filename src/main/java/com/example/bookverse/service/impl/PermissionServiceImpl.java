package com.example.bookverse.service.impl;

import com.example.bookverse.domain.Permission;
import com.example.bookverse.domain.response.ResPagination;
import com.example.bookverse.exception.global.ExistDataException;
import com.example.bookverse.exception.global.IdInvalidException;
import com.example.bookverse.repository.PermissionRepository;
import com.example.bookverse.service.PermissionService;
import com.example.bookverse.util.FindObjectInDataBase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {
    final private PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public Permission create(Permission permission) throws Exception {
        if (this.permissionRepository.existsByName(permission.getName())) {
            throw new ExistDataException( permission.getName() + "already exists");
        }
        return this.permissionRepository.save(permission);
    }

    @Override
    public Permission update(Permission permission) throws Exception {
        Permission permissionInDB = FindObjectInDataBase.findByIdOrThrow(permissionRepository, permission.getId());
        if (permission.getName() != null && !permission.getName().equals(permissionInDB.getName())) {
            permissionInDB.setName(permission.getName());
        }
        if (permission.getApiPath() != null && !permission.getApiPath().equals(permissionInDB.getApiPath())) {
            permissionInDB.setApiPath(permission.getApiPath());
        }
        if (permission.getMethod() != null && !permission.getMethod().equals(permissionInDB.getMethod())) {
            permissionInDB.setMethod(permission.getMethod());
        }
        return this.permissionRepository.save(permissionInDB);
    }

    @Override
    public Permission fetchPermissionById(long id) throws Exception {
        return FindObjectInDataBase.findByIdOrThrow(permissionRepository, id);
    }

    @Override
    public List<Permission> fetchAllPermissions() throws Exception {
        return this.permissionRepository.findAll();
    }

    @Override
    public ResPagination fetchAllPermissionWithPaginationAndFilter(String name, String method, LocalDate dateFrom, Pageable pageable) throws Exception {
        Page<Permission> permissionPage = this.permissionRepository.filter(name, method, dateFrom, pageable);
        ResPagination rs = new ResPagination();
        ResPagination.Meta mt = new ResPagination.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(permissionPage.getSize());

        mt.setPages(permissionPage.getTotalPages());
        mt.setTotal(permissionPage.getTotalElements());

        rs.setMeta(mt);

        List<Permission> permissions = permissionPage.getContent();
//        List<ResAuthorDTO> authorDTOS = new ArrayList<>();
//        for (Author author : authors) {
//            ResAuthorDTO authorDTO = ResAuthorDTO.from(author);
//            authorDTOS.add(authorDTO);
//        }

        rs.setResult(permissions);

        return rs;
    }

    @Override
    public void delete(long id) throws Exception {
        FindObjectInDataBase.findByIdOrThrow(permissionRepository, id);
        this.permissionRepository.deleteById(id);
    }
}
