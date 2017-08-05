package cn.qgg.erp.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.poifs.filesystem.NotOLE2FileException;
import org.apache.struts2.ServletActionContext;

import cn.qgg.erp.biz.IGoodsBiz;
import cn.qgg.erp.entity.Goods;
import cn.qgg.erp.exception.ErpException;

public class GoodsAction extends BaseAction<Goods> {

    private IGoodsBiz goodsBiz;
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
	public void setGoodsBiz(IGoodsBiz goodsBiz) {
        this.goodsBiz = goodsBiz;
        super.setBaseBiz(goodsBiz);
    }
    
    //商品的导出
    public void export(){
		// 表名
		String fileName = "商品数据表.xls";
		// 拿到输出流
		HttpServletResponse rep = ServletActionContext.getResponse();
		try {
			rep.setHeader("Content-Disposition",
					"attachment;filename=" + new String(fileName.getBytes(), "ISO-8859-1"));
			// 中文名称进行转码
			// 调用导出业务
			goodsBiz.export(rep.getOutputStream(), getT1());
		} catch (UnsupportedEncodingException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
      }
    
  //商品的导入
    public String doImport() {
        try {
            if (!fileContentType.equals("application/vnd.ms-excel")) {
                ajaxReturn(false, "上传的文件类型不正确");
            } else {
                int[] re = goodsBiz.doImport(new FileInputStream(file));
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

        String fileName =  "商品数据表.xls";
        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO-8859-1"));
            goodsBiz.exportModel(response.getOutputStream(), getT1());
        } catch (IOException e) {
            e.printStackTrace();
        }
    } 
}
