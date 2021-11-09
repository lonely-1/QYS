<%@ page contentType="text/html;charset=UTF-8"  language="java" %>
<%@ page isELIgnored="false"%>
<html>
<body>
<form enctype="multipart/form-data" method="post" action="upload">
    <input type="file" name="file"/>
    <input type="submit" value="上传"/>
</form>
<a href="download?uuid=b277b639fc6347bdad81311a9980ca56">文件下载</a>
<a href="getFileInfo?uuid=b277b639fc6347bdad81311a9980ca56">获取文件元数据</a>
</body>
</html>
