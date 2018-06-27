package com.ylzinfo.psBusiness.pdf.creater;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import sun.misc.BASE64Encoder;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.ylzinfo.common.GlobalConst;
import com.ylzinfo.psBusiness.pdf.dto.CbjfpzDTO;
import com.ylzinfo.psBusiness.pdf.ttf.FontType;

public class Cbjfpz {

	
//	Font st9 = FontType.getST(9);//宋体9号
	Font st10 = FontType.getST(11);//宋体10号
	Font st11 = FontType.getST(13);//宋体11号
//	Font st13 = FontType.getST(13);//宋体11号
//	Font stb11 = FontType.getSTB(11);//宋体11号，加粗
//	Font stb12 = FontType.getSTB(12);//宋体12号，加粗
//	Font stb13 = FontType.getSTB(13);//宋体13号，加粗
	Font stb18 = FontType.getSTB(18);//宋体18号，加粗
//	Font ht12 = FontType.getHT(12);//黑体12号
//	Font ht18 = FontType.getHT(18);//黑体18号
	
	float tablewidth = 1700f;
	
	CbjfpzDTO data;
	List<CbjfpzDTO> datalist;
	
	PdfWriter writer;
	Document document;
	
	public String getPdfString(List<CbjfpzDTO> list) throws DocumentException, IOException {
		datalist = list;
		data = list.get(0);
		//PageSize.A4,0,0,60f,60f
		document = new Document();
		//文档属性
		document.addTitle("山西省社会保险参保缴费人员证明");
		document.addAuthor("山西省人社厅");
		document.addCreator("易联众信息技术股份有限公司");
		 
		//页边空白
		document.setMargins(-10, -10, 60f,60f);
		//document.setPageSize(PageSize.A4.rotate());
		String filename = GlobalConst.PDF_TMP_PATH +"cbjfpz"+data.getSac101()+data.getAae140()+"_tmp.pdf";
		
		writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
		document.open();
		
		//标题
		document.add(title());
		document.add(header());
		//基本信息
		document.add(baseInfo());
		document.add(fundinfo());
		//低
		document.add(footer());
		
		document.close();
		writer.close();
		waterMarkB( filename);
		return getBase64();
	}
	
