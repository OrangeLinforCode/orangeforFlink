package com.orange.lin.utils.compareUtils;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompareResult {

    private String propertyName;

    private Object originalValue;

    private Object currentValue;
}
