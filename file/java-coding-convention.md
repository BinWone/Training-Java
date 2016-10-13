Java Coding Convention
==========================

1. 说明
------------

Google对大部分语言，都有比较完整的Style Guide，Java自然不例外。

我们的代码规范，基本上和Google Java Style Guide一致。

请参考：http://google-styleguide.googlecode.com/svn/trunk/javaguide.html

2. 源文件
------------

* 文件名和文件中顶层类的名字一致

* 所有源文件的编码为UTF-8

* 源文件中的空格必须是 **纯空格** ，不是`\t \r \n \f`等。



3. 源文件结构
------------

3.1. License和Copyright信息
    
* 如果有必要，可以有
* 去掉IDE自动模板里面的信息

3.2. Package声明

* 不折行(不受源代码80/100/120行的限制)

3.3. Import声明

* 不用`java.io.*`这样的通配符，明确列出所有imports
* 不折行(不受源代码80/100/120行的限制)
* Imports的顺序：
    1. 所有static import是一组
    2. 团队或公司内部类的import是一组
    3. 第三方库的import，按照库分成若干组
    4. java的import
    5. javax的import

3.4. Class的声明
    
* 一个文件有且只有一个顶层类。
* 类成员函数的顺序，逻辑紧密的在一起。public函数在前，protected/private在后。
* Overload的方法，要在一起。

4. 代码格式
------------

4.1. 花括号

* 例子

```
return new MyClass() {
  @Override public void method() {
    if (condition()) {
      try {
        something();
      } catch (ProblemException e) {
        recover();
      }
    }
  }
};
```

* 只有一句的，也要有花括号
* 没有内容的，花括号可以是这样的：`{}`

4.2. 缩进

* 缩进4个空格，Google是2个。

4.3. 每行宽度
    
* 每行宽度：80/100/120.
* 一些特殊场景是例外，比如package/import，还有命令行等不能折行的。

4.4. 变量定义

* 一行一个，`int a, b;`这种不要。
* 哪儿用哪儿定义，定义完了马上赋值。

4.5. Switch语句

* 必须有default
* 如果一个case没有break，那么要明确注释"//进入下一个case”等字样

4.6. 修饰符的顺序

* `public protected private abstract static final transient volatile synchronized native strictfp`

4.7. long型

* 必须要用大写L，不能用小写l。


5. 命名规则
------------

5.1. Package，

* 正确：`com.example.deepspace`
* 错误：`com.example.deepSpace` or `com.example.deep_space`

5.2 Class

* UpperCamelCase

5.3 函数和变量

* lowerCamelCase。

5.4 常量

* CONSTANT_CASE

5.5 CamelCase的例子

* 正确：`XmlHttpRequest, newCustomerId, supportsIpv6OnIos`
* 错误：`XMLHTTPRequest, newCustomerID, supportsIPv6OnIOS`


6. 其他
------------

6.1 try/catch的异常处理不能为空，如果确实是没啥处理，也要有注释说明此处确实没啥要干的。

6.2 静态方法必须通过类名访问，不能对象名访问。
