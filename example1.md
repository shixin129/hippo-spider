# Example 1 #

```
<?xml version="1.0" encoding="UTF-8"?>
<!-- 
	该配置文件用于抓取 http://www.linuxtoy.org 网站上的新闻条目, 可以抓取多页
 -->
<spider charset="UTF-8"> <!-- charset用于指定页面编码, 默认为“AUTO”, 即自动判断; 如果数据编码出现问题, 可以手工指定编码,如gb2312/UTF-8等 -->
	<params>
		<param name="filePath" type="script">"01-" + spider.datetime('yyyy-MM-dd-') + spider.uuid()</param>
	</params>
	<extracter behavior="list-loop"> <!-- 页面数据抽取器, 目前的行为模式只有“list-loop” -->
		<params>
			<param name="pageUrl">http://linuxtoy.org/</param>	<!-- 请求开始的URL -->
			<param name="itemsXPath">//div[@class='post']</param> <!-- 选取项目的XPath表达式 -->
			<param name="nextXPath">//a[contains(text(),'Next')]/@href</param> <!-- 如果存在多页, 可以使用该参数指定下一页的地址(XPath表达式) -->
			<param name="maxLoops">1000</param> <!-- 该参数指定获得全部的项目最大数量 -->
		</params>
	</extracter>
	<transformer behavior="default"> <!-- 数据字段转换器, 目前的行为模式只有"default" -->
		<params>
		</params>
		<fields>
			<field name="prName" type="xpath">.//h2/a/text()</field> <!-- 通过xpath表达式定义字段,注意:xpath的上下文是项目对象 -->
			<field name="prLink" type="xpath">.//h2/a/@href</field>
			<field name="prContent" type="script"> <!-- 通过javascript脚本定义字段, 可以使用js表达式, 其中最后一个表达式的值作为返回值 -->
				var url = x.data(".//h2/a/@href");  //x.data 是在当前项目上求指定xpath表达式的方法
				var cnt = x.fetchHtmlContent(url, "//div[@class='entry']/p");
				cnt;
			</field>
		</fields>
	</transformer>
	<loader behavior="text-file"> <!-- 数据装载器, 目前只有“text-file”, 即装载到文本文件 -->
		<params>
			<param name="file">d:\temp\${filePath}.txt</param> <!-- 文件地址 -->
			<param name="mode">append</param>
		</params>
		<template> <!-- 文件模板, 模板中可以使用 @{xxx} 的形式引用上面定义的字段 -->
			<!-- pre区域, 在循环开始之前生成 -->
			<pre><![CDATA[<?xml version="1.0" encoding="UTF-8"?>  
				<items>
			]]></pre>
			<!-- body区域, 针对每个项目生成 -->
			<body><![CDATA[
				<item>
					<name>@{prName}</name>
					<link>@{prLink}</link>
					<content>
						@{prContent}
					</content>
				</item>
			]]></body>
			<!-- post区域, 在循环结束之后生成 -->
			<post><![CDATA[</items>]]></post> 
		</template>
	</loader>
</spider>

```