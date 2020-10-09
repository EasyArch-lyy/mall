"# mall " 

es配置
-
因为安全问题elasticsearch不让用root用户直接运行，所以要创建新用户

第一步：liunx创建新用户 adduser XXX    然后给创建的用户加密码passwd XXX    输入两次密码。

第二步：切换刚才创建的用户su XXX  然后执行elasticsearch 会显示Permissiondenied 权限不足。

第三步：给新建的XXX赋权限，chmod 777*  这个不行，因为这个用户本身就没有权限，肯定自己不能给自己付权限。所以要用root用户登录付权限。

第四步：root给XXX赋权限，chown -RXXX /你的elasticsearch安装目录。
 

解决方法1：

在执行elasticSearch时加上参数-Des.insecure.allow.root=true，完整命令如下

[plain] viewplain copy

1.  ./elasticsearch -Des.insecure.allow.root=true  

解决办法2：

用vi打开elasicsearch执行文件，在变量ES_JAVA_OPTS使用前添加以下命令

[plain] viewplain copy

1.  ES_JAVA_OPTS="-Des.insecure.allow.root=true"  

如下图所示，这个方法的好处是以后不用添加参数就能以root身份执行了

 

 

config目录下有2个配置文件：es的配置文件（elasticsearch.yml） 和日志配置文件（logging.yml ）

cluster.name: elasticsearch
配置es的集群名称，默认是elasticsearch，es会自动发现在同一网段下的es，如果在同一网段下有多个集群，就可以用这个属性来区分不同的集群。

node.name: "Franz Kafka"
节点名，默认随机指定一个name列表中名字，该列表在es的jar包中config文件夹里name.txt文件中，其中有很多作者添加的有趣名字。

node.master: true
指定该节点是否有资格被选举成为node，默认是true，es是默认集群中的第一台机器为master，如果这台机挂了就会重新选举master。

node.data: true
指定该节点是否存储索引数据，默认为true。

index.number_of_shards: 5
设置默认索引分片个数，默认为5片。

index.number_of_replicas: 1
设置默认索引副本个数，默认为1个副本。

path.conf: /path/to/conf
设置配置文件的存储路径，默认是es根目录下的config文件夹。

path.data: /path/to/data
设置索引数据的存储路径，默认是es根目录下的data文件夹，可以设置多个存储路径，用逗号隔开，例：
path.data: /path/to/data1,/path/to/data2

path.work: /path/to/work
设置临时文件的存储路径，默认是es根目录下的work文件夹。

path.logs: /path/to/logs
设置日志文件的存储路径，默认是es根目录下的logs文件夹

path.plugins: /path/to/plugins
设置插件的存放路径，默认是es根目录下的plugins文件夹

bootstrap.mlockall: true
设置为true来锁住内存。因为当jvm开始swapping时es的效率会降低，所以要保证它不swap，可以把ES_MIN_MEM和ES_MAX_MEM两个环境变量设置成同一个值，并且保证机器有足够的内存分配给es。同时也要允许elasticsearch的进程可以锁住内存，Linux下可以通过`ulimit-l unlimited`命令。

network.bind_host: 192.168.0.1
设置绑定的ip地址，可以是ipv4或ipv6的，默认为0.0.0.0。


network.publish_host:192.168.0.1
设置其它节点和该节点交互的ip地址，如果不设置它会自动判断，值必须是个真实的ip地址。

network.host: 192.168.0.1
这个参数是用来同时设置bind_host和publish_host上面两个参数。

transport.tcp.port: 9300
设置节点间交互的tcp端口，默认是9300。

transport.tcp.compress: true
设置是否压缩tcp传输时的数据，默认为false，不压缩。

http.port: 9200
设置对外服务的http端口，默认为9200。

http.max_content_length: 100mb
设置内容的最大容量，默认100mb

http.enabled: false
是否使用http协议对外提供服务，默认为true，开启。

