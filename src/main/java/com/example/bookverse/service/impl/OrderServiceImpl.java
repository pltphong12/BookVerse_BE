package com.example.bookverse.service.impl;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bookverse.config.VnpayProperties;
import com.example.bookverse.domain.Book;
import com.example.bookverse.domain.Customer;
import com.example.bookverse.domain.Order;
import com.example.bookverse.domain.OrderDetail;
import com.example.bookverse.domain.OrderPayment;
import com.example.bookverse.domain.QOrder;
import com.example.bookverse.dto.criteria.CriteriaFilterOrder;
import com.example.bookverse.dto.enums.OrderPaymentStatus;
import com.example.bookverse.dto.enums.OrderStatus;
import com.example.bookverse.dto.enums.PaymentMethod;
import com.example.bookverse.dto.enums.PaymentStatus;
import com.example.bookverse.dto.request.ReqCreateOrderDTO;
import com.example.bookverse.dto.request.ReqOrderLineDTO;
import com.example.bookverse.dto.request.ReqUpdateOrderDTO;
import com.example.bookverse.dto.response.ResOrderDTO;
import com.example.bookverse.dto.response.ResOrderSummaryDTO;
import com.example.bookverse.dto.response.ResPagination;
import com.example.bookverse.exception.global.IdInvalidException;
import com.example.bookverse.repository.BookRepository;
import com.example.bookverse.repository.CartDetailRepository;
import com.example.bookverse.repository.CartRepository;
import com.example.bookverse.repository.CustomerRepository;
import com.example.bookverse.repository.OrderPaymentRepository;
import com.example.bookverse.repository.OrderRepository;
import com.example.bookverse.service.CartService;
import com.example.bookverse.service.OrderService;
import com.example.bookverse.util.CurrentCustomerAccessor;
import com.example.bookverse.util.VnpayUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Service
public class OrderServiceImpl implements OrderService {

    private static final String ORDER_VIEW_ALL_PAGED = "ORDER_VIEW_ALL_WITH_PAGINATION_AND_FILTER";

