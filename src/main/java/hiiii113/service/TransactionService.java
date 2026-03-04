package hiiii113.service;

import hiiii113.entity.TransactionRecord;
import hiiii113.util.Result;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService
{
    /**
     * 查看当前账户的交易流水
     * @param userId 要查看的用户id
     * @return 返回
     */
    List<TransactionRecord> getTransactionRecord(Integer userId);

    /**
     * 存款
     * @param userId 需要存入的用户id
     * @param amount 存入的金额
     * @return 返回是否存入成功以及相关消息和新的balance
     */
    Result deposit(Integer userId, BigDecimal amount);

    /**
     * 取款
     * @param userId 需要取款的用户id
     * @param amount 取出的金额
     * @return 返回是否取出成功以及相关消息和新的balance
     */
    Result withdraw(Integer userId, BigDecimal amount);

    /**
     * 转钱
     * @param userId 需要转出的账户id
     * @param targetUserId 需要转入的账户id
     * @param amount 交易金额
     * @return 返回是否交易成功和相关信息以及新的balance
     */
    Result transfer(Integer userId, Integer targetUserId, BigDecimal amount);

    /**
     * 生成用于业务展示的流水记录
     * @param transactionRecord 传入的流水记录
     * @return 返回一个字符串
     */
    String formatTransactionRecord(TransactionRecord transactionRecord);
}
