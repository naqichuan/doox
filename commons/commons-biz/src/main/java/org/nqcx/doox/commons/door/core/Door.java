/*
 * Copyright 2019 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.door.core;

import org.nqcx.doox.commons.lang.o.DTO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任意门
 *
 * @author naqichuan 17/8/18 11:21
 */
public class Door {

    // 用于注册 door
    private final static Map<String, IDoor> DOOR_MAP = new HashMap<>();

    /**
     * 用于 door 注册
     *
     * @param doorMap map
     */
    @Autowired(required = false)
    public void setDoorMap(Map<String, IDoor> doorMap) {
        DOOR_MAP.putAll(doorMap);
    }

    /**
     * list door
     *
     * @return map
     */
    public static List<String> list() {
        return new ArrayList<>(DOOR_MAP.keySet());
    }

    /**
     * get door
     *
     * @param door code
     * @return door
     */
    public static IDoor get(String door) {
        return DOOR_MAP.get(door);
    }

    /**
     * 开门
     *
     * @param door door
     * @param dto  dto
     * @return dto
     */
    public static DTO open(String door, DTO dto) {
        DTO result = new DTO(false);
        IDoor d = get(door);
        if (d == null)
            return result.setSuccess(false).putResult("11", "任意门不存在");

        DTO doorParam = new DTO();
        if (dto != null) {
            doorParam.setParamsMap(dto.getParamsMap());
            doorParam.setPage(dto.getPage());
            doorParam.setSort(dto.getSort());
        }

        DTO doorResult = d.open(doorParam);
        if (doorResult == null)
            return result.setSuccess(false).putResult("13", "任意门错误");

        return result.setSuccess(doorResult.isSuccess())
                .setObject(doorResult.getObject())
                .setList(doorResult.getList())
                .setResultMap(doorResult.getResultMap())
                .setPage(doorResult.getPage());
    }
}
