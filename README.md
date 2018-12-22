quartz-distributed-redis-tool
=================
用于quartz集群部署使，同一时间保证仅单个节点执行job，基于Redis实现

两种方式：

1、用切面进行拦截加锁   
参考：QuartzAspectTest

2、用工具手动加锁<br>
参考：QuartzLockUtilTest


