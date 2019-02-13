/*
 * Copyright 2019 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.data.converter;

import org.nqcx.doox.commons.lang.o.DTO;
import org.nqcx.doox.commons.lang.o.INPage;
import org.nqcx.doox.commons.lang.o.NPage;
import org.nqcx.doox.commons.lang.o.NSort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author naqichuan 2018/9/20 17:52
 */
public class DTOConverter {

    /**
     * to DTO
     *
     * @param page page
     * @param <T>  t
     * @return DTO
     */
    public static <T> DTO toDto(Page<T> page) {
        Assert.notNull(page, "page must be not null.");

        return new DTO(true)
                .setPage(new NPage(page.getNumber() + 1, page.getSize()).setTotalCount(page.getTotalElements()))
                .setList(page.getContent());
    }

    /**
     * to PageRequest
     *
     * @param dto dto
     * @return PageRequest
     */
    public static PageRequest toPageRequest(DTO dto) {
        Assert.notNull(dto, "page must be not null.");

        return toPageRequest(dto.getPage(), dto.getSort());
    }

    /**
     * to PageRequest
     *
     * @param nPage NPage
     * @param nSort NSort
     * @return PageRequest
     */
    public static PageRequest toPageRequest(INPage nPage, NSort nSort) {
        if (nPage == null)
            nPage = new NPage();

        return PageRequest.of((int) nPage.getPage() - 1, (int) nPage.getPageSize(), toSort(nSort));
    }

    /**
     * to PageRequest
     *
     * @param nPage NPage
     * @return PageRequest
     */
    public static PageRequest toPageRequest(INPage nPage) {
        return toPageRequest(nPage, null);
    }

    /**
     * to Sort
     *
     * @param nSort NSort
     * @return Sort
     */
    public static Sort toSort(NSort nSort) {
        List<Sort.Order> list = new ArrayList<>();
        if (nSort != null) {
            for (Iterator<NSort.NOrder> it = nSort.iterator(); it.hasNext(); ) {
                NSort.NOrder o = it.next();
                list.add(new Sort.Order(Sort.Direction.fromString(o.getDirection().toString()), o.getField()));
            }
        }

        return Sort.by(list);
    }
}
