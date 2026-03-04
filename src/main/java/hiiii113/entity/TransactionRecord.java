package hiiii113.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRecord
{
    private Integer id; // 流水id
    private Integer userId; // 用户id
    private Integer type; // 交易类型(1. 存款 2. 取款 3. 转入 4. 转出)
    private BigDecimal amount;// 单词流水金额
    private Integer targetUserId; // 目标用户id
    private BigDecimal newBalance;
    private LocalDateTime createTime; // 流水创建时间
}
