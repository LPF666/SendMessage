/**
 * 
 */
package com.ylzinfo.util;

import java.util.ArrayList;

/**
 * @author qinlei
 * @date 2011-4-2
 */
public class ResponseEntity {

    /**
     * 单个参数名称
     */
    private String[] paraName = null;

    /**
     * 单个参数值
     */
    private String[] paraValue = null;

    private boolean isOk = true;

    /**
     * 失败信息
     */
    private String errMsg = null;

    /**
     * 服务执行是否成功
     * 
     * @return
     * @author 秦磊
     */
    public boolean isOk() {
        return this.isOk;
    }

    /**
     * @return the msgInfo
     */
    public String getErrorMsg() {
        return errMsg;
    }

    /**
     * 设置失败信息
     * 
     * @param errorMsg
     *            the msgInfo to set
     */
    public void setErrorMsg(String errorMsg) {
        this.isOk = false;
        this.errMsg = errorMsg;
    }

    private ArrayList<ResultSet> resultSetList = new ArrayList<ResultSet>();

    /**
     * 获取参数名称
     * @return the paraName
     */
    public String[] getParaName() {
        return paraName;
    }

    /**
     * @param paraName
     *            the paraName to set
     */
    public void setParaName(String[] paraName) {
        this.paraName = paraName;
    }

    /**
     * @return the paraValue
     */
    public String[] getParaValue() {
        return paraValue;
    }

    /**
     * @param paraValue
     *            the paraValue to set
     */
    public void setParaValue(String[] paraValue) {
        this.paraValue = paraValue;
    }

    /**
     * @return the resultSetList
     */
    public ArrayList<ResultSet> getResultSetList() {
        return resultSetList;
    }

    /**
     * @param resultSetList
     *            the resultSetList to set
     */
    public void setResultSetList(ArrayList<ResultSet> resultSetList) {
        this.resultSetList = resultSetList;
    }

    /**
     * @param resultSetList
     *            the resultSetList to set
     */
    public void setResultSet(ResultSet resultSet) {
        if (null != resultSet) {
            this.resultSetList.add(resultSet);
        }
    }
}
