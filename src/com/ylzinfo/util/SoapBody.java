/**
 * 
 */
package com.ylzinfo.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ESB服务返回的SOAP中的body部分
 * @author qinlei
 * @date 2011-4-7
 */
public class SoapBody {

    private HashMap<String,List<HashMap<String, String>>> structsMap = new HashMap<String,List<HashMap<String, String>>>();
    
    private HashMap<String, String> structsDesMap = new HashMap<String, String>();
    
    private HashMap<String,List<HashMap<String, String>>>  resultSetMap = new HashMap<String,List<HashMap<String, String>>>();
    
    private HashMap<String, String> totalNumMap = new HashMap<String, String>();
    
    private HashMap<String, String> singleMap = new HashMap<String, String>();
    
    private boolean isOk = true;
    
    private String msg = null;
    
    public static final String MSG_OK = "ok";
    
    /**
     * 服务是否执行成功
     * @return
     * @author 秦磊
     * @date 2011-4-7
     */
    public boolean isOk() {
        return this.isOk;
    }
    
    /**
     * 根据返回的字符串设置执行结果
     * @param msg
     * @author 秦磊
     * @date 2011-4-7
     */
    public void setResult(String msg) {
        if (msg.equals(MSG_OK)){
            this.isOk = true;
            this.setMsg(msg);
        } else {
            this.isOk = false;
            this.setMsg(msg);
        }
    }
    
    /**
     * 获取数据集的结构描述。
     * @param name 数据集名称
     * @return 格式："字段汉字名:字段名[,字段汉字名:字段名]"
     * @author 秦磊
     */
    public String getStructDesc(String name) {
        return this.structsDesMap.get(name);
    }
    
    /**
     * 设置数据集的机构描述
     * @param name 数据集名称
     * @param desc 结构描述。格式："字段汉字名:字段名[,字段汉字名:字段名]"
     * @author 秦磊
     */
    public void setStructDesc(String name, String desc) {
        this.structsDesMap.put(name, desc);
    }
    
    /**
     * 获取服务执行失败时返回的失败信息
     * @return
     * @author 秦磊
     * @date 2011-4-7
     */
    public String getMsg() {
        return this.msg;
    }
    
    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    /**
     * 根据数据集名称获取数据集数据。
     * @param name
     * @return
     * @author 秦磊
     * @date 2011-4-7
     */
    public List<HashMap<String, String>> getResultSet(String name) {
        return (List<HashMap<String, String>>)this.resultSetMap.get(name);
    }
    
    /**
     * 返回所有数据集的名称集合
     * @return
     * @author 秦磊
     * @date 2011-4-20
     */
    public String[] getResultSetNames() {
        Object[] p = (Object[])this.resultSetMap.keySet().toArray();
        String[] rP = new String[p.length];
        for (int i = 0; i < p.length; i++) {
            rP[i] = (String)p[i];
        }
        return rP;
    }
    
    public void setResultSet(String name, List<HashMap<String, String>> resultSet) {
        this.resultSetMap.put(name, resultSet);
    }
    
    /**
     * 根据数据集名称获取数据集的数据结构
     * @param name
     * @return
     * @author 秦磊
     * @date 2011-4-7
     */
    public List<HashMap<String, String>> getStructs(String name) {
        return (List<HashMap<String, String>>)this.structsMap.get(name);
    }
    
    public void setStructs(String name, List<HashMap<String, String>> structs) {
        this.structsMap.put(name, structs);
    }
    
    /**
     * 取数据集的记录数
     * @param resultSetName
     * @return
     * @author 秦磊
     * @date 2011-4-7
     */
    public int getTotalNum(String resultSetName) {
        String tn = (String)this.totalNumMap.get(resultSetName);
        int r = 0;
        if (null == tn) {
            r = this.getResultSet(resultSetName).size();
        } else {
            r = Integer.parseInt(tn);
        }
        return r;
    }
    
    public void setTotalNum(String resultSetName, String value) {
        this.totalNumMap.put(resultSetName, value);
    }
    
    /**
     * 根据参数名获取独立非数据集参数
     * @param name
     * @return
     * @author 秦磊
     * @date 2011-4-7
     */
    public String getData(String name) {
        return (String) this.singleMap.get(name);
    }
    
    public void setData(String name, String value) {
        this.singleMap.put(name, value);
    }

    /**
     * 获取单挑记录数据的名称集合
     * @return
     * @author 秦磊
     * @date 2011-4-20
     */
    public String[] getDataNames() {
        Object[] p = (Object[])this.singleMap.keySet().toArray();
        String[] rP = new String[p.length];
        for (int i = 0; i < p.length; i++) {
            rP[i] = (String)p[i];
        }
        return rP;
    }
    
  
    public void toResponseEntity(ResponseEntity entity) {
        if (this.isOk()) {
            //设置单个参数的名称
            String[] nS = this.getDataNames();
            String[] onS = entity.getParaName();

            //设置单个参数的值
            String[] pV = new String[nS.length];
            for (int i = 0; i < pV.length; i++) {
                pV[i] = this.getData(nS[i]);
            }
            if (null != onS && 0 < onS.length) {
                String[] nnS = new String[nS.length + onS.length];
                System.arraycopy(onS, 0, nnS, 0, onS.length);
                System.arraycopy(nS, 0, nnS, onS.length, nS.length);
                String[] npV = new String[nnS.length];
                String[] opV = entity.getParaValue();
                if (null != opV && 0 < opV.length) {
                    System.arraycopy(opV, 0, npV, 0, opV.length);
                }
                System.arraycopy(pV, 0, npV, onS.length, pV.length);
            }
            entity.setParaName(nS);
            entity.setParaValue(pV);

            //设置数据集参数
            ArrayList<ResultSet> opL = entity.getResultSetList();
            ArrayList<ResultSet> pL = null;
            if (null == opL || 0 == opL.size()) {
                pL = new ArrayList<ResultSet>();
            } else {
                pL = opL;
            }
            String[] rsnS = this.getResultSetNames();
            ResultSet rs = null;
            String tn = null;
            List<HashMap<String, String>> p = null;
            for (int i = 0; i < rsnS.length; i++) {
                tn = rsnS[i];
                rs = new ResultSet();
                p = this.getResultSet(tn);
                rs.setName(tn);
                rs.setColumns(this.getStructDesc(tn));
                rs.setResultSet(p);
                rs.setTableName(tn);
                rs.setView(true);
                pL.add(rs);
            }
            entity.setResultSetList(pL);
        } else {
            entity.setErrorMsg(this.getMsg());
        }
    }

    /**
     * 移除单值参数
     * @param reqType
     * @author 秦磊
     */
    public void removeData(String reqType) {
        this.singleMap.remove(reqType);
    }
}
