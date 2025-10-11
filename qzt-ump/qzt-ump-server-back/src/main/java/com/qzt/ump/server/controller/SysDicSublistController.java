package com.qzt.ump.server.controller;

import com.qzt.common.core.model.PageModel;
import com.qzt.common.web.BaseController;
import com.qzt.common.web.model.ResultModel;
import com.qzt.common.web.util.ResultUtil;
import com.qzt.ump.model.SysDicSublist;
import com.qzt.ump.model.SysDicSublist;
import com.qzt.ump.rpc.api.SysDicSublistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Xiaofei
 * @since 2018-09-06
 */

@RestController
@RequestMapping("/back/sysDicSublist")
public class SysDicSublistController extends BaseController {
    @Autowired
    private SysDicSublistService service;

    /**
    * 根据ID查询
    *
    * @param id ID
    * @return ResultModel
    * @author Xiaofei
    * @date 2018-09-06
    */
    @GetMapping("/query/{id}")
    public ResultModel query(@PathVariable Long id) {
           // Assert.notNull(id);
            SysDicSublist entity = service.selectById(id);
            return ResultUtil.ok(entity);
    }

    /**
    * 查询分页方法
    *
    * @param pageModel 分页实体
    * @return ResultModel
    * @author Xiaofei
    * @date 2018-09-06
    */
    @PostMapping("/queryListPage")
    public ResultModel queryListPage(@RequestBody PageModel<SysDicSublist> pageModel) {
        pageModel = (PageModel<SysDicSublist>) service.find(pageModel);
        return ResultUtil.ok(pageModel);
    }

    /**
     * 新增方法
     *
     * @param entity 实体
     * @return ResultModel
     * @author Xiaofei
     * @date 2018-09-06
     */
    @PostMapping("/add")
    public ResultModel add(@Valid @RequestBody SysDicSublist entity) {
        if (entity != null) {
        entity.setCreateBy(super.getCurrentUserId());
        entity.setUpdateBy(this.getCurrentUserId());
        }
        return ResultUtil.ok(service.add(entity));
    }

    /**
     * 修改方法
     *
     * @param entity 实体
     * @return ResultModel
     * @author Xiaofei
     * @date 2018-09-06
     */
    @PostMapping("/modify")
    public ResultModel modify(@RequestBody SysDicSublist entity) {
        entity.setUpdateBy(this.getCurrentUserId());
        entity.setUpdateTime(new Date());
        service.modifyById(entity);
        return ResultUtil.ok();
    }

    /**
     * 批量删除方法
     *
     * @param ids ID集合
     * @return ResultModel
     * @author Xiaofei
     * @date 2018-09-06
     */
    @PostMapping("/delBatchByIds")
    public ResultModel delBatchByIds(@RequestBody Long[] ids) {
       // Assert.notNull(ids);
        return ResultUtil.ok(service.deleteBatchIds(Arrays.asList(ids)));
    }

    /**
     * 根据字典类型查询子表数据
     * @param dicType
     * @return
     */
    @GetMapping("/selectByDicType")
    public ResultModel selectByDicType(String dicType) {
        List<Map> alliance_store_type = service.selectDicListByDicType(dicType);
        return ResultUtil.ok(alliance_store_type);
    }
}

