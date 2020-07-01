package com.sxt.sys.service;

import com.sxt.sys.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 老雷
 * @since 2020-05-01
 */
public interface UserService extends IService<User> {

    /**
     * 保存用户和角色的关系
     * @param uid
     * @param ids
     */
    void saveUserRole(Integer uid, Integer[] ids);
}