gateway.type: local
gateway的类型，默认为local即为本地文件系统，可以设置为本地文件系统，分布式文件系统，Hadoop的HDFS，和amazon的s3服务器，其它文件系统的设置方法下次再详细说。

gateway.recover_after_nodes: 1
设置集群中N个节点启动时进行数据恢复，默认为1。

gateway.recover_after_time: 5m
设置初始化数据恢复进程的超时时间，默认是5分钟。

gateway.expected_nodes: 2
设置这个集群中节点的数量，默认为2，一旦这N个节点启动，就会立即进行数据恢复。

cluster.routing.allocation.node_initial_primaries_recoveries:4
初始化数据恢复时，并发恢复线程的个数，默认为4。

cluster.routing.allocation.node_concurrent_recoveries: 2
添加删除节点或负载均衡时并发恢复线程的个数，默认为4。

indices.recovery.max_size_per_sec: 0
设置数据恢复时限制的带宽，如入100mb，默认为0，即无限制。

indices.recovery.concurrent_streams: 5
设置这个参数来限制从其它分片恢复数据时最大同时打开并发流的个数，默认为5。

discovery.zen.minimum_master_nodes: 1
设置这个参数来保证集群中的节点可以知道其它N个有master资格的节点。默认为1，对于大的集群来说，可以设置大一点的值（2-4）

discovery.zen.ping.timeout: 3s
设置集群中自动发现其它节点时ping连接超时时间，默认为3秒，对于比较差的网络环境可以高点的值来防止自动发现时出错。

discovery.zen.ping.multicast.enabled: false
设置是否打开多播发现节点，默认是true。

discovery.zen.ping.unicast.hosts: ["host1","host2:port", "host3[portX-portY]"]
设置集群中master节点的初始列表，可以通过这些节点来自动发现新加入集群的节点。

下面是一些查询时的慢日志参数设置
index.search.slowlog.level: TRACE
index.search.slowlog.threshold.query.warn: 10s
index.search.slowlog.threshold.query.info: 5s
index.search.slowlog.threshold.query.debug: 2s
index.search.slowlog.threshold.query.trace: 500ms

index.search.slowlog.threshold.fetch.warn:1s
index.search.slowlog.threshold.fetch.info: 800ms
index.search.slowlog.threshold.fetch.debug:500ms
index.search.slowlog.threshold.fetch.trace: 200ms


kibana
-

