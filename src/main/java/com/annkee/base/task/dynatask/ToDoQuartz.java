package com.annkee.base.task.dynatask;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wangan
 * @date 2019/1/24
 */
@Data
public class ToDoQuartz implements Serializable {
    
    private static final long serialVersionUID = 4811527781172576219L;
    
    private String name = "";
    
    private Date executeDate = new Date();
}
