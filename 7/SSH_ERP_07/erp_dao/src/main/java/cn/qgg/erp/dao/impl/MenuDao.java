package cn.qgg.erp.dao.impl;

import cn.qgg.erp.dao.IMenuDao;
import cn.qgg.erp.entity.Menu;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * 菜单树
 */
public class MenuDao extends BaseDao<Menu> implements IMenuDao {
    //分页条件
    @Override
    public DetachedCriteria getCriteria(Menu t1, Menu t2, Object param) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Menu.class);
        if (t1 != null) {
            if (StringUtils.isNotBlank(t1.getMenuid())) {
                criteria.add(Restrictions.eq("menuid", t1.getMenuid()));
            }
            if (StringUtils.isNotBlank(t1.getMenuname())) {
                criteria.add(Restrictions.like("menuname", t1.getMenuname(), MatchMode.ANYWHERE));
            }
            if (StringUtils.isNotBlank(t1.getIcon())) {
                criteria.add(Restrictions.like("icon", t1.getIcon(), MatchMode.ANYWHERE));
            }
            if (StringUtils.isNotBlank(t1.getUrl())) {
                criteria.add(Restrictions.like("url", t1.getUrl(), MatchMode.ANYWHERE));
            }
        }
        return criteria.addOrder(Order.asc("menuid"));
    }

    @Override
    public List<Menu> getMenus(Long uuid) {
        String hql = "select m from Emp e join e.roleList r join r.menuList m where e.uuid = ?";
        return (List<Menu>) getHibernateTemplate().find(hql, uuid);
    }

}