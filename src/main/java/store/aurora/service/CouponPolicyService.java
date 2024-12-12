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

    //사용자 쿠폰 수정
    public void couponUpdate(@Valid RequestCouponDto requestCouponDto) {

    }

    //사용자 쿠폰 생성
    public void userCouponCreate(RequestCouponDto requestCouponDto) {

    }

    //사용자 쿠폰 삭제 굳이 필요한가 싶어서 킵
    //    public void couponDelete(String couponId) {
    //
    //    }
}
