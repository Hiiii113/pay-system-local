package hiiii113.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class User
{
    private Integer id;
    private String username;
    private String password;
    private BigDecimal balance;

    public User(String username, String password)
    {
        this.username = username;
        this.password = password;
        this.balance = new BigDecimal(0);
    }
}
