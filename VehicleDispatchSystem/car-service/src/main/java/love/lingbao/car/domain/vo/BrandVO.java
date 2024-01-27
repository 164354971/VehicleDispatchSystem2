package love.lingbao.car.domain.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import love.lingbao.car.domain.po.Brand;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "品牌列表页")
@Data
public class BrandVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String initial;
    private List<Brand> BrandList;
}
