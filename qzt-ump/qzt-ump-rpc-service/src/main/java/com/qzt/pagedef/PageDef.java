package com.qzt.pagedef;

import com.github.pagehelper.PageInfo;
import com.baomidou.mybatisplus.plugins.Page;
/**
 * Created by cgw on 2018/5/31.
 */
public final class PageDef {

    public static Page defPage(PageInfo pageInfos, Page page){
            page.setTotal(Integer.parseInt(pageInfos.getTotal()+""));
            page.setRecords(pageInfos.getList());
            page.setSize(pageInfos.getPageSize());
            page.setCurrent(pageInfos.getPageNum());
            return page;
    }
}
