package com.sxt.bus.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxt.bus.domain.Goods;
import com.sxt.bus.domain.Provider;
import com.sxt.bus.service.GoodsService;
import com.sxt.bus.service.ProviderService;
import com.sxt.bus.vo.GoodsVo;
import com.sxt.sys.common.AppFileUtils;
import com.sxt.sys.common.Constast;
import com.sxt.sys.common.DataGridView;
import com.sxt.sys.common.ResultObj;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lq
 * @since 2020-07-01
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private ProviderService providerService;

    /**
     * 查询
     */
    @RequestMapping("loadAllGoods")
    public DataGridView loadAllGoods(GoodsVo goodsVo){

        IPage<Goods> page=new Page<>(goodsVo.getPage(), goodsVo.getLimit());
        QueryWrapper<Goods> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq(goodsVo.getProviderid()!=null&&goodsVo.getProviderid()!=0,"providerid",goodsVo.getProviderid());
        queryWrapper.like(StringUtils.isNotBlank(goodsVo.getGoodsname()), "goodsname", goodsVo.getGoodsname());
        queryWrapper.like(StringUtils.isNotBlank(goodsVo.getProductcode()), "productcode", goodsVo.getProductcode());
        queryWrapper.like(StringUtils.isNotBlank(goodsVo.getPromitcode()), "promitcode", goodsVo.getPromitcode());
        queryWrapper.like(StringUtils.isNotBlank(goodsVo.getDescription()), "description", goodsVo.getDescription());
        queryWrapper.like(StringUtils.isNotBlank(goodsVo.getSize()), "size", goodsVo.getSize());

        this.goodsService.page(page, queryWrapper);
        List<Goods> records = page.getRecords();
        for (Goods goods :records){
            Provider provider = providerService.getById(goods.getProviderid());
            if (null!=provider){
                goods.setProvidername(provider.getProvidername());
            }
        }

        return new DataGridView(page.getTotal(),page.getRecords());
    }

    /**
     * 添加
     */
    @RequestMapping("addGoods")
    public ResultObj addGoods(GoodsVo goodsVo) {
        try {
            if (goodsVo.getGoodsimg()!=null&& goodsVo.getGoodsimg().endsWith("_temp")){
                String newName = AppFileUtils.renameFile(goodsVo.getGoodsimg());
                goodsVo.setGoodsimg(newName);
            }
            this.goodsService.save(goodsVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 修改
     */
    @RequestMapping("updateGoods")
    public ResultObj updateGoods(GoodsVo goodsVo) {
        try {
            //说明是不默认图片
            if(!(goodsVo.getGoodsimg()!=null&&goodsVo.getGoodsimg().equals(Constast.IMAGES_DEFAULT_PNG))) {
                if(goodsVo.getGoodsimg().endsWith("_temp")) {
                    String newName=AppFileUtils.renameFile(goodsVo.getGoodsimg());
                    goodsVo.setGoodsimg(newName);
                    //删除原先的图片
                    String oldPath=this.goodsService.getById(goodsVo.getId()).getGoodsimg();
                    AppFileUtils.removeFileByPath(oldPath);

                }
            }
            this.goodsService.updateById(goodsVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 删除
     */
    @RequestMapping("deleteGoods")
    public ResultObj deleteGoods(Integer id,String goodsimg) {
        try {
            AppFileUtils.removeFileByPath(goodsimg);
            this.goodsService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }


}

