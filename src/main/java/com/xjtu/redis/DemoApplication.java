package com.xjtu.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


class DemoApplication {
    private static Jedis jedis;
    static {
        jedis = RedisUtil.getJedis();
    }
    private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

    /**
     * redis操作字符串
     */
    public static void main(String[] args) {
        testString();
    }
    public static void testString() {

        // 添加数据
        jedis.set("user::1", "xiangjun1");
        jedis.set("user::2", "xiangjun2");
        jedis.set("user::3", "xiangjun3");
        jedis.set("user::4", "xiangjun4");
        jedis.set("user::5", "xiangjun5");
        jedis.set("user::6", "xiangjun6");
        jedis.set("user::7", "xiangjun7");
        System.out.println("redis中name的字符串:" + jedis.get("name"));
        String biaoName="user::";
        for(int i=1;i<8;i++){
            String s=jedis.get(biaoName+i);
            System.out.println(s);
        }

    }
    /**
     * redis操作map集合
     */
    static void testMap() {
        // 连接redis服务器
        jedis = RedisUtil.getJedis();
        // 添加数据
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "xiangjun");
        map.put("age", "22");
        map.put("qq", "5443343");
        jedis.hmset("user", map);

        // 取出user中的name，执行结果:[minxr]-->注意结果是一个泛型的List
        // 第一个参数是存入redis中map对象的key，后面跟的是放入map中的对象的key，后面的key可以跟多个，是可变
        List<String> rsmap = jedis.hmget("user", "name", "age", "qq");
        System.out.println("redis中map对象:" + rsmap);

//        // 删除map中的某个键值
//        jedis.hdel("user", "age");
        System.out.println("user的age被删除了，返回NULL:" + jedis.hmget("user", "age")); // 因为删除了，所以返回的是null
        System.out.println("user的键中存放的值的个数2:" + jedis.hlen("user")); // 返回key为user的键中存放的值的个数2
        System.out.println("存在key为user的记录 返回true:" + jedis.exists("user"));// 是否存在key为user的记录 返回true
        System.out.println("返回map对象中的所有key:" + jedis.hkeys("user"));// 返回map对象中的所有key
        System.out.println("返回map对象中的所有value:" + jedis.hvals("user"));// 返回map对象中的所有value

        Iterator<String> iter = jedis.hkeys("user").iterator();
        System.out.println("返回map对象中的对象:" + iter);// 返回map对象中的对象
        while (iter.hasNext()) {
            String key = iter.next();
            System.out.println(key + ":" + jedis.hmget("user", key));
        }
    }
    /**
     * redis操作list集合
     */
    public static void testList() {
        // 连接redis服务器
        jedis = RedisUtil.getJedis();
        // 开始前，先移除所有的内容
        jedis.del("java framework");
        System.out.println(jedis.lrange("java framework", 0, -1));
        // 先向key java framework中存放三条数据
        jedis.lpush("java framework", "spring");
        jedis.lpush("java framework", "struts");
        jedis.lpush("java framework", "hibernate");
        // 再取出所有数据jedis.lrange是按范围取出，
        // 第一个是key，第二个是起始位置，第三个是结束位置，jedis.llen获取长度 -1表示取得所有
        System.out.println("从左边放入：" + jedis.lrange("java framework", 0, -1));

        jedis.del("java framework");
        jedis.rpush("java framework", "spring");
        jedis.rpush("java framework", "struts");
        jedis.rpush("java framework", "hibernate");
        System.out.println("从右边放入：" + jedis.lrange("java framework", 0, -1));
    }
    /**
     * redis操作set集合
     */
    public static void testSet() {
        // 连接redis服务器
        jedis = RedisUtil.getJedis();
        // 移除myuser
        jedis.del("myuser");
        // 添加
        jedis.sadd("myuser", "liuling");
        jedis.sadd("myuser", "xinxin");
        jedis.sadd("myuser", "ling");
        jedis.sadd("myuser", "zhangxinxin");
        jedis.sadd("myuser", "who");
        // 移除myuser中元素
        jedis.srem("myuser", "who");
        System.out.println("获取集合中所有元素的value:" + jedis.smembers("myuser"));// 获取所有加入的value
        System.out.println("判断元素是否存在：" + jedis.sismember("myuser", "who"));// 判断 who
        // 是否是user集合的元素
        System.out.println("返回集合的一个随机元素:" + jedis.srandmember("myuser"));
        System.out.println("返回集合的元素个数:" + jedis.scard("myuser"));// 返回集合的元素个数
    }
    /**
     * redis排序
     */
    public static void testSort() {
        // 连接redis服务器
        jedis = RedisUtil.getJedis();
        // jedis 排序
        // 注意，此处的rpush和lpush是List的操作。是一个双向链表（但从表现来看的）
        jedis.del("a");// 先清除数据，再加入数据进行测试
        jedis.rpush("a", "1");
        jedis.lpush("a", "6");
        jedis.lpush("a", "3");
        jedis.lpush("a", "9");
        System.out.println("输出结果：" + jedis.lrange("a", 0, -1));// [9, 3, 6, 1]
        System.out.println("输出排序后结果：" + jedis.sort("a")); // [1, 3, 6, 9] //输出排序后结果
        System.out.println("输出结果：" + jedis.lrange("a", 0, -1));
    }
    /**
     * redis连接池
     */
    public static void testRedisPool() {

        RedisUtil.getJedis().set("newname", "test");
        System.out.println(RedisUtil.getJedis().get("newname"));

        logger.info(" LOG test.");
    }

}