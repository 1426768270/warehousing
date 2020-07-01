package com.sxt.sys.mapper;

import com.sxt.sys.domain.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 老雷
 * @since 2020-05-01
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 根据权限或菜单id删除权限表和角色关系表中的数据
     */
    void deleteRolePermissionByPid(@Param("id")Serializable id);
}
