package cn.qgg.erp.biz.impl;

import cn.qgg.erp.biz.ISupplierBiz;
import cn.qgg.erp.dao.ISupplierDao;
import cn.qgg.erp.entity.Supplier;
import cn.qgg.erp.exception.ErpException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * 供应商业务逻辑类
 *
 * @author Administrator
 */
public class SupplierBiz extends BaseBiz<Supplier> implements ISupplierBiz {

    private ISupplierDao supplierDao;

    @Override
    public void export(OutputStream os, Supplier t1) {
        List<Supplier> list = super.findList(t1, null, null);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(t1.getType().equals("1") ? "供应商" : "客户");
        //表头
        HSSFRow row0 = sheet.createRow(0);
        //表头内容
        String[] headerName = {"名称", "地址", "联系人", "电话", "email"};
        //列宽
        int[] widths = {4000, 8000, 2000, 3000, 8000};
        //初始化表头
        HSSFCell cell = null;
        for (int i = 0; i < headerName.length; i++) {
            cell = row0.createCell(i);
            cell.setCellValue(headerName[i]);
            sheet.setColumnWidth(i, widths[i]);
        }
        //写入数据
        int i = 1;
        for (Supplier supplier : list) {
            HSSFRow row = sheet.createRow(i);
            row.createCell(0).setCellValue(supplier.getName());
            row.createCell(1).setCellValue(supplier.getAddress());
            row.createCell(2).setCellValue(supplier.getContact());
            row.createCell(3).setCellValue(supplier.getTele());
            row.createCell(4).setCellValue(supplier.getEmail());
            i++;
        }
        //输出
        try {
            workbook.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public int[] doImport(InputStream is) throws IOException {
        //读取到re[0]条数据；更新re[1]条数据；新增re[2]条数据
        int[] re = new int[]{0, 0, 0};
        //获取工作表
        HSSFWorkbook workbook = null;
        workbook = new HSSFWorkbook(is);
        //工作表名称/type
        HSSFSheet sheet = workbook.getSheetAt(0);
        String type = sheet.getSheetName().equals("供应商") ? "1" : sheet.getSheetName().equals("客户") ? "2" : null;
        if (type == null) throw new ErpException("找不到对应名称的工作表,请检查工作表名称");

        //读取内容
        Row row = null;
        Supplier supplier = null;
        List<Supplier> list = null;
        System.out.println(sheet.getLastRowNum());
        for (int i = 1; i <= sheet.getLastRowNum(); i++, re[0]++) {
            row = sheet.getRow(i);
            supplier = new Supplier();
            //获取名字
            supplier.setName(row.getCell(0).getStringCellValue());
            //判断是否存在
            list = super.findList(null, supplier, null);
            //持久态
            if (!list.isEmpty()) {
                supplier = list.get(0);
                re[1]++;
            }
            supplier.setAddress(row.getCell(1).getStringCellValue());
            supplier.setContact(row.getCell(2).getStringCellValue());
            if(row.getCell(3).getCellTypeEnum().equals(CellType.NUMERIC)){
                supplier.setTele(row.getCell(3).getNumericCellValue() + "");
            }else{
                supplier.setTele(row.getCell(3).getStringCellValue());
            }
            supplier.setEmail(row.getCell(4).getStringCellValue());

            //持久化托管态
            if (list.isEmpty()) {
                supplier.setType(type);
                supplierDao.save(supplier);
                re[2]++;
            }
        }
        return re;
    }

    @Override
    public void exportModel(ServletOutputStream os, Supplier t1) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(t1.getType().equals("1") ? "供应商" : "客户");
        //表头
        HSSFRow row0 = sheet.createRow(0);
        //表头内容
        String[] headerName = {"名称", "地址", "联系人", "电话", "email"};
        //列宽
        int[] widths = {4000, 8000, 2000, 3000, 8000};
        //初始化表头
        HSSFCell cell = null;
        for (int i = 0; i < headerName.length; i++) {
            cell = row0.createCell(i);
            cell.setCellValue(headerName[i]);
            sheet.setColumnWidth(i, widths[i]);
        }
        //输出
        try {
            workbook.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setSupplierDao(ISupplierDao supplierDao) {
        this.supplierDao = supplierDao;
        super.setBaseDAO(this.supplierDao);
    }

}
