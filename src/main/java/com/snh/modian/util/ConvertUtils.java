package com.snh.modian.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConvertUtils {
    public static<T> List<T> convertToList(Object object, ObjectMapper objectMapper, Class<T> type) {
        if (object instanceof List) {
            List<T> resultList = new ArrayList<>();
            ((List) object).forEach(o -> {resultList.add(objectMapper.convertValue(o, type));});
            return resultList;
        }
        return Collections.EMPTY_LIST;
    }
}
