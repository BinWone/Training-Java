
RESTful API 开发规范
====================


基本形式
----------------------------------

* `GET /weibo` - 获取微博列表

* `GET /weibo/12` - 获取某个微博

* `POST /weibo` - 创建微博

* `PUT /weibo/12` - 修改某个微博

* `PATCH /weibo/12` - 部分修改某个微博

* `DELETE /weibo/12` - 删除某个微博

### 要用名词做资源，动词是操作

资源（EndPoint）应该是名词，而操作应该是动词：GET/POST/PUT/PATCH/DELETE，这五个动作。

### 名词是单数还是复数？

用老的数据库的思路，也就是用复数表示`collection`的含义的思路逐渐受到面向对象的表示对象的思路的挑战，新的API设计倾向于用单数表示各种资源。

### 有关系的资源

* `GET /weibo/12/comment` - 获取某个微博的评论列表

* `GET /weibo/12/comment/5` - 获取某个微博的某个评论

* `POST /weibo/12/comment` - 创建微博的评论

* `PUT /weibo/12/comment/5` - 修改某个微博的某个评论

* `PATCH /weibo/12/comment/5` - 部分修改某个微博的某个评论

* `DELETE /weibo/12/comment/5` - 删除某个微博的某个评论

### 一些不太能归类为CRUD的操作

有些操作，比如“搜索”这个动作，就不太能归结为某个对象上的CRUD操作，这种操作一般仍然用`/search`这种URL来表示，并不完全把它弄成一个名词。

还有些作用在某个对象上的操作，虽然说可以算作CRUD，但独立出来可能更方便。比如针对某个微博点赞的操作，可以独立出来两个操作：

*  `PUT /weibo/12/like` - 给某个微博点赞

*  `DELETE /weibo/12/like` - 删除某个微博的赞

### GET永远不应该修改对象内容

不要用GET操作的过程中修改对象的内容。修改要用相应的PUT/PATCH/DELETE。

### POST/PUT/PATCH可以随Response一起返回修改后的对象

可以把新创建的或者是修改后的对象随着POST/PUT/PATCH请求的Response返回给客户端，这样就不用客户端再去单独请求一次了。


如何做过滤、排序、搜索等
----------------------------

我们经常要在某个对象上做搜索过滤和排序等操作，这些操作貌似不太能对应到POST/PUT/PATCH/DELETE等这些操作上，而且我们并不需要修改对象，我们只是想更复杂的查询这些对象，这样的查询我们用GET加上参数来实现。

* `GET /weibo?deleted=true` - 得到所有标记为删除的微博

* `GET /weibo?sort=-create-date` - 按照创建日期反序排序得到微博列表

* `GET /weibo?sort=comment-count,-create-date` - 先按微博评论数再按创建日期反序排序得到微博列表

* `GET /weibo?keyword=xxx` - 得到包含关键字xxx的微博列表

* `GET /weibo?keyword=xxx&deleted=true&sort=comment-count,-create-date` - 搜索关键字包含xxx的标记为删除的微博，按照评论数再按创建日期反序排序得到微博列表

### 常用的查询可以独立出来

如果你的系统有些常用的查询，可以独立出来方便使用者调用。比如，top10的微博的操作，可以作为一个独立的查询，单独有个名字。

* `GET /weibo/top10` - 得到最热前10名的微博列表

如何控制返回结果
------------------------

我们可以用RESTful API做普通的CRUD，也可以做复杂查询，但是现在我想要得到不同的结果怎么办？这并不是一个很常见的需求，但也是一个正常的需求，遇到这种问题，同样也是用GET上带查询参数来解决。

### 包含或排除某些字段

* `GET /weibo?fields=id,subject` - 得到微博列表，每个微博里的字段只有`id`和`subject`两个，其他的都排除了。

* `GET /weibo?fields-exclude=id,subject` - 得到微博列表，每个微博里的`id`和`subject`被排除了，输出其他的字段。

### 嵌入某些相关的对象或者属性

* `GET /weibo?embed=owner.id,owner.name` - 得到微博列表，每个微博里的信息里加入这个微博的所有者的id和name两个属性。

最后的结果大致是这样的：
```
{
  “id” : 12,
  “subject”: “This is a Weibo message”,
  “owner”: {
     “id”: 5,
     “name”: “hu2"
   }
}
```

JSON格式
---------------

虽然XML也可以，但是我们基本上不会用它了，现在都是用JSON格式。

### 提交都是JSON格式

我们很熟悉用GET查询一些数据得到JSON的response，同样，如果我们有什么要提交的，也应该是JSON格式，不能是简单的Form提交的格式。

也就是说，在页面上的提交，都要封装成JSON的数据格式再用POST/PUT/PATCH发送到服务器上来。

### JSON的命名

JSON数据中的各个属性，都要按照camelCase来命名，因为JSON是JavaScript Object Notation，所以通常来讲我们按照 JavaScript的方式来命名，也就是camelCase。

这样命名可以保证API的一致性，不会因为后端采用的是什么语言而变化。

### 不要过度封装JSON返回

