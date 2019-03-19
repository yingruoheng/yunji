<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
<head>
    <title>file upload</title>

    <%--<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>--%>
</head>

<body>
    <form action="./file/fileupload" method="post" enctype="multipart/form-data">
        <label>文件上传</label>
        <input type="file" name="file" accept=".md">
        <input type="submit" value="提交">
    </form>
    <%--<script>
        $.ajax({
            url: 'http://10.8.5.120:8089/auth/wxLogin?code="12345"',
            type: 'get',
            dataType: 'json',
            success: function(data){
                console.log(data);
                alert(data)
            }
        })
    </script>--%>
</body>
</html>
