package com.sxt.sys.service.impl;

import com.sxt.sys.domain.Permission;
import com.sxt.sys.mapper.PermissionMapper;
import com.sxt.sys.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 老雷
 * @since 2020-05-01
 */
@Service
@Transactional
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Override
    public boolean removeById(Serializable id) {

        PermissionMapper permissionMapper = this.getBaseMapper();
        //根据权限或菜单id删除权限表和角色关系表中的数据
        permissionMapper.deleteRolePermissionByPid(id);

        //删除权限表的数据
        return super.removeById(id);
    }
}
