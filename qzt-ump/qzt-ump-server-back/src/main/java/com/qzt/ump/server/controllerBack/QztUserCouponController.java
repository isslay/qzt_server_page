package com.qzt.ump.server.controllerBack;

import com.qzt.bus.model.*;
import com.qzt.bus.rpc.api.*;
import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.core.model.PageModel;
import com.qzt.common.tools.DateTime;
import com.qzt.common.web.model.ResultModel;
import com.qzt.common.web.util.ResultUtil;
import io.swagger.annotations.Api;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import com.qzt.common.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
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
@RequestMapping("/back/qztUserCoupon")
@Api(value = "QztUserCouponController", description = "QztUserCouponController")
public class QztUserCouponController extends BaseController {

    @Autowired
    private IQztUserCouponService service;
    @Autowired
    private IDgmGoodsCouponService iDgmGoodsCouponService;
    @Autowired
    private IQztUserService iQztUserService;
    @Autowired
    private IDgmCouponService iDgmCouponService;
    @Autowired
    private IVErpOrderDetailService ivErpOrderDetailService;

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
            QztUserCoupon entity = service.selectById(id);
            return ResultUtil.ok(entity);
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
    public ResultModel queryListPage(@RequestBody PageModel<QztUserCoupon> pageModel) {
        try {
            pageModel = (PageModel<QztUserCoupon>) service.find(pageModel);
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
    public ResultModel add(@Valid @RequestBody QztUserCoupon entity) {
        try {
            entity.setCreateBy(super.getCurrentUserId());
            entity.setUpdateBy(this.getCurrentUserId());
            QztUserCoupon entityback = service.add(entity);
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
    public ResultModel modify(@RequestBody QztUserCoupon entity) {
        try {
            entity.setUpdateBy(this.getCurrentUserId());
            entity.setUpdateTime(new Date());
            QztUserCoupon entityback = service.modifyById(entity);
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

    @Scheduled(cron = "00 30 00  * * ?")// 00 01 00  * * ? 每天零点30分执行将过期券失效
    public void createDeductionMoney() {
        Map<String, Object> params = new HashMap<>();
        System.out.println("start ActiveShareMoney " + DateTime.getCurDate_yyyyMMddHHmmssRe());
        params.put("validTime", DateTime.getCurDate_yyyy_MM_dd());
        System.out.println("end ActiveShareMoney " + DateTime.getCurDate_yyyyMMddHHmmssRe());
        service.updatePassCoupon(params);
    }

    @Scheduled(cron = "00 00 01  * * ?")// 00 00 01  * * ? 每天1点执行 查询ERP商品
    public void checkUserGoodsCoupon() {
        Map<String, Object> map = new HashMap<>();
        map.put("status", 0);
        map.put("couponType", 3);
        List<DgmCoupon> rb = this.iDgmCouponService.findList(map);
        if (rb.size() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String ss = sdf.format(new Date());
            for (DgmCoupon dgmCoupon : rb) {
                Map<String, Object> map1 = new HashMap<>();
                map1.put("goodsId", dgmCoupon.getTargetId());
                map1.put("couponDate", ss);
                List<VErpOrderDetail> list = this.ivErpOrderDetailService.findList(map1);
                for (VErpOrderDetail erp : list) {
                    DgmGoodsCoupon dgmGoodsCoupon = new DgmGoodsCoupon();
                    dgmGoodsCoupon.setBillNo(erp.getBillNo());
                    dgmGoodsCoupon.setBillSn(erp.getBillSn());
                    dgmGoodsCoupon.setGoodsId(dgmCoupon.getGoodsId()+"");
                    dgmGoodsCoupon.setUserName(erp.getContact());
                    dgmGoodsCoupon.setUserMobile(erp.getMobile());
                    dgmGoodsCoupon.setCouponId(dgmCoupon.getId());
                    dgmGoodsCoupon.setGcStatus("0");
                    this.iDgmGoodsCouponService.add(dgmGoodsCoupon);
                }
            }
        }
    }

    @Scheduled(cron = "00 00 02  * * ?")// 00 00 02  * * ? 每天2点执行 用户创建商品优惠券
    public void createUserGoodsCoupon() {
        Map<String, Object> params = new HashMap<>();
        params.put("gcStatus", "0");
        List<DgmGoodsCoupon> list = this.iDgmGoodsCouponService.findList(params);
        for (DgmGoodsCoupon dgmGoodsCoupon : list) {
            if (dgmGoodsCoupon.getUserName() != null && dgmGoodsCoupon.getUserMobile() != null) {
                QztUser qztUser = this.iQztUserService.findUserByMobileName(dgmGoodsCoupon.getUserMobile(), dgmGoodsCoupon.getUserName());
                if (qztUser != null) {
                    this.iDgmCouponService.creatGoodsUserCoupon(qztUser.getUserId(), dgmGoodsCoupon.getCouponId());
                    dgmGoodsCoupon.setGcStatus("1");
                    this.iDgmGoodsCouponService.modifyById(dgmGoodsCoupon);
                }
            }
        }
    }

}