我们经常见的一种封装返回的方式：
```
{
  “return”: true,
  “message”: “xxxx”,
  “data”: {
    “id”: 12,
    “subject”: “This is a weibo message”,
  }
}
```
这种方式没有什么价值，应该直接用对象，不要外面再封装。我们可以用返回码和HTTP Header来表示其他的状态和数据，返回的正文就是留给对象本身的。

返回码
---------------------
我们直接采用HTTP的返回码来应对我们的操作。

* `200 OK` - 正常返回，用于GET, PUT, PATCH的这些正常操作。

* `201 Created` - 用于POST创建对象正常返回。

* `204 No Content` - 用于DELETE，成功操作但没啥返回的。

* `304 Not Modified` - 有Cache，表示没改过。

* `400 Bad Request` - 指请求里面有些参数不对。
* `401 Unauthorized` - 没有登录
* `403 Forbidden` - 登录了但是没有授权访问某个资源
* `404 Not Found` - 资源不存在
* `405 Method Not Allowed` - 登录了但是不允许做某个操作
* `410 Gone` - 表示资源不再提供了，用来做老版本提示用的
* `415 Unsupported Media Type` - 請求的Centent Type不對
* `422 Unprocessable Entity` - 一般用于validation校验
* `429 Too Many Requests` - 请求太快太多，达到限制

版本
------------------------

* `/api/v2/weibo/12/comment/5`

我们用这种在URL上加版本信息的方式来指定版本。这种在URL指定的版本属于大版本，表示着输入输出的格式是一定的，不会变化。可以在Requst的Header里面加入更详细的版本信息，表示想要哪个更小的版本。但是这种做法太过精细，维护起来比较困难，所以，通常都只是采用在URL上加大版本号的做法。

指定版本号，一般的要求是强制的。但是对于第一版来说，在功能还未完全定型的情况下，其实那个`v1`用不着，只有到功能基本完成，API也定型了，最后改为`v1`即可。

翻页
---------------------

翻页是个稍微混乱的事情，以前一般是在查询参数里面指定诸如`page=3&per-page=100`等来做翻页控制，而在返回数据中封装一个包含查询的结果总数，当前页数，总页数，和数据的东西。这么做违背了我们上面不要封装的要求，貌似不是很好。

最近比较流行的是一种Link Header的做法。它把翻页所需要的链接，直接作为header返回来，类似这样的：

```
Link: <https://api.github.com/search/code?q=addClass+user%3Amozilla&page=15>; rel="next",
  <https://api.github.com/search/code?q=addClass+user%3Amozilla&page=34>; rel="last",
  <https://api.github.com/search/code?q=addClass+user%3Amozilla&page=1>; rel="first",
  <https://api.github.com/search/code?q=addClass+user%3Amozilla&page=13>; rel="prev"
```
我们通过这个返回的header，就可以知道翻页的链接，就不用自己去组装了。

而所有条目总数可以用`X-Total-Count`这样的header来表示。

当然，查询的时候，仍然是用`page=3&per-page=100`来控制每页显示多少条目。

重载HTTP方法
-----------------
有些HTTP PROXY只能支持POST和GET两个方法，那我们定义的PUT/PATCH/DELETE怎么办呢？
我们可以在请求Header里面加入一个`X-HTTP-Method-Override`来表示我们的操作是什么，而整体的Request用POST发出去。


限制访问频率
-----------------

我们提供一种服务，就要防止被被人滥用，所以需要有一些机制做访问频率的限制（Rate-Limit）。通常是用返回码429表示用户达到了这个限制。但是在达到限制之前，最好也能让人知道限制是多少，这个通常用返回的Header来做。

* `X-Rate-Limit-Limit` - 表示每个累计周期内的访问次数最大是多少
* `X-Rate-Limit-Remaining` - 表示目前的累计周期内还剩余多少次可以用
* `X-Rate-Limit-Reset` - 表示还有多少秒钟累计周期就到期了，可以开始下一个周期了


Pretty Print加上gzip压缩
-----------------------------

如果你的JSON输出不是用回车和空格做好格式的，那么在浏览器上看到的时候就是一行长长的，很难看的懂，所以还是按照Pretty Print的方式输出比较好。虽然可以用各种浏览器插件来解决这个问题，但有时用命令行比如curl来调试的时候就没办法用浏览器的插件。所以，还是要用输出的比较好看才行。

另外，用gzip压缩一下JSON的文本，会让网络上传输的尺寸大幅度降低，这是很经济的事。

SSL
-----------------------

为了安全起见，API都必须要在https上面，不加密的http就像是裸奔，自己跑的欢，别人看的也欢。

文档
-----------------------

API，是针对程序员的User Interface，必须讲究用户体验。而对于程序员来说，通过文档来了解一套API是最直接有效的方式，好的API文档就是好的API。

一个API的文档中，要包含Request的使用方法，包括：URL(方法、资源、参数)的定义，header定义，body中的内容定义。Response的code，header定义，body内容定义。

上面的定义一般是通过表格的形式体现，在定义之后一般需要有示例的Request和Response。

