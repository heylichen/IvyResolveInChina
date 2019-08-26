####use HttpClient to access aliyun maven repo to download dependencies to speed up ivy resolving process in China.

**问题**：
1. ivy 默认使用maven 中央仓库，在中国下载速度很慢。
2. 国内使用阿里云的maven仓库，在maven项目中可以使用，速度也很快，但在ivy resolve时有问题：找不到artifact.
3. ivy文档较少，难以找到直接使用配置解决问题的办法。

**方案**：  
在unit test中直接执行ivy代码，使用HttpClient替代部分运行时实例，来实现ivy访问阿里云的maven仓库。

**配置**:  
ivy.xml放在 /src/main/resources/ivy中，可以放多个。   
ivy-settings.xml放在 /src/main/resources中，包含resolvers的相关配置。

**使用**:
ResolverTest
1. engineTestAll，解析/src/main/resources/ivy中所有配置的依赖。
2. 使用engineTestOne，可以解析某一个ivy.xml，以排查问题。

**example**：
将lucene-solr项目中的ivy.xml全部复制到/src/main/resources/ivy,解析依赖完成，排除个别不需要的无法解析的依赖。
