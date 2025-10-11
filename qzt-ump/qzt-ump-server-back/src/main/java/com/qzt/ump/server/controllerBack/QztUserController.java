package com.qzt.ump.server.controllerBack;

import com.qzt.bus.rpc.api.IQztUserRegService;
import com.qzt.common.core.constant.Constant;
import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.core.model.PageModel;
import com.qzt.common.web.model.ResultModel;
import com.qzt.common.web.util.ResultUtil;
import com.qzt.common.web.util.ReturnUtilServer;
import com.qzt.ump.server.annotation.SysLogOpt;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import com.qzt.common.web.BaseController;
import com.qzt.bus.model.QztUser;
import com.qzt.bus.rpc.api.IQztUserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Cgw
 * @since 2019-11-11
 */
@RestController
@RequestMapping("/back/qztUser")
@Api(value = "QztUserController", description = "QztUserController")
public class QztUserController extends BaseController {

    @Autowired
    private ReturnUtilServer returnUtil;

    @Autowired
    private IQztUserService service;

    @Autowired
    private IQztUserRegService qztUserRegService;

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return ResultModel
     * @author Cgw
     * @date 2019-11-11
     */
    @GetMapping("/query/{id}")
    public ResultModel query(@PathVariable Long id) {
        try {
            QztUser entity = service.findDGUserById(id);
            if (entity == null) {
                return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
            }
            return ResultUtil.ok(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    @PostMapping("/blacklist")
    public ResultModel blacklist(Long userId, String state) {
        Map<String, Object> map = new HashMap();
        try {
            map.put("id", userId);
            map.put("state", state);
            boolean b = service.updateUserById(map);
            if (b) {
                return ResultUtil.ok(true);
            }
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    /**
     * 查询分页方法
     *
     * @param pageModel 分页实体
     * @return ResultModel
     * @author Cgw
     * @date 2019-11-11
     */
    @PostMapping("/queryListPage")
    public ResultModel queryListPage(@RequestBody PageModel<QztUser> pageModel) {
        try {
            pageModel = (PageModel<QztUser>) service.findBack(pageModel);
            return ResultUtil.ok(pageModel);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    /**
     * 新增方法
     *
     * @param entity 实体
     * @return ResultModel
     * @author Cgw
     * @date 2019-11-11
     */
    @PostMapping("/add")
    public ResultModel add(@Valid @RequestBody QztUser entity) {
        try {
            entity.setCreateBy(super.getCurrentUserId());
            entity.setUpdateBy(this.getCurrentUserId());
            QztUser entityback = service.add(entity);
            if (entityback == null) {
                return ResultUtil.fail(SysConstant.ResultCodeEnum.INCREASE_THE_FAILURE);
            } else {
                return ResultUtil.ok();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    /**
     * 修改方法
     *
     * @param entity 实体
     * @return ResultModel
     * @author Cgw
     * @date 2019-11-11
     */
    @PostMapping("/modify")
    public ResultModel modify(@RequestBody QztUser entity) {
        try {
            entity.setUpdateBy(this.getCurrentUserId());
            entity.setUpdateTime(new Date());
            QztUser entityback = service.modifyById(entity);
            if (entityback == null) {
                return ResultUtil.fail(SysConstant.ResultCodeEnum.INCREASE_THE_FAILURE);
            } else {
                return ResultUtil.ok();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    /**
     * 批量删除方法
     *
     * @param ids ID集合
     * @return ResultModel
     * @author Cgw
     * @date 2019-11-11
     */
    @PostMapping("/delBatchByIds")
    public ResultModel delBatchByIds(@RequestBody Long[] ids) {
        try {
            return ResultUtil.ok(service.deleteBatchIds(Arrays.asList(ids)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    /**
     * 修改用户身份
     *
     * @param emap
     * @return com.qzt.common.web.model.ResultModel
     * @author Xiaofei
     * @date 2019-12-03
     */
    @SysLogOpt(module = "修改用户身份接口", value = "修改用户身份", operationType = SysConstant.LogOptEnum.MODIFY)
    @PostMapping("/updateUserType")
    public ResultModel updateUserType(@RequestBody Map emap) {
        try {
            Date date = new Date();
            emap.put("updateBy", this.getCurrentUserId());
            emap.put("updateTime", date);
            Integer result = this.service.updateUserType(emap);
            if (result != 1) {
                return ResultUtil.fail(SysConstant.ResultCodeEnum.INCREASE_THE_FAILURE);
            } else {
                this.service.userUpgrade(Long.valueOf(emap.get("userId").toString()));
                return ResultUtil.ok();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    /**
     * 添加服务站推荐人
     *
     * @param emap
     * @return com.qzt.common.web.model.ResultModel
     * @author Xiaofei
     * @date 2019-12-11
     */
    @SysLogOpt(module = "添加服务站推荐人", value = "添加服务站推荐人", operationType = SysConstant.LogOptEnum.ADD)
    @PostMapping("/addSuperior")
    public ResultModel addSuperior(@RequestBody Map emap) {
        try {
            Date date = new Date();
            emap.put("updateBy", this.getCurrentUserId());
            emap.put("updateTime", date);
            String result = this.service.addSuperior(emap);
            if (!"200".equals(result)) {
                return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY, result);
            }
            return ResultUtil.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY, e.getMessage());
        }
    }

    /**
     * 关系测试
     *
     * @author
     * @date
     */
//    @GetMapping("/test")
    public Map<String, Object> test(Long userId, Long referrerSecond) {
        try {
//            this.service.test(userId, referrerSecond);
            return returnUtil.returnMess(Constant.RESULT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

}

