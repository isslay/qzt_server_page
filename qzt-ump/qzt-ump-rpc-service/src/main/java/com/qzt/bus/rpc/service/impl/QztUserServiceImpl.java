package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.QztAccountMapper;
import com.qzt.bus.dao.mapper.QztUserMapper;
import com.qzt.bus.dao.mapper.QztUserRegMapper;
import com.qzt.bus.model.*;
import com.qzt.bus.rpc.api.IQztBusinessService;
import com.qzt.bus.rpc.api.IQztUserRegService;
import com.qzt.bus.rpc.api.IQztUserService;
import com.qzt.common.core.base.BaseServiceImpl;
import com.qzt.common.tools.DateUtil;
import com.qzt.common.tools.StringUtil;
import com.qzt.pagedef.PageDef;
import com.xiaoleilu.hutool.crypto.SecureUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Cgw
 * @since 2019-11-11
 */
@Log4j
@Service("qztUserService")
public class QztUserServiceImpl extends BaseServiceImpl<QztUserMapper, QztUser> implements IQztUserService {

    @Autowired
    private QztUserMapper qztUserMapper;

    @Autowired
    private QztUserRegMapper qztUserRegMapper;

    @Autowired
    private QztAccountMapper qztAccountMapper;

    @Autowired
    private IQztUserRegService qztUserRegService;

    @Autowired
    private IQztBusinessService qztBusinessService;

