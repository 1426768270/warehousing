package com.sxt.sys.service.impl;

import com.sxt.sys.domain.Role;
import com.sxt.sys.mapper.RoleMapper;
import com.sxt.sys.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lq
 * @since 2020-06-25
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public boolean removeById(Serializable id) {
        //根据角色id删除sys_role_permission
        this.getBaseMapper().deleteRolePermissionByRid(id);

        //根据角色id删除sys_role_user
        this.getBaseMapper().deleteRoleUserByRid(id);

        return super.removeById(id);
    }

    /**
     * 根据角色ID查询角色所有的权限或菜单ID
     * @param roleId
     * @return
     */
    @Override
    public List<Integer> queryRolePermissionIdsByRid(Integer roleId) {
        return this.getBaseMapper().queryRolePermissionIdsByRid(roleId);
    }

    /**
     * 保存角色和菜单之间的关系
     * @param rid
     * @param ids
     */
    @Override
    public void saveRolePermission(Integer rid, Integer[] ids) {
        RoleMapper roleMapper = this.getBaseMapper();
        //删除
        roleMapper.deleteRolePermissionByRid(rid);
        if (ids!=null&&ids.length>0){
            for (Integer pid:ids) {
                roleMapper.saveRolePermission(rid,pid);
            }
        }

    }

    /**
     * 查询当前用户拥有的角色ID集合
     * @param id
     * @return
     */
    @Override
    public List<Integer> queryUserRoleIdsByUid(Integer id) {
        return this.getBaseMapper().queryUserRoleIdsByUid(id);
    }
}
