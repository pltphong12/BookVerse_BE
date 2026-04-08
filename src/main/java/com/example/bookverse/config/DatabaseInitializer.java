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

@Component
@AllArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {

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
        createUsers();
    }

    // ======================== PERMISSIONS ========================

    private void createPermissions() {
        if (permissionRepository.count() > 0) return;

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

        // File
        permissions.add(perm("FILE_UPLOAD", "FILE", "/api/v1/files", "POST"));

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

        permissionRepository.saveAll(permissions);
        System.out.println(">>> Created " + permissions.size() + " permissions");
    }

    private Permission perm(String name, String domain, String apiPath, String method) {
        Permission p = new Permission();
        p.setName(name);
        p.setDomain(domain);
        p.setApiPath(apiPath);
        p.setMethod(method);
        p.setCreatedAt(Instant.now());
        p.setCreatedBy("system");
        return p;
    }

    // ======================== ROLES ========================

    private void createRoles() {
        if (roleRepository.count() > 0) return;

        List<Permission> all = permissionRepository.findAll();

        // ADMIN — toàn quyền
        Role admin = role("ADMIN", "Quản trị viên hệ thống — toàn quyền");
        admin.setPermissions(new ArrayList<>(all));
        roleRepository.save(admin);

        // MANAGER — quản lý sách, tác giả, NXB, thể loại, nhà cung cấp, xem đơn hàng/khách hàng, upload file
        Role manager = role("MANAGER", "Quản lý — quản lý sản phẩm, xem đơn hàng và khách hàng");
        manager.setPermissions(findPermissions(
                "BOOK_CREATE", "BOOK_UPDATE", "BOOK_DELETE", "BOOK_VIEW_ALL", "BOOK_VIEW_ALL_WITH_PAGINATION_AND_FILTER",
                "AUTHOR_CREATE", "AUTHOR_UPDATE", "AUTHOR_DELETE", "AUTHOR_VIEW_ALL", "AUTHOR_VIEW_BY_ID", "AUTHOR_VIEW_ALL_WITH_PAGINATION_AND_FILTER",
                "PUBLISHER_CREATE", "PUBLISHER_UPDATE", "PUBLISHER_DELETE", "PUBLISHER_VIEW_BY_ID", "PUBLISHER_VIEW_ALL_WITH_PAGINATION_AND_FILTER",
                "CATEGORY_CREATE", "CATEGORY_UPDATE", "CATEGORY_DELETE", "CATEGORY_VIEW_BY_ID", "CATEGORY_VIEW_ALL_WITH_PAGINATION_AND_FILTER",
                "SUPPLIER_CREATE", "SUPPLIER_UPDATE", "SUPPLIER_DELETE", "SUPPLIER_VIEW_BY_ID", "SUPPLIER_VIEW_ALL_WITH_PAGINATION_AND_FILTER",
                "CUSTOMER_VIEW_BY_ID", "CUSTOMER_VIEW_ALL_WITH_PAGINATION_AND_FILTER",
                "ORDER_VIEW_ALL_WITH_PAGINATION_AND_FILTER", "ORDER_VIEW_BY_ID", "ORDER_VIEW_MINE", "ORDER_UPDATE", "ORDER_CANCEL", "ORDER_DETAIL_UPDATE",
                "FILE_UPLOAD"
        ));
        roleRepository.save(manager);

        // STAFF — xem sản phẩm, quản lý khách hàng và đơn hàng, upload file
        Role staff = role("STAFF", "Nhân viên — quản lý khách hàng và đơn hàng");
        staff.setPermissions(findPermissions(
                "BOOK_VIEW_ALL", "BOOK_VIEW_ALL_WITH_PAGINATION_AND_FILTER",
                "AUTHOR_VIEW_ALL", "AUTHOR_VIEW_BY_ID", "AUTHOR_VIEW_ALL_WITH_PAGINATION_AND_FILTER",
                "PUBLISHER_VIEW_BY_ID", "PUBLISHER_VIEW_ALL_WITH_PAGINATION_AND_FILTER",
                "CATEGORY_VIEW_BY_ID", "CATEGORY_VIEW_ALL_WITH_PAGINATION_AND_FILTER",
                "SUPPLIER_VIEW_BY_ID", "SUPPLIER_VIEW_ALL_WITH_PAGINATION_AND_FILTER",
                "CUSTOMER_CREATE", "CUSTOMER_UPDATE", "CUSTOMER_DELETE", "CUSTOMER_VIEW_BY_ID", "CUSTOMER_VIEW_ALL_WITH_PAGINATION_AND_FILTER",
                "ORDER_CREATE", "ORDER_UPDATE", "ORDER_CANCEL", "ORDER_VIEW_ALL_WITH_PAGINATION_AND_FILTER", "ORDER_VIEW_BY_ID",
                "ORDER_DETAIL_CREATE", "ORDER_DETAIL_UPDATE", "ORDER_DETAIL_DELETE",
                "FILE_UPLOAD"
        ));
        roleRepository.save(staff);

        // CUSTOMER — giỏ hàng, đặt hàng, xem đơn hàng
        Role customer = role("CUSTOMER", "Khách hàng — mua sắm, giỏ hàng, đặt hàng");
        customer.setPermissions(findPermissions(
                "CART_ADD_TO_CART", "CART_VIEW_BY_ID",
                "ORDER_CREATE", "ORDER_VIEW_BY_ID", "ORDER_VIEW_MINE", "ORDER_UPDATE", "ORDER_CANCEL"
        ));
        roleRepository.save(customer);

        System.out.println(">>> Created 4 roles: ADMIN, MANAGER, STAFF, CUSTOMER");
    }

    private Role role(String name, String description) {
        Role r = new Role();
        r.setName(name);
        r.setDescription(description);
        r.setCreatedAt(Instant.now());
        r.setCreatedBy("system");
        return r;
    }

    private List<Permission> findPermissions(String... names) {
        List<Permission> result = new ArrayList<>();
        for (String name : names) {
            Permission p = permissionRepository.findByName(name);
            if (p != null) result.add(p);
        }
        return result;
    }

    // ======================== USERS ========================

    private void createUsers() {
        if (userRepository.count() > 0) return;

        Role adminRole = roleRepository.findByName("ADMIN");
        Role managerRole = roleRepository.findByName("MANAGER");
        Role staffRole = roleRepository.findByName("STAFF");
        Role customerRole = roleRepository.findByName("CUSTOMER");

        // 1 Admin
        createUser("admin@bookverse.com", "Nguyễn Văn Admin", "0900000001", "TP. Hồ Chí Minh", adminRole);

        // 2 Managers
        createUser("manager1@bookverse.com", "Trần Thị Manager", "0900000002", "Hà Nội", managerRole);
        createUser("manager2@bookverse.com", "Lê Văn Manager", "0900000003", "Đà Nẵng", managerRole);

        // 5 Staff
        createUser("staff1@bookverse.com", "Phạm Thị Nhân Viên", "0900000004", "TP. Hồ Chí Minh", staffRole);
        createUser("staff2@bookverse.com", "Hoàng Văn Nhân Viên", "0900000005", "Hà Nội", staffRole);
        createUser("staff3@bookverse.com", "Ngô Thị Nhân Viên", "0900000006", "Đà Nẵng", staffRole);
        createUser("staff4@bookverse.com", "Đỗ Văn Nhân Viên", "0900000007", "Cần Thơ", staffRole);
        createUser("staff5@bookverse.com", "Vũ Thị Nhân Viên", "0900000008", "Hải Phòng", staffRole);

        // 10 Customers — mỗi customer được liên kết với bảng customers + tạo cart
        createCustomerUser("customer1@bookverse.com", "Bùi Văn Khách", "0900000009", "TP. Hồ Chí Minh", customerRole, "079200001001");
        createCustomerUser("customer2@bookverse.com", "Đặng Thị Khách", "0900000010", "Hà Nội", customerRole, "079200001002");
        createCustomerUser("customer3@bookverse.com", "Lý Văn Khách", "0900000011", "Đà Nẵng", customerRole, "079200001003");
        createCustomerUser("customer4@bookverse.com", "Mai Thị Khách", "0900000012", "Cần Thơ", customerRole, "079200001004");
        createCustomerUser("customer5@bookverse.com", "Tô Văn Khách", "0900000013", "Hải Phòng", customerRole, "079200001005");
        createCustomerUser("customer6@bookverse.com", "Phan Thị Khách", "0900000014", "Huế", customerRole, "079200001006");
        createCustomerUser("customer7@bookverse.com", "Trịnh Văn Khách", "0900000015", "Nha Trang", customerRole, "079200001007");
        createCustomerUser("customer8@bookverse.com", "Cao Thị Khách", "0900000016", "Vũng Tàu", customerRole, "079200001008");
        createCustomerUser("customer9@bookverse.com", "Hồ Văn Khách", "0900000017", "Biên Hòa", customerRole, "079200001009");
        createCustomerUser("customer10@bookverse.com", "Dương Thị Khách", "0900000018", "Thủ Đức", customerRole, "079200001010");

        System.out.println(">>> Created 18 users: 1 admin, 2 managers, 5 staff, 10 customers");
    }

    private User createUser(String email, String fullName, String phone, String address, Role role) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("123456"));
        user.setFullName(fullName);
        user.setPhone(phone);
        user.setAddress(address);
        user.setRole(role);
        user.setCreatedAt(Instant.now());
        user.setCreatedBy("system");
        return userRepository.save(user);
    }

    private void createCustomerUser(String email, String fullName, String phone, String address,
                                    Role role, String identityCard) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("123456"));
        user.setFullName(fullName);
        user.setPhone(phone);
        user.setAddress(address);
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
