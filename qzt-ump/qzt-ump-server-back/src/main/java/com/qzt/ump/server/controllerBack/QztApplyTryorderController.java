package com.qzt.ump.server.controllerBack;

import com.qzt.bus.model.QztBusLog;
import com.qzt.bus.model.QztBusPic;
import com.qzt.bus.rpc.api.IQztBusLogService;
import com.qzt.bus.rpc.api.IQztBusPicService;
import com.qzt.common.core.constant.Constant;
import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.core.model.PageModel;
import com.qzt.common.redis.util.DicParamUtil;
import com.qzt.common.web.model.ResultModel;
import com.qzt.common.web.util.ResultUtil;
import com.qzt.common.web.util.ReturnUtilServer;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import com.qzt.common.web.BaseController;
import com.qzt.bus.model.QztApplyTryorder;
import com.qzt.bus.rpc.api.IQztApplyTryorderService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import java.util.*;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Cgw
 * @since 2019-11-11
 */
@RestController
@RequestMapping("/back/qztApplyTryorder")
@Api(value = "QztApplyTryorderController", description = "QztApplyTryorderController")
public class QztApplyTryorderController extends BaseController {

    @Autowired
    private IQztApplyTryorderService service;

    @Autowired
    private IQztBusLogService qztBusLogService;

    @Autowired
    private IQztBusPicService qztBusPicService;

    @Autowired
    private ReturnUtilServer returnUtil;

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return ResultModel
     * @author Cgw
     * @date 2019-11-11
     */
    @GetMapping("/query/{id}")
    public Map query(@PathVariable Long id) {
        try {
            Map remap = new HashMap();
            QztApplyTryorder qztApplyTryorder = service.selectById(id);
            Map applyTryorderMess = new HashMap();
            applyTryorderMess.put("orderState", qztApplyTryorder.getOrderState());
            applyTryorderMess.put("orderStateMc", DicParamUtil.getDicCodeByType("TRY_STATE", qztApplyTryorder.getOrderState()));
            applyTryorderMess.put("userId", qztApplyTryorder.getApplyUserId());//申请人
            applyTryorderMess.put("storeName", qztApplyTryorder.getStoreName());//联系电话
            applyTryorderMess.put("diseaseName", qztApplyTryorder.getDiseaseName());//疾病类型
            applyTryorderMess.put("busName", qztApplyTryorder.getBusName());//所属服务站
            applyTryorderMess.put("idCardno", qztApplyTryorder.getIdCardno());//身份证号
            applyTryorderMess.put("orderNo", qztApplyTryorder.getOrderNo());//订单编号
            applyTryorderMess.put("createTime", qztApplyTryorder.getCreateTime());//申请时间
            applyTryorderMess.put("updateTime", "07".equals(qztApplyTryorder.getOrderState()) ? qztApplyTryorder.getUpdateTime() : "");//完成时间
            //获得图片信息
            Map params = new HashMap();
            params.put("busId", qztApplyTryorder.getId());
            params.put("busType", "01");
            params.put("state", "0");
            List<QztBusPic> beforeTrialPic = this.qztBusPicService.getBusPics(params);//获得试用前图片信息
            params.put("busType", "02");
            List<QztBusPic> afterTrialPic = this.qztBusPicService.getBusPics(params);//获得试用后图片信息

            //查询试用订单日志
            List<QztBusLog> orderLogs = this.qztBusLogService.findByBusinessId("07", qztApplyTryorder.getOrderNo());
            remap.put("orderLogs", orderLogs);//订单日志
            remap.put("applyTryorderMess", applyTryorderMess);//订单信息
            remap.put("beforeTrialPic", beforeTrialPic);//试用前图
            remap.put("afterTrialPic", afterTrialPic);//试用后图
            return returnUtil.returnMessMap(remap);
        } catch (Exception e) {
            e.printStackTrace();
            return this.returnUtil.returnMess(Constant.DATA_ERROR);
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
    public ResultModel queryListPage(@RequestBody PageModel<QztApplyTryorder> pageModel) {
        try {
            pageModel = (PageModel<QztApplyTryorder>) service.find(pageModel);
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
    public ResultModel add(@Valid @RequestBody QztApplyTryorder entity) {
        try {
            entity.setCreateBy(super.getCurrentUserId());
            entity.setUpdateBy(this.getCurrentUserId());
            QztApplyTryorder entityback = service.add(entity);
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
    public ResultModel modify(@RequestBody QztApplyTryorder entity) {
        try {
            entity.setUpdateBy(this.getCurrentUserId());
            entity.setUpdateTime(new Date());
            QztApplyTryorder entityback = service.modifyById(entity);
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
}

