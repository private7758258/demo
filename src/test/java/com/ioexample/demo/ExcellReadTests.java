
import com.ioexample.utils.ReadExcellUtil;
import org.junit.Test;
/**
 * @author hyf
 * @version 1.0
 * @description: TODO
 * @date 202/4/24 9:48
 */
class ExcellReadTests  {
    @Test
    public void excellTest(){
        // 开始时间
        long stime = System.currentTimeMillis();
        ReadExcellUtil.ExcellRead("D:\\Utils\\defaults\\测试数据\\户籍\\户籍\\1 - 副本 (2).xlsx");
        long etime = System.currentTimeMillis();
        // 计算执行时间
        System.out.printf("执行时长：%d 毫秒.", (etime - stime));
    }

}
