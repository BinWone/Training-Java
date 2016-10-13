Java Coding Convention
==========================

1. ˵��
------------

Google�Դ󲿷����ԣ����бȽ�������Style Guide��Java��Ȼ�����⡣

���ǵĴ���淶�������Ϻ�Google Java Style Guideһ�¡�

��ο���http://google-styleguide.googlecode.com/svn/trunk/javaguide.html

2. Դ�ļ�
------------

* �ļ������ļ��ж����������һ��

* ����Դ�ļ��ı���ΪUTF-8

* Դ�ļ��еĿո������ **���ո�** ������`\t \r \n \f`�ȡ�



3. Դ�ļ��ṹ
------------

3.1. License��Copyright��Ϣ
    
* ����б�Ҫ��������
* ȥ��IDE�Զ�ģ���������Ϣ

3.2. Package����

* ������(����Դ����80/100/120�е�����)

3.3. Import����

* ����`java.io.*`������ͨ�������ȷ�г�����imports
* ������(����Դ����80/100/120�е�����)
* Imports��˳��
    1. ����static import��һ��
    2. �Ŷӻ�˾�ڲ����import��һ��
    3. ���������import�����տ�ֳ�������
    4. java��import
    5. javax��import

3.4. Class������
    
* һ���ļ�����ֻ��һ�������ࡣ
* ���Ա������˳���߼����ܵ���һ��public������ǰ��protected/private�ں�
* Overload�ķ�����Ҫ��һ��

4. �����ʽ
------------

4.1. ������

* ����

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

* ֻ��һ��ģ�ҲҪ�л�����
* û�����ݵģ������ſ����������ģ�`{}`

4.2. ����

* ����4���ո�Google��2����

4.3. ÿ�п��
    
* ÿ�п�ȣ�80/100/120.
* һЩ���ⳡ�������⣬����package/import�����������еȲ������еġ�

4.4. ��������

* һ��һ����`int a, b;`���ֲ�Ҫ��
* �Ķ����Ķ����壬�����������ϸ�ֵ��

4.5. Switch���

* ������default
* ���һ��caseû��break����ôҪ��ȷע��"//������һ��case��������

4.6. ���η���˳��

* `public protected private abstract static final transient volatile synchronized native strictfp`

4.7. long��

* ����Ҫ�ô�дL��������Сдl��


5. ��������
------------

5.1. Package��

* ��ȷ��`com.example.deepspace`
* ����`com.example.deepSpace` or `com.example.deep_space`

5.2 Class

* UpperCamelCase

5.3 �����ͱ���

* lowerCamelCase��

5.4 ����

* CONSTANT_CASE

5.5 CamelCase������

* ��ȷ��`XmlHttpRequest, newCustomerId, supportsIpv6OnIos`
* ����`XMLHTTPRequest, newCustomerID, supportsIPv6OnIOS`


6. ����
------------

6.1 try/catch���쳣������Ϊ�գ����ȷʵ��ûɶ����ҲҪ��ע��˵���˴�ȷʵûɶҪ�ɵġ�

6.2 ��̬��������ͨ���������ʣ����ܶ��������ʡ�
