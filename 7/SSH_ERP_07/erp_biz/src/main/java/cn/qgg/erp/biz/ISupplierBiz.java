package cn.qgg.erp.biz;
import cn.qgg.erp.entity.Supplier;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 供应商业务逻辑层接口
 * @author Administrator
 *
 */
public interface ISupplierBiz extends IbaseBiz<Supplier>{

    void export(OutputStream os, Supplier t1);

    int[] doImport(InputStream is) throws IOException;

    void exportModel(ServletOutputStream os, Supplier t1);
}

