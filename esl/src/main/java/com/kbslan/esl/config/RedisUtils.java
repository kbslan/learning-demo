package com.kbslan.esl.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ListPosition;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.util.SafeEncoder;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * redis操作工具类
 * @author zhuo.zhang
 *
 */
public class RedisUtils {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 生存时间
     */
    private int expireTime = 60000;

    /**
     * 连接池
     */
    private JedisPool jedisPool = null;

    /**
     * 对key的操作
     */
    public KEY key = new KEY();
    /**
     * 对字符串的操作
     */
    public STRING string = new STRING();
    /**
     * 对列表的操作
     */
    public LIST list = new LIST();
    /**
     * 对集合的操作
     */
    public SET set = new SET();
    /**
     * 对哈希表的操作
     */
    public HASH hash = new HASH();
    /**
     * 对有序集合的操作
     */
    public SORTEDSET sortedset = new SORTEDSET();

    public RedisUtils() {
    }

    public JedisPool getPool() {
        return jedisPool;
    }

    /**
     * 从池中获取jedis对象
     *
     * @return
     */
    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    /**
     * 释放jedis对象
     *
     * @param jedis
     */
    public void returnJedis(Jedis jedis) {
        if (null != jedis && null != jedisPool) {
            jedis.close();
        }
    }

    //****************Keys****************//
    public class KEY {

        /**
         * 删除所有数据库的所有key
         *
         * @return
         * @throws Exception
         */
        public boolean flushAll() throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                String s = jedis.flushAll();
                return "ok".equalsIgnoreCase(s);
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 将 oldkey 改名为 newkey
         * 当 oldkey 和 newkey 相同，或者 oldkey 不存在时，返回错误
         * 当 newkey 已经存在时， 将覆盖旧值
         *
         * @param oldkey
         * @param newkey
         * @return
         * @throws Exception
         */
        public boolean rename(String oldkey, String newkey) throws Exception {
            if (oldkey == null || oldkey.isEmpty() || newkey == null || newkey.isEmpty()) {
                throw new Exception("oldkey or newkey is null");
            }
            return rename(SafeEncoder.encode(oldkey),
                    SafeEncoder.encode(newkey));
        }

        /**
         * 当且仅当 newkey 不存在时，将 oldkey 改名为 newkey
         * 当 oldkey 不存在时，返回错误
         *
         * @param oldkey
         * @param newkey
         * @return
         * @throws Exception
         */
        public boolean renamenx(String oldkey, String newkey) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long status = jedis.renamenx(oldkey, newkey);
                return 1 == status;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 将 oldkey 改名为 newkey
         * 当 oldkey 和 newkey 相同，或者 oldkey 不存在时，返回一个错误
         * 当 newkey 已经存在时，将覆盖旧值
         *
         * @param oldkey
         * @param newkey
         * @return
         * @throws Exception
         */
        public boolean rename(byte[] oldkey, byte[] newkey) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                String status = jedis.rename(oldkey, newkey);
                return "ok".equalsIgnoreCase(status);
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 设置key的过期时间，用默认时间
         *
         * @param key
         * @return
         * @throws Exception
         */
        public long expire(String key) throws Exception {
            return expire(key, expireTime);
        }

