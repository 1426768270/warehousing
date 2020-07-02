package com.sxt.bus.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxt.bus.domain.Goods;
import com.sxt.bus.domain.Outport;
import com.sxt.bus.domain.Provider;
import com.sxt.bus.service.GoodsService;
import com.sxt.bus.service.OutportService;
import com.sxt.bus.service.ProviderService;
import com.sxt.bus.vo.OutportVo;
import com.sxt.sys.common.DataGridView;
import com.sxt.sys.common.ResultObj;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lq
 * @since 2020-07-02
 */
@RestController
@RequestMapping("/outport")
public class OutportController {

    @Autowired
    private OutportService outportService;

    @Autowired
    private ProviderService providerService;

    @Autowired
    private GoodsService goodsService;

    /**
     * 查询
     */
    @RequestMapping("loadAllOutport")
    public DataGridView loadAllOutport(OutportVo outportVo){

        IPage<Outport> page=new Page<>(outportVo.getPage(), outportVo.getLimit());
        QueryWrapper<Outport> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq(outportVo.getProviderid()!=null&&outportVo.getProviderid()!=0,"providerid",outportVo.getProviderid());
        queryWrapper.eq(outportVo.getGoodsid()!=null&&outportVo.getGoodsid()!=0,"goodsid",outportVo.getGoodsid());
        queryWrapper.ge(outportVo.getStartTime()!=null, "outporttime", outportVo.getStartTime());
        queryWrapper.ge(outportVo.getEndTime()!=null, "outporttime", outportVo.getEndTime());
        queryWrapper.like(StringUtils.isNotBlank(outportVo.getOperateperson()),"operateperson",outportVo.getOperateperson());
        queryWrapper.like(StringUtils.isNotBlank(outportVo.getRemark()),"remark",outportVo.getRemark());
        queryWrapper.orderByDesc("outporttime");
        this.outportService.page(page, queryWrapper);
        List<Outport> records = page.getRecords();
        for (Outport outport :records){
            Provider provider = providerService.getById(outport.getProviderid());
            if (null!=provider){
                outport.setProvidername(provider.getProvidername());
            }
            Goods goods = this.goodsService.getById(outport.getGoodsid());
            if (null != goods){
                outport.setGoodsname(goods.getGoodsname());
                outport.setSize(goods.getSize());
            }
        }

        return new DataGridView(page.getTotal(),page.getRecords());
    }

    /**
     * 添加退货信息
     */
    @RequestMapping("addOutport")
    public ResultObj addOutport(Integer id,Integer number, String remark){
        try {
            this.outportService.addOutport(id,number,remark);
            return ResultObj.OPERATE_SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            return ResultObj.OPERATE_ERROR;
        }
    }

}

