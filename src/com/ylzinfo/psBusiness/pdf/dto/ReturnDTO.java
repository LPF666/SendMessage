package com.ylzinfo.psBusiness.pdf.dto;

import com.ylzinfo.fj.utils.BaseDTO;
import com.ylzinfo.fj.utils.OutPara;

public class ReturnDTO extends BaseDTO {
	@OutPara(index = 0, label="pdf的Base64编码")
	private String pdfb64;

	/**
	 * @return the pdfb64
	 */
	public String getPdfb64() {
		return pdfb64;
	}

	/**
	 * @param pdfb64 the pdfb64 to set
	 */
	public void setPdfb64(String pdfb64) {
		this.pdfb64 = pdfb64;
	}
	
}