        /**
         * 设置key的过期时间，以秒为单位
         * 当 key 过期时(生存时间为 0 )，会被自动删除
         *
         * @param key
         * @param seconds
         * @return 影响的记录数
         * @throws Exception
         */
        public long expire(String key, int seconds) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long count = jedis.expire(key, seconds);
                return count;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 设置key的过期时间,距历元（即格林威治标准时间 1970 年 1 月 1 日的 00:00:00）的偏移量
         *
         * @param key
         * @param timestamp
         * @return 影响的记录数
         * @throws Exception
         */
        public long expireAt(String key, long timestamp) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long count = jedis.expireAt(key, timestamp);
                return count;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 查询key的过期时间
         *
         * @param key
         * @return 返回剩余的生存时间(以秒为单位), 如果key不存在或没有关联的过期时间, 返回-1
         * @throws Exception
         */
        public long ttl(String key) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long len = jedis.ttl(key);
                return len;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 取消对key过期时间的设置
         *
         * @param key
         * @return
         * @throws Exception
         */
        public boolean persist(String key) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long count = jedis.persist(key);
                return true;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 删除keys对应的记录,可以是多个key
         *
         * @param keys
         * @return 删除的记录数
         * @throws Exception
         */
        public long del(String... keys) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long count = jedis.del(keys);
                return count;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 删除keys对应的记录,可以是多个key
         *
         * @param keys
         * @return 删除的记录数
         * @throws Exception
         */
        public long del(byte[]... keys) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long count = jedis.del(keys);
                return count;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 判断key是否存在
         *
         * @param key
         * @return boolean
         * @throws Exception
         */
        public boolean exists(String key) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                boolean exis = jedis.exists(key);
                return exis;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 对List,Set,SortSet进行排序,如果集合数据较大避免使用这个方法
         *
         * @param key
         * @return 返回集合的全部记录
         * @throws Exception
         */
        public List<String> sort(String key) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                List<String> list = jedis.sort(key);
                return list;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 对List,Set,SortSet进行排序或limit
         *
         * @param key
         * @param params 定义排序类型或limit的起止位置.
         * @return 返回全部或部分记录
         **/
        public List<String> sort(String key, SortingParams params) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                List<String> list = jedis.sort(key, params);
                return list;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 返回指定key存储的类型
         *
         * @param key
         * @return string|list|set|zset|hash..
         * @throws Exception
         */
        public String type(String key) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                String type = jedis.type(key);
                return type;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 查找所有匹配给定的模式的key
         *
         * @param pattern key的表达式,*表示多个，?表示一个
         * @return
         * @throws Exception
         */
        public Set<String> keys(String pattern) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                Set<String> set = jedis.keys(pattern);
                return set;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }
    }

    //*************Set*************//

    public class SET {

        /**
         * 向Set添加多个值
         *
         * @param key
         * @param members
         * @return 返回添加成功的数量
         * @throws Exception
         */
        public long sadd(String key, String... members) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long s = jedis.sadd(key, members);
                return s;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 向Set添加多个值
         *
         * @param key
         * @param members
         * @return 返回添加成功的数量
         * @throws Exception
         */
        public long sadd(byte[] key, byte[]... members) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long s = jedis.sadd(key, members);
                return s;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 返回集合中元素的数量
         *
         * @param key
         * @return 返回元素个数
         * @throws Exception
         */
        public long scard(String key) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long len = jedis.scard(key);
                return len;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 返回从第一组和所有的给定集合之间的差异的成员
         *
         * @param keys
         * @return 返回差异的成员集合
         * @throws Exception
         */
        public Set<String> sdiff(String... keys) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                Set<String> set = jedis.sdiff(keys);
                return set;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 等于sdiff,但返回的不是结果集,而是将结果集存储在新的集合中，如果目标已存在则覆盖
         *
         * @param newkey 新结果集的key
         * @param keys   比较的集合
         * @return 结果集元素的个数
         * @throws Exception
         */
        public long sdiffstore(String newkey, String... keys) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long s = jedis.sdiffstore(newkey, keys);
                return s;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 返回给定集合交集的成员,如果其中一个集合为不存在或为空，则返回空Set
         *
         * @param keys
         * @return 返回交集成员的集合
         * @throws Exception
         */
        public Set<String> sinter(String... keys) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                Set<String> set = jedis.sinter(keys);
                return set;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 等于sinter,但返回的不是结果集,而是将结果集存储在新的集合中，如果目标已存在则覆盖
         *
         * @param newkey 新结果集的key
         * @param keys   比较的集合
         * @return 结果集中成员的个数
         * @throws Exception
         */
        public long sinterstore(String newkey, String... keys) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long s = jedis.sinterstore(newkey, keys);
                return s;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 确定一个给定的值是否存在
         *
         * @param key
         * @param member
         * @return
         * @throws Exception
         */
        public boolean sismember(String key, String member) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                boolean s = jedis.sismember(key, member);
                return s;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 返回集合中的所有成员
         *
         * @param key
         * @return
         * @throws Exception
         */
        public Set<String> smembers(String key) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                Set<String> set = jedis.smembers(key);
                return set;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 返回集合中的所有成员
         *
         * @param key
         * @return
         * @throws Exception
         */
        public Set<byte[]> smembers(byte[] key) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                Set<byte[]> set = jedis.smembers(key);
                return set;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 将成员从源集合移出放入目标集合
         * 如果源集合不存在或不包含指定成员，不进行任何操作，返回0
         * 否则该成员从源集合上删除，并添加到目标集合，如果目标集合中成员已存在，则只在源集合进行删除
         *
         * @param srckey 源集合
         * @param dstkey 目标集合
         * @param member 源集合中的成员
         * @return 如果该元素成功移除, 返回1, 如果该元素不是 source集合成员,无任何操作,则返回0
         * @throws Exception
         */
        public long smove(String srckey, String dstkey, String member) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long s = jedis.smove(srckey, dstkey, member);
                return s;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 移除并返回集合中的一个随机元素
         * 如果只想获取一个随机元素，但不想该元素从集合中被移除的话，可以使用 SRANDMEMBER 命令
         *
         * @param key
         * @return 被删除的成员
         * @throws Exception
         */
        public String spop(String key) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                String s = jedis.spop(key);
                return s;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 从集合中删除指定成员
         *
         * @param key
         * @param members
         * @return 从集合中移除元素的个数，不包括不存在的成员
         * @throws Exception
         */
        public long srem(String key, String... members) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long s = jedis.srem(key, members);
                return s;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 合并多个集合并返回合并后的结果，合并后的结果集合并不保存
         *
         * @param keys
         * @return 合并后的结果集合
         * @see
         */
        public Set<String> sunion(String... keys) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                Set<String> set = jedis.sunion(keys);
                return set;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 合并多个集合并将合并后的结果集保存在指定的新集合中，如果新集合已经存在则覆盖
         *
         * @param newkey 新集合的key
         * @param keys   要合并的集合
         * @return
         * @throws Exception
         */
        public long sunionstore(String newkey, String... keys) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long s = jedis.sunionstore(newkey, keys);
                return s;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }
    }

    //*************SortSet*************//

    public class SORTEDSET {

        /**
         * 将一个或多个 member 元素及其 score 值加入到有序集 key 当中
         * 如果某个 member 已经是有序集的成员，那么更新这个 member 的 score 值，并通过重新插入这个 member 元素，来保证该 member 在正确的位置上
         * 如果 key 不存在，则创建一个空的有序集并执行 ZADD 操作
         * 当 key 存在但不是有序集类型时，返回一个错误
         *
         * @param key
         * @param score  权重,score 值可以是整数值或双精度浮点数。
         * @param member 要加入的值
         * @return 被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员
         * @throws Exception
         */
        public long zadd(String key, double score, String member) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long s = jedis.zadd(key, score, member);
                return s;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /*public long zadd(String key, Map<Double, String> scoreMembers) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long s = jedis.zadd(key, scoreMembers);
                return s;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }*/

        /**
         * 获取集合中元素的数量
         *
         * @param key
         * @return 当 key 存在且是有序集类型时，返回有序集的基数,当 key 不存在时，返回0
         * @throws Exception
         */
        public long zcard(String key) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long len = jedis.zcard(key);
                return len;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 返回有序集 key 中， score 值在 min 和 max 之间(默认包括 score 值等于 min 或 max )的成员的数量
         *
         * @param key
         * @param min 最小排序位置
         * @param max 最大排序位置
         * @return score 值在 min 和 max 之间的成员的数量
         * @throws Exception
         */
        public long zcount(String key, double min, double max) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long len = jedis.zcount(key, min, max);
                return len;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 获得set的长度
         *
         * @param key
         * @return
         * @throws Exception
         */
        public long zlength(String key) throws Exception {
            long len = 0;
            Set<String> set = zrange(key, 0, -1);
            len = set.size();
            return len;
        }

        /**
         * 为有序集 key 的成员 member 的 score 值加上增量 increment
         *
         * @param key
         * @param score  要增的权重
         * @param member 要插入的值
         * @return member 成员的新 score 值，以字符串形式表示
         * @throws Exception
         */
        public double zincrby(String key, double score, String member) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                double s = jedis.zincrby(key, score, member);
                return s;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 返回有序集 key 中，指定区间内的成员,其中成员的位置按 score 值递增(从小到大)来排序
         * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。
         * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推
         * 当 start 的值比有序集的最大下标还要大，或是 start > stop 时， ZRANGE 命令只是简单地返回一个空列表
         *
         * @param key
         * @param start 开始位置(包含)
         * @param end   结束位置(包含)
         * @return 指定区间内，带有 score 值(可选)的有序集成员的列表
         * @throws Exception
         */
        public Set<String> zrange(String key, int start, int end) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                Set<String> set = jedis.zrange(key, start, end);
                return set;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。有序集成员按 score 值递增(从小到大)次序排列
         *
         * @param key
         * @param min 上限权重
         * @param max 下限权重
         * @return 指定区间内，带有 score 值(可选)的有序集成员的列表
         * @throws Exception
         */
        public Set<String> zrangeByScore(String key, double min, double max) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                Set<String> set = jedis.zrangeByScore(key, min, max);
                return set;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递增(从小到大)顺序排列。
         * 排名以 0 为底，也就是说， score 值最小的成员排名为 0
         *
         * @param key
         * @param member
         * @return 如果 member 是有序集 key 的成员，返回 member 的排名,如果 member 不是有序集 key 的成员，返回 nil
         * @see
         */
        public long zrank(String key, String member) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long index = jedis.zrank(key, member);
                return index;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递减(从大到小)排序
         * 排名以 0 为底，也就是说， score 值最大的成员排名为 0
         *
         * @param key
         * @param member
         * @return 如果 member 是有序集 key 的成员，返回 member 的排名,如果 member 不是有序集 key 的成员，返回 nil 。
         * @throws Exception
         */
        public long zrevrank(String key, String member) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long index = jedis.zrevrank(key, member);
                return index;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 移除有序集 key 中的一个或多个成员，不存在的成员将被忽略
         * 当 key 存在但不是有序集类型时，返回一个错误
         *
         * @param key
         * @param members
         * @return 被成功移除的成员的数量，不包括被忽略的成员
         * @throws Exception
         */
        public long zrem(String key, String... members) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long s = jedis.zrem(key, members);
                return s;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 移除有序集 key 中，指定排名(rank)区间内的所有成员
         * 区间分别以下标参数 start 和 stop 指出，包含 start 和 stop 在内
         * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推
         * 也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推
         *
         * @param key
         * @param start 开始区间，从0开始(包含)
         * @param end   结束区间,-1为最后一个元素(包含)
         * @return 被移除成员的数量
         * @throws Exception
         */
        public long zremrangeByRank(String key, int start, int end) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long s = jedis.zremrangeByRank(key, start, end);
                return s;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 移除有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员
         * 自版本2.1.6开始， score 值等于 min 或 max 的成员也可以不包括在内
         *
         * @param key
         * @param min 下限权重(包含)
         * @param max 上限权重(包含)
         * @return 被移除成员的数量
         */
        public long zremrangeByScore(String key, double min, double max) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long s = jedis.zremrangeByScore(key, min, max);
                return s;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 返回有序集 key 中，指定区间内的成员
         * 其中成员的位置按 score 值递减(从大到小)来排列
         *
         * @param key
         * @param start
         * @param end
         * @return 指定区间内，带有 score 值(可选)的有序集成员的列表
         * @throws Exception
         */
        public Set<String> zrevrange(String key, int start, int end) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                Set<String> set = jedis.zrevrange(key, start, end);
                return set;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 返回有序集 key 中，成员 member 的 score 值
         * 如果 member 元素不是有序集 key 的成员，或 key 不存在，返回 nil
         *
         * @param key
         * @param member
         * @return member 成员的 score 值，以字符串形式表示
         * @throws Exception
         */
        public double zscore(String key, String member) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                Double score = jedis.zscore(key, member);
                if (score != null) {
                    return score;
                }
                return 0;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }
    }

    //*************Hash*************//

    public class HASH {

        /**
         * 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略
         * 返回被成功移除的域的数量，不包括被忽略的域
         *
         * @param key
         * @param fields
         * @return 被成功移除的域的数量，不包括被忽略的域
         * @throws Exception
         */
        public long hdel(String key, String... fields) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long s = jedis.hdel(key, fields);
                return s;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 查看哈希表 key 中，给定域 field 是否存在
         *
         * @param key
         * @param fieid
         * @return
         * @throws Exception
         */
        public boolean hexists(String key, String fieid) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                boolean s = jedis.hexists(key, fieid);
                return s;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 返回哈希表 key 中给定域 field 的值
         *
         * @param key
         * @param fieid
         * @return
         * @throws Exception
         */
        public String hget(String key, String fieid) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                String s = jedis.hget(key, fieid);
                return s;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 返回哈希表 key 中给定域 field 的值
         *
         * @param key
         * @param fieid
         * @return
         * @throws Exception
         */
        public byte[] hget(byte[] key, byte[] fieid) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                byte[] s = jedis.hget(key, fieid);
                return s;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 返回哈希表 key 中，所有的域和值。
         * 在返回值里，紧跟每个域名(field name)之后是域的值(value)，所以返回值的长度是哈希表大小的两倍
         *
         * @param key
         * @return
         * @throws Exception
         */
        public Map<String, String> hgetAll(String key) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                Map<String, String> map = jedis.hgetAll(key);
                return map;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 将哈希表 key 中的域 field 的值设为 value
         * 如果 key 不存在，一个新的哈希表被创建并进行 HSET 操作
         * 如果域 field 已经存在于哈希表中，旧值将被覆盖
         *
         * @param key
         * @param fieid
         * @param value
         * @return 如果 field 是哈希表中的一个新建域，并且值设置成功，返回 1, 如果哈希表中域 field 已经存在且旧值已被新值覆盖，返回 0
         * @throws Exception
         */
        public long hset(String key, String fieid, String value) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long s = jedis.hset(key, fieid, value);
                return s;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 将哈希表 key 中的域 field 的值设为 value
         * 如果 key 不存在，一个新的哈希表被创建并进行 HSET 操作
         * 如果域 field 已经存在于哈希表中，旧值将被覆盖
         *
         * @param key
         * @param fieid
         * @param value
         * @return
         * @throws Exception
         */
        public long hset(String key, String fieid, byte[] value) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long s = jedis.hset(key.getBytes(), fieid.getBytes(), value);
                return s;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 将哈希表 key 中的域 field 的值设置为 value ，当且仅当域 field 不存在。若域 field 已经存在，该操作无效。
         * 如果 key 不存在，一个新哈希表被创建并执行 HSETNX 命令。
         *
         * @param key
         * @param fieid
         * @param value
         * @return 如果 field 是哈希表中的一个新建域，并且值设置成功，返回 1, 如果哈希表中域 field 已经存在且旧值已被新值覆盖，返回 0
         **/
        public long hsetnx(String key, String fieid, String value) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long s = jedis.hsetnx(key, fieid, value);
                return s;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 返回哈希表 key 中所有域的值
         *
         * @param key
         * @return 一个包含哈希表中所有值的表, 当 key 不存在时，返回一个空表
         * @throws Exception
         */
        public List<String> hvals(String key) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                List<String> list = jedis.hvals(key);
                return list;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 为哈希表 key 中的域 field 的值加上增量 increment
         * 增量也可以为负数，相当于对给定域进行减法操作
         * 如果 key 不存在，一个新的哈希表被创建并执行 HINCRBY 命令
         * 如果域 field 不存在，那么在执行命令前，域的值被初始化为 0
         * 对一个储存字符串值的域 field 执行 HINCRBY 命令将造成一个错误
         * 本操作的值被限制在 64 位(bit)有符号数字表示之内
         *
         * @param key
         * @param fieid 存储位置
         * @param value 要增加的值,可以是负数
         * @return 执行 HINCRBY 命令之后，哈希表 key 中域 field 的值
         * @throws Exception
         */
        public long hincrby(String key, String fieid, long value) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long s = jedis.hincrBy(key, fieid, value);
                return s;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 返回哈希表 key 中的所有域
         *
         * @param key
         * @return 一个包含哈希表中所有域的表, 当 key 不存在时，返回一个空表
         * @throws Exception
         */
        public Set<String> hkeys(String key) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                Set<String> set = jedis.hkeys(key);
                return set;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 返回哈希表 key 中域的数量
         *
         * @param key
         * @return 哈希表中域的数量, 当 key 不存在时，返回 0
         * @throws Exception
         */
        public long hlen(String key) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long len = jedis.hlen(key);
                return len;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 返回哈希表 key 中，一个或多个给定域的值
         * 如果给定的域不存在于哈希表，那么返回一个 nil 值
         * 因为不存在的 key 被当作一个空哈希表来处理，所以对一个不存在的 key 进行 HMGET 操作将返回一个只带有 nil 值的表
         *
         * @param key
         * @param fields 存储位置
         * @return 一个包含多个给定域的关联值的表，表值的排列顺序和给定域参数的请求顺序一样
         * @throws Exception
         */
        public List<String> hmget(String key, String... fields) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                List<String> list = jedis.hmget(key, fields);
                return list;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 返回哈希表 key 中，一个或多个给定域的值
         * 如果给定的域不存在于哈希表，那么返回一个 nil 值
         * 因为不存在的 key 被当作一个空哈希表来处理，所以对一个不存在的 key 进行 HMGET 操作将返回一个只带有 nil 值的表
         *
         * @param key
         * @param fields
         * @return 一个包含多个给定域的关联值的表，表值的排列顺序和给定域参数的请求顺序一样
         * @throws Exception
         */
        public List<byte[]> hmget(byte[] key, byte[]... fields) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                List<byte[]> list = jedis.hmget(key, fields);
                return list;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 同时将多个 field-value (域-值)对设置到哈希表 key 中
         * 此命令会覆盖哈希表中已存在的域
         * 如果 key 不存在，一个空哈希表被创建并执行 HMSET 操作
         *
         * @param key
         * @param map
         * @return 如果命令执行成功，返回 true,当 key 不是哈希表(hash)类型时，返回false
         * @throws Exception
         */
        public boolean hmset(String key, Map<String, String> map) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                String s = jedis.hmset(key, map);
                return "ok".equalsIgnoreCase(s);
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 同时将多个 field-value (域-值)对设置到哈希表 key 中
         * 此命令会覆盖哈希表中已存在的域
         * 如果 key 不存在，一个空哈希表被创建并执行 HMSET 操作
         *
         * @param key
         * @param map
         * @return 如果命令执行成功，返回 true,当 key 不是哈希表(hash)类型时，返回false
         * @throws Exception
         */
        public boolean hmset(byte[] key, Map<byte[], byte[]> map) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                String s = jedis.hmset(key, map);
                return "ok".equalsIgnoreCase(s);
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

    }

    //*************String*************//

    public class STRING {
        /**
         * 返回 key 所关联的字符串值
         *
         * @param key
         * @return 当 key 不存在时，返回 null ，否则，返回 key 的值,如果 key 不是字符串类型，那么返回一个错误
         * @throws Exception
         */
        public String get(String key) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                String value = jedis.get(key);
                return value;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 返回 key 所关联的值
         *
         * @param key
         * @return
         * @throws Exception
         */
        public byte[] get(byte[] key) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                byte[] value = jedis.get(key);
                return value;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 将值 value 关联到 key ，并将 key 的生存时间设为 seconds (以秒为单位)
         * 如果 key 已经存在， SETEX 命令将覆写旧值
         *
         * @param key
         * @param seconds
         * @param value
         * @return 设置成功时返回 true ,当 seconds 参数不合法时，返回false
         */
        public boolean setex(String key, int seconds, String value) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                String s = jedis.setex(key, seconds, value);
                return "ok".equalsIgnoreCase(s);
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 将值 value 关联到 key ，并将 key 的生存时间设为 seconds (以秒为单位)
         * 如果 key 已经存在， SETEX 命令将覆写旧值
         *
         * @param key
         * @param seconds
         * @param value
         * @return 设置成功时返回 true ,当 seconds 参数不合法时，返回false
         * @throws Exception
         */
        public boolean setex(byte[] key, int seconds, byte[] value) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                String s = jedis.setex(key, seconds, value);
                return "ok".equalsIgnoreCase(s);
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 将 key 的值设为 value ，当且仅当 key 不存在
         * 若给定的 key 已经存在，则 SETNX 不做任何动作
         *
         * @param key
         * @param value
         * @return 设置成功，返回 1,设置失败，返回 0
         * @throws Exception
         */
        public long setnx(String key, String value) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long str = jedis.setnx(key, value);
                return str;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 将字符串值 value 关联到 key
         * 如果 key 已经持有其他值， SET 就覆写旧值，无视类型
         * 对于某个原本带有生存时间（TTL）的键来说， 当 SET 命令成功在这个键上执行时， 这个键原有的 TTL 将被清除
         * <p>
         * 从 Redis 2.6.12 版本开始， SET 命令的行为可以通过一系列参数来修改
         * SET key value [EX seconds] [PX milliseconds] [NX|XX]
         * EX second ：设置键的过期时间为 second 秒。 SET key value EX second 效果等同于 SETEX key second value
         * PX millisecond ：设置键的过期时间为 millisecond 毫秒。 SET key value PX millisecond 效果等同于 PSETEX key millisecond value
         * NX ：只在键不存在时，才对键进行设置操作。 SET key value NX 效果等同于 SETNX key value
         * XX ：只在键已经存在时，才对键进行设置操作
         *
         * @param key
         * @param value
         * @return 在 Redis 2.6.12 版本以前， SET 命令总是返回 OK
         * 从 Redis 2.6.12 版本开始， SET 在设置操作成功完成时，才返回 OK
         * 如果设置了 NX 或者 XX ，但因为条件没达到而造成设置操作未执行，那么命令返回空批量回复（NULL Bulk Reply）
         * @throws Exception
         */
        public String set(String key, String value) throws Exception {
            if (key == null || value == null) {
                throw new Exception("key or value is null");
            }
            return set(SafeEncoder.encode(key), SafeEncoder.encode(value));
        }

        /**
         * 添加记录,如果记录已存在将覆盖原有的value
         *
         * @param key
         * @param value
         * @return 状态码
         */
        public String set(String key, byte[] value) throws Exception {
            if (key == null) {
                throw new Exception("key is null");
            }
            return set(SafeEncoder.encode(key), value);
        }

        /**
         * 添加记录,如果记录已存在将覆盖原有的value
         *
         * @param key
         * @param value
         * @return
         */
        public String set(byte[] key, byte[] value) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                String status = jedis.set(key, value);
                return status;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 刪除rendis中的key
         * @param key
         * @return
         * @throws Exception
         */
        public Long del(String key) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                Long status = jedis.del(key);
                return status;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 用 value 参数覆写(overwrite)给定 key 所储存的字符串值，从偏移量 offset 开始
         * 不存在的 key 当作空白字符串处理
         * SETRANGE 命令会确保字符串足够长以便将 value 设置在指定的偏移量上，如果给定 key 原来储存的字符串长度比偏移量小(比如字符串只有 5 个字符长，但你设置的 offset 是 10 )，
         * 那么原字符和偏移量之间的空白将用零字节(zerobytes, "\x00" )来填充
         * 注意你能使用的最大偏移量是 2^29-1(536870911) ，因为 Redis 字符串的大小被限制在 512 兆(megabytes)以内。如果你需要使用比这更大的空间，你可以使用多个 key
         *
         * @param key
         * @param offset
         * @param value
         * @return 被 SETRANGE 修改之后，字符串的长度
         * @throws Exception
         */
        public long setRange(String key, long offset, String value) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long len = jedis.setrange(key, offset, value);
                return len;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 如果 key 已经存在并且是一个字符串， APPEND 命令将 value 追加到 key 原来的值的末尾
         * 如果 key 不存在， APPEND 就简单地将给定 key 设为 value ，就像执行 SET key value 一样
         *
         * @param key
         * @param value
         * @return 追加 value 之后， key 中字符串的长度
         * @throws Exception
         */
        public long append(String key, String value) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long len = jedis.append(key, value);
                return len;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 将 key 所储存的值减去减量 decrement
         * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECRBY 操作
         * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误
         * 本操作的值限制在 64 位(bit)有符号数字表示之内
         *
         * @param key
         * @param decrement
         * @return 减去 decrement 之后， key 的值
         * @throws Exception
         */
        public long decrBy(String key, long decrement) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long len = jedis.decrBy(key, decrement);
                return len;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 将 key 所储存的值加上增量 incremen
         * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCRBY 命令
         * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误
         * 本操作的值限制在 64 位(bit)有符号数字表示之内
         *
         * @param key
         * @param increment
         * @return 加上 increment 之后， key 的值
         * @throws Exception
         */
        public long incrBy(String key, long increment) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long len = jedis.incrBy(key, increment);
                return len;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 返回 key 中字符串值的子字符串，字符串的截取范围由 start 和 end 两个偏移量决定(包括 start 和 end 在内)
         * 负数偏移量表示从字符串最后开始计数， -1 表示最后一个字符， -2 表示倒数第二个，以此类推
         * GETRANGE 通过保证子字符串的值域(range)不超过实际字符串的值域来处理超出范围的值域请求
         *
         * @param key
         * @param startOffset 开始位置(包含)
         * @param endOffset   结束位置(包含)
         * @return 截取得出的子字符串
         * @throws Exception
         */
        public String getrange(String key, long startOffset, long endOffset) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                String value = jedis.getrange(key, startOffset, endOffset);
                return value;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)
         * 当 key 存在但不是字符串类型时，返回一个错误
         *
         * @param key
         * @param value
         * @return 返回给定 key 的旧值,当 key 不存在时，返回 null
         * @throws Exception
         */
        public String getSet(String key, String value) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                String str = jedis.getSet(key, value);
                return str;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                e.printStackTrace();
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 返回所有(一个或多个)给定 key 的值
         * 如果给定的 key 里面，有某个 key 不存在，那么这个 key 返回特殊值 nil 。因此，该命令永不失败
         *
         * @param keys
         * @return 一个包含所有给定 key 的值的列表
         * @throws Exception
         */
        public List<String> mget(String... keys) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                List<String> str = jedis.mget(keys);
                return str;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 同时设置一个或多个 key-value 对
         * 如果某个给定 key 已经存在，那么 MSET 会用新值覆盖原来的旧值，如果这不是你所希望的效果，请考虑使用 MSETNX 命令：它只会在所有给定 key 都不存在的情况下进行设置操作
         * MSET 是一个原子性(atomic)操作，所有给定 key 都会在同一时间内被设置，某些给定 key 被更新而另一些给定 key 没有改变的情况，不可能发生
         *
         * @param keysvalues 例:keysvalues="key1","value1","key2","value2";
         * @return 总是返回 OK (因为 MSET 不可能失败)
         * @throws Exception
         */
        public boolean mset(String... keysvalues) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                String s = jedis.mset(keysvalues);
                return "ok".equalsIgnoreCase(s);
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 返回 key 所储存的字符串值的长度
         * 当 key 储存的不是字符串值时，返回一个错误
         *
         * @param key
         * @return 字符串值的长度, 当 key 不存在时，返回 0
         * @throws Exception
         */
        public long strlen(String key) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long len = jedis.strlen(key);
                return len;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }
    }

    //*************List*************//

    public class LIST {
        /**
         * 返回列表 key 的长度
         * 如果 key 不存在，则 key 被解释为一个空列表，返回 0
         * 如果 key 不是列表类型，返回一个错误
         *
         * @param key
         * @return 列表 key 的长度
         * @throws Exception
         */
        public long llen(String key) throws Exception {
            if (key == null) {
                return 0;
            }
            return llen(SafeEncoder.encode(key));
        }

        /**
         * 返回列表 key 的长度
         * 如果 key 不存在，则 key 被解释为一个空列表，返回 0
         * 如果 key 不是列表类型，返回一个错误
         *
         * @param key
         * @return 列表 key 的长度
         * @throws Exception
         */
        public long llen(byte[] key) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long count = jedis.llen(key);
                return count;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 将列表 key 下标为 index 的元素的值设置为 value
         * 当 index 参数超出范围，或对一个空列表( key 不存在)进行 LSET 时，返回一个错误
         * 关于列表下标的更多信息，请参考 LINDEX 命令
         *
         * @param key
         * @param index 位置
         * @param value 值
         * @return 操作成功返回 true ，否则返回false
         * @throws Exception
         */
        public boolean lset(byte[] key, int index, byte[] value) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                String s = jedis.lset(key, index, value);
                return "ok".equalsIgnoreCase(s);
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 将列表 key 下标为 index 的元素的值设置为 value
         * 当 index 参数超出范围，或对一个空列表( key 不存在)进行 LSET 时，返回一个错误
         * 关于列表下标的更多信息，请参考 LINDEX 命令
         *
         * @param key
         * @param index 位置
         * @param value 值
         * @return 操作成功返回 true ，否则返回false
         * @throws Exception
         */
        public boolean lset(String key, int index, String value) throws Exception {
            if (key == null || value == null) {
                throw new Exception("key or value is null");
            }
            return lset(SafeEncoder.encode(key), index,
                    SafeEncoder.encode(value));
        }

        /**
         * 将值 value 插入到列表 key 当中，位于值 pivot 之前或之后
         * 当 pivot 不存在于列表 key 时，不执行任何操作
         * 当 key 不存在时， key 被视为空列表，不执行任何操作
         * 如果 key 不是列表类型，返回一个错误
         *
         * @param key
         * @param where 前面插入或后面插入
         * @param pivot 相对位置的内容
         * @param value 插入的内容
         * @return 如果命令执行成功，返回插入操作完成之后，列表的长度, 如果没有找到 pivot ，返回 -1,如果 key 不存在或为空列表，返回 0
         */
        public long linsert(String key, ListPosition where, String pivot,
                            String value) throws Exception {
            if (key == null || pivot == null || value == null) {
                return -1;
            }
            return linsert(SafeEncoder.encode(key), where,
                    SafeEncoder.encode(pivot), SafeEncoder.encode(value));
        }

        /**
         * 将值 value 插入到列表 key 当中，位于值 pivot 之前或之后
         * 当 pivot 不存在于列表 key 时，不执行任何操作
         * 当 key 不存在时， key 被视为空列表，不执行任何操作
         * 如果 key 不是列表类型，返回一个错误
         *
         * @param key
         * @param where 前面插入或后面插入
         * @param pivot 相对位置的内容
         * @param value 插入的内容
         * @return 如果命令执行成功，返回插入操作完成之后，列表的长度, 如果没有找到 pivot ，返回 -1,如果 key 不存在或为空列表，返回 0
         * @throws Exception
         */
        public long linsert(byte[] key, ListPosition where, byte[] pivot,
                            byte[] value) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long count = jedis.linsert(key, where, pivot, value);
                return count;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 返回列表 key 中，下标为 index 的元素
         * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推
         * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推
         * 如果 key 不是列表类型，返回一个错误
         *
         * @param key
         * @param index
         * @return 列表中下标为 index 的元素,如果 index 参数的值不在列表的区间范围内(out of range)，返回 nil
         * @throws Exception
         */
        public String lindex(String key, int index) throws Exception {
            if (key == null) {
                return "key is null";
            }
            return SafeEncoder.encode(lindex(SafeEncoder.encode(key), index));
        }

        /**
         * 返回列表 key 中，下标为 index 的元素
         * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推
         * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推
         * 如果 key 不是列表类型，返回一个错误
         *
         * @param key
         * @param index
         * @return 列表中下标为 index 的元素,如果 index 参数的值不在列表的区间范围内(out of range)，返回 nil
         * @throws Exception
         */
        public byte[] lindex(byte[] key, int index) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                byte[] value = jedis.lindex(key, index);
                return value;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 移除并返回列表 key 的头元素
         *
         * @param key
         * @return 列表的头元素, 当 key 不存在时，返回 nil
         * @throws Exception
         */
        public String lpop(String key) throws Exception {
            if (key == null) {
                return null;
            }
            byte[] lpop = lpop(SafeEncoder.encode(key));
            if (lpop == null) {
                return null;
            }
            return SafeEncoder.encode(lpop);
        }

        /**
         * 移除并返回列表 key 的头元素
         *
         * @param key
         * @return 列表的头元素, 当 key 不存在时，返回 nil
         * @throws Exception
         */
        public byte[] lpop(byte[] key) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                byte[] value = jedis.lpop(key);
                return value;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 移除并返回列表 key 的尾元素
         *
         * @param key
         * @return 列表的尾元素, 当 key 不存在时，返回 nil
         * @throws Exception
         */
        public String rpop(String key) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                String value = jedis.rpop(key);
                return value;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 将一个或多个值 value 插入到列表 key 的表头
         * 如果有多个 value 值，那么各个 value 值按从左到右的顺序依次插入到表头： 比如说，对空列表 mylist 执行命令 LPUSH mylist a b c ，列表的值将是 c b a ，
         * 这等同于原子性地执行 LPUSH mylist a 、 LPUSH mylist b 和 LPUSH mylist c 三个命令
         * 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作
         * 当 key 存在但不是列表类型时，返回一个错误
         *
         * @param key
         * @param value
         * @return 执行 LPUSH 命令后，列表的长度
         * @throws Exception
         */
        public long lpush(String key, String... value) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long count = jedis.lpush(key, value);
                return count;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 将一个或多个值 value 插入到列表 key 的表头
         * 如果有多个 value 值，那么各个 value 值按从左到右的顺序依次插入到表头： 比如说，对空列表 mylist 执行命令 LPUSH mylist a b c ，列表的值将是 c b a ，
         * 这等同于原子性地执行 LPUSH mylist a 、 LPUSH mylist b 和 LPUSH mylist c 三个命令
         * 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作
         * 当 key 存在但不是列表类型时，返回一个错误
         *
         * @param key
         * @param value
         * @return 执行 LPUSH 命令后，列表的长度
         * @throws Exception
         */
        public long lpush(byte[] key, byte[]... value) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long count = jedis.lpush(key, value);
                return count;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 将一个或多个值 value 插入到列表 key 的表尾(最右边)
         * 如果有多个 value 值，那么各个 value 值按从左到右的顺序依次插入到表尾：比如对一个空列表 mylist 执行 RPUSH mylist a b c ，得出的结果列表为 a b c ，
         * 等同于执行命令 RPUSH mylist a 、 RPUSH mylist b 、 RPUSH mylist c
         * 如果 key 不存在，一个空列表会被创建并执行 RPUSH 操作
         * 当 key 存在但不是列表类型时，返回一个错误
         *
         * @param key
         * @param value
         * @return 执行 RPUSH 操作后，表的长度
         * @throws Exception
         */
        public long rpush(String key, String... value) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long count = jedis.rpush(key, value);
                return count;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 将一个或多个值 value 插入到列表 key 的表尾(最右边)
         * 如果有多个 value 值，那么各个 value 值按从左到右的顺序依次插入到表尾：比如对一个空列表 mylist 执行 RPUSH mylist a b c ，得出的结果列表为 a b c ，
         * 等同于执行命令 RPUSH mylist a 、 RPUSH mylist b 、 RPUSH mylist c
         * 如果 key 不存在，一个空列表会被创建并执行 RPUSH 操作
         * 当 key 存在但不是列表类型时，返回一个错误
         *
         * @param key
         * @param value
         * @return 执行 RPUSH 操作后，表的长度
         * @throws Exception
         */
        public long rpush(byte[] key, byte[]... value) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long count = jedis.rpush(key, value);
                return count;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 stop 指定
         * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推
         * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推
         *
         * @param key
         * @param start
         * @param end
         * @return 一个列表，包含指定区间内的元素
         * @throws Exception
         */
        public List<String> lrange(String key, long start, long end) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                List<String> list = jedis.lrange(key, start, end);
                return list;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 stop 指定
         * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推
         * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推
         *
         * @param key
         * @param start
         * @param end
         * @return 一个列表，包含指定区间内的元素
         * @throws Exception
         */
        public List<byte[]> lrange(byte[] key, int start, int end) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                List<byte[]> list = jedis.lrange(key, start, end);
                return list;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 根据参数 count 的值，移除列表中与参数 value 相等的元素
         * count 的值可以是以下几种
         * count > 0 : 从表头开始向表尾搜索，移除与 value 相等的元素，数量为 count 。
         * count < 0 : 从表尾开始向表头搜索，移除与 value 相等的元素，数量为 count 的绝对值。
         * count = 0 : 移除表中所有与 value 相等的值。
         *
         * @param key
         * @param count
         * @param value
         * @return 被移除元素的数量, 因为不存在的 key 被视作空表(empty list)，所以当 key 不存在时， LREM 命令总是返回 0
         * @throws Exception
         */
        public long lrem(byte[] key, int count, byte[] value) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                long c = jedis.lrem(key, count, value);
                return c;
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 删除List中c条记录，被删除的记录值为value
         *
         * @param key
         * @param count
         * @param value
         * @return 被移除元素的数量, 因为不存在的 key 被视作空表(empty list)，所以当 key 不存在时， LREM 命令总是返回 0
         * @throws Exception
         */
        public long lrem(String key, int count, String value) throws Exception {
            if (key == null || value == null) {
                throw new Exception("key or value is null");
            }
            return lrem(SafeEncoder.encode(key), count, SafeEncoder.encode(value));
        }

        /**
         * 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除
         * 举个例子，执行命令 LTRIM list 0 2 ，表示只保留列表 list 的前三个元素，其余元素全部删除
         * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推
         * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推
         * 当 key 不是列表类型时，返回一个错误
         *
         * @param key
         * @param start
         * @param end
         * @return 命令执行成功时，返回 true
         */
        public boolean ltrim(byte[] key, int start, int end) throws Exception {
            Jedis jedis = null;
            try {
                jedis = getJedis();
                String s = jedis.ltrim(key, start, end);
                return "ok".equalsIgnoreCase(s);
            } catch (Exception e) {
                logger.error("Redis操作失败", e);
                throw e;
            } finally {
                returnJedis(jedis);
            }
        }

        /**
         * 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除
         * 举个例子，执行命令 LTRIM list 0 2 ，表示只保留列表 list 的前三个元素，其余元素全部删除
         * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推
         * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推
         * 当 key 不是列表类型时，返回一个错误
         *
         * @param key
         * @param start
         * @param end
         * @return 命令执行成功时，返回 true
         */
        public boolean ltrim(String key, int start, int end) throws Exception {
            if (key == null) {
                throw new Exception("key is null");
            }
            return ltrim(SafeEncoder.encode(key), start, end);
        }
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }
}
