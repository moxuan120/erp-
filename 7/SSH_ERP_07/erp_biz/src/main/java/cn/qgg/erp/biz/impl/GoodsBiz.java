package cn.qgg.erp.biz.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletOutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cn.qgg.erp.biz.IGoodsBiz;
import cn.qgg.erp.dao.IGoodsDao;
import cn.qgg.erp.dao.IGoodsTypeDao;
import cn.qgg.erp.entity.Goods;
import cn.qgg.erp.entity.GoodsType;
import cn.qgg.erp.exception.ErpException;

public class GoodsBiz extends BaseBiz<Goods> implements IGoodsBiz {
	private IGoodsDao goodsDao;
	private IGoodsTypeDao goodsTypeDao;

	public void setGoodsTypeDao(IGoodsTypeDao goodsTypeDao) {
		this.goodsTypeDao = goodsTypeDao;
	}

	public void setGoodsDao(IGoodsDao goodsDao) {
		this.goodsDao = goodsDao;
		super.setBaseDAO(goodsDao);

	}

	// 添加的代码 商品导出
	public void export(OutputStream os, Goods t1) {
		List<Goods> goodslist = goodsDao.findList(t1, null, null);
		// 创建工作
		HSSFWorkbook wk = new HSSFWorkbook();
		HSSFSheet sheet = wk.createSheet("商品数据表");
		// 创建第一行 写入表头
		HSSFRow row = sheet.createRow(0);
		// 定义好每一列的标题
		String[] headernames = { "名称", "产地", "厂家", "单位", "进货价", "销售价", "商品类型" };
		// 指定每一列的宽度
		int[] columnWidths = { 4000, 4000, 4000, 3000, 4000, 4000, 4000 };
		HSSFCell cell = null;
		for (int i = 0; i < headernames.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(headernames[i]);
			sheet.setColumnWidth(i, columnWidths[i]);
		}

		// 写入内容
		int i = 1;
		for (Goods g : goodslist) {
			row = sheet.createRow(i);
			row.createCell(0).setCellValue(g.getName());
			row.createCell(1).setCellValue(g.getOrigin());
			row.createCell(2).setCellValue(g.getProducer());
			row.createCell(3).setCellValue(g.getUnit());
			row.createCell(4).setCellValue(g.getInprice());
			row.createCell(5).setCellValue(g.getOutprice());
			row.createCell(6).setCellValue(g.getGoodsType().getName());
			i++;
		}
		// 写入到输出流中
		try {
			wk.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				wk.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public int[] doImport(InputStream is) throws Exception {
		// 读取到re[0]条数据；更新re[1]条数据；新增re[2]条数据
		int[] re = new int[] { 0, 0, 0 };
		HSSFWorkbook wb = new HSSFWorkbook(is);
		// 获取第一个工作表
		HSSFSheet sheet = wb.getSheetAt(0);
		// 读取内容
		HSSFRow row = null;
		Goods goods = null;
		List<Goods> list = null;
		for (int i = 1; i <= sheet.getLastRowNum(); i++, re[0]++) {
			// 判断 是否 存在？名称
			row = sheet.getRow(i);
			goods = new Goods();
			// 获取名字
			
			goods.setName(row.getCell(0).getStringCellValue());
			list = super.findList(null, goods, null);
			if (list.size() > 0) {
				goods = list.get(0);
				re[1]++;
				
			}
			// 设置属性内容
			goods.setOrigin(row.getCell(1).getStringCellValue());
			goods.setProducer(row.getCell(2).getStringCellValue());
			goods.setUnit(row.getCell(3).getStringCellValue());
			goods.setInprice((row.getCell(4).getNumericCellValue()));
			goods.setOutprice(row.getCell(5).getNumericCellValue());
			String goodstypeName = row.getCell(6).getStringCellValue();
			if (goods.getUuid()==null||!goods.getGoodsType().getName().equals(goodstypeName)) {
				GoodsType goodsType = new GoodsType();
				goodsType.setName(goodstypeName);
				List<GoodsType> findList = goodsTypeDao.findList(null, goodsType, null);
				if (findList.size() > 0) {
					goods.setGoodsType(findList.get(0));
				}else {
					throw new ErpException("商品类型不存在");
				}
			}
			// 如果不存在，就插入新的数据
			if (goods.getUuid()==null){
				goodsDao.save(goods);
				re[2]++;
			}
		}
		wb.close();
		return re;
	}

	public void exportModel(ServletOutputStream os, Goods t1) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("商品");
		// 表头
		HSSFRow row0 = sheet.createRow(0);
		// 定义好每一列的标题
		String[] headernames = { "名称", "产地", "厂家", "单位", "进货价", "销售价", "商品类型" };
		// 指定每一列的宽度
		int[] columnWidths = { 4000, 4000, 4000, 3000, 4000, 4000, 4000 };
		// 初始化表头
		HSSFCell cell = null;
		for (int i = 0; i < headernames.length; i++) {
			cell = row0.createCell(i);
			cell.setCellValue(headernames[i]);
			sheet.setColumnWidth(i, columnWidths[i]);
		}
		// 输出
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

}