    @Override
    public Page<QztUser> find(Page<QztUser> page) {
        PageHelper.startPage(page.getCurrent(), page.getSize());
        List<QztUser> rb = this.qztUserMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos, page);
    }

    @Override
    public Page<QztUser> findBack(Page<QztUser> page) {
        PageHelper.startPage(page.getCurrent(), page.getSize());
        List<QztUser> rb = this.qztUserMapper.findBack(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos, page);
    }

    @Override
    public List<QztUser> findListByOpenId(Map<String, Object> params) {
        return this.qztUserMapper.findListByOpenId(params);
    }

    @Override
    public QztUser findUserById(String openId,String unionId) {
        return this.qztUserMapper.findUserById(openId,unionId);
    }

    @Override
    public QztUser findDGUserById(Long id) {
        return this.qztUserMapper.findDGUserById(id);
    }

    @Override
    public QztUser findUserByMobile(String mobile,String realName,Long id) {
        return this.qztUserMapper.findUserByMobile(mobile,realName,id);
    }

    @Override
    public QztUser findUserByMobileName(String mobile,String realName) {
        return this.qztUserMapper.findUserByMobileName(mobile,realName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveUserAndAccount(QztUser qztUser) throws Exception {

        try {
            this.insert(qztUser);
//            if (qztUser.getReferrerFirst() != null) {
//                //发展用户的关系
//                Map<String, Object> params = new HashMap<>();
//                params.put("regUserId", qztUser.getReferrerFirst());
//                List<QztUserReg> rs = qztUserRegMapper.find(params);
//                List<QztUserReg> addUserReg = new ArrayList<>();
//                for (QztUserReg qztUserReg : rs) {
//                    qztUserReg.setId(null);
//                    qztUserReg.setCreateTime(new Date());
//                    qztUserReg.setNum(qztUserReg.getNum() + 1);
//                    qztUserReg.setRegUserId(qztUser.getId());
//                    addUserReg.add(qztUserReg);
//                }
//                QztUserReg qztUserReg = new QztUserReg();
//                qztUserReg.setCreateTime(new Date());
//                qztUserReg.setNum(1);
//                qztUserReg.setType(0);
//                qztUserReg.setRegUserId(qztUser.getId());
//                qztUserReg.setParentUserId(qztUser.getReferrerFirst());
//                addUserReg.add(qztUserReg);
//                qztUserRegMapper.saveUserRegAll(addUserReg);
//            }
            QztAccount ka = new QztAccount();
            long def = 0;
            ka.setUserId(qztUser.getId());
            ka.setUserType("1"); //前端用户
            ka.setAccountMoney(def);
            ka.setRecommendMoney(def);
            ka.setUsedMoney(def);
            ka.setFrozenMoney(def);
            ka.setCreateTime(new Date());
            qztAccountMapper.insert(ka);

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("用户注册错误");
        }
        System.out.println(qztUser.getId());

        return qztUser.getId();
    }

    @Override
    public Long findCount(Map<String, Object> params) {
        return this.qztUserMapper.findCount(params);
    }


    @Override
    public Long verifyPayPwd(Long userId, String payPwd) {
        return this.qztUserMapper.verifyPayPwd(userId, SecureUtil.md5(payPwd));
    }

    @Override
    public int updateUserMess(Map<String, Object> dataParams) {
        int res = this.qztUserMapper.updateUserMess(dataParams);
//        if (Integer.parseInt(dataParams.get("conType").toString()) == 3) {
//            Map<String, Object> params = new HashMap<>();
//            params.put("regUserId", dataParams.get("referId").toString());
//            List<QztUserReg> rs = qztUserRegMapper.find(params);
//            List<QztUserReg> addUserReg = new ArrayList<>();
//            for (QztUserReg qztUserReg : rs) {
//                qztUserReg.setId(null);
//                qztUserReg.setCreateTime(new Date());
//                qztUserReg.setNum(qztUserReg.getNum() + 1);
//                qztUserReg.setRegUserId((Long) params.get("userId"));
//                addUserReg.add(qztUserReg);
//            }
//            QztUserReg qztUserReg = new QztUserReg();
//            qztUserReg.setCreateTime(new Date());
//            qztUserReg.setNum(1);
//            qztUserReg.setType(0);
//            qztUserReg.setRegUserId((Long) dataParams.get("userId"));
//            qztUserReg.setParentUserId((Long) dataParams.get("referId"));
//            addUserReg.add(qztUserReg);
//            qztUserRegMapper.saveUserRegAll(addUserReg);
//        }

        return res;
    }

    @Override
    public List<QztUser> findListByType(Map<String, Object> params) {
        return qztUserMapper.find(params);
    }

    @Override
    public boolean updateUserById(Map<String, Object> map) {
        return this.qztUserMapper.updateUserById(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void userUpgrade(Long userId) throws Exception {
        //查询用户的上级关系树集合
        List<QztUser> relationalTree = this.qztUserMapper.findRelationalTree(userId, 2);
        for (QztUser qztUsery : relationalTree) {
            Integer upuserType;//升级用户类型
            Integer deuserType;//降级用户类型
            switch (qztUsery.getUserType()) {
                case 10://服务站10
                    upuserType = 20;
                    deuserType = 10;
                    break;
                case 20://督导20
                    upuserType = 30;
                    deuserType = 10;
                    break;
                case 30://合伙人30
                    upuserType = 40;
                    deuserType = 20;
                    break;
                case 40://股东40
                    upuserType = 40;
                    deuserType = 30;
                    break;
                default:
                    continue;
            }
            //判断用户(服务站为基层因此过滤)是否可以保级
            if ("Y".equals(qztUsery.getIsDemotion()) && qztUsery.getUserType() != 10 && this.qztUserMapper.queryDirectlyUnderLowerNum(qztUsery.getUserId(), deuserType, 2) < 10) {
                Integer result = this.qztUserMapper.updateUserType(qztUsery.getUserId(), deuserType);//降级
                log.info("结果=" + result + ";用户身份降级：" + qztUsery.getUserId() + "; 身份：" + deuserType);
                if (result != 1) {
                    throw new Exception("用户身份降级失败：" + qztUsery.getUserId() + "; 身份：" + deuserType);
                }
            } else if ("Y".equals(qztUsery.getIsUpgrade()) && qztUsery.getUserType() != 40) {//股东为顶级不在升级因此过滤
                Long upuserSize = this.qztUserMapper.queryDirectlyUnderLowerNum(qztUsery.getUserId(), qztUsery.getUserType(), 2);//升级条件
                if (upuserSize >= 10) {
                    Integer result = this.qztUserMapper.updateUserType(qztUsery.getUserId(), upuserType);//升级
                    log.info("结果=" + result + ";用户身份升级：" + qztUsery.getUserId() + "; 身份：" + upuserType);
                    if (result != 1) {
                        throw new Exception("用户身份升级失败：" + qztUsery.getUserId() + "; 身份：" + upuserType);
                    }
                }
            }
        }
    }

    @Override
    public boolean modifyUserInfo(QztUser qztUser) {
        Integer resultin = this.qztUserMapper.modifyUserInfo(qztUser.getUserId(), qztUser.getBussId(), qztUser.getReferrerSecond(), qztUser.getUserType(), DateUtil.getFormatDate(new Date()));
        return resultin == 1;
    }

    @Override
    public Integer updateUserType(Map emap) {
        return this.qztUserMapper.updateUserTypepMap(emap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addSuperior(Map emap) throws Exception {
        Long userId = Long.valueOf(emap.get("userId").toString());
        Long referrerSecond = Long.valueOf(emap.get("referrerSecond").toString());
        QztUser user = this.findUserById(userId.toString(),"");
        if (user == null) {
            return userId + "该用户不存在";
        } else if (user.getReferrerSecond() != null) {//如该用户有推荐人则无法添加
            return userId + "该用户已存在推荐人";
        }
        QztUser referrerSecondUser = this.findUserById(referrerSecond.toString(),"");
        if (referrerSecondUser == null) {
            return "请填写有效推荐人";
        }
        Long resultrut = this.qztUserRegMapper.queryByPuserIdAndRuserId(new QztUserReg(referrerSecond, userId, 2));
        if (resultrut > 0) {
            return userId + "该用户与推荐人" + referrerSecond + "已存在关系";
        }
        //向用户添加推荐人
        Integer resuule = this.qztUserMapper.updateUserTypepMap(emap);
        if (resuule != 1) {
            return "添加推荐人失败";
        }
        //修改服务站推荐人
        QztBusiness qztBusiness = this.qztBusinessService.modifyById(new QztBusiness(user.getBussId(), referrerSecond.toString(), (Long) emap.get("updateBy")));
        if (qztBusiness == null) {
            throw new Exception("添加推荐人失败");
        }
        //创建关系树
        boolean resultcrt = this.qztUserRegService.createRelationshipTree("03", 2, userId, referrerSecond);
        if (!resultcrt) {
            throw new Exception("创建关系树失败");
        }
        this.userUpgrade(userId);//处理升级
        return "200";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void test(Long userId, Long referrerSecond) throws Exception {
        Map map = new HashMap();
        map.put("userId", userId);
        map.put("referrerSecond", referrerSecond);
        map.put("userType", 10);
        Integer updateUserTypepMapre = this.qztUserMapper.updateUserTypepMap(map);
        if (updateUserTypepMapre != 1) {
            throw new Exception("更新用户信息失败");
        }
        boolean relationshipTree = this.qztUserRegService.createRelationshipTree("03", 2, userId, referrerSecond);
        if (!relationshipTree) {
            throw new Exception("创建关系树失败");
        }
        this.userUpgrade(userId);//处理升级
    }

    @Override
    public List<Map<String, Object>> userExcel(Map map) {

        List<Map<String, Object>> retmaplist = new ArrayList();
        List<QztUser> rb = this.qztUserMapper.findBack(map);
        for (QztUser qztUser : rb) {
            Map<String, Object> linkmaps = new LinkedHashMap();
            linkmaps.put("userId", qztUser.getUserId());
            linkmaps.put("createTime", DateUtil.dateTimeFormat.format(qztUser.getCreateTime()));
            linkmaps.put("mobile", qztUser.getMobile());
            linkmaps.put("realName", qztUser.getRealName());
            linkmaps.put("state", "0".equals(qztUser.getState())?"正常":"已拉黑");
            linkmaps.put("referrerFirst", qztUser.getReferrerFirst());
            linkmaps.put("referrerSecond", qztUser.getReferrerSecond());
            linkmaps.put("phShortName", qztUser.getPhShortName());
            linkmaps.put("wxOpenId", qztUser.getWxOpenId());
            retmaplist.add(linkmaps);
        }
        return retmaplist;
    }

}
