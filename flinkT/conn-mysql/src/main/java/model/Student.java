package model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @program: flinkT
 * @description:
 * @author: geqx
 * @create: 2020-11-26 15:38
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
//    public int id;
    public String name;
    public String password;
    public int age;
}
