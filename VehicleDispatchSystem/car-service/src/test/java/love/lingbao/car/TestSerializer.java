package love.lingbao.car;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import love.lingbao.car.domain.po.Model;
import love.lingbao.common.domain.RedisConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class TestSerializer {

    @Autowired
    private RedisTemplate<String, ? extends Object> redisTemplate;

    @Test
    public void test() {
        Integer brandId = 1;
        //从redis中获取数据
        ListOperations<String, Model> listOperations = (ListOperations<String, Model>) redisTemplate.opsForList();
        List<Model> modelList = listOperations.range(RedisConstants.MODEL_LIST_KEY + brandId, 0, -1);
        //redis没有数据再去mysql找

        modelList = new LinkedList<>();
        modelList.add(new Model());

        listOperations.rightPushAll(RedisConstants.MODEL_LIST_KEY + brandId, modelList);
        redisTemplate.expire(RedisConstants.MODEL_LIST_KEY + brandId, 7L, TimeUnit.DAYS);

    }
}
