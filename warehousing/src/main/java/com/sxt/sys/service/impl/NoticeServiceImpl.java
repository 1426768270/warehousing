package com.sxt.sys.service.impl;

import com.sxt.sys.domain.Notice;
import com.sxt.sys.mapper.NoticeMapper;
import com.sxt.sys.service.NoticeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lq
 * @since 2020-05-02
 */
@Service
@Transactional
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

}
