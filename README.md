配方管理系统

项目地址：待部署

# 项目介绍

配方管理系统包括对供应商，原料，配方，产品，产品系列，用户，角色的管理，其中的功能包括对信息的增删查改，导出导出，权限管理。

# 整体效果

登录界面

![Image text](https://github.com/youzhi0403/formula_system/blob/master/README_PICTURE/p1.png)

系统界面

![Image text](https://github.com/youzhi0403/formula_system/blob/master/README_PICTURE/p2.png)

功能展示

![Image text](https://github.com/youzhi0403/formula_system/blob/master/README_PICTURE/p3.png)

![Image text](https://github.com/youzhi0403/formula_system/blob/master/README_PICTURE/p4.png)

# 技术栈

## 前端技术栈

1.jquery  
2.javascript  
3.bootstrap  
4.handsontable（配方的展示）  
5.ztree  
6.ocupload(文件上传)  

## 后端技术栈

1.c3p0（数据库连接池）  
2.gson（json解析）  
3.poi（解析excel表）  
4.log4j（日志）  
5.mysql  

# 快速部署

1.clone项目到本地。  

2.数据库脚本在sql文件夹中，在mysql中执行脚本数据。 

3.数据库配置在src文件夹下的c3p0-config.xml中。

4.将项目放在eclipse中，并配置tomcat7（tocmat8会出问题）。

5.eclipse中运行项目，访问 http://localhost:8080/FormulaSystem/  

# 文档

待开发

# bug

1.ocupload插件上传相同文件时发生错误
