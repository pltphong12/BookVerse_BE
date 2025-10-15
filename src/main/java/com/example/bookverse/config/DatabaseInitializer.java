package com.example.bookverse.config;

import com.example.bookverse.domain.Permission;
import com.example.bookverse.domain.Role;
import com.example.bookverse.domain.User;
import com.example.bookverse.repository.PermissionRepository;
import com.example.bookverse.repository.RoleRepository;
import com.example.bookverse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Tạo permissions
        createPermissions();

        // Tạo roles
        createRoles();

        // Tạo admin user
        createAdminUser();
    }

    private void createPermissions() {
        if (permissionRepository.count() > 0) {
            System.out.println("Permissions already exist, skipping...");
            return;
        }

        List<Permission> permissions = new ArrayList<>();

        // Book permissions
        permissions.add(createPermission("BOOK_CREATE", "BOOK", "/api/v1/books", "POST"));
        permissions.add(createPermission("BOOK_UPDATE", "BOOK", "/api/v1/books", "PUT"));
        permissions.add(createPermission("BOOK_DELETE", "BOOK", "/api/v1/books/{id}", "DELETE"));
        permissions.add(createPermission("BOOK_VIEW_ALL", "BOOK", "/api/v1/books", "GET"));
        permissions.add(createPermission("BOOK_VIEW_BY_ID", "BOOK", "/api/v1/books/{id}", "GET"));
        permissions.add(
                createPermission("BOOK_VIEW_ALL_WITH_PAGINATION_AND_FILTER", "BOOK", "/api/v1/books/search", "GET"));

        // User permissions
        permissions.add(createPermission("USER_CREATE", "USER", "/api/v1/users", "POST"));
        permissions.add(createPermission("USER_UPDATE", "USER", "/api/v1/users", "PUT"));
        permissions.add(createPermission("USER_VIEW_ALL", "USER", "/api/v1/users", "GET"));
        permissions.add(createPermission("USER_VIEW_BY_ID", "USER", "/api/v1/users/{id}", "GET"));
        permissions.add(
                createPermission("USER_VIEW_ALL_WITH_PAGINATION_AND_FILTER", "USER", "/api/v1/users/search", "GET"));

        // Author permissions
        permissions.add(createPermission("AUTHOR_CREATE", "AUTHOR", "/api/v1/authors", "POST"));
        permissions.add(createPermission("AUTHOR_UPDATE", "AUTHOR", "/api/v1/authors", "PUT"));
        permissions.add(createPermission("AUTHOR_DELETE", "AUTHOR", "/api/v1/authors/{id}", "DELETE"));
        permissions.add(createPermission("AUTHOR_VIEW_ALL", "AUTHOR", "/api/v1/authors", "GET"));
        permissions.add(createPermission("AUTHOR_VIEW_BY_ID", "AUTHOR", "/api/v1/authors/{id}", "GET"));
        permissions.add(createPermission("AUTHOR_VIEW_ALL_WITH_PAGINATION_AND_FILTER", "AUTHOR",
                "/api/v1/authors/search", "GET"));

        // Publisher permissions
        permissions.add(createPermission("PUBLISHER_CREATE", "PUBLISHER", "/api/v1/publishers", "POST"));
        permissions.add(createPermission("PUBLISHER_UPDATE", "PUBLISHER", "/api/v1/publishers", "PUT"));
        permissions.add(createPermission("PUBLISHER_DELETE", "PUBLISHER", "/api/v1/publishers/{id}", "DELETE"));
        permissions.add(createPermission("PUBLISHER_VIEW_ALL", "PUBLISHER", "/api/v1/publishers", "GET"));
        permissions.add(createPermission("PUBLISHER_VIEW_BY_ID", "PUBLISHER", "/api/v1/publishers/{id}", "GET"));
        permissions.add(createPermission("PUBLISHER_VIEW_ALL_WITH_PAGINATION_AND_FILTER", "PUBLISHER",
                "/api/v1/publishers/search", "GET"));

        // Category permissions
        permissions.add(createPermission("CATEGORY_CREATE", "CATEGORY", "/api/v1/categories", "POST"));
        permissions.add(createPermission("CATEGORY_UPDATE", "CATEGORY", "/api/v1/categories", "PUT"));
        permissions.add(createPermission("CATEGORY_DELETE", "CATEGORY", "/api/v1/categories/{id}", "DELETE"));
        permissions.add(createPermission("CATEGORY_VIEW_ALL", "CATEGORY", "/api/v1/categories", "GET"));
        permissions.add(createPermission("CATEGORY_VIEW_BY_ID", "CATEGORY", "/api/v1/categories/{id}", "GET"));
        permissions.add(createPermission("CATEGORY_VIEW_ALL_WITH_PAGINATION_AND_FILTER", "CATEGORY",
                "/api/v1/categories/search", "GET"));

        // Order permissions
        permissions.add(createPermission("ORDER_CREATE", "ORDER", "/api/v1/orders", "POST"));
        permissions.add(createPermission("ORDER_UPDATE", "ORDER", "/api/v1/orders", "PUT"));
        permissions.add(createPermission("ORDER_DELETE", "ORDER", "/api/v1/orders/{id}", "DELETE"));
        permissions.add(createPermission("ORDER_VIEW_ALL", "ORDER", "/api/v1/orders", "GET"));
        permissions.add(createPermission("ORDER_VIEW_BY_ID", "ORDER", "/api/v1/orders/{id}", "GET"));
        permissions.add(createPermission("ORDER_DETAIL_CREATE", "ORDER", "/api/v1/order_details", "POST"));
        permissions.add(createPermission("ORDER_DETAIL_UPDATE", "ORDER", "/api/v1/order_details", "PUT"));
        permissions.add(createPermission("ORDER_DETAIL_DELETE", "ORDER", "/api/v1/order_details/{id}", "DELETE"));

        // Cart permissions
        permissions.add(createPermission("CART_CREATE", "CART", "/api/v1/carts", "POST"));
        permissions.add(createPermission("CART_UPDATE", "CART", "/api/v1/carts", "PUT"));
        permissions.add(createPermission("CART_VIEW_BY_ID", "CART", "/api/v1/carts/{id}", "GET"));
        permissions.add(createPermission("CART_DETAIL_CREATE", "CART", "/api/v1/cart_details", "POST"));
        permissions.add(createPermission("CART_DETAIL_UPDATE", "CART", "/api/v1/cart_details", "PUT"));
        permissions.add(createPermission("CART_DETAIL_DELETE", "CART", "/api/v1/cart_details/{id}", "DELETE"));

        // Role permissions
        permissions.add(createPermission("ROLE_CREATE", "ROLE", "/api/v1/roles", "POST"));
        permissions.add(createPermission("ROLE_UPDATE", "ROLE", "/api/v1/roles", "PUT"));
        permissions.add(createPermission("ROLE_DELETE", "ROLE", "/api/v1/roles/{id}", "DELETE"));
        permissions.add(createPermission("ROLE_VIEW_ALL", "ROLE", "/api/v1/roles", "GET"));
        permissions.add(createPermission("ROLE_VIEW_BY_ID", "ROLE", "/api/v1/roles/{id}", "GET"));
        permissions.add(
                createPermission("ROLE_VIEW_ALL_WITH_PAGINATION_AND_FILTER", "ROLE", "/api/v1/roles/search", "GET"));

        // Permission permissions
        permissions.add(createPermission("PERMISSION_CREATE", "PERMISSION", "/api/v1/permissions", "POST"));
        permissions.add(createPermission("PERMISSION_UPDATE", "PERMISSION", "/api/v1/permissions", "PUT"));
        permissions.add(createPermission("PERMISSION_DELETE", "PERMISSION", "/api/v1/permissions/{id}", "DELETE"));
        permissions.add(createPermission("PERMISSION_VIEW_ALL", "PERMISSION", "/api/v1/permissions", "GET"));
        permissions.add(createPermission("PERMISSION_VIEW_BY_ID", "PERMISSION", "/api/v1/permissions/{id}", "GET"));
        permissions.add(createPermission("PERMISSION_VIEW_ALL_WITH_PAGINATION_AND_FILTER", "PERMISSION",
                "/api/v1/permissions/search", "GET"));

        // File permissions
        permissions.add(createPermission("FILE_UPLOAD", "FILE", "/api/v1/files", "POST"));

        // Supplier permissions
        permissions.add(createPermission("SUPPLIER_CREATE", "SUPPLIER", "/api/v1/suppliers", "POST"));
        permissions.add(createPermission("SUPPLIER_UPDATE", "SUPPLIER", "/api/v1/suppliers", "PUT"));
        permissions.add(createPermission("SUPPLIER_DELETE", "SUPPLIER", "/api/v1/suppliers/{id}", "DELETE"));
        permissions.add(createPermission("SUPPLIER_VIEW_ALL", "SUPPLIER", "/api/v1/suppliers", "GET"));
        permissions.add(createPermission("SUPPLIER_VIEW_BY_ID", "SUPPLIER", "/api/v1/suppliers/{id}", "GET"));
        permissions.add(createPermission("SUPPLIER_VIEW_ALL_WITH_PAGINATION_AND_FILTER", "SUPPLIER",
                "/api/v1/suppliers/search", "GET"));

        permissionRepository.saveAll(permissions);
        System.out.println("Created " + permissions.size() + " permissions");
    }

    private Permission createPermission(String name, String domain, String apiPath, String method) {
        Permission permission = new Permission();
        permission.setName(name);
        permission.setDomain(domain);
        permission.setApiPath(apiPath);
        permission.setMethod(method);
        permission.setCreatedAt(Instant.now());
        permission.setCreatedBy("system");
        return permission;
    }

    private void createRoles() {
        if (roleRepository.count() > 0) {
            System.out.println("Roles already exist, skipping...");
            return;
        }

        // Tạo ADMIN role
        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        adminRole.setDescription("Quản trị viên với toàn quyền truy cập");
        adminRole.setCreatedAt(Instant.now());
        adminRole.setCreatedBy("system");

        // Gán tất cả permissions cho ADMIN
        List<Permission> allPermissions = permissionRepository.findAll();
        adminRole.setPermissions(allPermissions);

        roleRepository.save(adminRole);

        // Tạo USER role
        Role userRole = new Role();
        userRole.setName("CUSTOMER");
        userRole.setDescription("Người dùng thường với quyền truy cập giới hạn");
        userRole.setCreatedAt(Instant.now());
        userRole.setCreatedBy("system");

        // Gán permissions cơ bản cho USER
        List<Permission> userPermissions = new ArrayList<>();
        userPermissions.add(permissionRepository.findByName("BOOK_VIEW_ALL"));
        userPermissions.add(permissionRepository.findByName("BOOK_VIEW_BY_ID"));
        userPermissions.add(permissionRepository.findByName("AUTHOR_VIEW_ALL"));
        userPermissions.add(permissionRepository.findByName("AUTHOR_VIEW_BY_ID"));
        userPermissions.add(permissionRepository.findByName("PUBLISHER_VIEW_ALL"));
        userPermissions.add(permissionRepository.findByName("PUBLISHER_VIEW_BY_ID"));
        userPermissions.add(permissionRepository.findByName("CATEGORY_VIEW_ALL"));
        userPermissions.add(permissionRepository.findByName("CATEGORY_VIEW_BY_ID"));
        userPermissions.add(permissionRepository.findByName("CART_CREATE"));
        userPermissions.add(permissionRepository.findByName("CART_UPDATE"));
        userPermissions.add(permissionRepository.findByName("CART_VIEW_BY_ID"));
        userPermissions.add(permissionRepository.findByName("CART_DETAIL_CREATE"));
        userPermissions.add(permissionRepository.findByName("CART_DETAIL_UPDATE"));
        userPermissions.add(permissionRepository.findByName("CART_DETAIL_DELETE"));
        userPermissions.add(permissionRepository.findByName("ORDER_CREATE"));
        userPermissions.add(permissionRepository.findByName("ORDER_VIEW_BY_ID"));

        // Lọc null values
        userPermissions.removeIf(permission -> permission == null);
        userRole.setPermissions(userPermissions);

        roleRepository.save(userRole);

        // Tạo MANAGER role
        Role managerRole = new Role();
        managerRole.setName("MANAGER");
        managerRole.setDescription("Quản trị viên với quyền quản lý sách và tác giả");
        managerRole.setCreatedAt(Instant.now());
        managerRole.setCreatedBy("system");

        // Gán permissions cho MANAGER (book và author management)
        List<Permission> managerPermissions = new ArrayList<>();
        managerPermissions.add(permissionRepository.findByName("BOOK_CREATE"));
        managerPermissions.add(permissionRepository.findByName("BOOK_UPDATE"));
        managerPermissions.add(permissionRepository.findByName("BOOK_DELETE"));
        managerPermissions.add(permissionRepository.findByName("BOOK_VIEW_ALL"));
        managerPermissions.add(permissionRepository.findByName("BOOK_VIEW_BY_ID"));
        managerPermissions.add(permissionRepository.findByName("AUTHOR_CREATE"));
        managerPermissions.add(permissionRepository.findByName("AUTHOR_UPDATE"));
        managerPermissions.add(permissionRepository.findByName("AUTHOR_DELETE"));
        managerPermissions.add(permissionRepository.findByName("AUTHOR_VIEW_ALL"));
        managerPermissions.add(permissionRepository.findByName("AUTHOR_VIEW_BY_ID"));
        managerPermissions.add(permissionRepository.findByName("PUBLISHER_CREATE"));
        managerPermissions.add(permissionRepository.findByName("PUBLISHER_UPDATE"));
        managerPermissions.add(permissionRepository.findByName("PUBLISHER_DELETE"));
        managerPermissions.add(permissionRepository.findByName("PUBLISHER_VIEW_ALL"));
        managerPermissions.add(permissionRepository.findByName("PUBLISHER_VIEW_BY_ID"));
        managerPermissions.add(permissionRepository.findByName("CATEGORY_CREATE"));
        managerPermissions.add(permissionRepository.findByName("CATEGORY_UPDATE"));
        managerPermissions.add(permissionRepository.findByName("CATEGORY_DELETE"));
        managerPermissions.add(permissionRepository.findByName("CATEGORY_VIEW_ALL"));
        managerPermissions.add(permissionRepository.findByName("CATEGORY_VIEW_BY_ID"));
        managerPermissions.add(permissionRepository.findByName("ORDER_VIEW_ALL"));
        managerPermissions.add(permissionRepository.findByName("ORDER_VIEW_BY_ID"));

        // Lọc null values
        managerPermissions.removeIf(permission -> permission == null);
        managerRole.setPermissions(managerPermissions);

        roleRepository.save(managerRole);

        System.out.println("Created 3 roles: ADMIN, CUSTOMER, MANAGER");
    }

    private void createAdminUser() {
        if (userRepository.findByUsername("admin") != null) {
            System.out.println("Admin user already exists, skipping...");
            return;
        }

        User adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setPassword(passwordEncoder.encode("123456")); // Mật khẩu đã được hash
        adminUser.setEmail("phanlathanhphong19@gmail.com");
        adminUser.setFullName("Quản trị viên");
        adminUser.setAddress("Hồ Chí Minh");
        adminUser.setPhone("0767557431");
        adminUser.setAvatar("admin-avatar.jpg");
        adminUser.setCreatedAt(Instant.now());
        adminUser.setCreatedBy("system");

        // Gán ADMIN role
        Role adminRole = roleRepository.findByName("ADMIN");
        adminUser.setRole(adminRole);

        userRepository.save(adminUser);
        System.out.println("Created admin user with username: admin, password: 123456");
    }
}