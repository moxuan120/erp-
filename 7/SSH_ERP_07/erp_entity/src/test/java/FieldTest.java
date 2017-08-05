import cn.qgg.erp.entity.Orders;
import org.junit.Test;

import java.lang.reflect.Field;

public class FieldTest {
    @Test
    public void test1() throws IllegalAccessException {
        Orders t = new Orders();

        Class cls = t.getClass();
        Field[] fields = cls.getDeclaredFields();//取出属性列表
        for (Field field : fields) { //遍历
            //field.setAccessible(true); //去除私有化
            System.out.println("fieldName-----"+field.getName());
            System.out.println(field.isAccessible());
            try {
                field.get(t);
            } catch (IllegalAccessException e) {
                field.setAccessible(true); //去除私有化
                System.out.println(field.get(t));
            }
            System.out.println("---------------------------------------------------");
        }
    }
}
