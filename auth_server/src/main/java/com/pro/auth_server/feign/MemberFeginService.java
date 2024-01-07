package com.pro.auth_server.feign;
import com.pro.auth_server.vo.LoginVo;
import com.pro.auth_server.vo.UserRegisterVo;
import com.pro.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 会员服务
 */
@FeignClient("member")
public interface MemberFeginService {
    @PostMapping("/member/member/register")
    public R register(@RequestBody UserRegisterVo vo);


    @RequestMapping("/member/member/login")
    public R login(@RequestBody LoginVo vo);

//    @RequestMapping("/member/member/oauth2/login")
//    public R socialLogin(@RequestBody SocialUser vo);
}
