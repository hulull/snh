package com.snh.modian.util;

import com.snh.modian.SnhApplication;
import com.snh.modian.domain.cqp.CqpHttpApiResp;
import com.snh.modian.service.RollCardServiceImpl;
import com.snh.modian.util.cqp.CqpHttpApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SnhApplication.class)
public class CqpApiTest {
    @Autowired
    RollCardServiceImpl rollCardService;
    @Test
    public void test() {
        for (int i = 0; i <= 2; i++) {
            CqpHttpApiResp xx = CqpHttpApi.getInstance().sendGroupMsg(565690548, "[CQ:image,file=l1/火锅的诱惑-1星.jpg]");
            if (xx.getRetcode() == 0) {
                break;
            }
        }
        //CqpHttpApiResp xx = CqpHttpApi.sendPrivateMsg(1846253361, "[CQ:shake]");
    }

    @Test
    public void testGetMsg() {
        String ret = CqpHttpApi.getInstance().getMsg();
        System.out.println(ret);
    }
}
