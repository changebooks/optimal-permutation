# optimal-permutation
### 全排列数组，计算所有排列的结果，从中选最优结果。
### full permutation array, calculate optimal permutation's result.

---

### 使用场景，如：共享优惠
```
7种折扣方式，可折上折
1）订单金额满700.00元，1折
2）订单金额满600.00元，2折
3）订单金额满500.00元，3折
4）订单金额满400.00元，4折
5）订单金额满300.00元，5折
6）订单金额满200.00元，6折
7）订单金额满100.00元，7折
```

### 计算折扣流程
```
订单金额：1000.00元
    流程1
        满700元，1折 > 折后100元 > 满100元，7折 > 最终70元
    流程2
        满600元，2折 > 折后200元 > 流程2.1
                                     满200元，6折 > 折后120元 > 满100元，7折 > 最终84元
                                 流程2.2
                                     满100元，7折 > 折后140元 > 满100元，7折 > 最终98元
    流程3
        满500元，3折 > 折后300元 > 流程3.1
                                     满300元，5折 > 折后150元 > 满100元，7折 > 折后105元 > 满100元，7折 > 最终73.5元
                                 流程3.2
                                     满200元，6折 > 折后180元 > 满100元，7折 > 折后126元 > 满100元，7折 > 最终88.2元
                                 流程3.3
                                     满100元，7折 > 折后210元 > 流程3.3.1
                                                                  满200元，6折 > 折后126元 > 满100元，7折 > 最终88.2元
                                                              流程3.3.2
                                                                  满100元，7折 > 折后147元 > 满100元，7折 > 折后102.9元 > 满100元，7折 > 最终72.03元
    流程...
```

### 引包 pom.xml
```
<dependency>
  <groupId>io.github.changebooks</groupId>
  <artifactId>optimal-permutation</artifactId>
  <version>1.0.3</version>
</dependency>
```

### 例子 sample
```
/**
 * 折扣规则
 */
public class Rule {
    /**
     * 订单金额
     */
    private BigDecimal price;

    /**
     * 折扣率
     */
    private BigDecimal rate;

    public Rule(BigDecimal price, BigDecimal rate) {
        this.price = price;
        this.rate = rate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getRate() {
        return rate;
    }

}

// 规则列表
Rule[] rules = {
        // 订单金额满700.00元，1折
        new Rule(BigDecimal.valueOf(700.00), BigDecimal.valueOf(0.1)),

        // 订单金额满600.00元，2折
        new Rule(BigDecimal.valueOf(600.00), BigDecimal.valueOf(0.2)),

        // 订单金额满500.00元，3折
        new Rule(BigDecimal.valueOf(500.00), BigDecimal.valueOf(0.3)),

        // 订单金额满400.00元，4折
        new Rule(BigDecimal.valueOf(400.00), BigDecimal.valueOf(0.4)),

        // 订单金额满300.00元，5折
        new Rule(BigDecimal.valueOf(300.00), BigDecimal.valueOf(0.5)),

        // 订单金额满200.00元，6折
        new Rule(BigDecimal.valueOf(200.00), BigDecimal.valueOf(0.6)),

        // 订单金额满100.00元，7折
        new Rule(BigDecimal.valueOf(100.00), BigDecimal.valueOf(0.7))
};
```

### 生成数组下标的全排列
```
// 生成数组下标的全排列
// 如，
// 数组下标：[0, 1, 2]
// 数组下标的全排列：[0, 1, 2], [0, 2, 1], [1, 0, 2], [1, 2, 0], [2, 0, 1], [2, 1, 0]
Permutations swapPermutations = new SwapPermutations();

// 生成数组下标的全排列的优化算法
// 预缓存n个全排列，避免重复计算
// 如，n = 3
// 预缓存
// [
//   [],
//   [0],
//   [
//     [0, 1],
//     [1, 0]
//   ],
//   [
//     [0, 1, 2],
//     [0, 2, 1],
//     [2, 0, 1],
//     [2, 1, 0],
//     [1, 0, 2],
//     [1, 2, 0]
//   ]
// ]

// 缓存中最大排列长度
int n = 7;
Permutations cachePermutations = new CachePermutations(swapPermutations, n);
```

### 标准算法，计算数组的最优排列
```
// 数组长度：n
// 计算次数：(n * n!)
// 如，n = 7，计算次数：35280
Calculator<Rule, BigDecimal> standardCalculator = new StandardCalculator<>(cachePermutations);

// 求最优订单金额，和该金额的下标排列
Result<BigDecimal> optimalResult = standardCalculator.calculate(rules, (values, indexes, startInclusive, endExclusive, prepareR) -> {
    // 原价
    BigDecimal price = BigDecimal.valueOf(1000.00);

    for (int i : indexes) {
        Rule rule = values[i];
        if (price.compareTo(rule.getPrice()) >= 0) {
            // 满足规则，打折
            price = price.multiply(rule.getRate());
        }
    }

    return price;
});

// optimalResult = {"data": 70.00, "indexes": [0, 1, 2, 3, 4, 5, 6]}
```

### 预缓存算法，缓存前3个元素的计算结果，减少计算次数
```
// 数组长度：n
// 计算次数：(3 * n!) / ((n - 3)!) + (n - 3) * n!
// 如，n = 7，计算次数：20790
// 注：最大支持的数组长度 = 7
Calculator<Rule, BigDecimal> cache3Calculator = new Cache3Calculator<>(cachePermutations);

// 求最优订单金额，和该金额的下标排列
Result<BigDecimal> optimalResult = cache3Calculator.calculate(rules, (values, indexes, startInclusive, endExclusive, prepareR) -> {
    if (startInclusive == 0) {
        // 预计算

        // 原价
        BigDecimal price = BigDecimal.valueOf(1000.00);
        for (int i = startInclusive; i < endExclusive; i++) {
            Rule rule = values[i];
            if (price.compareTo(rule.getPrice()) >= 0) {
                price = price.multiply(rule.getRate());
            }
        }

        return price;
    }

    // 业务计算，prepareR = 预缓存的订单金额（前3个元素的计算结果）
    BigDecimal price = prepareR;

    for (int i = startInclusive; i < endExclusive; i++) {
        Rule rule = values[i];
        if (price.compareTo(rule.getPrice()) >= 0) {
            price = price.multiply(rule.getRate());
        }
    }

    return price;
});

// optimalResult = {"data": 70.00, "indexes": [0, 1, 2, 3, 4, 5, 6]}
```
