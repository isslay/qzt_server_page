package com.qzt.ump.rpc.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.tools.AutonaviUtil;
import com.qzt.ump.rpc.api.SysAreaService;
import com.qzt.common.core.base.BaseServiceImpl;
import com.qzt.common.redis.serialMode.RredisMode;
import com.qzt.common.redis.util.CacheUtil;
import com.qzt.pagedef.PageDef;
import com.qzt.ump.dao.mapper.SysAreaMapper;
import com.qzt.ump.model.SysArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Xiaofei
 * @since 2018-10-17
 */
@Service("sysAreaService")
public class SysAreaServiceImpl extends BaseServiceImpl<SysAreaMapper, SysArea> implements SysAreaService {

    @Autowired
    private SysAreaMapper sysAreaMapper;

    @Override
    public Page<SysArea> find(Page<SysArea> page) {
        PageHelper.startPage(page.getCurrent(), page.getSize());
        List<SysArea> rb = this.sysAreaMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos, page);
    }

    @Override
    public List<Map> findJson(String parentId) {
        Map pmap = new HashMap();
        pmap.put("parentId", parentId);
        return this.sysAreaMapper.findListMap(pmap);
    }

    @Override
    public List<Map> findListMap(Map pmap) {
        return this.sysAreaMapper.findListMap(pmap);
    }

    @Override
    public String selectAreaName(String province, String city, String area) {
        province =Integer.valueOf(province).toString();
        city =Integer.valueOf(city).toString();
        area =Integer.valueOf(area).toString();
        String areaName = "";
        try {
            RredisMode rm = (RredisMode) CacheUtil.getCache().get(SysConstant.CacheNamespaceEnum.AREA.value() + province);
            if (rm != null) {
                Map<String, Object> areaMap = rm.getAreaMap();
                Map cAreaMap = (Map) areaMap.get("cAreaMap");
                Map cAreaMaps = (Map) cAreaMap.get(city);
                Map aAreaMap = (Map) cAreaMaps.get("aAreaMap");
                Map aAreaMaps = (Map) aAreaMap.get(area);
                areaName = "" + areaMap.get("areaName") + cAreaMaps.get("areaName") + aAreaMaps.get("areaName");
            } else {
                Map rhAreaMap = this.sysAreaMapper.findMapOne(province);
                areaName += rhAreaMap==null?"-":rhAreaMap.get("areaName");
                //市级信息
                List<Map> cAreaList = this.findJson(rhAreaMap.get("id").toString());
                Map mapp = new HashMap();
                for (Map cAreaMap : cAreaList) {
                    if (city.equals(cAreaMap.get("id").toString())) {
                        areaName += cAreaMap.get("areaName");
                    }
                    //区县级信息
                    List<Map> aAreaList = this.findJson(cAreaMap.get("id").toString());
                    Map maps = new HashMap();
                    for (Map aAreaMap : aAreaList) {
                        if (area.equals(aAreaMap.get("id").toString())) {
                            areaName += aAreaMap.get("areaName");
                        }
                        maps.put(aAreaMap.get("id"), aAreaMap);
                    }
                    cAreaMap.put("aAreaMap", maps);
                    mapp.put(cAreaMap.get("id"), cAreaMap);
                }
                rhAreaMap.put("cAreaMap", mapp);
                RredisMode redisMode = new RredisMode();
                redisMode.setAreaMap(rhAreaMap);
                CacheUtil.getCache().set(SysConstant.CacheNamespaceEnum.AREA.value() + rhAreaMap.get("id"), redisMode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return areaName;
    }

    @Override
    public void initializeArea() {
        List<Map> areaList = this.findJson("0");
        for (Map rhAreaMap : areaList) {
            //市级信息
            List<Map> cAreaList = this.findJson(rhAreaMap.get("id").toString());
            Map mapp = new HashMap();
            for (Map cAreaMap : cAreaList) {
                //区县级信息
                List<Map> aAreaList = this.findJson(cAreaMap.get("id").toString());
                Map maps = new HashMap();
                for (Map aAreaMap : aAreaList) {
                    maps.put(aAreaMap.get("id"), aAreaMap);
                }
                cAreaMap.put("aAreaMap", maps);
                mapp.put(cAreaMap.get("id"), cAreaMap);
            }
            rhAreaMap.put("cAreaMap", mapp);
            RredisMode redisMode = new RredisMode();
            redisMode.setAreaMap(rhAreaMap);
            CacheUtil.getCache().set(SysConstant.CacheNamespaceEnum.AREA.value() + rhAreaMap.get("id"), redisMode);
        }
        RredisMode redisModes = new RredisMode();
        redisModes.setAreaList(areaList);
        CacheUtil.getCache().set(SysConstant.CacheNamespaceEnum.AREA.value() + "p", redisModes);
    }


    @Override
    public String findAreaParentInfo(String code) {
        String allArea = "";
        do {
            Map aeraInfo = this.sysAreaMapper.findAreaInfo(code);
            String praentId = aeraInfo.get("parentId") + "";
            allArea = aeraInfo.get("areaName") + allArea;
            if ("0".equals(praentId)) {
                break;
            }
            Map mapOne = this.sysAreaMapper.findMapOne(praentId);
            code = mapOne.get("areaCode").toString();
        } while (1 == 1);

        return allArea;
    }

    @Override
    public List<SysArea> findList(Map map) {
        return this.sysAreaMapper.find(map);
    }

    @Override
    public SysArea getCityAccordingCoordinates(String longitudeAndLatitude, String cityType) {
        try {
            Map ablalmap = AutonaviUtil.getAddressByLongitudeAndLatitude(longitudeAndLatitude);
            System.out.println(ablalmap);
            Map omap = new HashMap();
            String citycode = ablalmap.get("citycode").toString();
            String adcode = ablalmap.get("adcode").toString();
            if ("".equals(citycode) || "".equals(adcode)) {
                return null;
            }
            omap.put("autonaviCityCode", ablalmap.get("citycode"));
            omap.put("autonaviAreaCode", ablalmap.get("adcode"));
            SysArea sysArea = this.sysAreaMapper.findOne(omap);
            int areaLength = sysArea.getAreaCode().length();//code长度
            if ("p".equals(cityType)) {//省：p、市：c、区：a
                sysArea = areaLength == 2 ? sysArea : this.queryById(Long.valueOf(sysArea.getAreaCode().substring(0, 2)));
            } else if ("a".equals(cityType) && areaLength == 12) {//区：a
            } else {
                if (areaLength > 7) {//7位市级
                    sysArea = areaLength == 7 ? sysArea : this.queryById(Long.valueOf(sysArea.getAreaCode().substring(2, 7)));
                }
            }
//        {country=中国, formattedAddress=贵州省六盘水市水城县南开苗族彝族乡南开乡, province=贵州省, citycode=0858, city=六盘水市, adcode=520221, towncode=520221204000, district=水城县, township=南开苗族彝族乡}
//        {country=中国, formattedAddress=吉林省长春市朝阳区前进街道长春市朝阳区人民政府, province=吉林省, citycode=0431, city=长春市, adcode=220104, towncode=220104001000, district=朝阳区, township=前进街道}
//        {country=中国, formattedAddress=海南省保亭黎族苗族自治县保城镇保亭黎族苗族自治县人大常委会, province=海南省, citycode=0801, city=, adcode=469029, towncode=469029100000, district=保亭黎族苗族自治县, township=保城镇}
//        {country=中国, formattedAddress=海南省琼海市嘉积镇琼海市嘉积中学, province=海南省, citycode=1894, city=, adcode=469002, towncode=469002100000, district=琼海市, township=嘉积镇}
//        {country=中国, formattedAddress=山西省忻州市五台县阳白乡驴月沟, province=山西省, citycode=0350, city=忻州市, adcode=140922, towncode=140922209000, district=五台县, township=阳白乡}
//        {country=中国, formattedAddress=北京市门头沟区王平镇王平大街东路, province=北京市, citycode=010, city=, adcode=110109, towncode=110109005000, district=门头沟区, township=王平镇}
            return sysArea;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Map test(Map fmap) {
        return this.sysAreaMapper.test(fmap);
    }

    @Override
    public Map test2(Map fmap) {
        return this.sysAreaMapper.test2(fmap);
    }

}
