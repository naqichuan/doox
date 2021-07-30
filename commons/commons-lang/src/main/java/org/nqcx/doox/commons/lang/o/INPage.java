/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.commons.lang.o;

/**
 * Page interface object
 *
 * @author naqichuan 2014年8月14日 上午11:03:21
 */
public interface INPage {

    /**
     * @param totalCount 总记录数
     * @return 自身
     */
    INPage setTotalCount(long totalCount);

    /**
     * @param page 页数
     * @return 自身
     */
    INPage setPage(long page);

    /**
     * @param pageSize 每页数据条数
     * @return 自身
     */
    INPage setPageSize(long pageSize);

    /**
     * @param offset offset 起始偏移值
     * @return {@link INPage}
     * @author naqichuan 7/30/21 1:57 PM
     */
    INPage setOffset(long offset);

    /**
     * setShowPage
     *
     * @param showPage showPage
     * @return {@link INPage}
     * @author naqichuan 7/30/21 1:57 PM
     */
    INPage setShowPage(long showPage);

    /**
     * 取得记录总数
     *
     * @return 返回 long 型的记录总数
     */
    long getTotalCount();

    /**
     * 取得记录分页后的总页数
     *
     * @return 总页数
     */
    long getTotalPage();

    /**
     * 取得当前页
     *
     * @return 当前页
     */
    long getPage();

    /**
     * 取得每页显示记录条数
     *
     * @return 每页记录数
     */
    long getPageSize();

    /**
     * 取得起始偏移值
     *
     * @return long
     */
    long getOffset();

    /**
     * 取得记录起始位置，在 SQL 中调用 从0开始
     *
     * @return 开始位置
     */
    long getStartIndex();

    /**
     * 取得记录的结束位置，要 SQL 中调用
     *
     * @return 结束位置
     */
    long getEndIndex();

    /**
     * 取得每页显示分页页数
     *
     * @return long
     */
    long getShowPage();

    /**
     * 取得分页数组
     *
     * @return long [][]
     */
    long[][] getShowArray();

    /**
     * 获取上一页页码
     *
     * @return long
     */
    long getPrevPage();

    /**
     * 获取下一页页码
     *
     * @return long
     */
    long getNextPage();
}
