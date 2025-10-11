package com.qzt.ump.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.common.core.base.BaseService;
import com.qzt.ump.model.SysArea;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Xiaofei
 * @since 2018-10-17
 */
public interface SysAreaService extends BaseService<SysArea> {

    Page<SysArea> find(Page<SysArea> pageModel);

    /**
     * 获取Json格式地区
     *
     * @return
     */
    List<Map> findJson(String parentId);

    /**
     * 根据省市区Id查询name
     *
     * @param province
     * @param city
     * @param area
     * @return
     */
    String selectAreaName(String province, String city, String area);

    /**
     * 初始化地区信息
     *
     * @return void
     * @author Xiaofei
     * @date 2019-07-31
     */
    void initializeArea();

    /**
     * 根据地区code查询全部名称(省+市+区)
     *
     * @param code
     * @return
     */
    String findAreaParentInfo(String code);

    /**
     * 查询地址信息
     *
     * @param map
     * @return java.util.List<com.qzt.ump.model.SysArea>
     * @author Xiaofei
     * @date 2019-09-24
     */
    List<SysArea> findList(Map map);

    /**
     * 查询地址信息返回mao
     *
     * @param map
     * @return java.util.List<com.qzt.ump.model.SysArea>
     * @author Xiaofei
     * @date 2019-09-28
     */
    List<Map> findListMap(Map map);

    /**
     * 根据经纬度获取城市
     *
     * @param longitudeAndLatitude 经纬度逗号隔开
     * @param cityType             需要查询的城市类型（省：p、市：c、区：a）默认为c
     * @return com.qzt.ump.model.SysArea
     * @author Xiaofei
     * @date 2019-09-26
     */
    SysArea getCityAccordingCoordinates(String longitudeAndLatitude, String cityType);

    //测试处理数据使用
    Map test(Map fmap);

    //测试处理数据使用
    Map test2(Map fmap);

}
