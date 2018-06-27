package com.ylzinfo.psBusiness.pdf.po;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.apache.log4j.Logger;

import com.ylzinfo.psBusiness.pdf.creater.Cbjfpz;
import com.ylzinfo.psBusiness.pdf.dto.CbjfpzDTO;
import com.ylzinfo.util.DBQueryUtil;
import com.ylzinfo.util.JdbcUtil;

public class PdfPrintPO {
	static Logger logger = Logger.getLogger(PdfPrintPO.class);

	/**
	 * 参保缴费凭证
	 * 
	 * @param aac002
	 * @return
	 */
	public String getCbjfpz(String aac002, String aae140) throws Exception {

		String sqlac01 = "select sac101 from ac01 where aac002='"+aac002+"'" ;
		List<CbjfpzDTO> listac01 = DBQueryUtil.query("sz", sqlac01, CbjfpzDTO.class);
		if(null == listac01 || listac01.size() == 0){
			throw new Exception("找不到人员信息");
		}
		else if(listac01.size() > 1){
			throw new Exception("身份证号码找到多条人员信息，请到经办窗口办理");
		}
		
		String sql = "select * from person_prove t where sac101="
				+ "p_v_param.set_Id('"+listac01.get(0).getSac101()+"')" 
				+ " and aae140='"+aae140+"' order by nd";

		List<CbjfpzDTO> list = DBQueryUtil.query("sz", sql, CbjfpzDTO.class);
		if(null != list && list.size() > 0) {
			String check = checkPrint(list.get(0).getSac101(),aae140);
			if(!"1".equals(check)){
				throw new Exception(check);
			}
			Cbjfpz pdfcreate = new Cbjfpz();
			String base64 = pdfcreate.getPdfString(list);
			return base64;
		}else{
			throw new Exception("找不到参保缴费凭证信息");
		}
	}
	
	/**
	 * 校验是否已打印
	 * @param sac101 人员编号
	 * @param aae140 险种代码
	 * @return 返回1，表示校验通过，可以打印，不为1，即不可打印原因
	 */
	private String checkPrint(String sac101, String aae140){
		Connection conn = null;
		CallableStatement cscheck = null;
		CallableStatement csprint = null;
		String ret = "";
		try {
			conn = JdbcUtil.getConnection("sz");
			//判断是否打印
			cscheck=conn.prepareCall("{call sp_get_pzdy(?,?,?,?)}");
			
			cscheck.setString(1,sac101);
			cscheck.setString(2,aae140);
			cscheck.setString(3,"1");
			cscheck.registerOutParameter(4,Types.NUMERIC);
			cscheck.execute();
			int pi_out = cscheck.getInt(4);
			if(pi_out == 0){
				//插入打印日志
				csprint=conn.prepareCall("{call sp_zmdy_insert(?,?,?)}");
				csprint.setString(1,sac101);
				csprint.setString(2,aae140);
				csprint.setString(3,"1");
				csprint.execute();
				ret = "1";
			}
			else {
				ret = "该险种当天已打印，不能再次打印！";
			}
		}catch (SQLException e) {
			logger.error(e);
			ret = e.getMessage();
		} catch (Exception e) {
			logger.error(e);
			ret = e.getMessage();
		} 
		finally {
			try {
				if (cscheck != null) {
					cscheck.close();
					cscheck = null;
				}
				if (csprint != null) {
					csprint.close();
					csprint = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				logger.error(e);
				ret = e.getMessage();
			}
		}
		return ret;
	}
}
