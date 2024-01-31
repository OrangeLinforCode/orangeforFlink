package com.orange.lin.utils.compareUtils;

import cn.hutool.core.lang.Assert;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.collections.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Data
@Slf4j
public class CompareObject<T> {

    private CompareStatus status;


    /**
     * 原始值
     */
    private T original;

    /**
     * 当前值
     */
    private T current;

    /**
     * 需要对比的属性
     */
    private List<String> includeProperties;


    /**
     * 需要排除的属性
     */
    private List<String> excludeProperties;


    /**
     * 不相等的比较结果
     */
    private List<CompareResult> noEqualCompareResults = new ArrayList<>();


    private List<CompareResult> equalCompareResults = new ArrayList<>();


    public void compareDiff(Class<T> cls){
        Assert.notNull(this.current,"current is must be no empty");

        boolean isEqual =true;

        Field[] fields =this.getAllFields(cls);
        for (Field field : fields){
            try{
                if(CollectionUtils.isNotEmpty(this.includeProperties) && !this.includeProperties.contains(field.getName())){
                    continue;
                }

                if(CollectionUtils.isNotEmpty(this.excludeProperties) && this.excludeProperties.contains(field.getName())){
                    continue;
                }

                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), cls);
                Method readMethod = propertyDescriptor.getReadMethod();
                Object o1 = this.original == null ? null: readMethod.invoke(this.original);
                Object o2 = this.current == null ? null : readMethod.invoke(this.current);
                CompareResult compareResult = new CompareResult(field.getName(), o1, o2);
                if(Objects.equals(o2,o1)){
                    this.equalCompareResults.add(compareResult);
                }else {
                    isEqual=false;
                    this.noEqualCompareResults.add(compareResult);
                }

            }catch (Exception e){
                log.debug(e.getMessage());
            }
        }

        if(this.original == null){
            this.status = CompareStatus.NEW;
        }else if(isEqual) {
            this.status = CompareStatus.NO_CHANGE;
        }else {
            this.status = CompareStatus.CHANGE;
        }
    }

    private Field[]  getAllFields(Class clazz){
        ArrayList<Field> list = new ArrayList<>();
        while(clazz!=null){
            list.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[list.size()];
        list.toArray(fields);
        return fields;
    }

}
