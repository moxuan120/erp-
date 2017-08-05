package cn.qgg.erp.biz;

import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;

import cn.qgg.erp.entity.Emp;
import cn.qgg.erp.entity.Goods;
import cn.qgg.erp.entity.Supplier;

public interface IGoodsBiz extends IbaseBiz<Goods>{
	 //商品的导出
    public  void  export(OutputStream os,Goods t1);
    //商品的导入
    public int[] doImport(InputStream is) throws  Exception;  
    //下载模板
    void exportModel(ServletOutputStream os, Goods t1);
}
