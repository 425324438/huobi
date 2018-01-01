<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
    <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
          crossorigin="anonymous">
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
        text-align: center;
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
                <div class="form-group">
                    <label for="exampleInputEmail1">用户</label>
                    <input type="email" class="form-control" id="user_user" placeholder="Email格式">
                </div>
                <div class="form-group">
                    <label for="exampleInputEmail1">关注的货币</label>
                    <input type="text" class="form-control" id="user_currency" placeholder="格式：xrpusdt/eosusdt/btcusdt...">
                </div>
                <div class="form-group">
                    <label for="exampleInputEmail1">是否接收邮件</label>
                    <input type="text" class="form-control" id="user_email" placeholder="true/false">
                </div>
                <div class="form-group">
                    <label for="exampleInputEmail1">接收邮件时间区间</label>
                    <input type="text" class="form-control" id="user_start" placeholder="开始时间：08:30">
                    <input type="text" class="form-control" id="user_end" placeholder="结束时间：23:30">
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="addUser()">添加</button>
            </div>
        </div>
    </div>
</div>


<div id="configCurrency" class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">系统监控的货币</h4>
            </div>
            <div class="row" style="margin: 0px;">
                <div class="form-group col-sm-9">
                    <label for="exampleInputEmail1">格式：多个币种以“,”号分割。币币交易（例：eosusdt  eos 是一个币种 usdt是兑换币种）</label>
                    <input type="text" class="form-control" id="config_Currency" >
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="updateCurrency()">添加</button>
            </div>
        </div>
    </div>
</div>
</body>
<script>

    getCurrency();
    getUser();
    var currencyList = '';
    function getCurrency() {
        $.ajax({
            type: "get",
            url: "/currency",
            success: function(msg){
                var html='';
                currencyList = msg;
                html += '<span >'+msg+'</span>，';
                html += '<button class="btn btn-primary" onclick="showConfig()">修改/添加</button>';
                $('#currency').html(html);
            }
        });
    }

    function showConfig() {
        $('#config_Currency').val(currencyList);
        $('#configCurrency').modal('show');
    }

    function updateCurrency() {
        var str = $('#config_Currency').val();
        $.ajax({
            type: "get",
            url: "/updateCurrency",
            data :{
                "currency" : str
            },
            success: function(msg){
                $('#configCurrency').modal('hide');
                getCurrency();
            }
        });
    }

    function getUser() {
        $.ajax({
            type: "get",
            url: "/user",
            success: function(msg){
                $('#user').empty();
                var html='';
                for (var i=0;i < msg.length; i++){
                    var json = JSON.parse(msg[i]);
                    html += '<li class="list-group-item" >';
                    html += '用户：<span id="del_user">'+json.user+'</span><br>';
                    html += '关注的货币：<span id="del_currency">'+json.currency+'</span><br>';
                    html += '是否接收邮件：<span id="email">'+json.email+'</span><br>';
                    html += '接收邮件：开始时间<span id="del_start">'+json.start+'</span>~';
                    html += '结束时间：<span id="del_end">'+json.end+'</span><br>';
                    html += '<button class="btn" onclick="testEmail(this)">发送测试邮件</button>';
                    html += '</li>';
                }
                $('#user').html(html);
            }
        });
    }
    
//    function delUser(_this) {
//        var li = $(_this).parent();
//        var del_user = $(li).find("#del_user");
//        var del_currency = $(li).find("#del_currency");
//        var email = $(li).find("#email");
//
//        var del_start = $(li).find("#del_start");
//        var del_end = $(li).find("#del_end");
//        if(confirm('确定删除么？')){
//            $.ajax({
//                type: "get",
//                data :{
//                    'user': del_user[0].innerText,
//                    'currency': del_currency[0].innerText,
//                    'email': email[0].innerText,
//                    'user_start': del_start[0].innerText,
//                    'user_end': del_end[0].innerText
//                },
//                success: function(msg) {
//                    alert(msg);
//                    getUser();
//                }
//            });
//        }
//    }

    function addUser() {
        var user_user = $('#user_user').val();
        var user_currency = $('#user_currency').val();
        var user_email = $('#user_email').val();
        var user_start = $('#user_start').val();
        var user_end = $('#user_end').val();

        $.ajax({
            type: "get",
            url: "/addUser",
            data :{
                'user':user_user,
                'currency':user_currency,
                'email':user_email,
                'user_start':user_start,
                'user_end':user_end
            },
            success: function(msg) {
                $('#userDetail').modal('hide');
                getUser();
            }
        });
    }
    
</script>
</html>


