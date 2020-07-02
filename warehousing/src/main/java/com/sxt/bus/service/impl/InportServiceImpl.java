package com.sxt.bus.service.impl;

import com.sxt.bus.domain.Goods;
import com.sxt.bus.domain.Inport;
import com.sxt.bus.mapper.GoodsMapper;
import com.sxt.bus.mapper.InportMapper;
import com.sxt.bus.service.InportService;
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
 * @author lq
 * @since 2020-07-02
 */
@Service
@Transactional
public class InportServiceImpl extends ServiceImpl<InportMapper, Inport> implements InportService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public boolean save(Inport entity) {
        //根据商品编号查询商品
        Goods goods = goodsMapper.selectById(entity.getGoodsid());
        goods.setId(entity.getGoodsid());
        goods.setNumber(entity.getNumber()+goods.getNumber());
        goodsMapper.updateById(goods);
        //保存进货信息
        return super.save(entity);
    }

    @Override
    public boolean updateById(Inport entity) {
        //根据进货id查询进货信息
        Inport inport = this.baseMapper.selectById(entity.getId());
        //根据商品编号查询商品
        Goods goods = goodsMapper.selectById(entity.getGoodsid());
        //仓库算法 当前-进货单之前的+修改后的
        goods.setNumber(goods.getNumber()-inport.getNumber()+entity.getNumber());
        this.goodsMapper.updateById(goods);
        //更新进货单
        return super.updateById(entity);
    }
}
