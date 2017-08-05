package cn.qgg.erp.dao.impl;

import cn.qgg.erp.dao.IEmpDao;
import cn.qgg.erp.entity.Emp;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * 员工
 */
public class EmpDao extends BaseDao<Emp> implements IEmpDao {
    //分页条件
    @Override
    public DetachedCriteria getCriteria(Emp t1, Emp t2, Object param) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Emp.class);
        if (t1 != null) {
            //模糊查询
            if (StringUtils.isNotBlank(t1.getUsername()))
                criteria.add(Restrictions.like("username", t1.getUsername(), MatchMode.ANYWHERE));
            if (StringUtils.isNotBlank(t1.getName()))
                criteria.add(Restrictions.like("name", t1.getName(), MatchMode.ANYWHERE));
            if (StringUtils.isNotBlank(t1.getEmail()))
                criteria.add(Restrictions.like("email", t1.getEmail(), MatchMode.ANYWHERE));
            if (StringUtils.isNotBlank(t1.getAddress()))
                criteria.add(Restrictions.like("address", t1.getAddress(), MatchMode.ANYWHERE));
            if (StringUtils.isNotBlank(t1.getTele()))
                criteria.add(Restrictions.like("tele", t1.getTele(), MatchMode.ANYWHERE));
            if (t1.getGender() != null)
                criteria.add(Restrictions.eq("gender", t1.getGender()));

            //匹配部门
            if (null != t1.getDep() && null != t1.getDep().getUuid()) {
                criteria.add(Restrictions.eq("dep", t1.getDep()));
            }

            //生日开始
            if (t1.getBirthday() != null)
                criteria.add(Restrictions.ge("birthday", t1.getBirthday()));
        }
        if (t2 != null) {
            //生日结束
            if (t2.getBirthday() != null){
                criteria.add(Restrictions.le("birthday", t2.getBirthday()));
            }
          //精确匹配  导入构造条件
            if (StringUtils.isNotBlank(t2.getUsername())) {
                criteria.add(Restrictions.eq("username", t2.getUsername()));
            }
        }
   
        return criteria.addOrder(Order.asc("uuid"));

    }

    @Override
    public Emp findUser(String username, String password) {
        String sql = "from Emp where username = ? and pwd = ?";
        List<Emp> list = (List<Emp>) getHibernateTemplate().find(sql, username, password);
        return list == null || list.size() < 1 ? null : list.get(0);
    }

    @Override
    public Emp findByUsername(String username) {
        String sql = "from Emp where username = ?";
        List<Emp> list = (List<Emp>) getHibernateTemplate().find(sql, username);
        return list == null || list.size() < 1 ? null : list.get(0);
    }

    @Override
    public void alterPwd(long uuid, String newPwd) {
        String sql = "update Emp set pwd = ? where uuid = ?";
        getHibernateTemplate().bulkUpdate(sql, newPwd, uuid);
    }

	@Override
	//查询部门下的所有员工
	public List<Emp> findEmpDepList(String depUuid) {
		String sql = "from Emp where depuuid = ?";
		List<Emp> EmpList = (List<Emp>) getHibernateTemplate().find(sql, depUuid);
		return EmpList;
	}
}