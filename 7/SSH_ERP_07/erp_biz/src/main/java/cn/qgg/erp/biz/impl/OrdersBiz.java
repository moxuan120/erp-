package cn.qgg.erp.biz.impl;

import cn.qgg.erp.biz.IOrdersBiz;
import cn.qgg.erp.dao.IEmpDao;
import cn.qgg.erp.dao.IOrdersDao;
import cn.qgg.erp.dao.ISupplierDao;
import cn.qgg.erp.entity.Orderdetail;
import cn.qgg.erp.entity.Orders;
import cn.qgg.erp.exception.ErpException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;


public class OrdersBiz extends BaseBiz<Orders> implements IOrdersBiz {
    private static final Logger log = LoggerFactory.getLogger(OrdersBiz.class);
    private IOrdersDao ordersDao;
    private IEmpDao empDao;
    private ISupplierDao supplierDao;

    @Override
    public void add(Orders orders) {
        orders.setCreatetime(new Date());//生成时间
        orders.setState(orders.STATE_CREATE);//未审核
        double total = 0.0;
        for (Orderdetail orderdetail : orders.getOrderdetails()) {
            orderdetail.setOrders(orders);
            orderdetail.setState(orderdetail.STATE_NOT_IN);
            orderdetail.setMoney(orderdetail.getPrice() * orderdetail.getNum());
            total += orderdetail.getMoney();
        }
        orders.setTotalmoney(total);
        log.info("添加一笔订单，订单类型为：{}", (orders.getType().equals("1") ? "采购" : "销售"));
        super.add(orders);
    }

    @Override
    public List<Orders> findPageList(Orders t1, Orders t2, Object param, int firstResult, int maxResult) {

        List<Orders> orders = super.findPageList(t1, t2, param, firstResult, maxResult);
        log.info("查询{}条订单信息",orders.size());
        //补上这些操作员的名称和供应商的名称
        for (Orders order : orders) {
            order.setCreaterName(getName(order.getCreater(), empDao));
            order.setCheckerName(getName(order.getChecker(), empDao));
            order.setStarterName(getName(order.getStarter(), empDao));
            order.setEnderName(getName(order.getEnder(), empDao));
            order.setSupplierName(getName(order.getSupplieruuid(), supplierDao));
        }
        log.info("补全orders的操作员名称和供应商名称");
        return orders;
    }

    /**
     * 审核订单
     *
     * @param uuid    订单编号
     * @param empuuid 员工编号
     */
    @Override
    public void doCheck(Long uuid, Long empuuid) {
        Orders orders = ordersDao.get(uuid);
        if (!Orders.STATE_CREATE.equals(orders.getState())) {
            throw new ErpException("该订单已经审核，不能重复审核");
        }
        orders.setChecker(empuuid);
        orders.setChecktime(new Date());
        orders.setState(Orders.STATE_CHECK);
    }

    /**
     * 确认订单
     *
     * @param uuid    订单编号
     * @param empuuid 员工编号
     */
    @Override
    public void doStart(long uuid, Long empuuid) {
        Orders orders = ordersDao.get(uuid);
        if (!Orders.STATE_CHECK.equals(orders.getState())) {
            throw new ErpException("该订单已经确认，不能重复确认");
        }
        orders.setStarter(empuuid);
        orders.setStarttime(new Date());
        orders.setState(Orders.STATE_START);
    }

