package com.assignment.stringmixer.services;

import org.springframework.stereotype.Service;

@Service
public interface StringMixerService {

    public String mix(String s1, String s2);
}