    /**
     * Phí ship do server quyết định (sau này có thể đọc từ cấu hình / đối tác vận
     * chuyển).
     */
    private static final double DEFAULT_SHIPPING_FEE = 0;

    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final CustomerRepository customerRepository;
    private final OrderPaymentRepository orderPaymentRepository;
    private final CurrentCustomerAccessor currentCustomerAccessor;
    private final JPAQueryFactory queryFactory;
    private final VnpayProperties vnpayProperties;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
            BookRepository bookRepository,
            CustomerRepository customerRepository,
            OrderPaymentRepository orderPaymentRepository,
            CurrentCustomerAccessor currentCustomerAccessor,
            JPAQueryFactory queryFactory,
            VnpayProperties vnpayProperties,
            CartRepository cartRepository,
            CartDetailRepository cartDetailRepository) {
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
        this.customerRepository = customerRepository;
        this.orderPaymentRepository = orderPaymentRepository;
        this.currentCustomerAccessor = currentCustomerAccessor;
        this.queryFactory = queryFactory;
        this.vnpayProperties = vnpayProperties;
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
    }

    private boolean hasAuthority(String authority) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return false;
        }
        return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(authority));
    }

    private Customer getCurrentCustomer() throws IdInvalidException {
        return currentCustomerAccessor.requireCurrentCustomer();
    }

    private void assertCanAccessOrder(Order order) throws IdInvalidException {
        if (hasAuthority(ORDER_VIEW_ALL_PAGED)) {
            return;
        }
        Customer me = getCurrentCustomer();
        if (order.getCustomer() == null || order.getCustomer().getId() != me.getId()) {
            throw new IdInvalidException("Bạn không có quyền thao tác trên đơn hàng này");
        }
    }

    @Override
    @Transactional
    public ResOrderDTO create(ReqCreateOrderDTO req) throws Exception {
        Customer customer = getCurrentCustomer();

        Map<Long, Long> qtyByBookId = new HashMap<>();
        for (ReqOrderLineDTO line : req.getItems()) {
            qtyByBookId.merge(line.getBookId(), line.getQuantity(), Long::sum);
        }
        for (Map.Entry<Long, Long> e : qtyByBookId.entrySet()) {
            Book book = bookRepository.findById(e.getKey())
                    .orElseThrow(() -> new IdInvalidException("Không tìm thấy sách id = " + e.getKey()));
            if (book.getQuantity() < e.getValue()) {
                throw new IdInvalidException("Sách \"" + book.getTitle() + "\" không đủ tồn kho");
            }
        }

        double subtotal = 0;
        double discount = 0;
        List<OrderDetail> details = new ArrayList<>();

        for (ReqOrderLineDTO line : req.getItems()) {
            Book book = bookRepository.findById(line.getBookId())
                    .orElseThrow(() -> new IdInvalidException("Không tìm thấy sách id = " + line.getBookId()));
            double unitAfterDiscount = book.getPrice() - book.getPrice() * book.getDiscount() / 100.0;
            subtotal += book.getPrice() * line.getQuantity();
            discount += (book.getDiscount() * book.getPrice() / 100.0) * line.getQuantity();
            OrderDetail od = new OrderDetail();
            od.setBook(book);
            od.setQuantity(line.getQuantity());
            od.setPrice(unitAfterDiscount);
            details.add(od);
        }

        // COD: trừ kho ngay; VNPAY: trừ kho khi nhận xác nhận thanh toán thành công
        if (req.getPaymentMethod() == PaymentMethod.COD) {
            deductStock(qtyByBookId);
        }

        double shipping = DEFAULT_SHIPPING_FEE;
        double total = subtotal + shipping - discount;

        Order order = new Order();
        order.setCustomer(customer);
        order.setReceiverName(req.getReceiverName());
        order.setReceiverAddress(req.getReceiverAddress());
        order.setReceiverPhone(req.getReceiverPhone());
        order.setReceiverEmail(req.getReceiverEmail());
        order.setSubtotal(subtotal);
        order.setShippingFee(shipping);
        order.setDiscountTotal(discount);
        order.setTotalPrice(total);
        order.setNote(req.getNote());
        order.setPaymentMethod(req.getPaymentMethod());
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setStatus(OrderStatus.PENDING);

        for (OrderDetail od : details) {
            od.setOrder(order);
        }
        order.setOrderDetails(details);

        Order saved = orderRepository.save(order);

        customer.setTotalOrder(customer.getTotalOrder() == null ? 1L : customer.getTotalOrder() + 1);
        customerRepository.save(customer);

        ResOrderDTO result = ResOrderDTO.from(
                orderRepository.fetchDetailById(saved.getId()).orElse(saved));

        if (req.getPaymentMethod() == PaymentMethod.VNPAY) {
            String paymentUrl = createVnpayPayment(saved, req.getClientIpAddress());
            result.setPaymentUrl(paymentUrl);
        }

        // remove cart
        removeCart(customer);
        return result;
    }

    // remove cart
    private void removeCart(Customer customer) {
        cartDetailRepository.deleteAllByCart(customer.getCart());
    }

    /**
     * Trừ tồn kho + tăng sold cho từng sách trong map.
     * Dùng chung cho COD (lúc tạo đơn) và VNPAY (lúc xác nhận thanh toán).
     */
    public void deductStock(Map<Long, Long> qtyByBookId) throws IdInvalidException {
        for (Map.Entry<Long, Long> e : qtyByBookId.entrySet()) {
            Book book = bookRepository.findById(e.getKey())
                    .orElseThrow(() -> new IdInvalidException("Không tìm thấy sách id = " + e.getKey()));
            book.setQuantity(book.getQuantity() - e.getValue());
            book.setSold(book.getSold() + e.getValue());
            bookRepository.save(book);
        }
    }

    /**
     * Tạo OrderPayment INITIATED và build URL thanh toán VNPAY.
     */
    private String createVnpayPayment(Order order, String clientIp) {
        String providerRef = "BV" + order.getId() + "T" + System.currentTimeMillis();

        OrderPayment payment = new OrderPayment();
        payment.setProviderRef(providerRef);
        payment.setMethod(PaymentMethod.VNPAY);
        payment.setAmount(order.getTotalPrice());
        payment.setStatus(OrderPaymentStatus.INITIATED);
        payment.setOrder(order);
        orderPaymentRepository.save(payment);

        long amountInSmallestUnit = Math.round(order.getTotalPrice() * 100);

        Map<String, String> params = new HashMap<>();
        params.put("vnp_Version", vnpayProperties.getVersion());
        params.put("vnp_Command", vnpayProperties.getCommand());
        params.put("vnp_TmnCode", vnpayProperties.getTmnCode());
        params.put("vnp_Amount", String.valueOf(amountInSmallestUnit));
        params.put("vnp_CurrCode", vnpayProperties.getCurrCode());
        params.put("vnp_TxnRef", providerRef);
        params.put("vnp_OrderInfo", "Thanh toan don hang " + order.getOrderCode());
        params.put("vnp_OrderType", vnpayProperties.getOrderType());
        params.put("vnp_Locale", vnpayProperties.getLocale());
        params.put("vnp_ReturnUrl", vnpayProperties.getReturnUrl());
        params.put("vnp_IpAddr", clientIp != null ? clientIp : "127.0.0.1");
        params.put("vnp_CreateDate", VnpayUtil.now());
        params.put("vnp_ExpireDate", VnpayUtil.nowPlusMinutes(15));

        return VnpayUtil.buildPaymentUrl(
                vnpayProperties.getPaymentUrl(), params, vnpayProperties.getHashSecret());
    }

    @Override
    @Transactional
    public ResOrderDTO update(ReqUpdateOrderDTO req) throws Exception {
        Order orderInDB = orderRepository.findById(req.getId())
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy đơn hàng"));

        assertCanAccessOrder(orderInDB);

        if (orderInDB.getStatus() == OrderStatus.CANCELLED) {
            throw new IdInvalidException("Đơn hàng đã hủy, không thể cập nhật");
        }

        boolean admin = hasAuthority(ORDER_VIEW_ALL_PAGED);

        if (!admin) {
            if (orderInDB.getStatus() != OrderStatus.PENDING) {
                throw new IdInvalidException("Chỉ có thể sửa thông tin nhận hàng khi đơn đang chờ xử lý");
            }
            if (req.getStatus() != null) {
                throw new IdInvalidException("Bạn không có quyền thay đổi trạng thái đơn hàng");
            }
            if (req.getPaymentStatus() != null) {
                throw new IdInvalidException("Bạn không có quyền thay đổi trạng thái thanh toán");
            }
        }

        if (req.getReceiverName() != null) {
            orderInDB.setReceiverName(req.getReceiverName());
        }
        if (req.getReceiverAddress() != null) {
            orderInDB.setReceiverAddress(req.getReceiverAddress());
        }
        if (req.getReceiverPhone() != null) {
            orderInDB.setReceiverPhone(req.getReceiverPhone());
        }
        if (req.getReceiverEmail() != null) {
            orderInDB.setReceiverEmail(req.getReceiverEmail());
        }
        if (admin && req.getStatus() != null) {
            orderInDB.setStatus(req.getStatus());
        }
        if (admin && req.getPaymentStatus() != null) {
            orderInDB.setPaymentStatus(req.getPaymentStatus());
            if (req.getPaymentStatus() == PaymentStatus.PAID && orderInDB.getPaidAt() == null) {
                orderInDB.setPaidAt(Instant.now());
            }
        }

        orderRepository.save(orderInDB);
        return ResOrderDTO.from(orderRepository.fetchDetailById(orderInDB.getId()).orElse(orderInDB));
    }

    @Override
    @Transactional(readOnly = true)
    public ResOrderDTO getById(long id) throws Exception {
        Order order = orderRepository.fetchDetailById(id)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy đơn hàng"));
        assertCanAccessOrder(order);
        return ResOrderDTO.from(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResOrderDTO> listMine() throws Exception {
        Customer customer = getCurrentCustomer();
        return orderRepository.fetchDetailsByCustomerId(customer.getId()).stream()
                .map(ResOrderDTO::from)
                .toList();
    }

    private Page<Order> filterOrders(CriteriaFilterOrder criteria, Pageable pageable) throws IdInvalidException {
        QOrder qOrder = QOrder.order;
        BooleanBuilder builder = new BooleanBuilder();

        if (criteria.getOrderCode() != null && !criteria.getOrderCode().isBlank()) {
            builder.and(qOrder.orderCode.containsIgnoreCase(criteria.getOrderCode().trim()));
        }
        if (criteria.getCustomerId() != null && criteria.getCustomerId() > 0) {
            builder.and(qOrder.customer.id.eq(criteria.getCustomerId()));
        }
        if (criteria.getStatus() != null && !criteria.getStatus().isBlank()) {
            try {
                builder.and(qOrder.status.eq(OrderStatus.valueOf(criteria.getStatus().trim().toUpperCase())));
            } catch (IllegalArgumentException e) {
                throw new IdInvalidException("status không hợp lệ: " + criteria.getStatus());
            }
        }
        if (criteria.getPaymentMethod() != null && !criteria.getPaymentMethod().isBlank()) {
            try {
                builder.and(qOrder.paymentMethod
                        .eq(PaymentMethod.valueOf(criteria.getPaymentMethod().trim().toUpperCase())));
            } catch (IllegalArgumentException e) {
                throw new IdInvalidException("paymentMethod không hợp lệ: " + criteria.getPaymentMethod());
            }
        }
        if (criteria.getPaymentStatus() != null && !criteria.getPaymentStatus().isBlank()) {
            try {
                builder.and(qOrder.paymentStatus
                        .eq(PaymentStatus.valueOf(criteria.getPaymentStatus().trim().toUpperCase())));
            } catch (IllegalArgumentException e) {
                throw new IdInvalidException("paymentStatus không hợp lệ: " + criteria.getPaymentStatus());
            }
        }
        if (criteria.getDateFrom() != null) {
            Instant from = criteria.getDateFrom().atStartOfDay(ZoneId.systemDefault()).toInstant();
            builder.and(qOrder.createdAt.goe(from));
        }
        if (criteria.getDateTo() != null) {
            Instant to = criteria.getDateTo().atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();
            builder.and(qOrder.createdAt.loe(to));
        }

        List<Order> content = queryFactory.selectFrom(qOrder)
                .where(builder)
                .orderBy(orderSpecifiers(pageable, qOrder))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(qOrder)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    private static OrderSpecifier<?>[] orderSpecifiers(Pageable pageable, QOrder q) {
        if (pageable.getSort().isEmpty()) {
            return new OrderSpecifier<?>[] { q.createdAt.desc() };
        }
        List<OrderSpecifier<?>> list = new ArrayList<>();
        for (Sort.Order o : pageable.getSort()) {
            String p = o.getProperty();
            boolean asc = o.getDirection().isAscending();
            switch (p) {
                case "id" -> list.add(asc ? q.id.asc() : q.id.desc());
                case "createdAt" -> list.add(asc ? q.createdAt.asc() : q.createdAt.desc());
                case "totalPrice" -> list.add(asc ? q.totalPrice.asc() : q.totalPrice.desc());
                case "status" -> list.add(asc ? q.status.asc() : q.status.desc());
                case "orderCode" -> list.add(asc ? q.orderCode.asc() : q.orderCode.desc());
                default -> {
                    /* ignore */
                }
            }
        }
        if (list.isEmpty()) {
            return new OrderSpecifier<?>[] { q.createdAt.desc() };
        }
        return list.toArray(OrderSpecifier[]::new);
    }

    @Override
    @Transactional(readOnly = true)
    public ResPagination fetchAllOrdersWithPaginationAndFilter(CriteriaFilterOrder criteria, Pageable pageable)
            throws Exception {
        if (!hasAuthority(ORDER_VIEW_ALL_PAGED)) {
            throw new IdInvalidException("Bạn không có quyền xem danh sách đơn hàng");
        }
        Page<Order> page = filterOrders(criteria, pageable);
        ResPagination rs = new ResPagination();
        ResPagination.Meta mt = new ResPagination.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(page.getSize());
        mt.setPages(page.getTotalPages());
        mt.setTotal(page.getTotalElements());
        rs.setMeta(mt);
        List<ResOrderSummaryDTO> rows = new ArrayList<>();
        for (Order order : page.getContent()) {
            rows.add(ResOrderSummaryDTO.from(order));
        }
        rs.setResult(rows);
        return rs;
    }

    @Override
    @Transactional
    public void cancel(long id) throws Exception {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy đơn hàng"));

        assertCanAccessOrder(order);

        if (order.getStatus() == OrderStatus.CANCELLED) {
            return;
        }
        if (order.getStatus() == OrderStatus.SHIPPING || order.getStatus() == OrderStatus.DELIVERED) {
            throw new IdInvalidException("Không thể hủy đơn đang giao hoặc đã giao");
        }
        if (order.getPaymentStatus() == PaymentStatus.PAID) {
            throw new IdInvalidException("Đơn đã thanh toán, không thể hủy qua hệ thống");
        }

        if (order.getOrderDetails() != null) {
            for (OrderDetail detail : order.getOrderDetails()) {
                Book book = detail.getBook();
                if (book != null) {
                    book.setQuantity(book.getQuantity() + detail.getQuantity());
                    book.setSold(Math.max(0, book.getSold() - detail.getQuantity()));
                    bookRepository.save(book);
                }
            }
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
}
