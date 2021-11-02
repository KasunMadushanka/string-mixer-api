package com.assignment.stringmixer.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StringMixerController {

    @PostMapping("/mix")
    public String mixStrings(@RequestBody String[] inputs) {
      return "";
    }

    @GetMapping("/mix")
    public String mixStrings2() {
        String s1 = "mmmmm m nnnnn y&friend&Paul has heavy hats! &";
        String s2 = "my frie n d Joh n has ma n y ma n y frie n ds n&";

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
                    listOfMaps.get(j).put(c, new Integer(val + 1));
                } else {
                    listOfMaps.get(j).put(c, 1);
                }
            }

        }

        String result = "";
        HashMap<Character, Integer> mergedMap = new HashMap<Character, Integer>(map1);
        map2.forEach((key, value) -> mergedMap.merge(key, value, (v1, v2) -> v1.equals(v2) ? v1 : v1 > v2 ? v1 : v2));

        LinkedHashMap<Character, Integer> sortedMap = mergedMap.entrySet().stream()
                .sorted(Map.Entry.<Character, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new));

        System.out.println(sortedMap);

        for (Map.Entry<Character, Integer> entry : sortedMap.entrySet()) {
            Integer mergedFrequency = entry.getValue();
            Integer s1Frequency = map1.get(entry.getKey());
            Integer s2Frequency = map2.get(entry.getKey());

            if (mergedFrequency <= 1) {
                continue;
            }
            if (s2Frequency.equals(null)) {
                result += "1:" + entry.getKey().toString().repeat(s1Frequency);
            } else if (s1Frequency.equals(null)) {
                result += "2:" + entry.getKey().toString().repeat(s2Frequency);
            } else if (s1Frequency.equals(s2Frequency)) {
                result += "=:" + entry.getKey().toString().repeat(s1Frequency);
            } else if (s1Frequency > s2Frequency) {
                result += "1:" + entry.getKey().toString().repeat(s1Frequency);
            } else {
                result += "2:" + entry.getKey().toString().repeat(s2Frequency);
            }
            result += "/";
        }

        result = result.substring(0, result.length() - 1);

        System.out.println(result);
        return "";
    }

}