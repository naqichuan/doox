/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.commons.service;

import org.nqcx.doox.commons.dao.IDAO;
import org.nqcx.doox.commons.lang.o.DTO;
import org.nqcx.doox.commons.util.orika.Orika;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * @author naqichuan 2014年8月14日 上午10:48:24
 */
public abstract class ServiceSupport<DAO extends IDAO<PO, ID>, DO, PO, ID> implements IService<DO, PO, ID> {

    protected final Class<DO> classdo;
    protected final Class<PO> classpo;

    protected final DAO dao;

    public ServiceSupport(DAO dao) {
        Type t = getClass().getGenericSuperclass();
        if (t instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) t).getActualTypeArguments();

            this.classdo = (Class<DO>) types[1];
            this.classpo = (Class<PO>) types[2];
        } else
            throw new RuntimeException("Class not found");

        this.dao = dao;
    }

    @Override
    public PO save(PO po) {
        try {
            return dao.save(beforeSave(po));
        } catch (Exception e) {
            throw new ServiceException("ServiceSupport save error", e);
        }
    }

    @Override
    public List<PO> saveAll(List<PO> pos) {
        try {
            return dao.saveAll(beforeSave(pos));
        } catch (Exception e) {
            throw new ServiceException("ServiceSupport saveAll error", e);
        }
    }

    @Override
    @SafeVarargs
    public final List<PO> saveAll(PO... pos) {
        if (pos == null)
            return null;

        return saveAll(Arrays.asList(pos));
    }

    /**
     * 保存之前处理
     *
     * @param po po
     * @return PO
     */
    protected PO beforeSave(PO po) {
        return po;
    }

    /**
     * 保存之前处理
     *
     * @param pos pos
     * @return List
     */
    protected List<PO> beforeSave(List<PO> pos) {
        if (pos != null && pos.size() > 0)
            pos.forEach(this::beforeSave);
        return pos;
    }

    @Override
    public PO modify(PO po) {
        try {
            return dao.modify(beforeModify(po));
        } catch (Exception e) {
            throw new ServiceException("ServiceSupport save error", e);
        }
    }

    @Override
    public List<PO> modifyAll(List<PO> pos) {
        try {
            return dao.modifyAll(beforeModify(pos));
        } catch (Exception e) {
            throw new ServiceException("ServiceSupport modifyAll error", e);
        }
    }

    @Override
    @SafeVarargs
    public final List<PO> modifyAll(PO... pos) {
        if (pos == null)
            return null;

        return modifyAll(Arrays.asList(pos));
    }

    /**
     * 保存之前处理
     *
     * @param po po
     * @return PO
     */
    protected PO beforeModify(PO po) {
        return po;
    }

    /**
     * 保存之前处理
     *
     * @param pos pos
     * @return List
     */
    protected List<PO> beforeModify(List<PO> pos) {
        if (pos != null && pos.size() > 0)
            pos.forEach(this::beforeModify);
        return pos;
    }

    @Override
    public DO findById(ID id) {
        try {
            PO po = dao.findById(id);
            if (classdo == classpo)
                return afterFoud((DO) po);
            return afterFoud(Orika.o2o(dao.findById(id), classdo));
        } catch (Exception e) {
            throw new ServiceException("ServiceSupport findById error", e);
        }
    }

    @Override
    public List<DO> findAllByIds(List<ID> ids) {
        try {
            List<PO> list = dao.findAllByIds(ids);
            if (classdo == classpo)
                return (List<DO>) list;
            return afterFoud(Orika.l2l(list, classdo));
        } catch (Exception e) {
            throw new ServiceException("ServiceSupport findAllByIds error", e);
        }
    }

    @Override
    @SafeVarargs
    public final List<DO> findAllByIds(ID... ids) {
        if (ids == null)
            return null;

        return findAllByIds(Arrays.asList(ids));
    }

    /**
     * 查询数据列表后处理
     *
     * @param pos pos
     * @return pos
     */
    protected List<DO> afterFoud(List<DO> pos) {
        if (pos != null && pos.size() > 0)
            pos.forEach(this::afterFoud);
        return pos;
    }

    @Override
    public List<DO> listAll(DTO dto) {
        try {
            List<PO> list = dao.listAll(dto);
            if (classdo == classpo)
                return (List<DO>) list;
            return afterFoud(Orika.l2l(list, classdo));
        } catch (Exception e) {
            throw new ServiceException("ServiceSupport listAll error", e);
        }
    }

    @Override
    public DTO findAll(DTO dto) {
        if (dto == null)
            throw new ServiceException("Parameter dto can't be null");

        try {
            DTO result = dao.findAll(dto);
            if (result == null)
                result = new DTO();

            if (result.isSuccess() && result.getList().size() > 0 && classdo != classpo)
                result.setList(Orika.l2l(result.getList(), classdo));

            return result.setSuccess(true).setList(afterFoud(result.getList()));
        } catch (Exception e) {
            throw new ServiceException("ServiceSupport findAll error", e);
        }
    }

    /**
     * 查询一条数据后处理
     *
     * @param do_ do_
     * @return DO
     */
    protected DO afterFoud(DO do_) {
        return do_;
    }

    @Override
    public long getCount(DTO dto) {
        if (dto == null)
            throw new ServiceException("Parameter dto can't be null");

        try {
            return dao.getCount(dto);
        } catch (Exception e) {
            throw new ServiceException("ServiceSupport getCount error", e);
        }
    }

    @Override
    public void deleteById(ID id) {
        try {
            dao.deleteById(id);
        } catch (Exception e) {
            throw new ServiceException("ServiceSupport deleteById error", e);
        }
    }

    @Override
    public void deleteByIds(List<ID> ids) {
        try {
            dao.deleteByIds(ids);
        } catch (Exception e) {
            throw new ServiceException("ServiceSupport deleteByIds error", e);
        }
    }

    @Override
    @SafeVarargs
    public final void deleteByIds(ID... ids) {
        if (ids == null)
            return;

        this.deleteByIds(Arrays.asList(ids));
    }
}
