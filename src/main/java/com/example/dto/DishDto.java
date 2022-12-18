package com.example.dto;

import com.example.pojo.Dish;
import com.example.pojo.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

//數據傳輸對象(Data Transfer Object,DTO):一般用於展示層與服務層之間的數據傳輸
@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
