package com.example.bookverse.config;

import com.example.bookverse.domain.*;
import com.example.bookverse.dto.enums.CustomerLevel;
import com.example.bookverse.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {

    private static final Set<String> STAFF_EXCLUDED_DOMAINS = Set.of("ROLE", "PERMISSION");

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        createPermissions();
        createRoles();
        migrateManagerRoleToStaff();
        createUsers();
    }

    // ======================== PERMISSIONS ========================

    private void createPermissions() {
        List<Permission> permissions = new ArrayList<>();

        // Author
        permissions.add(perm("AUTHOR_CREATE", "AUTHOR", "/api/v1/authors", "POST"));
        permissions.add(perm("AUTHOR_UPDATE", "AUTHOR", "/api/v1/authors", "PUT"));
        permissions.add(perm("AUTHOR_DELETE", "AUTHOR", "/api/v1/authors/{id}", "DELETE"));
        permissions.add(perm("AUTHOR_VIEW_ALL", "AUTHOR", "/api/v1/authors", "GET"));
        permissions.add(perm("AUTHOR_VIEW_BY_ID", "AUTHOR", "/api/v1/authors/{id}", "GET"));
        permissions.add(perm("AUTHOR_VIEW_ALL_WITH_PAGINATION_AND_FILTER", "AUTHOR", "/api/v1/authors/search", "GET"));

        // Book
        permissions.add(perm("BOOK_CREATE", "BOOK", "/api/v1/books", "POST"));
        permissions.add(perm("BOOK_UPDATE", "BOOK", "/api/v1/books", "PUT"));
        permissions.add(perm("BOOK_DELETE", "BOOK", "/api/v1/books/{id}", "DELETE"));
        permissions.add(perm("BOOK_VIEW_ALL", "BOOK", "/api/v1/books", "GET"));
        permissions.add(perm("BOOK_VIEW_ALL_WITH_PAGINATION_AND_FILTER", "BOOK", "/api/v1/books/search", "GET"));

        // Cart
        permissions.add(perm("CART_ADD_TO_CART", "CART", "/api/v1/carts/items", "POST"));
        permissions.add(perm("CART_VIEW_BY_ID", "CART", "/api/v1/carts", "GET"));

        // Category
        permissions.add(perm("CATEGORY_CREATE", "CATEGORY", "/api/v1/categories", "POST"));
        permissions.add(perm("CATEGORY_UPDATE", "CATEGORY", "/api/v1/categories", "PUT"));
        permissions.add(perm("CATEGORY_DELETE", "CATEGORY", "/api/v1/categories/{id}", "DELETE"));
        permissions.add(perm("CATEGORY_VIEW_BY_ID", "CATEGORY", "/api/v1/categories/{id}", "GET"));
        permissions.add(perm("CATEGORY_VIEW_ALL_WITH_PAGINATION_AND_FILTER", "CATEGORY", "/api/v1/categories/search", "GET"));

        // Customer
        permissions.add(perm("CUSTOMER_CREATE", "CUSTOMER", "/api/v1/customers", "POST"));
        permissions.add(perm("CUSTOMER_UPDATE", "CUSTOMER", "/api/v1/customers", "PUT"));
        permissions.add(perm("CUSTOMER_DELETE", "CUSTOMER", "/api/v1/customers/{id}", "DELETE"));
        permissions.add(perm("CUSTOMER_VIEW_BY_ID", "CUSTOMER", "/api/v1/customers/{id}", "GET"));
        permissions.add(perm("CUSTOMER_VIEW_ALL_WITH_PAGINATION_AND_FILTER", "CUSTOMER", "/api/v1/customers/search", "GET"));

        // Customer address
        permissions.add(perm("ADDRESS_CREATE", "CUSTOMER_ADDRESS", "/api/v1/addresses", "POST"));
        permissions.add(perm("ADDRESS_UPDATE", "CUSTOMER_ADDRESS", "/api/v1/addresses", "PUT"));
        permissions.add(perm("ADDRESS_VIEW_MINE", "CUSTOMER_ADDRESS", "/api/v1/addresses/me", "GET"));
        permissions.add(perm("ADDRESS_DELETE", "CUSTOMER_ADDRESS", "/api/v1/addresses/{id}", "DELETE"));

        // File
        permissions.add(perm("FILE_UPLOAD", "FILE", "/api/v1/files/**", "POST"));

        // Order
        permissions.add(perm("ORDER_CREATE", "ORDER", "/api/v1/orders", "POST"));
        permissions.add(perm("ORDER_UPDATE", "ORDER", "/api/v1/orders", "PUT"));
        permissions.add(perm("ORDER_CANCEL", "ORDER", "/api/v1/orders/{id}", "DELETE"));
        permissions.add(perm("ORDER_VIEW_ALL_WITH_PAGINATION_AND_FILTER", "ORDER", "/api/v1/orders/search", "GET"));
        permissions.add(perm("ORDER_VIEW_BY_ID", "ORDER", "/api/v1/orders/{id}", "GET"));
        permissions.add(perm("ORDER_VIEW_MINE", "ORDER", "/api/v1/orders/me", "GET"));

        // Permission
        permissions.add(perm("PERMISSION_CREATE", "PERMISSION", "/api/v1/permissions", "POST"));
        permissions.add(perm("PERMISSION_UPDATE", "PERMISSION", "/api/v1/permissions", "PUT"));
        permissions.add(perm("PERMISSION_DELETE", "PERMISSION", "/api/v1/permissions/{id}", "DELETE"));
        permissions.add(perm("PERMISSION_VIEW_ALL", "PERMISSION", "/api/v1/permissions", "GET"));
        permissions.add(perm("PERMISSION_VIEW_BY_ID", "PERMISSION", "/api/v1/permissions/{id}", "GET"));
        permissions.add(perm("PERMISSION_VIEW_ALL_WITH_PAGINATION_AND_FILTER", "PERMISSION", "/api/v1/permissions/search", "GET"));

        // Publisher
        permissions.add(perm("PUBLISHER_CREATE", "PUBLISHER", "/api/v1/publishers", "POST"));
        permissions.add(perm("PUBLISHER_UPDATE", "PUBLISHER", "/api/v1/publishers", "PUT"));
        permissions.add(perm("PUBLISHER_DELETE", "PUBLISHER", "/api/v1/publishers/{id}", "DELETE"));
        permissions.add(perm("PUBLISHER_VIEW_BY_ID", "PUBLISHER", "/api/v1/publishers/{id}", "GET"));
        permissions.add(perm("PUBLISHER_VIEW_ALL_WITH_PAGINATION_AND_FILTER", "PUBLISHER", "/api/v1/publishers/search", "GET"));

        // Role
        permissions.add(perm("ROLE_CREATE", "ROLE", "/api/v1/roles", "POST"));
        permissions.add(perm("ROLE_UPDATE", "ROLE", "/api/v1/roles", "PUT"));
        permissions.add(perm("ROLE_DELETE", "ROLE", "/api/v1/roles/{id}", "DELETE"));
        permissions.add(perm("ROLE_VIEW_ALL", "ROLE", "/api/v1/roles", "GET"));
        permissions.add(perm("ROLE_VIEW_BY_ID", "ROLE", "/api/v1/roles/{id}", "GET"));
        permissions.add(perm("ROLE_VIEW_ALL_WITH_PAGINATION_AND_FILTER", "ROLE", "/api/v1/roles/search", "GET"));

        // Supplier
        permissions.add(perm("SUPPLIER_CREATE", "SUPPLIER", "/api/v1/suppliers", "POST"));
        permissions.add(perm("SUPPLIER_UPDATE", "SUPPLIER", "/api/v1/suppliers", "PUT"));
        permissions.add(perm("SUPPLIER_DELETE", "SUPPLIER", "/api/v1/suppliers/{id}", "DELETE"));
        permissions.add(perm("SUPPLIER_VIEW_BY_ID", "SUPPLIER", "/api/v1/suppliers/{id}", "GET"));
        permissions.add(perm("SUPPLIER_VIEW_ALL_WITH_PAGINATION_AND_FILTER", "SUPPLIER", "/api/v1/suppliers/search", "GET"));

        // User
        permissions.add(perm("USER_CREATE", "USER", "/api/v1/users", "POST"));
        permissions.add(perm("USER_UPDATE", "USER", "/api/v1/users", "PUT"));
        permissions.add(perm("USER_DELETE", "USER", "/api/v1/users/{id}", "DELETE"));
        permissions.add(perm("USER_VIEW_ALL", "USER", "/api/v1/users", "GET"));
        permissions.add(perm("USER_VIEW_BY_ID", "USER", "/api/v1/users/{id}", "GET"));
        permissions.add(perm("USER_VIEW_ALL_WITH_PAGINATION_AND_FILTER", "USER", "/api/v1/users/search", "GET"));

        // Dashboard
        permissions.add(perm("DASHBOARD_VIEW", "DASHBOARD", "/api/v1/dashboard/overview", "GET"));

        permissionRepository.saveAll(permissions);
        System.out.println(">>> Synchronized " + permissions.size() + " permissions");
    }

    private Permission perm(String name, String domain, String apiPath, String method) {
        Permission p = permissionRepository.findByName(name);
        if (p == null) {
            p = new Permission();
            p.setName(name);
        }
        p.setDomain(domain);
        p.setApiPath(apiPath);
        p.setMethod(method);
        return p;
    }

    // ======================== ROLES ========================

    private void createRoles() {
        List<Permission> all = permissionRepository.findAll();

        // ADMIN — toàn quyền
        Role admin = getOrCreateRole("ADMIN", "Quản trị viên hệ thống — toàn quyền");
        admin.setPermissions(new ArrayList<>(all));
        roleRepository.save(admin);

        // STAFF — toàn quyền trên các endpoint nghiệp vụ, ngoại trừ role và permission
        Role staff = getOrCreateRole("STAFF",
                "Nhân viên — quản lý toàn bộ dữ liệu, ngoại trừ role và permission");
        staff.setPermissions(all.stream()
                .filter(permission -> !STAFF_EXCLUDED_DOMAINS.contains(permission.getDomain()))
                .toList());
        roleRepository.save(staff);

        // CUSTOMER — catalog là public; các quyền dưới đây chỉ cho dữ liệu thuộc khách hàng hiện tại
        Role customer = getOrCreateRole("CUSTOMER",
                "Khách hàng — quản lý giỏ hàng, địa chỉ và đơn hàng của chính mình");
        customer.setPermissions(findPermissions(
                "CART_ADD_TO_CART", "CART_VIEW_BY_ID",
                "ADDRESS_CREATE", "ADDRESS_UPDATE", "ADDRESS_VIEW_MINE", "ADDRESS_DELETE",
                "ORDER_CREATE", "ORDER_VIEW_BY_ID", "ORDER_VIEW_MINE", "ORDER_UPDATE", "ORDER_CANCEL"
        ));
        roleRepository.save(customer);

        System.out.println(">>> Synchronized 3 roles: ADMIN, STAFF, CUSTOMER");
    }

    private Role getOrCreateRole(String name, String description) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role();
            role.setName(name);
        }
        role.setDescription(description);
        return role;
    }

    private List<Permission> findPermissions(String... names) {
        List<Permission> result = new ArrayList<>();
        for (String name : names) {
            Permission p = permissionRepository.findByName(name);
            if (p != null) result.add(p);
        }
        return result;
    }

    private void migrateManagerRoleToStaff() {
        Role manager = roleRepository.findByName("MANAGER");
        if (manager == null) {
            return;
        }
        Role staff = roleRepository.findByName("STAFF");
        List<User> managerUsers = userRepository.findAll().stream()
                .filter(user -> user.getRole() != null && user.getRole().getId() == manager.getId())
                .toList();
        managerUsers.forEach(user -> user.setRole(staff));
        userRepository.saveAll(managerUsers);
        roleRepository.delete(manager);
        System.out.println(">>> Migrated MANAGER users to STAFF and removed the MANAGER role");
    }

    // ======================== USERS ========================

    private void createUsers() {
        if (userRepository.count() > 0) return;

        Role adminRole = roleRepository.findByName("ADMIN");
        Role staffRole = roleRepository.findByName("STAFF");
        Role customerRole = roleRepository.findByName("CUSTOMER");

        // 1 Admin
        createUser("admin@bookverse.com", "Nguyễn Văn Admin", "0900000001", adminRole);

        // 5 Staff
        createUser("staff1@bookverse.com", "Phạm Thị Nhân Viên", "0900000004", staffRole);
        createUser("staff2@bookverse.com", "Hoàng Văn Nhân Viên", "0900000005", staffRole);
        createUser("staff3@bookverse.com", "Ngô Thị Nhân Viên", "0900000006", staffRole);
        createUser("staff4@bookverse.com", "Đỗ Văn Nhân Viên", "0900000007", staffRole);
        createUser("staff5@bookverse.com", "Vũ Thị Nhân Viên", "0900000008", staffRole);

        // 10 Customers — mỗi customer được liên kết với bảng customers + tạo cart
        createCustomerUser("customer1@bookverse.com", "Bùi Văn Khách", "0900000009", customerRole, "079200001001");
        createCustomerUser("customer2@bookverse.com", "Đặng Thị Khách", "0900000010", customerRole, "079200001002");
        createCustomerUser("customer3@bookverse.com", "Lý Văn Khách", "0900000011", customerRole, "079200001003");
        createCustomerUser("customer4@bookverse.com", "Mai Thị Khách", "0900000012", customerRole, "079200001004");
        createCustomerUser("customer5@bookverse.com", "Tô Văn Khách", "0900000013", customerRole, "079200001005");
        createCustomerUser("customer6@bookverse.com", "Phan Thị Khách", "0900000014", customerRole, "079200001006");
        createCustomerUser("customer7@bookverse.com", "Trịnh Văn Khách", "0900000015", customerRole, "079200001007");
        createCustomerUser("customer8@bookverse.com", "Cao Thị Khách", "0900000016", customerRole, "079200001008");
        createCustomerUser("customer9@bookverse.com", "Hồ Văn Khách", "0900000017", customerRole, "079200001009");
        createCustomerUser("customer10@bookverse.com", "Dương Thị Khách", "0900000018", customerRole, "079200001010");

        System.out.println(">>> Created 16 users: 1 admin, 5 staff, 10 customers");
    }

    private User createUser(String email, String fullName, String phone, Role role) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("123456"));
        user.setFullName(fullName);
        user.setPhone(phone);
        user.setRole(role);
        user.setCreatedAt(Instant.now());
        user.setCreatedBy("system");
        return userRepository.save(user);
    }

    private void createCustomerUser(String email, String fullName, String phone,
                                    Role role, String identityCard) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("123456"));
        user.setFullName(fullName);
        user.setPhone(phone);
        user.setRole(role);
        user.setCreatedAt(Instant.now());
        user.setCreatedBy("system");

        Customer customer = new Customer();
        customer.setUser(user);
        customer.setIdentityCard(identityCard);
        customer.setTotalOrder(0L);
        customer.setTotalSpending(BigDecimal.ZERO);
        customer.setCustomerLevel(CustomerLevel.BRONZE);
        customer.setCreatedAt(Instant.now());
        customer.setCreatedBy("system");
        Customer savedCustomer = customerRepository.save(customer);

        Cart cart = new Cart();
        cart.setCustomer(savedCustomer);
        cart.setSum(0);
        cart.setCreatedAt(Instant.now());
        cart.setCreatedBy("system");
        cartRepository.save(cart);
    }
}
