/**
 * 
 */
package com.ylzinfo.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 传递的数据集
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
     * 设置数据集名称
     * 
     * @param name
     *            数据集名称
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
     * 设置数据集对应的数据库表名。若不存在则为空
     * 
     * @param tableName
     *            数据库表名
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
     * 字段名称
     * @return
     * @author 秦磊
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
     * 字段汉字描述
     * @return
     * @author 秦磊
     */
    public String[] getColumnLabels() {
        String[] arrayColumn = this.columns.split(",");
        for (int i = 0; i < arrayColumn.length; i++) {
            arrayColumn[i] = arrayColumn[i].split(":")[0];
        }
        return arrayColumn;
        
    }

    /**
     * 设置数据集中记录的描述。
     * 
     * @param columns
     *            格式："字段汉字名:字段名[,字段汉字名:字段名]"
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
     * 设置数据集是否对应数据库表。
     * 
     * @param isView
     *            true 有对应的数据库表；false 对应的为视图或不存在对应的表
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
     * 设置此次查询符合条件的记录总数。分页查询使用，服务传入参数中有startNum和queryNum时设置此属性。
     * 
     * @param totalNum
     *            value-0 不显示记录总数；非0 soap中增加totalNum。分页查询时使用
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
     * 设置数据集
     * 
     * @param resultSet
     *            数据集。每条记录以一个HashMap对象保存。
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
     * 设置对应的JDBCTemplate
     * 
     * @param template
     *            the template to set
     */
    public void setTemplate(JdbcTemplate template) {
        this.template = template;
    }

    /**
     * 在数据集中增加一条记录
     * 
     * @param item
     * @author 秦磊
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
