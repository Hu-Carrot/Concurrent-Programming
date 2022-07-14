#Go

## Go mod



## Go 单元测试
>https://books.studygolang.com/The-Golang-Standard-Library-by-Example/chapter09/09.1.html

`go test`

```go
func TestXxx(*testing.T)
```

## Go 编程风格


### 包名
包名应该为小写单词，尽量不要使用下划线或者混合大小写

### 函数

常量、变量、类型、结构体、接口、函数、方法、属性等，全部使用驼峰法 MixedCaps 或 mixedCaps

下划线开头的命名更不允许，Go 语言的公私有统一用大小写开头来区分

## 常量
