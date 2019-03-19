package util;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisUtils {

    private static  GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
//    private static JedisPool jedisPool = new JedisPool(poolConfig, "62.234.179.133", 4000);
    private static JedisPool jedisPool = new JedisPool(poolConfig, "192.168.1.12", 4000);

    public static void saveKey(String key,String value){

        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            jedis.set(key,value);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis != null) {
// 如果使用 JedisPool ， close 操作不是关闭连接，代表归还连接池
                jedis.close();
            }
        }
    }

    public static String getKey(String key){

        Jedis jedis = null;
        String value = null;
        try{
            jedis = jedisPool.getResource();
            value = jedis.get(key);
            System.out.println("-------" + value);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return value;
    }

    public static Long  delKey(String key){
        Jedis jedis = null;
        Long value = null;
        try{
            jedis = jedisPool.getResource();
            value = jedis.del(key);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return value;
    }

}

