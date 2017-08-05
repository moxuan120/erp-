package cn.qgg.erp.dao.impl;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import cn.qgg.erp.dao.IReturnorderdetailDao;
import cn.qgg.erp.entity.Returnorderdetail;
/**
 * 退货订单明细
 *
 */
public class ReturnorderdetailDao extends BaseDao<Returnorderdetail> implements IReturnorderdetailDao {

	@Override
	public DetachedCriteria getCriteria(Returnorderdetail t1,Returnorderdetail t2,Object param){
		DetachedCriteria criteria=DetachedCriteria.forClass(Returnorderdetail.class);
		if(t1!=null){
			//模糊查询
			if(StringUtils.isNotBlank(t1.getGoodsname())){
				criteria.add(Restrictions.like("goodsname", t1.getGoodsname(), MatchMode.ANYWHERE));
			}
			//精确匹配
			if(null != t1.getState()){
				criteria.add(Restrictions.eq("state", t1.getState()));
			}
			if(null != t1.getReturnorders() && null!=t1.getReturnorders().getUuid()){
				criteria.add(Restrictions.eq("returnorders", t1.getReturnorders()));
			}
			
		}
		return criteria;
	}

}
