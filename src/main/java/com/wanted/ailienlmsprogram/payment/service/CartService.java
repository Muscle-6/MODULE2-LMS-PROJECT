package com.wanted.ailienlmsprogram.payment.service;

import com.wanted.ailienlmsprogram.course.entity.Course;
import com.wanted.ailienlmsprogram.course.repository.CourseRepository;
import com.wanted.ailienlmsprogram.member.entity.Member;
import com.wanted.ailienlmsprogram.payment.dto.CartItemResponse;
import com.wanted.ailienlmsprogram.payment.entity.Cart;
import com.wanted.ailienlmsprogram.payment.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final CourseRepository courseRepository;

    public List<CartItemResponse> getCartItems(Member member) {
        return cartRepository.findCartItemsByMemberId(member.getMemberId());
    }

    @Transactional
    public void addToCart(Long courseId, Member member) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 강좌입니다."));

        if (cartRepository.existsByCourseCourseIdAndMemberMemberId(courseId, member.getMemberId())) {
            throw new IllegalArgumentException("이미 장바구니에 담긴 강좌입니다.");
        }

        if (cartRepository.countEnrollmentByMemberAndCourse(member.getMemberId(), courseId) > 0) {
            throw new IllegalArgumentException("이미 수강 중인 강좌입니다.");
        }

        Cart cart = new Cart();
        cart.setCourse(course);
        cart.setMember(member);
        cart.setCreatedAt(LocalDateTime.now());
        cartRepository.save(cart);
    }

    @Transactional
    public void removeFromCart(Long cartId, Member member) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니 항목이 존재하지 않습니다."));

        if (!cart.getMember().getMemberId().equals(member.getMemberId())) {
            throw new IllegalArgumentException("본인의 장바구니 항목만 삭제할 수 있습니다.");
        }

        cartRepository.delete(cart);
    }

    /**
     * 선택된 cartIds를 검증하고 토스페이먼츠 결제에 필요한 주문 정보를 생성한다.
     * orderId는 여기서 생성되어 컨트롤러가 세션에 저장하고 토스 SDK로 전달한다.
     */
    public Map<String, Object> prepareTossPayment(List<Long> cartIds, Member member) {
        if (cartIds == null || cartIds.isEmpty()) {
            throw new IllegalArgumentException("선택된 강좌가 없습니다.");
        }

        List<Cart> carts = cartRepository.findAllById(cartIds);

        if (carts.size() != cartIds.size()) {
            throw new IllegalArgumentException("존재하지 않는 장바구니 항목이 포함되어 있습니다.");
        }

        boolean allBelongToMember = carts.stream()
                .allMatch(c -> c.getMember().getMemberId().equals(member.getMemberId()));
        if (!allBelongToMember) {
            throw new IllegalArgumentException("접근 권한이 없는 항목이 포함되어 있습니다.");
        }

        int totalAmount = carts.stream().mapToInt(c -> c.getCourse().getPrice()).sum();

        String orderId = "ORD-"
                + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + "-"
                + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();

        String orderName = carts.size() == 1
                ? carts.get(0).getCourse().getTitle()
                : carts.get(0).getCourse().getTitle() + " 외 " + (carts.size() - 1) + "개";

        return Map.of(
                "orderId", orderId,
                "amount", totalAmount,
                "orderName", orderName
        );
    }
}