    @Override
    public void exportExcel(OutputStream ops, Long uuid) {
        //查询数据
        Orders orders = super.get(uuid);
        List<Orderdetail> orderdetails = orders.getOrderdetails();

        //<editor-fold desc="创建表格">
        //类型判断
        String type = orders.getType();
        String sheetName = null;//表头
        int offset = 0;//偏移量
        if (Orders.TYPE_IN.equals(type)) {
            sheetName = "采 购 单";
            offset = 2;
        }
        if (Orders.TYPE_OUT.equals(type)) {
            sheetName = "销 售 单";
        }
        //创建工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建工作表
        HSSFSheet sheet = workbook.createSheet(sheetName);
        //内容样式
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        //创建8+offset行4列
        for (int i = 2; i < 8 + offset + orderdetails.size(); i++) {
            HSSFRow row = sheet.createRow(i);
            row.setHeight((short) 500);//设置内容行高
            for (int j = 0; j < 4; j++) {
                HSSFCell cell = row.createCell(j);
                cell.setCellStyle(cellStyle);//设置内容样式
            }
        }
        //合并单元格
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 3));
        sheet.addMergedRegion(new CellRangeAddress(5 + offset, 5 + offset, 0, 3));
        //文本内容
        sheet.createRow(0).createCell(0).setCellValue(sheetName);
        sheet.getRow(2).getCell(0).setCellValue("供应商");

        sheet.getRow(3).getCell(0).setCellValue("下单日期");
        sheet.getRow(3).getCell(2).setCellValue("经办人");
        if (type.equals(Orders.TYPE_IN)) {//采购多两行
            sheet.getRow(4).getCell(0).setCellValue("审核日期");
            sheet.getRow(4).getCell(2).setCellValue("经办人");
            sheet.getRow(5).getCell(0).setCellValue("采购日期");
            sheet.getRow(5).getCell(2).setCellValue("经办人");
        }
        sheet.getRow(4 + offset).getCell(0).setCellValue(type.equals("1") ? "入库日期" : "出库日期");
        sheet.getRow(4 + offset).getCell(2).setCellValue("经办人");

        sheet.getRow(5 + offset).getCell(0).setCellValue("订单明细");
        sheet.getRow(6 + offset).getCell(0).setCellValue("商品名称");
        sheet.getRow(6 + offset).getCell(1).setCellValue("数量");
        sheet.getRow(6 + offset).getCell(2).setCellValue("价格");
        sheet.getRow(6 + offset).getCell(3).setCellValue("金额");

        //标题行高
        sheet.getRow(0).setHeight((short) 1000);
        //列宽
        for (int i = 0; i < 4; i++) {
            sheet.setColumnWidth(i, 5000);
        }

        //内容部分对齐
        cellStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        //内容部分字体
        HSSFFont font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 11);
        cellStyle.setFont(font);

        //标题对齐
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        //标题字体
        HSSFFont headerFont = workbook.createFont();
        headerFont.setFontName("黑体");
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 18);
        headerStyle.setFont(headerFont);
        sheet.getRow(0).getCell(0).setCellStyle(headerStyle);

        //日期样式
        HSSFCellStyle style_date = workbook.createCellStyle();
        style_date.cloneStyleFrom(cellStyle);
        HSSFDataFormat dataFormat = workbook.createDataFormat();
        style_date.setDataFormat(dataFormat.getFormat("yyyy-MM-dd hh:mm"));
        //设置日期格式
        sheet.getRow(3).getCell(1).setCellStyle(style_date);
        sheet.getRow(4).getCell(1).setCellStyle(style_date);
        if (type.equals(Orders.TYPE_IN)) {//采购多两行
            sheet.getRow(5).getCell(1).setCellStyle(style_date);
            sheet.getRow(6).getCell(1).setCellStyle(style_date);
        }
        //</editor-fold>

        //<editor-fold desc="写入表格数据">

        //设置供应商
        sheet.getRow(2).getCell(1).setCellValue(supplierDao.get(orders.getSupplieruuid()).getName());
        //设置日期
        if (null != orders.getCreatetime()) sheet.getRow(3).getCell(1).setCellValue(orders.getCreatetime());
        if (type.equals(Orders.TYPE_IN)) {//采购多两行
            if (null != orders.getChecktime()) sheet.getRow(4).getCell(1).setCellValue(orders.getChecktime());
            if (null != orders.getStarttime()) sheet.getRow(5).getCell(1).setCellValue(orders.getStarttime());
        }
        if (null != orders.getEndtime()) sheet.getRow(4 + offset).getCell(1).setCellValue(orders.getEndtime());
        //设置经办人
        if (null != orders.getCreater())
            sheet.getRow(3).getCell(3).setCellValue(empDao.get(orders.getCreater()).getName());
        if (type.equals(Orders.TYPE_IN)) {//采购多两行
            if (null != orders.getChecker())
                sheet.getRow(4).getCell(3).setCellValue(empDao.get(orders.getChecker()).getName());
            if (null != orders.getStarter())
                sheet.getRow(5).getCell(3).setCellValue(empDao.get(orders.getStarter()).getName());
        }
        if (null != orders.getEnder())
            sheet.getRow(4 + offset).getCell(3).setCellValue(empDao.get(orders.getEnder()).getName());
        //订单明细
        int i = 7 + offset;
        HSSFRow row = null;
        for (Orderdetail od : orderdetails) {
            row = sheet.getRow(i);
            row.getCell(0).setCellValue(od.getGoodsname());
            row.getCell(1).setCellValue(od.getNum());
            row.getCell(2).setCellValue(od.getPrice());
            row.getCell(3).setCellValue(od.getMoney());
            i++;
        }
        //合计金额
        sheet.getRow(i).getCell(2).setCellValue("合计金额");
        sheet.getRow(i).getCell(3).setCellValue(orders.getTotalmoney());
        //</editor-fold>

        log.info("导出【{}】，编号为：{}",sheetName , orders.getUuid());
        //写入输出流
        try {
            workbook.write(ops);
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


    public void setOrdersDao(IOrdersDao ordersDao) {
        this.ordersDao = ordersDao;
        super.setBaseDAO(ordersDao);
    }

    public void setEmpDao(IEmpDao empDao) {
        this.empDao = empDao;
    }

    public void setSupplierDao(ISupplierDao supplierDao) {
        this.supplierDao = supplierDao;
    }

}
