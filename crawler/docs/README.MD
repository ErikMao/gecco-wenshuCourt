##

### 1.TOMCAT的JNDI配置
在tomcat中,jndi方式默认使用的是tomcat-dbcp连接池,这个已经广受诟病,而tomcat-jdbc因其优异的性能,现正逐步取代dbcp;
$TOMCAT_HOME/conf/context.xml
增加如下代码：
	
	<Resource name="jdbc/crawler" auth="Container" type="javax.sql.DataSource"
               maxActive="100" maxIdle="30" maxWait="10000"
               username="root" password="dragon" driverClassName="com.mysql.jdbc.Driver"
               url="jdbc:mysql://localhost:3306/law_case?useUnicode=true&amp;characterEncoding=utf-8&amp;zeroDateTimeBehavior=convertToNull&amp;transformedBitIsBoolean=true"/>

	<Resource name="jdbc/crawler"
      auth="Container"
      type="javax.sql.DataSource"
      factory="org.apache.tomcat.jdbc.pool.DataSourceFactory"
      testWhileIdle="true"
      testOnBorrow="true"
      testOnReturn="false"
      validationQuery="SELECT 1 from dual"
      validationInterval="3000"
      timeBetweenEvictionRunsMillis="3000"
      maxActive="300"
      minIdle="10"
      maxWait="10000"
      initialSize="10"
      removeAbandonedTimeout="60"
      removeAbandoned="true"
      logAbandoned="true"
      minEvictableIdleTimeMillis="30000"
      jmxEnabled="true"
      jdbcInterceptors="org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer"
      driverClassName="org.gjt.mm.mysql.Driver"
      url="jdbc:mysql://localhost:3306/law_case?useUnicode=true&amp;characterEncoding=utf-8&amp;zeroDateTimeBehavior=convertToNull&amp;transformedBitIsBoolean=true"
      username="crawler" 
      password="crawler.dragon"/>

	<Resource name="jdbc/pdcourt"
      auth="Container"
      type="javax.sql.DataSource"
      factory="org.apache.tomcat.jdbc.pool.DataSourceFactory"
      testWhileIdle="true"
      testOnBorrow="true"
      testOnReturn="false"
      validationQuery="SELECT 1 from dual"
      validationInterval="3000"
      timeBetweenEvictionRunsMillis="3000"
      maxActive="300"
      minIdle="10"
      maxWait="10000"
      initialSize="10"
      removeAbandonedTimeout="60"
      removeAbandoned="true"
      logAbandoned="true"
      minEvictableIdleTimeMillis="30000"
      jmxEnabled="true"
      jdbcInterceptors="org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer"
      driverClassName="oracle.jdbc.OracleDriver"
      url="jdbc:oracle:thin:@127.0.0.1:1521:test"
      username="test" 
      password="test"/>

### 2.爬虫参数
#### 2.1 文书网参数
/resources/sysConfig.properties

	court_page=d\:/limq/court_page.json

#### 2.2 任务定时
/resources/spring-core.xml

	<bean id="CrawlerDailyTaskCronTriggerBean" class="org.springframework.scheduling.quartz.CronTriggerFactoryBean">  
	       <property name="jobDetail" ref="crawlerJobDetail"/>  
	       <property name="cronExpression" value="0 0/30 * * * ?"/><!-- Second  Minutes Hour Day-of-Month Month Year(可选)-->  
	</bean>