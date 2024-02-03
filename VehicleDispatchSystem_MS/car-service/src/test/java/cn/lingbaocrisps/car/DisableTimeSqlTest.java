package cn.lingbaocrisps.car;

import cn.lingbaocrisps.car.service.impl.DisableTimeServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DisableTimeSqlTest {

    @Autowired
    private DisableTimeServiceImpl disableTimeService;

    @Test
    public void test(){
        boolean usableCarTime = disableTimeService.isUsableCarTime(1L, "2021-01-01 00:00:00", "2021-01-01 00:00:00");
        System.out.println(usableCarTime);
    }
}
