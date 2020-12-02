package com.xiaoan.demo.provider.service.impl;

import com.xiaoan.demo.client.service.GreetingsService;

public class GreetingsServiceImpl implements GreetingsService {
    @Override
    public String sayHi(String name) {
    return "hi, " + name;
}
}