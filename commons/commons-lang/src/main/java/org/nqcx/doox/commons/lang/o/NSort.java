/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.commons.lang.o;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author naqichuan 2014年8月14日 上午11:03:31
 */
public class NSort implements Serializable {

    public static final NDirection DEFAULT_DIRECTION = NDirection.ASC;
    private static final NSort UNSORTED = NSort.by(new NOrder[0]);
    private final List<NOrder> orders;

    public NSort(List<NOrder> _orders) {
        if (_orders == null)
            throw new IllegalArgumentException("Orders must not be null!");
        this.orders = _orders;
    }

    public Iterator<NOrder> iterator() {
        return this.orders.iterator();
    }

    public String orderString() {
        StringBuilder s = new StringBuilder();
        for (Iterator<NOrder> it = iterator(); it != null && it.hasNext(); ) {
            NOrder o = it.next();
            if (o == null)
                continue;

            if (s.length() > 0)
                s.append(", ");

            s.append(o.field).append(" ").append(o.getDirection());
        }
        return s.toString();
    }

    public static NSort by(String... fields) {
        return fields.length == 0 ? NSort.unsorted() : NSort.by(DEFAULT_DIRECTION, fields);
    }

    public static NSort by(NOrder... orders) {
        return orders.length == 0 ? NSort.unsorted() : new NSort(Arrays.asList(orders));
    }

    public static NSort by(List<NOrder> orders) {
        return orders.isEmpty() ? NSort.unsorted() : new NSort(orders);
    }

    public static NSort by(NDirection direction, String... fields) {
        return NSort.by(Arrays.stream(fields)//
                .map(it -> new NOrder(direction, it))//
                .collect(Collectors.toList()));
    }

    public static NSort unsorted() {
        return UNSORTED;
    }

    public static enum NDirection {
        ASC, DESC;

        public boolean isAscending() {
            return this.equals(ASC);
        }

        public boolean isDescending() {
            return this.equals(DESC);
        }

        public static NDirection of(String value) {
            try {
                return NDirection.valueOf(value.toUpperCase());
            } catch (Exception e) {
                throw new IllegalArgumentException("unknown value:" + value);
            }
        }
    }

    public static class NOrder implements Serializable {

        private final NDirection direction;
        private final String field;

        public NOrder(NDirection _direction, String _field) {
            if (_field == null || _field.length() == 0)
                throw new IllegalArgumentException("Field must not null or empty!");

            this.direction = _direction == null ? DEFAULT_DIRECTION : _direction;
            this.field = _field;
        }

        public NOrder by(String field) {
            return new NOrder(DEFAULT_DIRECTION, field);
        }

        public NOrder asc(String field) {
            return new NOrder(NDirection.ASC, field);
        }

        public NOrder desc(String field) {
            return new NOrder(NDirection.DESC, field);
        }

        public NDirection getDirection() {
            return direction;
        }

        public String getField() {
            return field;
        }
    }

    @Override
    public String toString() {
        return "NSort{" +
                "orders=" + orders +
                '}';
    }

    public static void main(String[] args) {
        NSort nSort = NSort.by(new NOrder(NDirection.DESC, "id"), new NOrder(NDirection.ASC, "cd"));
        StringBuilder s = new StringBuilder();
        for (Iterator<NOrder> it = nSort.iterator(); it.hasNext(); ) {
            System.out.println(it.next());
        }

        System.out.println(nSort.orderString());

//        System.out.println(new NSort().putField("0", "id").putField("1", "id1").putField("2", "id2").setSort("-1,-2").getOrder());
    }
}