一、Kibana 核心目录结构
项目	Value
bin	二进制脚本，包括 kibana 启动 Kibana 服务和 kibana-plugin 安装插件。
config	配置文件包括 kibana.yml 。
data	Kibana 和其插件写入磁盘的数据文件位置。
optimize	编译过的源码。某些管理操作(如，插件安装)导致运行时重新编译源码。
plugins	插件文件位置。每一个插件都一个单独的二级目录。
src	存放着kibana 前端源码资源
package.json	该文件记录着当前Kibana版本相关信息
二、Kibana 核心配置文件
配置项目	默认值	描述
server.port	5601	Kibana 由后端服务器提供服务，该配置指定使用的端口号。
server.host	localhost	此设置指定后端服务器的主机。要允许远程用户连接，请将值设置为Kibana服务器的IP地址或DNS名称。
server.basePath		如果启用了代理，则可以指定安装Kibana的路径。使用该server.rewriteBasePath设置告诉Kibana是否应从收到的请求中删除basePath，并在启动时防止弃用警告。此设置不能以斜杠（/）结尾。
server.rewriteBasePath	在Kibana 7.x中，该设置已被弃用	指定Kibana收到的请求中删除前缀server.basePath，或由反向代理重写请求
server.maxPayloadBytes	1048576	传入服务器请求的最大有效负载大小（以字节为单位）
server.name	your-hostname	用于标识此Kibana实例，Kibana 实例对外展示的名称
elasticsearch.hosts	[ “http://localhost:9200” ]	用于查询的Elasticsearch全部实例的URL。此处列出的所有节点必须位于同一群集上。
elasticsearch.preserveHost	true	true:Kibana使用server.host设置中指定的主机名，
false:Kibana使用连接到该Kibana实例的主机的主机名。
kibana.index	.kibana	Kibana在Elasticsearch中使用索引来存储保存的搜索，可视化和仪表板。如果索引不存在，则Kibana会创建一个新索引。如果配置定制索引，则名称必须为小写，并符合Elasticsearch索引名称的限制。
kibana.defaultAppId	home	要加载的默认应用程序
elasticsearch.username
elasticsearch.password		如果您的Elasticsearch受基本身份验证保护，那么这些设置将提供Kibana服务器在启动时用于对Kibana索引执行维护的用户名和密码。Kibana用户仍然需要通过Elasticsearch进行身份验证，该令牌通过Kibana服务器代理。
server.ssl	false	该配置为从Kibana服务器向浏览器发出请求的SSL启用配置。设置true时，为Kibana的入站连接启用SSL/TLS，必须提供证书及其对应的私钥。这些可以通过 server.ssl.certificate和server.ssl.key来指定。
server.ssl.certificate		配合server.ssl.enabled配置项使用，证书位置
server.ssl.key		配合server.ssl.enabled配置项使用，私钥位置
elasticsearch.ssl.certificate
elasticsearch.ssl.key		提供通往PEM格式SSL证书和密钥文件的路径的可选设置。这些文件用于向Elasticsearch验证Kibana的身份，并且xpack.ssl.verification_mode在Elasticsearch中设置为certificate或时需要这些文件
elasticsearch.ssl.certificateAuthorities		指定用于 Elasticsearch 实例的 PEM 证书文件路径列表。
elasticsearch.ssl.verificationMode	full	控制Elasticsearch提供的证书的验证。有效值是"full"、“certificate"和"none”。
"full"执行主机名验证
"certificate"跳过主机名验证
"none"完全使用跳过验证
elasticsearch.pingTimeout	elasticsearch.requestTimeout设置值	等待Elasticsearch响应ping的时间（以毫秒为单位）。
elasticsearch.requestTimeout	30000	等待后端或Elasticsearch响应的时间（以毫秒为单位）。该值必须是正整数。
elasticsearch.requestHeadersWhitelist	[ authorization ]	Kibana 客户端发送到 Elasticsearch 头体，发送 no 头体，设置该值为[] ,authorization 从白名单中删除标头意味着您不能在Kibana中使用 基本身份验证
elasticsearch.customHeaders	{}	发往 Elasticsearch的头体和值， 不管 elasticsearch.requestHeadersWhitelist 如何配置，任何自定义的头体不会被客户端头体覆盖
elasticsearch.shardTimeout	30000	Elasticsearch等待分片响应的时间（以毫秒为单位）。设置为0禁用。
elasticsearch.startupTimeout	5000	Kibana 启动时等待 Elasticsearch 的时间，单位微秒
elasticsearch.logQueries	false	配置查询日志是否发送到Elasticsearch。需要logging.verbose是设置为true，这对于查看由当前没有检查器的应用程序（例如，Timelion和Monitoring）生成的查询DSL很有用。
pid.file	/var/run/kibana.pid	指定 Kibana 的进程 ID 文件的路径
logging.dest	stdout	指定 Kibana 日志输出的文件
logging.silent	false	该值设为 true 时，禁止所有日志输出
logging.quiet	false	该值设为 true 时，禁止除错误信息除外的所有日志输出
logging.verbose	false	设置为true记录所有事件，包括系统使用情况信息和所有请求。在Elastic Cloud Enterprise上受支持。
ops.interval	5000	设置系统和进程取样间隔，单位微妙，最小值100。
i18n.locale	en	设置此值可以更改Kibana界面语言。有效的语言环境是：en，zh-CN，ja-JP。
