package org.example.springboot.ai.tool;

import org.example.springboot.common.Result;
import org.example.springboot.entity.Product;
import org.example.springboot.service.ProductService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.List;

@Component
public class FreshTool {

    @Resource
    private ProductService productService;

    @Tool(description = "根据商品名称模糊查询商品信息，返回商品名称、价格、库存数量、产地、保质期。例如用户问'苹果还有多少库存'时调用")
    public String searchProduct(String name) {
        Result<?> result = productService.searchProductForAi(name);
        List<Product> list = (List<Product>) result.getData();
        if (list == null || list.isEmpty()) {
            return "没有找到与「" + name + "」相关的商品。";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("共找到 ").append(list.size()).append(" 个商品：\n");
        for (Product p : list) {
            sb.append(String.format(
                    "商品名称：%s，价格：%.2f 元，库存数量：%d\n",
                    p.getName(), p.getPrice(), p.getStock()
            ));
        }
        return sb.toString();
    }

    @Tool(description = "根据分类ID查询商品列表。分类ID：1-蔬菜，2-水果，3-肉禽蛋，4-海鲜，5-乳品")
    public String listByCategory(Long categoryId) {
        Result<?> result = productService.listByCategoryForAi(categoryId);
        List<Product> list = (List<Product>) result.getData();
        if (list == null || list.isEmpty()) {
            return "该分类下暂无商品。";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("分类 ").append(categoryId).append(" 下共 ").append(list.size()).append(" 个商品：\n");
        for (Product p : list) {
            sb.append(String.format(
                    "商品名称：%s，价格：%.2f 元，库存：%d\n",
                    p.getName(), p.getPrice(), p.getStock()
            ));
        }
        return sb.toString();
    }

    @Tool(description = "查询库存低于指定阈值的商品，用于库存预警。参数threshold为库存阈值，例如50")
    public String lowStock(Integer threshold) {
        Result<?> result = productService.lowStockForAi(threshold);
        List<Product> list = (List<Product>) result.getData();
        if (list == null || list.isEmpty()) {
            return "没有库存低于 " + threshold + " 的商品。";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("以下商品库存低于 ").append(threshold).append("：\n");
        for (Product p : list) {
            sb.append(String.format(
                    "商品名称：%s，当前库存：%d，价格：%.2f 元\n",
                    p.getName(), p.getStock(), p.getPrice()
            ));
        }
        return sb.toString();
    }
}