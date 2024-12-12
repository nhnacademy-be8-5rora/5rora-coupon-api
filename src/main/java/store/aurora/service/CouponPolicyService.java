package store.aurora.service;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.aurora.dto.AddPolicyDTO;
import store.aurora.dto.DiscountRuleDTO;
import store.aurora.dto.RequestCouponDto;
import store.aurora.dto.RequestCouponPolicyDTO;

import org.springframework.beans.factory.annotation.Autowired;
import store.aurora.entity.*;
import store.aurora.exception.CouponInsertException;
import store.aurora.repository.*;

import java.util.List;
import java.time.LocalDate;

@Service
public class CouponPolicyService {

    private final CouponPolicyRepository couponPolicyRepository;
    private final CouponRepository couponRepository;
    private final DisCountRuleRepository disCountRuleRepository;
    private  final CategoryPolicyRepository categoryPolicyRepository;
    private  final BookPolicyRepository bookPolicyRepository;

    @Autowired
    public CouponPolicyService(CouponPolicyRepository couponPolicyRepository, CouponRepository couponRepository
    , DisCountRuleRepository disCountRuleRepository, CategoryPolicyRepository categoryPolicyRepository
            , BookPolicyRepository bookPolicyRepository) {
        this.couponPolicyRepository = couponPolicyRepository;
        this.couponRepository = couponRepository;
        this.disCountRuleRepository = disCountRuleRepository;
        this.categoryPolicyRepository = categoryPolicyRepository;
        this.bookPolicyRepository = bookPolicyRepository;
    }


    //쿠폰 정책 생성(쿠폰계산 및 쿠폰 정책 개체 생성)
    @Transactional
    public void couponPolicyCreate(@Valid RequestCouponPolicyDTO requestCouponPolicyDTO
            , @Valid DiscountRuleDTO discountRuleDTO, @Valid AddPolicyDTO addPolicyDTO) {

        CouponPolicy couponPolicy = new CouponPolicy();
        couponPolicy.setName(requestCouponPolicyDTO.getPolicyName());
        couponPolicy.setSaleType(requestCouponPolicyDTO.getSaleType());

        DiscountRule discountRule = new DiscountRule();
        discountRule.setPolicy(couponPolicy);
        discountRule.setMaxSale(discountRuleDTO.getMaxSale());
        discountRule.setNeedCost(discountRuleDTO.getNeedCost());
        discountRule.setSaleAmount(discountRuleDTO.getSaleAmount());
        discountRule.setSalePercent(discountRuleDTO.getSalePercent());


        // 카테고리 정책 생성
        if (addPolicyDTO.getCategoryId() != null) {
            List<CategoryPolicy> categoryPolicies = addPolicyDTO.getCategoryId().stream()
                    .map(categoryId -> {
                        CategoryPolicy categoryPolicy = new CategoryPolicy();
                        categoryPolicy.setPolicy(couponPolicy);
                        categoryPolicy.setCategoryId(categoryId);
                        return categoryPolicy;
                    })
                    .toList();
            categoryPolicyRepository.saveAll(categoryPolicies);
        }

        // 책 정책 생성
        if (addPolicyDTO.getBookId() != null) {
            List<BookPolicy> bookPolicies = addPolicyDTO.getBookId().stream()
                    .map(bookId -> {
                        BookPolicy bookPolicy = new BookPolicy();
                        bookPolicy.setPolicy(couponPolicy);
                        bookPolicy.setBookId(bookId);
                        return bookPolicy;
                    })
                    .toList();
            bookPolicyRepository.saveAll(bookPolicies);
        }

        couponPolicyRepository.save(couponPolicy);
        disCountRuleRepository.save(discountRule);
    }

    //사용자 쿠폰 수정(요청한 유저 ID 리스트를 통해 해당 ID에 포함된 userCoupons 들을 수정)
    @Transactional
    public void couponUpdate(@Valid RequestCouponDto requestCouponDto) {
        if (requestCouponDto.getUserId() == null || requestCouponDto.getUserId().isEmpty()) {
            throw new IllegalArgumentException("User ID list must not be empty");
        }
        if (requestCouponDto.getPolicy() == null) {
            throw new IllegalArgumentException("Policy must not be null");
        }

        couponRepository.updateCouponAttributesByUserIds(
                requestCouponDto.getState(),                    // 쿠폰 상태
                requestCouponDto.getPolicy().getId(),           // 정책 ID (CouponPolicy에서 가져옴)
                requestCouponDto.getStartDate(),                // 시작일
                requestCouponDto.getEndDate(),                  // 종료일
                requestCouponDto.getUserId()                    // 유저 ID 리스트
        );
    }

    //사용자 쿠폰 생성
    @Transactional
    public void userCouponCreate(RequestCouponDto requestCouponDto) {
        if (requestCouponDto.getUserId() == null || requestCouponDto.getUserId().isEmpty()) {
            throw new IllegalArgumentException("User ID list must not be empty");
        }
        if (requestCouponDto.getPolicy() == null) {
            throw new IllegalArgumentException("Policy must not be null");
        }

        List<Long> userIds = requestCouponDto.getUserId(); // 유저 ID 리스트
        CouponPolicy policy = requestCouponDto.getPolicy(); // 적용할 정책
        CouponState state = requestCouponDto.getState();   // 쿠폰 초기 상태
        LocalDate startDate = requestCouponDto.getStartDate(); // 시작일
        LocalDate endDate = requestCouponDto.getEndDate();     // 종료일

        List<UserCoupon> newCoupons = userIds.stream()
                .map(userId -> {
                    UserCoupon userCoupon = new UserCoupon();
                    userCoupon.setUserId(userId); // 사용자 ID 설정
                    userCoupon.setPolicy(policy); // 정책 설정
                    userCoupon.setCouponState(state); // 초기 상태 설정
                    userCoupon.setStartDate(startDate); // 시작일 설정
                    userCoupon.setEndDate(endDate); // 종료일 설정
                    return userCoupon;
                })
                .toList();

        couponRepository.saveAll(newCoupons); // 한 번에 저장
    }

    private UserCoupon createUserCoupon(Long userId, CouponPolicy policy, CouponState state, LocalDate startDate, LocalDate endDate) {
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUserId(userId);
        userCoupon.setPolicy(policy);
        userCoupon.setCouponState(state);
        userCoupon.setStartDate(startDate);
        userCoupon.setEndDate(endDate);
        return userCoupon;
    }
}
