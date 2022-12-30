package com.yang.gulimall.cart.to;

import lombok.Data;

/**
 * @Description:
 * @Created: with IntelliJ IDEA.
 **/

@Data
public class UserInfoTo {

    private Long userId;

    private String userKey;

    /**
     * 是否临时用户
     */
    private Boolean tempUser = false;

}
