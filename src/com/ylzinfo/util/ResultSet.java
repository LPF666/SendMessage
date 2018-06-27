/**
 * 
 */
package com.ylzinfo.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * ���ݵ����ݼ�
 * 
 * @author qinlei
 * @date 2011-4-12
 */
public class ResultSet {

    private String name = null;

    private String tableName = null;

    private boolean isView = true;

    private String columns = null;

    private int totalNum = 0;

    private List<HashMap<String, String>> resultSet = null;

    private JdbcTemplate template = null;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * �������ݼ�����
     * 
     * @param name
     *            ���ݼ�����
     */
    public void setName(String name) {
        this.name = name.toLowerCase();
    }

    /**
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * �������ݼ���Ӧ�����ݿ����������������Ϊ��
     * 
     * @param tableName
     *            ���ݿ����
     */
    public void setTableName(String tableName) {
        this.tableName = tableName.toLowerCase();
    }

    /**
     * @return the columns
     */
    public String getColumns() {
        return columns;
    }
    
    /**
     * �ֶ�����
     * @return
     * @author ����
     */
    public String[] getColumnNames() {
        String[] arrayColumn = null;
        if (null != this.getColumns() && !"".equals(this.getColumns())) {
            arrayColumn = this.columns.split(",");
            for (int i = 0; i < arrayColumn.length; i++) {
                arrayColumn[i] = arrayColumn[i].split(":")[1];
            }
        } else {
            arrayColumn = new String[] {};
        }
        return arrayColumn;
    }
    
    /**
     * �ֶκ�������
     * @return
     * @author ����
     */
    public String[] getColumnLabels() {
        String[] arrayColumn = this.columns.split(",");
        for (int i = 0; i < arrayColumn.length; i++) {
            arrayColumn[i] = arrayColumn[i].split(":")[0];
        }
        return arrayColumn;
        
    }

    /**
     * �������ݼ��м�¼��������
     * 
     * @param columns
     *            ��ʽ��"�ֶκ�����:�ֶ���[,�ֶκ�����:�ֶ���]"
     */
    public void setColumns(String columns) {
        this.columns = null == columns? columns : columns.toUpperCase();
    }

    /**
     * @return the isView
     */
    public boolean isView() {
        return isView;
    }

    /**
     * �������ݼ��Ƿ��Ӧ���ݿ��
     * 
     * @param isView
     *            true �ж�Ӧ�����ݿ��false ��Ӧ��Ϊ��ͼ�򲻴��ڶ�Ӧ�ı�
     */
    public void setView(boolean isView) {
        this.isView = isView;
    }

    /**
     * @return the totalNum
     */
    public int getTotalNum() {
        return totalNum;
    }

    /**
     * ���ô˴β�ѯ���������ļ�¼��������ҳ��ѯʹ�ã��������������startNum��queryNumʱ���ô����ԡ�
     * 
     * @param totalNum
     *            value-0 ����ʾ��¼��������0 soap������totalNum����ҳ��ѯʱʹ��
     */
    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    /**
     * @return the resultSet
     */
    public List<HashMap<String, String>> getResultSet() {
        return resultSet;
    }

    /**
     * �������ݼ�
     * 
     * @param resultSet
     *            ���ݼ���ÿ����¼��һ��HashMap���󱣴档
     */
    public void setResultSet(List<HashMap<String, String>> resultSet) {
        this.resultSet = resultSet;
    }

    /**
     * @return the template
     */
    public JdbcTemplate getTemplate() {
        return template;
    }

    /**
     * ���ö�Ӧ��JDBCTemplate
     * 
     * @param template
     *            the template to set
     */
    public void setTemplate(JdbcTemplate template) {
        this.template = template;
    }

    /**
     * �����ݼ�������һ����¼
     * 
     * @param item
     * @author ����
     */
    public void addItem(HashMap<String, String> item) {
        if (null != item) {
            if (null == this.resultSet) {
                this.resultSet = new ArrayList<HashMap<String, String>>();
            }
            this.resultSet.add(item);
        }
    }
}
