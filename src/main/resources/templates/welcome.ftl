<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
    <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
          crossorigin="anonymous">
    <!-- 可选的 Bootstrap 主题文件（一般不用引入） -->
    <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"
          integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp"
          crossorigin="anonymous">
    <script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
    <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"
            integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
            crossorigin="anonymous"></script>
    <meta name=”viewport” content=”width=device-width, initial-scale=1, maximum-scale=1″>
</head>
<style>
    body :{
        background-color: #fff;
    }
</style>
<body>

<div class="container">
    <div class="row">
        <div class="col-sm-9">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h3 class="panel-title">系统监控的货币</h3>
                </div>
                <div class="panel-body" id="currency">
                    <span ></span>
                </div>
            </div>

            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h3 class="panel-title">当前用户</h3>
                </div>
                <div class="panel-body">
                    <ul class="list-group" id="user">
                        <li class="list-group-item" >
                            <span >阿斯顿</span>
                            <span >123123</span>
                            <button class="btn" >获得名字</button>
                        </li>
                    </ul>
                    <button type="button" class="btn btn-primary" data-toggle="modal" data-target=".bs-example-modal-sm">添加用户</button>
                </div>
            </div>
        </div>
    </div>
</div>

<div id="userDetail" class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">用户信息</h4>
            </div>
            <div class="modal-body">
                详情
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary">Save changes</button>
            </div>
        </div>
    </div>
</div>
</div>
</body>
<script>

    getCurrency();
    getUser();
    function getCurrency() {
        $.ajax({
            type: "get",
            url: "/currency",
            success: function(msg){
                var html='';
                for (var i=0;i < msg.length; i++){
                    html += '<span >'+msg[i]+'</span>，';
                }
                html += '<button class="btn" >修改</button>';
                $('#currency').html(html);
            }
        });
    }

    function getUser() {
        $.ajax({
            type: "get",
            url: "/user",
            success: function(msg){
                var html='';
                for (var i=0;i < msg.length; i++){
                    var json = JSON.parse(msg[i]);
                    html += '<li class="list-group-item" >';
                    html += '<span >用户：'+json.user+'</span><br>';
                    html += '<span >关注的货币：'+json.currency+'</span><br>';
                    html += '<span >是否接收邮件：'+json.email+'</span><br>';
                    html += '<button class="btn" >修改</button>';
                    html += '</li>';
                }
                $('#user').html(html);
            }
        });
    }

</script>
</html>


