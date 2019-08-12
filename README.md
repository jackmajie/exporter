# exporter
构建微服务必不可少的导出导入服务



一、背景

1.	使用场景
管理后台excel表格导入导出。
2.	解决问题
导出 : 解决财务订单对账问题，方便客服，运营导出列表数据进行日常工作需要。
导入 : 方便运营批量发放优惠卷。


二、业务流程
 
1. 导出业务流程图
 
(1) Execl模板配置
导出需要配置两张表t_data_exporter_template和t_data_exporter_conf。
t_data_exporter_template：报表模板，表中的columns列配置的是Execl列标题和列长度，多列之间用分号隔开。如： 银行户名:6;开户银行:6;
t_data_exporter_conf：报表配置，保存的是Excel导出任务的标识及对应模关系，以下的文档中的导出任务标识举例为优惠的导出标识:
COUPOND_INFO_LIST_01
(2) Zookeeper配置
在Zookeeper配置文件的taskServices节点下增加
<taskService>
  <version>0.0.4</version>
  <group>COUPOND_INFO_LIST_01</group>
  <timeOut>3000</timeOut>
  <application>exporter-service</application>
</taskService
Zookeeper配置文件路径/ConfigCenter/Dataexporter/defaultMultiConf
0.0.4为第4步中的导出回调实现类的duboo服务版本
 
 (3) 创建导出任务
创建导出任务，任务标识为COUPOND_INFO_LIST_01，然后等待导出。
 
(4) 导出回调
创建导出回调实现类(dubbo服务)，导出回调实现类中查询需要导出的所有数据，然后按照t_data_exporter_template的columns中配置列的顺序进行数据填充。
第2步创建导出任务之后，导出服务器会定时检查没有完成导出的任务，
当导出服务器检查到第2步的任务之后，调用优惠券中的导出回调实现方法(dubbo服务)进行数据导出。

2. 导入流程

同步流程: 
前端上传excel表格 -> 应用传递excel表格-> 导入应用接收excel表格->按照约定规则进行解析 -> 封装数据并返回

(1) Execl数据准备
Excel表格导入格式如下：
第一行->变量名，取数据时通过数量名来取数据，模板中这一行可隐藏。
第二行->变量中文名称，方便用户更好理解这一列的含义。
第三行或第三行之后->数据内容
示例如图1，当前为优惠券快速导入商品Execl，商品的变量名为id,中文称为”商品ID”，商品id是10.
 

(2) 导入Excel
后端模拟http请求进行导入，http请求参数的数据格式为MultipartFile,请求之后会返回json格式的数据，json格式如图2:
 


然后后端需要解析json数据，取得data中的数据进行其他的业务处理，
data节点下的数据是数组格式的,每一个数组代表Excel中的一行。





