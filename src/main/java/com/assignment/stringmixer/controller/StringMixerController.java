package com.assignment.stringmixer.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class StringMixerController {

    @GetMapping(value = "/mix")
    public Object getMixedString(@RequestParam("s1") String s1, @RequestParam("s2") String s2) {
        String[] strings = { s1, s2 };

        HashMap<Character, Integer> map1 = new HashMap<Character, Integer>();
        HashMap<Character, Integer> map2 = new HashMap<Character, Integer>();

        List<Map<Character, Integer>> listOfMaps = new ArrayList<Map<Character, Integer>>();
        listOfMaps.add(map1);
        listOfMaps.add(map2);

        for (int j = 0; j < strings.length; j++) {
            for (int i = 0; i < strings[j].length(); i++) {
                char c = strings[j].charAt(i);
                if (!Character.isLowerCase(c)) {
                    continue;
                }

                Integer val = listOfMaps.get(j).get(c);
                if (val != null) {
                    listOfMaps.get(j).put(c, val + 1);
                } else {
                    listOfMaps.get(j).put(c, 1);
                }
            }

        }

        HashMap<Character, Integer> mergedMap = new HashMap<Character, Integer>(map1);
        map2.forEach((key, value) -> mergedMap.merge(key, value, (v1, v2) -> v1.equals(v2) ? v1 : v1 > v2 ? v1 : v2));

        LinkedHashMap<Character, Integer> sortedMap = mergedMap.entrySet().stream()
                .sorted(Map.Entry.<Character, Integer>comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new));

        List<String> subStringList = new ArrayList<String>();

        for (Map.Entry<Character, Integer> entry : sortedMap.entrySet()) {
            Integer mergedFrequency = entry.getValue();
            Integer s1Frequency = map1.get(entry.getKey());
            Integer s2Frequency = map2.get(entry.getKey());

            if (mergedFrequency <= 1) {
                continue;
            }
            if (s2Frequency == null) {
                subStringList.add("1:" + entry.getKey().toString().repeat(s1Frequency));
            } else if (s1Frequency == null) {
                subStringList.add("2:" + entry.getKey().toString().repeat(s2Frequency));
            } else if (s1Frequency.equals(s2Frequency)) {
                subStringList.add("=:" + entry.getKey().toString().repeat(s1Frequency));
            } else if (s1Frequency > s2Frequency) {
                subStringList.add("1:" + entry.getKey().toString().repeat(s1Frequency));
            } else {
                subStringList.add("2:" + entry.getKey().toString().repeat(s2Frequency));
            }

        }
        subStringList.sort(Comparator.comparingInt(String::length).reversed().thenComparing(Comparator.naturalOrder()));
        String result = String.join("/", subStringList);
        return Collections.singletonMap("result", result);

    }

}