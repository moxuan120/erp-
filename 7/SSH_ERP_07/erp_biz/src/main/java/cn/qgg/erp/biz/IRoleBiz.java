package cn.qgg.erp.biz;
import cn.qgg.erp.entity.Role;
import cn.qgg.erp.entity.Tree;

import java.util.List;

/**
 * 角色业务逻辑层接口
 * @author Administrator
 *
 */
public interface IRoleBiz extends IbaseBiz<Role>{

    List<Tree> getRoleMenus(Long uuid);

    void updateRoleMenus(Long uuid, String checked);
}

