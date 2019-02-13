/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.commons.web.controller;

import com.alibaba.fastjson.JSON;
import org.nqcx.doox.commons.lang.o.DTO;
import org.nqcx.doox.commons.lang.url.UrlBuilder;
import org.nqcx.doox.commons.web.WebSupport;
import org.nqcx.doox.commons.web.result.NqcxResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author naqichuan 2014年8月14日 上午11:50:15
 */
@Controller
@RequestMapping("/r")
public class ResultController extends WebSupport {

    private NqcxResult rs;

    private final UrlBuilder homeUrl;

    @Autowired(required = false)
    public ResultController(@Qualifier("_homeUrl") UrlBuilder homeUrl) {
        this.homeUrl = homeUrl;
    }

    @RequestMapping(value = "/e", method = {RequestMethod.GET, RequestMethod.POST})
    public String error(String url, Model model, HttpServletResponse response) {
        return error("0", url, model, response);
    }

    @RequestMapping(value = "/e/{code}", method = {RequestMethod.GET, RequestMethod.POST})
    public String error(@PathVariable String code, String url, Model model, HttpServletResponse response) {
        return result("error", code, url, model, response);
    }

    @RequestMapping(value = "/m", method = {RequestMethod.GET, RequestMethod.POST})
    public String msg(String url, Model model, HttpServletResponse response) throws Exception {
        return msg("0", url, model, response);
    }

    @RequestMapping(value = "/m/{code}", method = {RequestMethod.GET, RequestMethod.POST})
    public String msg(@PathVariable String code, String url, Model model, HttpServletResponse response) throws Exception {
        return result("msg", code, url, model, response);
    }

    @RequestMapping(value = "/s", method = {RequestMethod.GET, RequestMethod.POST})
    public String success(String url, Model model, HttpServletResponse response) throws Exception {
        return success("0", url, model, response);
    }

    @RequestMapping(value = "/s/{code}", method = {RequestMethod.GET, RequestMethod.POST})
    public String success(@PathVariable String code, String url, Model model, HttpServletResponse response) throws Exception {
        return result("success", code, url, model, response);
    }

    private String result(String type, String code, String url, Model model, HttpServletResponse response) {
        if (this.rs == null)
            this.rs = new NqcxResult();

        if (this.isAjax()) {
            String result = null;
            if (type.endsWith("error"))
                result = JSON.toJSONString(this.buildResult(new DTO(false).putResult(code, code)));
            else
                result = JSON.toJSONString(this.buildResult(new DTO(true).setObject(super.getPropertyValue(code))));

            super.responseJsonResult(response, result);

            return null;
        }

        BeanUtils.copyProperties(getResult(type, code), rs);

        if (homeUrl != null)
            rs.setIndex(homeUrl.forPath().build());

        if (url != null && url.length() > 0)
            rs.setUrl(url);

        model.addAttribute("type", rs.getType());
        model.addAttribute("title", getPropertyValue(rs.getTitle(), null));
        model.addAttribute("subject", getPropertyValue(rs.getSubject(), null));

        if (rs.getSuggestTitle() != null)
            model.addAttribute("suggestTitle", getPropertyValue(rs.getSuggestTitle(), null));
        else
            model.addAttribute("suggestTitle", "");

        if (rs.getSuggest() != null && rs.getSuggest().size() > 0)
            model.addAttribute("suggest", getValue(rs.getSuggest()));
        else
            model.addAttribute("suggest", null);

        model.addAttribute("auto", getPropertyValue(rs.getAuto(), null));
        model.addAttribute("index", rs.getIndex());
        model.addAttribute("url", rs.getUrl());

        return "msg";
    }

    /**
     * @param list
     * @return
     * @author naqichuan Dec 24, 2013 9:37:54 PM
     */
    private Object getValue(List<String> list) {
        List<String> rl = new ArrayList<String>();
        for (String code : list) {
            rl.add((String) getPropertyValue(code, null));
        }
        return rl;
    }

    public NqcxResult getResult() {
        return rs;
    }

    public void setResult(NqcxResult nqcxResult) {
        this.rs = nqcxResult;
    }
}
