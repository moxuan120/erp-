package cn.qgg.erp.action;

import cn.qgg.erp.biz.IGoodsTypeBiz;
import cn.qgg.erp.entity.GoodsType;

public class GoodsTypeAction extends BaseAction<GoodsType> {

    private IGoodsTypeBiz goodsTypeBiz;

    public void setGoodsTypeBiz(IGoodsTypeBiz goodsTypeBiz) {
        this.goodsTypeBiz = goodsTypeBiz;
        super.setBaseBiz(goodsTypeBiz);
    }
}