	private void waterMarkB(String filename) throws IOException, DocumentException {
		String outputFile = GlobalConst.PDF_TMP_PATH +"cbjfpz"+data.getSac101()+data.getAae140()+".pdf";
		
        PdfReader reader = new PdfReader(filename);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outputFile));
        PdfContentByte under;
        String projectpath = getClass().getResource("/").getPath();
		File file = new File(projectpath);
		projectpath = file.getParent()+"/pdf/";
    	Image img = Image.getInstance(projectpath+"cbjfpz_xz.png");// 插入水印     
    	//img.scaleAbsolute(225, 90);
    	//img.scaleToFit(290, 120);
    	img.scalePercent(67);
        img.setAbsolutePosition(60, 474); 
        
        Image img2 = Image.getInstance(projectpath+"cbjfpz_zt.png");// 插入水印     
    	//img2.scaleAbsolute(127.5f, 90);
        img2.scalePercent(67);
        img2.setAbsolutePosition(310, 474); 
        
        under = stamper.getUnderContent(1);  
        under.addImage(img); 
        under.addImage(img2); 
        
        stamper.close();
        reader.close();
        
        File filetmp = new File(filename);
        filetmp.delete();
    }
	
	private String getBase64() throws IOException{
		String filename = GlobalConst.PDF_TMP_PATH +"cbjfpz"+data.getSac101()+data.getAae140()+".pdf";
		File file = new File(filename);
		FileInputStream inputFile = new FileInputStream(file);
		byte[] buffer = new byte[(int) file.length()];
		inputFile.read(buffer);
		inputFile.close();
		//file.delete();
		return new BASE64Encoder().encode(buffer);
	}
	
	private PdfPTable title() throws DocumentException{
		PdfPCell cell;
		float[] widths = {1f};  
		PdfPTable table = new PdfPTable(widths);  
		table.getDefaultCell().setHorizontalAlignment(Phrase.ALIGN_CENTER);
		table.setTotalWidth(tablewidth);
		cell = new PdfPCell(new Phrase("      ",st10));
		cell.setHorizontalAlignment(Phrase.ALIGN_CENTER);
		cell.setBorder(0);
		cell.setMinimumHeight(2);
		table.addCell(cell);
		cell = new PdfPCell(new Phrase("山西省社会保险参保缴费人员证明",stb18));
		cell.setHorizontalAlignment(Phrase.ALIGN_CENTER);
		cell.setBorder(0);
		cell.setMinimumHeight(30);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("      ",st10));
		cell.setHorizontalAlignment(Phrase.ALIGN_CENTER);
		cell.setBorder(0);
		table.addCell(cell);
		//table.getDefaultCell().setBorder(0);//设置表格默认为无边框
		
		return table;
	}
	
	private String dateFormat(String date){
		String s_date = "";
		s_date += date.substring(0, 4);
		s_date += "-"+date.substring(4, 6);
		s_date += "-"+date.substring(6, 8);
		return s_date;
	}
	
	private PdfPTable baseInfo() throws DocumentException, MalformedURLException, IOException{
		PdfPCell cell;
		float[] widths = {0.2f, 0.3f, 0.2f, 0.3f};  
		PdfPTable table = new PdfPTable(widths);  
		table.getDefaultCell().setHorizontalAlignment(Phrase.ALIGN_CENTER);
		table.getDefaultCell().setVerticalAlignment(Phrase.ALIGN_MIDDLE);
		table.setTotalWidth(tablewidth);
		table.getDefaultCell().setMinimumHeight(25);
		
		cell = new PdfPCell(new Phrase("个人基本信息",st11));
		cell.setColspan(4);
		cell.setHorizontalAlignment(Phrase.ALIGN_CENTER);
		cell.setVerticalAlignment(Phrase.ALIGN_MIDDLE);
		cell.setMinimumHeight(25);
		table.addCell(cell);
		
		table.addCell(new Phrase("社保编号",st10));
		table.addCell(new Phrase(data.getSac101(),st10));
		
		table.addCell(new Phrase("姓名",st10));
		table.addCell(new Phrase(data.getAac003(),st10));
		
		table.addCell(new Phrase("身份证号码",st10));
		table.addCell(new Phrase(data.getAac002(),st10));
		
		table.addCell(new Phrase("出生日期",st10));
		table.addCell(new Phrase(dateFormat(data.getAac006()),st10));
		
		table.addCell(new Phrase("参加工作时间",st10));
		table.addCell(new Phrase(dateFormat(data.getAac007()),st10));
		
		table.addCell(new Phrase("性别",st10));
		table.addCell(new Phrase(data.getAaa103(),st10));
		
		table.addCell(new Phrase("首次参保时间",st10));
		table.addCell(new Phrase(dateFormat(data.getAac060()),st10));
		
		table.addCell(new Phrase("户口所在地",st10));
		table.addCell(new Phrase(data.getAac010(),st10));
		
		cell = new PdfPCell(new Phrase("  单位或代理机构名称："+data.getAab004(),st10));
		cell.setColspan(4);
		cell.setHorizontalAlignment(Phrase.ALIGN_LEFT);
		cell.setVerticalAlignment(Phrase.ALIGN_MIDDLE);
		cell.setMinimumHeight(25);
		table.addCell(cell);
		
		String sx = "";
		if("140".equals(data.getAae140())){
			sx = "   参加险种：\n\n           基本养老保险√         工伤保险  \n\n           "
					+ "基本医疗保险             生育保险  \n\n           失业保险  ";
		}
		else if("002".equals(data.getAae140())){
			sx = "   参加险种：\n\n           基本养老保险             工伤保险  \n\n           "
					+ "基本医疗保险             生育保险  \n\n           失业保险√";
		}
		else if("003".equals(data.getAae140())){
			sx = "   参加险种：\n\n           基本养老保险             工伤保险  \n\n           "
					+ "基本医疗保险√         生育保险  \n\n           失业保险  ";
		}
		else if("004".equals(data.getAae140())){
			sx = "   参加险种：\n\n           基本养老保险             工伤保险√\n\n           "
					+ "基本医疗保险             生育保险  \n\n           失业保险  ";
		}
		else if("005".equals(data.getAae140())){
			sx = "   参加险种：\n\n           基本养老保险             工伤保险  \n\n           "
					+ "基本医疗保险             生育保险√\n\n           失业保险  ";
		}
		cell = new PdfPCell(new Phrase(sx,st10));
		cell.setColspan(2);
		cell.setHorizontalAlignment(Phrase.ALIGN_LEFT);
		cell.setVerticalAlignment(Phrase.ALIGN_TOP);
		cell.setMinimumHeight(90);
		table.addCell(cell);
		
		String zt = "";
		if("0".equals(data.getAac031())){
			zt = "   参保缴费状态：\n\n          正常 √        欠费   \n\n"
					+ "          停保             退休";
		}
		else if("1".equals(data.getAac031())){
			zt = "   参保缴费状态：\n\n          正常             欠费 √  \n\n"
					+ "          停保             退休";
		}
		else if("2".equals(data.getAac031())){
			zt = "   参保缴费状态：\n\n          正常             欠费   \n\n"
					+ "          停保 √        退休";
		}
		else if("3".equals(data.getAac031())){
			zt = "   参保缴费状态：\n\n          正常             欠费   \n\n"
					+ "          停保             退休 √";
		}
		cell = new PdfPCell(new Phrase(zt,st10));
		cell.setColspan(2);
		cell.setHorizontalAlignment(Phrase.ALIGN_LEFT);
		cell.setVerticalAlignment(Phrase.ALIGN_TOP);
		cell.setMinimumHeight(90);
		table.addCell(cell);
		
		return table;
	}
	
	
	/**
	 * 
	 * @return
	 */
	private PdfPTable footer(){
		PdfPCell cell;
		float[] widths2 = {0.60f, 0.40f};  
		PdfPTable table2 = new PdfPTable(widths2);  
		table2.getDefaultCell().setHorizontalAlignment(Phrase.ALIGN_LEFT);
		table2.getDefaultCell().setVerticalAlignment(Phrase.ALIGN_MIDDLE);
		table2.setTotalWidth(tablewidth);
		table2.getDefaultCell().setMinimumHeight(20);
		table2.getDefaultCell().setBorder(0);
		
		cell = new PdfPCell(new Phrase("       验证说明：登录“www.sxshbxj.com”网站后，输入本人社保编号和身份证号可查询到个人账户明细。",st10));
		cell.setColspan(2);
		cell.setHorizontalAlignment(Phrase.ALIGN_LEFT);
		cell.setVerticalAlignment(Phrase.ALIGN_TOP);
		cell.setMinimumHeight(30);
		cell.setBorder(0);
		table2.addCell(cell);
		
		table2.addCell(new Phrase("",st10));
		cell = new PdfPCell(new Phrase("（章）",st10));
		cell.setHorizontalAlignment(Phrase.ALIGN_CENTER);
		cell.setVerticalAlignment(Phrase.ALIGN_MIDDLE);
		cell.setMinimumHeight(20);
		cell.setBorder(0);
		table2.addCell(cell);
		
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
		
		table2.addCell(new Phrase("经办人签章：      系统管理 ",st10));
		cell = new PdfPCell(new Phrase(format.format(date),st10));
		cell.setHorizontalAlignment(Phrase.ALIGN_CENTER);
		cell.setVerticalAlignment(Phrase.ALIGN_MIDDLE);
		cell.setMinimumHeight(20);
		cell.setBorder(0);
		table2.addCell(cell);

		return table2;
	}
	
	private PdfPTable header() throws DocumentException{
		PdfPCell cell;
		float[] widths = {0.16f,0.84f};  
		PdfPTable table = new PdfPTable(widths);  
		table.getDefaultCell().setHorizontalAlignment(Phrase.ALIGN_CENTER);
		table.setTotalWidth(tablewidth);
		cell = new PdfPCell(new Phrase("编 号：",st10));
		cell.setHorizontalAlignment(Phrase.ALIGN_LEFT);
		cell.setBorder(0);
		cell.setMinimumHeight(15);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("机制表",st10));
		cell.setHorizontalAlignment(Phrase.ALIGN_RIGHT);
		cell.setBorder(0);
		cell.setMinimumHeight(15);
		table.addCell(cell);
		
		return table;
	}
	
	private PdfPTable fundinfo() throws DocumentException{
		PdfPCell cell;
		float[] widths = {0.1f,0.3f,0.1f,0.15f,0.2f,0.15f};  
		PdfPTable table = new PdfPTable(widths);  
		table.getDefaultCell().setHorizontalAlignment(Phrase.ALIGN_CENTER);
		table.setTotalWidth(tablewidth);
		
		cell = new PdfPCell(new Phrase("在职人员个人账户明细",st11));
		cell.setColspan(6);
		cell.setHorizontalAlignment(Phrase.ALIGN_CENTER);
		cell.setVerticalAlignment(Phrase.ALIGN_MIDDLE);
		cell.setMinimumHeight(25);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("年度",st10));
		cell.setHorizontalAlignment(Phrase.ALIGN_CENTER);
		cell.setMinimumHeight(20);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("起止年月",st10));
		cell.setHorizontalAlignment(Phrase.ALIGN_CENTER);
		cell.setMinimumHeight(20);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("缴费月数",st10));
		cell.setHorizontalAlignment(Phrase.ALIGN_CENTER);
		cell.setMinimumHeight(20);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("月缴费工资",st10));
		cell.setHorizontalAlignment(Phrase.ALIGN_CENTER);
		cell.setMinimumHeight(20);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("上年度月社平工资",st10));
		cell.setHorizontalAlignment(Phrase.ALIGN_CENTER);
		cell.setMinimumHeight(20);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("年缴费额",st10));
		cell.setHorizontalAlignment(Phrase.ALIGN_CENTER);
		cell.setMinimumHeight(20);
		table.addCell(cell);

		for(int i=0;i < datalist.size();i++){
			CbjfpzDTO dto = datalist.get(i);
			cell = new PdfPCell(new Phrase(dto.getNd(),st10));
			cell.setHorizontalAlignment(Phrase.ALIGN_CENTER);
			cell.setMinimumHeight(20);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(dto.getQzny(),st10));
			cell.setHorizontalAlignment(Phrase.ALIGN_CENTER);
			cell.setMinimumHeight(20);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(dto.getBac004(),st10));
			cell.setHorizontalAlignment(Phrase.ALIGN_CENTER);
			cell.setMinimumHeight(20);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(dto.getAae180(),st10));
			cell.setHorizontalAlignment(Phrase.ALIGN_CENTER);
			cell.setMinimumHeight(20);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(dto.getAac040(),st10));
			cell.setHorizontalAlignment(Phrase.ALIGN_CENTER);
			cell.setMinimumHeight(20);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(dto.getAae019(),st10));
			cell.setHorizontalAlignment(Phrase.ALIGN_CENTER);
			cell.setMinimumHeight(20);
			table.addCell(cell);
		}
		return table;
	}
}
