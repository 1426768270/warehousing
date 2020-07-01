package com.sxt.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxt.sys.common.*;
import com.sxt.sys.domain.Dept;
import com.sxt.sys.domain.Permission;
import com.sxt.sys.domain.User;
import com.sxt.sys.service.PermissionService;
import com.sxt.sys.service.RoleService;
import com.sxt.sys.service.UserService;
import com.sxt.sys.vo.DeptVo;
import com.sxt.sys.vo.PermissionVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Wrapper;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lq
 * @since 2020-05-01
 */
@RestController
@RequestMapping("/menu")
public class MenuController {


    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleService roleService;

    @RequestMapping("loadIndexLeftMenuJson")
    public DataGridView loadIndexLeftMenuJson(PermissionVo permissionVo){

        //查询所有菜单
        QueryWrapper<Permission> queryWrapper =  new QueryWrapper<>();
        //设置只能查询菜单
        queryWrapper.eq("type", Constast.TYPE_MENU);
        queryWrapper.eq("available",Constast.AVAILABLE_TRUE);

        User user = (User) WebUtils.getSession().getAttribute("user");

        List<Permission> list =null;
        //判断是否为超级管理员
        if (user.getType()==Constast.USER_TYPE_SUPER){
           list = permissionService.list(queryWrapper);
        }else {
            //感觉用户ID+角色+权限去查
            Integer userId = user.getId();
            //根据用户id查询角色
            List<Integer> currentUserRoleIds = roleService.queryUserRoleIdsByUid(userId);
            //根据角色id得到权限和菜单id
            Set<Integer> pids = new HashSet<>();
            for (Integer rid: currentUserRoleIds){
                List<Integer> permissionIds = roleService.queryRolePermissionIdsByRid(rid);
                pids.addAll(permissionIds);
            }
            //根据角色id查询权限
            if (pids.size()>0){
                queryWrapper.in("id",pids);
                list = permissionService.list(queryWrapper);
            }else {
                list = new ArrayList<>();
            }

        }

        List<TreeNode> treeNodes = new ArrayList<TreeNode>();
        //处理上下级关系
        //1.添加
        for (Permission p : list) {
            Integer id=p.getId();
            Integer pid=p.getPid();
            String title=p.getTitle();
            String icon=p.getIcon();
            String href=p.getHref();
            Boolean spread=p.getOpen()==Constast.OPEN_TRUE?true:false;
            treeNodes.add(new TreeNode(id,pid,title,icon,href,spread));
        }
        //2.构造上下级关系
        List<TreeNode> list2 = TreeNodeBuilder.build(treeNodes, 1);

        return new DataGridView(list2);
    }

    //菜单管理开始
    /**
     * 加载菜单管理左边的dtree
     */
    @RequestMapping("loadMenuManagerLeftTreeJson")
    public DataGridView loadMenuManagerLeftTreeJson(PermissionVo permissionVo){

        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type",Constast.TYPE_MENU);

        List<Permission> list = this.permissionService.list(queryWrapper);

        List<TreeNode> treeNodes = new ArrayList<TreeNode>();

        for (Permission permission: list){
            Boolean spread = permission.getOpen()==1?true:false;
            treeNodes.add(new TreeNode(permission.getId(),permission.getPid(),permission.getTitle(),spread));
        }
        return new DataGridView(treeNodes);
    }

    /**
     * 查询
     */
    @RequestMapping("loadAllMenu")
    public DataGridView loadAllMenu(PermissionVo permissionVo){

        IPage<Permission> page=new Page<>(permissionVo.getPage(), permissionVo.getLimit());
        QueryWrapper<Permission> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq(permissionVo.getId()!=null,"id",permissionVo.getId()).or().eq(permissionVo.getId()!=null,"pid",permissionVo.getId());
        queryWrapper.eq("type",Constast.TYPE_MENU);  //只能查询菜单

        queryWrapper.like(StringUtils.isNotBlank(permissionVo.getTitle()), "title", permissionVo.getTitle());
        queryWrapper.orderByAsc("ordernum"); //排序码

        this.permissionService.page(page, queryWrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    /**
     *保存菜单
     * @param permissionVo
     * @return
     */
    @RequestMapping("addMenu")
    public ResultObj addMenu(PermissionVo permissionVo){
        try {
            permissionVo.setType(Constast.TYPE_MENU); //设置添加类型
            this.permissionService.save(permissionVo);
            return ResultObj.ADD_SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     *修改菜单
     * @param permissionVo
     * @return
     */
    @RequestMapping("updateMenu")
    public ResultObj updateMenu(PermissionVo permissionVo){
        try {
            this.permissionService.updateById(permissionVo);
            return ResultObj.UPDATE_SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     *删除菜单
     * @param permissionVo
     * @return
     */
    @RequestMapping("deleteMenu")
    public ResultObj deleteMenu(PermissionVo permissionVo){
        try {
            this.permissionService.removeById(permissionVo.getId());
            return ResultObj.DELETE_SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     *加载最大的排序码
     * @return
     */
    @RequestMapping("loadMenuMaxOrderNum")
    public Map<String,Object> loadMenuMaxOrderNum(){
        Map<String, Object> map=new HashMap<String, Object>();

        QueryWrapper<Permission> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("ordernum");
        IPage<Permission> page = new Page<>(1,1);
        List<Permission> list = this.permissionService.page(page,queryWrapper).getRecords();
        if(list.size()>0) {
            map.put("value", list.get(0).getOrdernum()+1);
        }else {
            map.put("value", 1);
        }
        return map;
    }

    /**
     * 检查当前ID的菜单有没有子菜单
     */
    @RequestMapping("checkMenuHasChildrenNode")
    public Map<String,Object> checkMenuHasChildrenNode(PermissionVo permissionVo){
        Map<String, Object> map=new HashMap<String, Object>();

        QueryWrapper<Permission> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("pid",permissionVo.getId());
        List<Permission> list = this.permissionService.list(queryWrapper);
        if (list.size()>0){
            map.put("value",true);
        }else {
            map.put("false",false);
        }
        return map;
    }
}

