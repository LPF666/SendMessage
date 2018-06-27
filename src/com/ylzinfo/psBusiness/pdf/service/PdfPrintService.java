package com.ylzinfo.psBusiness.pdf.service;

import com.ylzinfo.esb.soap.ReaderSoapXmlOut;
import com.ylzinfo.psBusiness.pdf.dto.ReturnDTO;
import com.ylzinfo.psBusiness.pdf.po.PdfPrintPO;

public class PdfPrintService {

	/**
	 * 打印参保缴费凭证   ylz.ps.sx.si.printCbjfpz
	 * @param aac002
	 * @return
	 * @throws Exception
	 */
	public String printCbjfpz(String aac002, String aae140) throws Exception{
		PdfPrintPO po = new PdfPrintPO();
		ReturnDTO dto = new ReturnDTO();
		try {
			dto.setPdfb64(po.getCbjfpz(aac002, aae140));
			return ReaderSoapXmlOut.readerSoapXMlOut(dto.getColset(),
					dto.getStructsList(), "");
		} catch (Exception e) {
			return ReaderSoapXmlOut.BuildSoapXMlError(901, e.getMessage());
		}
		
	}
}
