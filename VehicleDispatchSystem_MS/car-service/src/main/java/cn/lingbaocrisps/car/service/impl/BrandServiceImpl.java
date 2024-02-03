package cn.lingbaocrisps.car.service.impl;

import cn.lingbaocrisps.car.domain.po.Brand;
import cn.lingbaocrisps.car.domain.vo.BrandVO;
import cn.lingbaocrisps.car.mapper.BrandMapper;
import cn.lingbaocrisps.car.service.IBrandService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import cn.lingbaocrisps.common.domain.RedisConstants;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements IBrandService {

    private final RedisTemplate<String, ? extends Object> redisTemplate;

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public List<BrandVO> findBrandVOList(String str) {
        //1.先去redis里查询 BRAND_VO_LIST_KEY 列表
        ListOperations<String, BrandVO> brandVOListOperations = (ListOperations<String, BrandVO>) redisTemplate.opsForList();
        List<BrandVO> brandVOList = brandVOListOperations.range(RedisConstants.BRAND_VO_LIST_KEY + str, 0, -1);
        //2.如果redis中没有查到 BRAND_VO_LIST_KEY 数据则去数据库查
        if(brandVOList == null || brandVOList.size() == 0){
            LambdaQueryWrapper<Brand> lqw = new LambdaQueryWrapper<>();
            //如果str为空则找全部
            lqw.like(!StringUtils.isEmpty(str), Brand::getBrand, str).or().like(!StringUtils.isEmpty(str), Brand::getInitial, str);
            List<Brand> brandList = list(lqw);
            //品牌列表进行首字母分类，使用TreeMap排序
            TreeMap<String, List<Brand>> treeMap = new TreeMap<>();
            brandList.forEach(brand -> {
                //获取品牌首字母
                String initial = brand.getInitial();
                List<Brand> list = new LinkedList<>();
                //如果treeMap的key已存在此首字母，则将该品牌加入value列表中
                if(treeMap.containsKey(initial)){
                    list = treeMap.get(initial);
                }
                list.add(brand);
                treeMap.put(initial, list);
            });
            //2.1 遍历treeMap，写入brandVOList
            brandVOList = new LinkedList<>();
            Set<Map.Entry<String, List<Brand>>> entries = treeMap.entrySet();
            for (Map.Entry<String, List<Brand>> entry : entries) {
                BrandVO brandVO = new BrandVO();
                brandVO.setInitial(entry.getKey());
                brandVO.setBrandList(entry.getValue());
                brandVOList.add(brandVO);
            }
            //2.2 将本次查询结果存入redis,保存7日
            if(brandVOList.size() != 0){
                //str不能为null
                if(StringUtils.isEmpty(str)){
                    str = "";
                }
                brandVOListOperations.rightPushAll(RedisConstants.BRAND_VO_LIST_KEY + str, brandVOList);
                redisTemplate.expire(RedisConstants.BRAND_VO_LIST_KEY + str, 7L, TimeUnit.DAYS);
            }
        }

        //返回结果列表
        return brandVOList;
    }
}
