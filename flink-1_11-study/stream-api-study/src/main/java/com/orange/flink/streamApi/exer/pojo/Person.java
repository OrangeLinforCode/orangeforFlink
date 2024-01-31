package com.orange.flink.streamApi.exer.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    public String name;
    public Integer age;

    @Override
    public String toString(){
        return this.name.toString() + ": age " + this.age.toString();
    }
}
