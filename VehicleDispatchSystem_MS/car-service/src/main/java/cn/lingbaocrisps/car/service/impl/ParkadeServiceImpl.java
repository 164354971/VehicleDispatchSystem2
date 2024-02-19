package cn.lingbaocrisps.car.service.impl;

import cn.lingbaocrisps.car.domain.po.Parkade;
import cn.lingbaocrisps.car.mapper.ParkadeMapper;
import cn.lingbaocrisps.car.service.IParkadeService;
import cn.lingbaocrisps.common.domain.RedisConstants;
import cn.lingbaocrisps.common.exception.ForbiddenException;
import cn.lingbaocrisps.common.utils.RedisTools;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParkadeServiceImpl extends ServiceImpl<ParkadeMapper, Parkade> implements IParkadeService {

    private final RedisTools redisTools;
    @Override
    public List<Parkade> findParkadeListByArea(String area) {
        if(area == null)
            throw new ForbiddenException("区域不能为空");
        List<Parkade> parkadeList = null;
        String areaString = null;
        switch (area){
            case "永川":
                areaString = "yongchuan";
                break;
            case "巴南":
                areaString = "banan";
                break;
            case "全部":
                areaString = "all";
                break;
        }
        parkadeList = redisTools.range(RedisConstants.PARKADE_List_KEY + areaString, 0, -1);
        if(parkadeList == null || parkadeList.size() == 0){
            if(areaString.equals("all")){
                parkadeList = list();
            }else{
                parkadeList = list(new QueryWrapper<Parkade>().eq("area_school", area));
            }
            if(parkadeList != null && parkadeList.size() > 0){
                synchronized (this){
                    redisTools.delete(RedisConstants.PARKADE_List_KEY + areaString);
                    redisTools.rightPushAll(RedisConstants.PARKADE_List_KEY + areaString, parkadeList);
                    redisTools.expire(RedisConstants.PARKADE_List_KEY + areaString, RedisConstants.CACHE_TTL, TimeUnit.DAYS);
                }
            }
        }
        return parkadeList;
    }
}
