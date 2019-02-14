/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.tree;

import java.util.StringJoiner;

/**
 * @author naqichuan Dec 15, 2013
 */
public class TreeTest {

    public static void main(String[] args) {

        TreeBuilder<Integer, MyO> t = new TreeBuilder<>();

        MyO n1 = new MyO();
        n1.setId(1);
        n1.setPid(0);
        n1.setJxx("ni hao kcd1");

        t.put(n1);

        MyO n2 = new MyO();
        n2.setId(2);
        n2.setPid(1);
        n2.setJxx("ni hao kcd2");

        t.put(n2);

        MyO n3 = new MyO();
        n3.setId(3);
        n3.setPid(2);
        n3.setJxx("ni hao kcd3");

        t.put(n3);

        System.out.println(t.get(1));//
        System.out.println(t.get(1).get().getJxx());//

        System.out.println(t.getNode(2));
        System.out.println(t.getNode(2).get().getObject());
        System.out.println(t.getNode(2).get().getObject().get().getJxx());
        System.out.println(t.getNode(2).get().hasParent());
        System.out.println(t.getNode(2).get().getParent());
        System.out.println(t.getNode(2).get().getParent().get().getJxx());
        System.out.println(t.getNode(2).get().hasChilds());
        System.out.println(t.getNode(2).get().getChilds());
        System.out.println(t.getNode(2).get().getChilds().size());
    }

    static class MyO implements INode<Integer> {

        private int id;
        private int pid;

        private String jxx;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        @Override
        public Integer getNodeId() {
            return getId();
        }

        @Override
        public Integer getParentNodeId() {
            return getPid();
        }

        public String getJxx() {
            return jxx;
        }

        public void setJxx(String jxx) {
            this.jxx = jxx;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", MyO.class.getSimpleName() + " {", "}")
                    .add("\"id\": " + id)
                    .add("\"pid\": " + pid)
                    .add("\"jxx\": \"" + jxx + "\"")
                    .toString();
        }
    }
}
