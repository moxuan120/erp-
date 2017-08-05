package cn.qgg.erp.action;

import cn.qgg.erp.biz.IEmpBiz;
import cn.qgg.erp.entity.Emp;
import cn.qgg.erp.entity.Tree;
import cn.qgg.erp.exception.ErpException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.poifs.filesystem.NotOLE2FileException;
import org.apache.struts2.ServletActionContext;

public class EmpAction extends BaseAction<Emp> {

    private IEmpBiz empBiz;
    private List<Tree> treeList;
    private List<Emp> empList;
    private String checked;
    private String depUuid;
    //导入所需要的参数
    private File file;
	private String fileFileName;
	private String fileContentType;

    public void setFile(File file) {
		this.file = file;
	}
	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}
	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

	/**
     * 获取用户角色
     *
     */
    public String getRoles(){
        treeList = empBiz.getRoles(getId());
        return "treeList";
    }

    /**
     * 更新用户角色
     */
    public String updateRoles(){
        try {
            empBiz.updateRoles(getId(),checked);
            ajaxReturn(true,"更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxReturn(false,"更新失败");
        }
        return SUCCESS;
    }
    
    /**
     * 查询部门下的全部员工
     * @param empBiz
     */
    public String empDepList(){
    	empList = empBiz.empDepList(depUuid);
    	return "empList";
    }
    
    public void setEmpBiz(IEmpBiz empBiz) {
        this.empBiz = empBiz;
        super.setBaseBiz(empBiz);
    }
    public void setChecked(String checked) {
        this.checked = checked;
    }

    public List<Tree> getTreeList() {
        return treeList;
    }
    
       //员工的导出
    public void export() {
		// 表名
		String fileName = "员工表.xls";
		
		// 拿到输出流
		HttpServletResponse rep = ServletActionContext.getResponse();
		try {
			rep.setHeader("Content-Disposition",
					"attachment;filename=" + new String(fileName.getBytes(), "ISO-8859-1"));
			// 中文名称进行转码
			// 调用导出业务
			empBiz.export(rep.getOutputStream(), getT1());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
     }
    //员工的导入
    public String doImport() {
        try {
            if (!fileContentType.equals("application/vnd.ms-excel")) {
                ajaxReturn(false, "上传的文件类型不正确");
            } else {
                int[] re = empBiz.doImport(new FileInputStream(file));
                ajaxReturn(true, "导入成功！<br>读取到" + re[0] + "条数据<br>更新" + re[1] + "条数据<br>新增" + re[2] + "条数据");
            }
        } catch (ErpException e) {
            e.printStackTrace();
            ajaxReturn(false, "导入失败：" + e.getMessage());
        } catch (NotOLE2FileException e) {
            e.printStackTrace();
            ajaxReturn(false, "导入失败：读取文件失败");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxReturn(false, "导入失败");
        }
        return SUCCESS;
    } 
    public void exportModel(){

        String fileName =  "员工表.xls";
        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO-8859-1"));
            empBiz.exportModel(response.getOutputStream(), getT1());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }  

	public String getDepUuid() {
		return depUuid;
	}

	public void setDepUuid(String depUuid) {
		this.depUuid = depUuid;
	}

	public List<Emp> getEmpList() {
		return empList;
	}

	public void setEmpList(List<Emp> empList) {
		this.empList = empList;
	}
}
