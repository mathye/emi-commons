package emi.commons.jdbc.shard.exceptions;

/**
 * 分表策略重复异常，发现对单张表的分表有多个分表策略
 *
 * @author mathye
 * @date 2020-01-01
 */
public class TooManyShardTableResultException extends RuntimeException {

    public TooManyShardTableResultException() {
    }

    public TooManyShardTableResultException(String message) {
        super(message);
    }

    public TooManyShardTableResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public TooManyShardTableResultException(Throwable cause) {
        super(cause);
    }
}
