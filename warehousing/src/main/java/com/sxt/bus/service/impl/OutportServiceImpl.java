package com.sxt.bus.service.impl;

import com.sxt.bus.domain.Goods;
import com.sxt.bus.domain.Inport;
import com.sxt.bus.domain.Outport;
import com.sxt.bus.mapper.GoodsMapper;
import com.sxt.bus.mapper.InportMapper;
import com.sxt.bus.mapper.OutportMapper;
import com.sxt.bus.service.OutportService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxt.sys.common.WebUtils;
import com.sxt.sys.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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
public class OutportServiceImpl extends ServiceImpl<OutportMapper, Outport> implements OutportService {

    @Autowired
    private InportMapper inportMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public void addOutport(Integer id, Integer number, String remark) {
        //1.根据进货单id查询进货信息
        Inport inport = this.inportMapper.selectById(id);

        //2.根据商品id查询商品信息
        Goods goods = this.goodsMapper.selectById(inport.getGoodsid());
        goods.setNumber(goods.getNumber()-number);
        this.goodsMapper.updateById(goods);
        //3.添加退货单信息
        Outport outport = new Outport();
        outport.setGoodsid(inport.getGoodsid());
        outport.setNumber(number);
        User operateperson = (User)WebUtils.getSession().getAttribute("user");
        outport.setOperateperson(operateperson.getName());
        outport.setOutportprice(inport.getInportprice());
        outport.setOutporttime(new Date());
        outport.setPaytype(inport.getPaytype());
        outport.setProviderid(inport.getProviderid());
        outport.setRemark(remark);
        this.getBaseMapper().insert(outport);


    }
}
