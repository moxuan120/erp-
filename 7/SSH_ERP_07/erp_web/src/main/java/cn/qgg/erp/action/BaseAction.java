package cn.qgg.erp.action;

import cn.qgg.erp.biz.IbaseBiz;
import cn.qgg.erp.entity.Emp;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseAction<T> {
    protected static final String SUCCESS = "success";
    private IbaseBiz<T> baseBiz;
    private T t1;   //主实体类/过滤条件1
    private T t2;   //过滤条件2
    private Object param;   //分页查询过滤参数
    private int page;   //当前页
    private int rows;   //分页行数
    private Long id;    //主键
    private String q;   //搜索参数
    private Map<String, Object> jsonResult = new HashMap<>(); //json返回数据
    private List<T> simpleList; //json返回数据
    private Jedis jedis;

    public Emp getUser() {
        return (Emp) SecurityUtils.getSubject().getPrincipal();
    }

    /**
     * @return
     * @throws IOException 简单列表，无分页，无条件，用于获取类型列表
     */
    public String simpleList() throws IOException {
        simpleList = baseBiz.findList(null, null, null);
        return "simpleList";
    }

    /**
     * @return
     * @throws IOException 分页条件查询
     */
    public String list() throws IOException {
        int firstResult = (page - 1) * rows;
        List<T> list = baseBiz.findPageList(t1, t2, param, firstResult, rows);
        long count = baseBiz.findCount(t1, t2, param);
        jsonResult.put("rows", list);
        jsonResult.put("total", count);
        return SUCCESS;
    }

    /**
     * @return
     * @throws Exception 增加一个数据
     */
    public String add() throws Exception {
        try {
            baseBiz.add(t1);
            ajaxReturn(true, "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxReturn(false, "保存失败");
            throw e;
        }
        return SUCCESS;
    }

    /**
     * @return
     * @throws Exception 获取单个数据
     */
    public String get() throws Exception {
        T t = baseBiz.get(id);

        formatJson(t, "t1");

        return SUCCESS;
    }


    /**
     * @param t   格式化类
     * @param key 格式化的key
     * @throws IllegalAccessException 格式化get()方法返回的json数据
     *                                原因:form.load不能解析json，只解析key：value
     */
    protected void formatJson(Object t, String key) throws IllegalAccessException {
        Class cls = t.getClass();
        Field[] fields = cls.getDeclaredFields();//取出属性列表
        for (Field field : fields) { //遍历
            try {
                field.get(t);
            } catch (IllegalAccessException e) { //排除公有静态变量
                field.setAccessible(true); //去除私有化
                String fieldName = "." + field.getName();//取出属性名称
                Object o = field.get(t); //获取属性值
                if (o == null) continue; //排除null
                if (fieldName.equals(".pwd") || fieldName.equals(".orders")) continue;//排除特定属性
                if (!(o instanceof Serializable)) { //属性为类递归
                    String oClassName = StringUtils.uncapitalize(o.getClass().getSimpleName());
                    formatJson(o, key + "." + oClassName);
                } else jsonResult.put(key + fieldName, o);//存入jsonResult
            }
        }
    }

    /**
     * @return
     * @throws Exception 根据id删除一行数据
     */
    public String del() throws Exception {
        try {
            baseBiz.delete(id);
            ajaxReturn(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxReturn(false, "删除失败：" + e.getMessage());
            throw e;
        }
        return SUCCESS;
    }

    /**
     * @return
     * @throws Exception 更新一行数据
     */
    public String update() throws Exception {
        try {
            baseBiz.update(t1);
            ajaxReturn(true, "更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxReturn(false, "更新失败");
            throw e;
        }
        return SUCCESS;
    }

    /**
     * @param success ：操作是否成功
     * @param message ：回显的信息
     *                ajax回调参数，用于回显信息
     */
    protected void ajaxReturn(boolean success, String message) {
        jsonResult.put("success", success);
        jsonResult.put("message", message);
    }

    public void setBaseBiz(IbaseBiz<T> baseBiz) {
        this.baseBiz = baseBiz;
    }

    public void setT1(T t1) {
        this.t1 = t1;
    }

    public void setT2(T t2) {
        this.t2 = t2;
    }

    public void setParam(Object param) {
        this.param = param;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public T getT1() {
        return t1;
    }

    public T getT2() {
        return t2;
    }

    public Map<String, Object> getJsonResult() {
        return jsonResult;
    }

    public List<T> getSimpleList() {
        return simpleList;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }
}
