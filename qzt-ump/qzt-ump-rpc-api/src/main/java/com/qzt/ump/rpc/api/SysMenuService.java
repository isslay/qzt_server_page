package com.qzt.ump.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.common.core.base.BaseService;
import com.qzt.common.core.model.PageModel;
import com.qzt.ump.model.SysMenuModel;
import com.qzt.ump.model.SysTreeModel;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单 服务类
 * </p>
 *
 * @author cgw
 * @since 2017-11-29
 */
public interface SysMenuService extends BaseService<SysMenuModel> {

    /**
     * 查找所有菜单
     *
     * @return List<SysMenuModel>
     * @author cgw
     * @date 2017-12-02 13:59
     */
    List<SysMenuModel> queryList();

    /**
     * 分页查询所有菜单
     *
     * @param page
     * @return Page<SysMenuModel>
     * @author shadj
     * @date 2017/12/18 13:52
     */
    Page<SysMenuModel> queryListPage(Page<SysMenuModel> page);

    /**
     * 根据用户ID查找菜单树（包含目录和菜单，不包含按钮）
     *
     * @param userId
     * @return List<SysMenuModel>
     * @author cgw
     * @date 2017-12-03 00:56
     */
    List<SysTreeModel> queryMenuTreeByUserId(Long userId);

    /**
     * 查找功能菜单树（包含目录、菜单和按钮）
     *
     * @return List<TreeModel>
     * @author cgw
     * @date 2017-12-19 11:14
     */
    List<SysTreeModel> queryFuncMenuTree();

    /**
     * 根据角色ID查找功能菜单树（包含目录、菜单和按钮）
     *
     * @param roleId
     * @return List<TreeModel>
     * @author cgw
     * @date 2017-12-19 11:14
     */
    List<SysTreeModel> queryFuncMenuTree(Long roleId);

    /**
     * 查询菜单树，供页面选择父菜单使用，过滤自己及子菜单
     *
     * @param id
     * @param menuType
     * @return List<SysTreeModel>
     * @author shadj
     * @date 2017/12/22 22:59
     */
    List<SysTreeModel> queryTree(Long id, Integer menuType);

    /**
     * 删除单个菜单（设置删除状态为是）
     *
     * @param id 菜单编号
     * @return Boolean 删除成功返回true,否则返回false
     * @author shadj
     * @date 2017/12/23 23:20
     */
    Boolean delete(Long id);

    /**
     * 批量删除菜单
     *
     * @param ids 要删除的菜单编号数组
     * @return 返回删除成功记录数
     * @author shadj
     * @date 2017/12/24 14:06
     */
    Integer deleteBatch(Long[] ids);

    /**
     * 分页查询所有菜单
     *
     * @param page
     * @return Page<SysMenuModel>
     * @author Xiaofei
     * @date 2019/04/17
     */
    Page<SysMenuModel> find(Page<SysMenuModel> page);

    /**
     * 获取菜单树
     *
     * @return java.util.List<SysTreeModel>
     * @author Xiaofei
     * @date 2019-10-16
     */
    List<SysTreeModel> queryTreeMenu(Long id, Integer menuType, Long parentId);

}
