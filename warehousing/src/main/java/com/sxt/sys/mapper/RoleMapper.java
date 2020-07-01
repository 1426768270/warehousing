package com.sxt.sys.mapper;

import com.sxt.sys.domain.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lq
 * @since 2020-06-25
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据角色id删除sys_role_permission
     * @param id
     */
    void deleteRolePermissionByRid(Serializable id);

    /**
     * 根据角色id删除sys_role_user
     * @param id
     */
    void deleteRoleUserByRid(Serializable id);

    /**
     * 根据角色ID查询角色所有的权限或菜单ID
     * @param roleId
     * @return
     */
    List<Integer> queryRolePermissionIdsByRid(Integer roleId);

    /**
     * 保存角色和菜单之间的关系
     * @param rid
     * @param pid
     */
    void saveRolePermission(@Param("rid") Integer rid, @Param("pid") Integer pid);

    /**
     * 根据用户id删除用户的中间表数据
     * @param id
     */
    void deleteRoleUserByUid(@Param("id") Serializable id);

    /**
     * 查询当前用户拥有的角色ID集合
     * @param id
     * @return
     */
    List<Integer> queryUserRoleIdsByUid(Integer id);

    /**
     * 保存用户和角色之间的关系
     * @param uid
     * @param rid
     */
    void insertUserRole(@Param("uid") Integer uid, @Param("rid") Integer rid);
}
