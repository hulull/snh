package com.snh.modian.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortUtils<E> {
    public void sort(List<E> list, final String method, final String sort) {
        Collections.sort(list, new Comparator() {
            public int compare(Object a, Object b) {
                int ret = 0;
                try {
                    Method m1 = ((E) a).getClass().getMethod(method, new Class[0]);
                    Method m2 = ((E) b).getClass().getMethod(method, new Class[0]);
                    if (sort != null && "desc".equals(sort)) {
                        ret = m2.invoke(b, new Object[]{}).toString()
                                .compareTo(m1.invoke(a, new Object[]{}).toString());
                    } else {
                        ret = m1.invoke(a, new Object[]{}).toString()
                                .compareTo(m2.invoke(b, new Object[]{}).toString());
                    }
                } catch (NoSuchMethodException ne) {
                    ne.printStackTrace();
                } catch (IllegalAccessException ie) {
                    ie.printStackTrace();
                } catch (InvocationTargetException it) {
                    it.printStackTrace();
                }
                return ret;
            }
        });
    }
}
