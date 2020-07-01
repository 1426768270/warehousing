package com.sxt.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.sxt.sys.domain.User;
import com.sxt.sys.mapper.RoleMapper;
import com.sxt.sys.mapper.UserMapper;
import com.sxt.sys.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public boolean save(User entity) {
        return super.save(entity);
    }

    @Override
    public boolean updateById(User entity) {
        return super.updateById(entity);
    }

    @Override
    public User getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    public boolean removeById(Serializable id) {
        //根据用户id删除用户的中间表数据
        roleMapper.deleteRoleUserByUid(id);
        //删除用户头像 ，默认头像不删除

        return super.removeById(id);
    }

    @Override
    public void saveUserRole(Integer uid, Integer[] ids) {
        //根据用户id删除sys_role_user的数据
        this.roleMapper.deleteRoleUserByUid(uid);
        if (null!=ids&&ids.length>0){
            for (Integer rid:ids){
                this.roleMapper.insertUserRole(uid,rid);
            }
        }
    }
}
