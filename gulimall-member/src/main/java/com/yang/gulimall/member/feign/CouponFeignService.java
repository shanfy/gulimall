package com.yang.gulimall.member.feign;

import com.yang.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 远程调用coupon服务的feign接口
 */
@FeignClient(name = "gulimall-coupon",path = "/coupon/coupon")
public interface CouponFeignService {
    /**
     * 获取会员的所有优惠券信息
     */
    @GetMapping("/member/coupons")
    R memberCoupons();
}
