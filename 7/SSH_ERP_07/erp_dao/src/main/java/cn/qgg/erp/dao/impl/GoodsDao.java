package cn.qgg.erp.dao.impl;

import cn.qgg.erp.dao.IGoodsDao;
import cn.qgg.erp.entity.Goods;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * 商品
 */
public class GoodsDao extends BaseDao<Goods> implements IGoodsDao{
    //分页条件
    @Override
    public DetachedCriteria getCriteria(Goods t1, Goods t2, Object param) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Goods.class);
        if(t1 != null){
            //模糊查询
            if(StringUtils.isNotBlank(t1.getName()))
                criteria.add(Restrictions.like("name", t1.getName(), MatchMode.ANYWHERE));
            if(StringUtils.isNotBlank(t1.getOrigin()))
                criteria.add(Restrictions.like("origin", t1.getOrigin(), MatchMode.ANYWHERE));
            if(StringUtils.isNotBlank(t1.getProducer()))
                criteria.add(Restrictions.like("producer", t1.getProducer(), MatchMode.ANYWHERE));
            if(StringUtils.isNotBlank(t1.getUnit()))
                criteria.add(Restrictions.like("unit", t1.getUnit(), MatchMode.ANYWHERE));

            //根据商品类型查询
            if(null != t1.getGoodsType() && null != t1.getGoodsType().getUuid()){
                criteria.add(Restrictions.eq("goodsType", t1.getGoodsType()));
            }
            //大于进货价
            if (t1.getInprice() != null)
                criteria.add(Restrictions.ge("inprice", t1.getInprice()));
            //大于销售价
            if (t1.getOutprice() != null)
                criteria.add(Restrictions.ge("outprice", t1.getOutprice()));
        }
        if (t2 != null) {
            //小于进货价
            if (t2.getInprice() != null)
                criteria.add(Restrictions.le("inprice", t1.getInprice()));
            //小于销售价
            if (t2.getOutprice() != null)
                criteria.add(Restrictions.le("outprice", t1.getOutprice()));
          //精确匹配 导入构造条件
	        if (StringUtils.isNotBlank(t2.getName())) {
	            criteria.add(Restrictions.eq("name", t2.getName()));
	        }
        }
	       
        return criteria.addOrder(Order.desc("uuid"));

    }
}