/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.commons.service;

/**
 * @author naqichuan 2014年8月14日 上午10:47:49
 */
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 2985737702436895518L;

    public ServiceException(){
        super();
    }

    public ServiceException(String msg){
        super(msg);
    }

    public ServiceException(Exception e){
        super(e);
    }

    public ServiceException(String msg, Exception e){
        super(msg, e);
    }

    public ServiceException(Throwable cause){
        super(cause);
    }

}
