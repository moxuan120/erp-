package cn.qgg.erp.action;
import cn.qgg.erp.biz.IRoleBiz;
import cn.qgg.erp.entity.Role;
import cn.qgg.erp.entity.Tree;

import java.util.List;

/**
 * 角色Action 
 * @author Administrator
 *
 */
public class RoleAction extends BaseAction<Role> {

	private IRoleBiz roleBiz;
	private List<Tree> treeList;
	private String checked;

	/**
	 * 获取角色权限
	 * @return
	 */
	public String getRoleMenus(){
		treeList = roleBiz.getRoleMenus(getId());
		return "treeList";
	}

	/**
	 * 更新角色权限
	 * @return
	 */
	public String updateRoleMenus(){
		try {
			roleBiz.updateRoleMenus(super.getId(),checked);
			ajaxReturn(true,"更新成功");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxReturn(false,"更新失败");
		}
		return SUCCESS;
	}
	public void setRoleBiz(IRoleBiz roleBiz) {
		this.roleBiz = roleBiz;
		super.setBaseBiz(this.roleBiz);
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public List<Tree> getTreeList() {
		return treeList;
	}

}
