package cn.qgg.erp.action;

import cn.qgg.erp.biz.ISupplierBiz;
import cn.qgg.erp.entity.Supplier;
import cn.qgg.erp.exception.ErpException;
import org.apache.poi.poifs.filesystem.NotOLE2FileException;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 供应商Action
 *
 * @author Administrator
 */
public class SupplierAction extends BaseAction<Supplier> {
    private ISupplierBiz supplierBiz;
    private File file;
    private String fileFileName;
    private String fileContentType;

    @Override
    public String list() throws IOException {
        if (getQ() != null)
            getT1().setName(getQ().trim());
        return super.list();
    }

    public void export() {
        String fileName = getT1().getType().equals("1") ? "供应商.xls" : "客户.xls";
        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO-8859-1"));
            supplierBiz.export(response.getOutputStream(), getT1());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String doImport() {
        try {
            if (!fileContentType.equals("application/vnd.ms-excel")) {
                ajaxReturn(false, "上传的文件类型不正确");
            } else {
                int[] re = supplierBiz.doImport(new FileInputStream(file));
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

        String fileName = getT1().getType().equals("1") ? "供应商导入模板.xls" : "客户导入模板.xls";
        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO-8859-1"));
            supplierBiz.exportModel(response.getOutputStream(), getT1());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSupplierBiz(ISupplierBiz supplierBiz) {
        this.supplierBiz = supplierBiz;
        super.setBaseBiz(this.supplierBiz);
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }
}
