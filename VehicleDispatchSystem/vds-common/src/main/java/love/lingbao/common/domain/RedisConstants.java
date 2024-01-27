package love.lingbao.common.domain;

public class RedisConstants {
    public static final Long NO_KEY = -2L;

    public static final String LOGIN_CODE_KEY = "login:code:";
    public static final Long LOGIN_CODE_TTL = 2L;
    public static final String LOGIN_USER_KEY = "login:token:";
    public static final Long LOGIN_USER_TTL = 36000L;

    public static final Long CACHE_NULL_TTL = 2L;

    public static final Long CACHE_SHOP_TTL = 30L;
    public static final String CACHE_SHOP_KEY = "cache:shop:";

    public static final String LOCK_SHOP_KEY = "lock:shop:";
    public static final Long LOCK_SHOP_TTL = 10L;

    public static final String PARKADE_YONGCHUAN_List_KEY = "vehicle:parkade:yongchuan:list";
    public static final String PARKADE_BANAN_List_KEY = "vehicle:parkade:banan:list";
    public static final String CAR_IMG_LIST_KEY = "vehicle:car:img:list:";
    public static final String BRAND_VO_LIST_KEY = "vehicle:brand:vo:list:";
    public static final String MODEL_LIST_KEY = "vehicle:model:list:";
    public static final String CAR_LIST_KEY = "vehicle:car:list:";
    public static final String ORDER_KEY = "vehicle:order:";
    public static final String ORDER_LIST = "vehicle:order:list";
    public static final String ORDER_MAP = "vehicle:order";
    public static final String ORDER_NUMBER_KEY = "vehicle:order:no";
    public static final String USER_KEY = "vehicle:user:";
    public static final String COUPON_USER_KEY = "vehicle:user_coupon:";

}
