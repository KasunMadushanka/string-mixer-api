package com.assignment.stringmixer.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class StringMixerServiceImpl implements StringMixerService {

    @Override
    public String mix(String s1, String s2) {
        String[] strings = { s1, s2 };

        HashMap<Character, Integer> s1Map = new HashMap<Character, Integer>();
        HashMap<Character, Integer> s2Map = new HashMap<Character, Integer>();

        List<Map<Character, Integer>> listOfMaps = new ArrayList<Map<Character, Integer>>();
        listOfMaps.add(s1Map);
        listOfMaps.add(s2Map);

        int j = 0;

        for (String s : strings) {
            for (char c : s.toCharArray()) {
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
            j++;
        }

        HashMap<Character, Integer> mergedMap = new HashMap<Character, Integer>(s1Map);
        s2Map.forEach((key, value) -> mergedMap.merge(key, value, (v1, v2) -> v1.equals(v2) ? v1 : v1 > v2 ? v1 : v2));

        LinkedHashMap<Character, Integer> sortedMap = mergedMap.entrySet().stream()
                .sorted(Map.Entry.<Character, Integer>comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new));

        List<String> subStringList = new ArrayList<String>();

        for (Map.Entry<Character, Integer> entry : sortedMap.entrySet()) {
            Integer mergedFrequency = entry.getValue();
            Integer s1Frequency = s1Map.get(entry.getKey());
            Integer s2Frequency = s2Map.get(entry.getKey());

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
        return result;
    }
}
