
数据库设计命名规范
===================

1. 数据库、表、字段的名字要含义明确，并保持一致
----------------------------------

这估计是最难的。

这个过程需要业务人员透彻讲解业务，技术人员吸收后定义出含义明确的命名，然后这些命名，会直接影响到编程实现的命名，还有其他各个层次的命名。所以这个命名的过程是非常重要的，急不得，要跟业务员仔细讨论。

命名不要用缩写，比如date缩写成dt，这种缩写对于中国人来说毫无意义，凭空增加烦恼，会对以后的开发维护人员造成很多的学习障碍。名字长一点没关系，但是又不能无所谓的拉长。简洁、易懂、易维护。

数据库的名词要一致。不能在这个地方叫一个名字，到另外一个表又叫另外一个名字。


2. Table用单数，Column用单数
--------------------------------

我在Table名字上到底是用单数还是复数曾经纠结过很多年，但是最近几年倾向于用单数。原因有两个：

首先，面向对象的编程方式占据主流，而ORM框架的流行，让程序员离SQL数据库更远，从对象的角度讲，单数更合理些。大多数支持复数的角度，都是在数据库本身的角度，认为一个Table是一个集合，所以应该用复数。但是现在越来越多的对象化，直接利用到数据库的程序员少了，所以，按照对象的角度来考虑更符合潮流。所以我支持单数。

其次，中国人其实对单数复数不敏感。有些英语单词的复数形式很复杂，容易写错，还有些单词没有复数形式，让中国人看了产生不一致的感觉，而且，还有在语意上有单复数区别的就更难把握，比如Person和People等。中国人其实都用单数，反而更简单更直接，也不会有理解上的问题，因为中文原本就没有单复数这个玩意儿。

所以，数据库的命名，都是用单数，对于我们更合适。


3. Snake Case，不用特殊符号，避免SQL关键字。
--------------------------------

lowercase_separated_by_underscores，也表示成：Snake Case。全部小写用下划线分隔的命名方式，也是最近几年逐渐流行的。之前是首字母大写的CamelCase形式。再之前是全部大写下划线分隔，那简直是噩梦。我觉得全部小写下划线分隔之所以会逐渐流行，除了有科学研究说，可读性比CamelCase大20%之外，也是因为面向对象的ORM流行的结果。

名字中不要带空格，不要带标点符号，不要带除了小写字母和数字之外的任何奇怪东西。

名字也要避免SQL关键字，Postgresql有个列表， http://www.postgresql.org/docs/9.3/static/sql-keywords-appendix.html ，在起名字的时候可以先搜索一下这个表。

关键字虽然可用，但是要加引号引起来，在手写SQL的时候经常忘记，然后就会出错，然后就会莫名其妙，然后半天找不到错在哪儿，这种坑相信很多人都碰到过，然后就会问候当初这个数据库的设计者的长辈。

常用的time/date来表示这个数据的时间概念，但是这是否精确呢？这时间到底是指的什么时间，用create_date/update_date/sample_date这些更明确的定义，不仅表达更准确，而且避免了用关键字的麻烦。


4. Table不用Prefix，用schema来划分命名空间
--------------------------------

有的人用prefix来区分用途，比如Table、View、Function、Procedure等一个Database下的东西，就用tb_、vw_、fn_、usp_，等前缀来区分他们。

首先，这样做有问题，Table和View对外提供功能类似，是可以互相转化的，View改成了Table，如果加上这些前缀，就增加难度了。

其次，我觉得现在的趋势，Function、Procedure等大量使用的机会已经不多了，往使用Procedure的系统仅仅是一些特殊的地方，主体程序中都是面向对象的，几乎不会看到这些数据库Procedure什么的了。所以，不用把所有的Table都加上前缀，Table就用简单直接的形式，少量的Function、Procedure自己加前缀就好了。

另外，还有加Prefix，是因为想分组，比如auth_xxx都是auth的功能，employe_xxx都是employee的功能。对于这种需求，现在也都是用schema来分组，不再用prefix这种很难维护的方式了。Postgresql关于schema的使用场景的说明：
```
There are several reasons why one might want to use schemas:

To allow many users to use one database without interfering with each other.

To organize database objects into logical groups to make them more manageable.

Third-party applications can be put into separate schemas so they do not collide with the names of other objects.

Schemas are analogous to directories at the operating system level, except that schemas cannot be nested.
```

5. Index、Constraints要指定的命名
--------------------------------

很多可视化的SQL工具，IDE等，都可以通过对象关系直接生成Index和其他的Constraints的限制等，但是这些东西的名字都是随机的，或者是加了数字后缀等，总之，经常变化，不统一。所以，还是需要特别指定他们的名字，这样，在git等版本控制做diff的时候，就非常明确的知道这个东西到底是改了还只是名字被IDE给换了。


6. 主键的名字是id，外键才要是`xxx_id`
--------------------------------

主键的名字永远都是同一的，就是id。有的说要加上table的名字，表示是哪个表的主键，这太浪费了，而且，跟外键的规则重复，无法分辨了。外键才需要加table的名字，诸如`xxx_id`、`yyy_id`，这样的。

7. 代表个数、次数等数量概念的，用count
--------------------------------

表示数量可以写成`xxx_number`/`xxx_num`/`xxx_no`等，但这些都不好，最好的是`xxx_count`。

8. timestamp类型要有timezone，字段名用`xxx_date`的形式
----------------------------------

在定义时间类型时，要使用timestamp with timezone的定义，不要为了节省一点点空间把timezone去掉，这会造成时间的不确定和不统一，尤其是在地理上不在一个时区的分布式环境中会有大问题。

时间类型（timestamp）字段的名字，会是一个比较混乱的地方，有叫`xxx_time`的，有的直接用了SQL关键字：`date`/`time`/`timestamp`，我们在讲避免使用SQL关键字的时候说到过时间类型。我们对时间类型统一规定命名为`xxx_date`的形式。

在数据库设计中绝大多数情况是用`timestamp`来表示通用的时间概念，很少情况才会用到单纯的年月日的`date`类型和单纯的时分秒的`time`类型。但是如果每个时间字段都叫做`xxx_timestamp`，又感觉太长了，不舒服。我们遇到的大部分编程语言对时间的概念用的是date这个词，从编程这个角度出发，大家发现其实用`xxx_date`这种更简短的形式更舒服，含义也没有什么歧义，因为数据库中真正用到单纯的年月日的`date`类型的机会真心不多，所以绝大部分情况用`xxx_date`来表示timestamp是可行的，而在用到单纯的年月日的时候，可以用`xxx_day`这种名称，单纯的时分秒的时候用`xxx_time`。

所以总结来说，通常的时间用`xxx_date`，仅表示年月日用`xxx_day`，仅表示时分秒用`xxx_time`。

9. boolean类型的命名要用`is_xxx`格式
----------------------------------

bool值的字段，表示的是一个状态，所有的状态都可以表示为`is_xxx`的形式，这是一个很通用的命名规则。
