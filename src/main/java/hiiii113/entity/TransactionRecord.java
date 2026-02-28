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
    private Integer id;
    private Integer userId;
    private Integer type;
    private BigDecimal amount;
    private Integer targetUserId;
    private LocalDateTime createTime;
}
