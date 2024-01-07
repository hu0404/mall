package com.pro.member.exception;

public class PhoneExsitExecption extends RuntimeException{
    public PhoneExsitExecption() {
        super("手机号不存在");
    }
}
