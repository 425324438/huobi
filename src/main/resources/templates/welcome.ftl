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
            <h3>火币网数字货币监控系统
                <small>
                    <a target="view_window" href="https://github.com/425324438/huobi">项目代码：https://github.com/425324438/huobi</a>
                    欢迎<strong>Star</strong>
                </small>
            </h3>
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h3 class="panel-title">系统监控的货币</h3>
                </div>
                <div class="panel-body" id="currency">
                    <span></span>
                </div>
            </div>
        <#--说明-->
            <p class="text-primary">本系统只监控【火币网的数据】（因为开发者本人目前只在火币网玩）</p>

            <div class="panel-group" id="accordion">
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a data-toggle="collapse" data-parent="#accordion"
                               href="#collapseOne">
                                你开发这个的是用来卖钱吗？
                            </a>
                        </h4>
                    </div>
                    <div id="collapseOne" class="panel-collapse collapse in">
                        <div class="panel-body">
                            不是啊，各位<strong>大佬</strong>要是因为我的这个小程序赚到了钱了，我心里就很开心了。说明我做的东西有价值，同时我也欢迎打赏。
                        </div>
                    </div>
                </div>
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a data-toggle="collapse" data-parent="#accordion"
                               href="#collapseTwo">
                                做公益啊！
                            </a>
                        </h4>
                    </div>
                    <div id="collapseTwo" class="panel-collapse collapse">
                        <div class="panel-body">
                            对呀！其实我的做 项目有人用 我就很开心了
                        </div>
                    </div>
                </div>
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a data-toggle="collapse" data-parent="#accordion"
                               href="#collapseThree_qq">
                                可以连到微信上吗?或者QQ,这样更方便呀
                            </a>
                        </h4>
                    </div>
                    <div id="collapseThree_qq" class="panel-collapse collapse">
                        <div class="panel-body">
                            <p>这个 还没有想过呢</p>
                            <p>我估计微信可以  QQ应该不行的</p>
                            <p>因为你想要接收到 微信通知的话 你至少得关注 公众号 ，然后调用公众号的接口，或者你得加一个人的微信好友，通过好友通知</p>
                        </div>
                    </div>
                </div>
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a data-toggle="collapse" data-parent="#accordion"
                               href="#collapseThree_phone">
                                或者手机短信，也比邮箱方便
                            </a>
                        </h4>
                    </div>
                    <div id="collapseThree_phone" class="panel-collapse collapse">
                        <div class="panel-body">
                            <p>我一开始也是这么想的</p>
                            <p>但是我后来发现 还是自己太天真了</p>
                            <p>郭嘉对 短信这块儿管的很严的</p>
                            <p>只能用 几个模版发送短信，多余的什么乱系八糟的 都不行</p>
                            <p>数字货币就很典型了 </p>
                            <p>找了几个短信平台都不行，而且发短信 还是收费的，邮件是免费的。多好</p>
                        </div>
                    </div>
                </div>
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a data-toggle="collapse" data-parent="#accordion"
                               href="#collapseThree">
                                你这个东西怎么用呢？
                            </a>
                        </h4>
                    </div>
                    <div id="collapseThree" class="panel-collapse collapse">
                        <div class="panel-body">
                            <p>这你算是问到关键问题了。</p>
                            <p>首先你要看看上面[系统关注的货币]有没有你想要的，如果没有的话你就添加上你需要的货币（格式是 交易币种 + 基础币种，比如：你想关注 USDT区的 LTC 那么就写成 ltcusdt ）</p>
                            <p>然后在下面[当前用户]里点击添加用户按钮，添加一个新用户（个人建议申请一个新的邮箱，然后关注的货币格式与上面系统关注货币格式是一样的，还有一个要说明的就是[接收邮件的时间区间] 如果24都会发邮件的话 肯定是很烦人的，所以各位<strong>大佬</strong>根据自己的喜好自己配置。）</p>
                            <p>然后手机上下载一个QQ邮箱的应用，登录你的QQ邮箱，就可以发送邮件提醒了，你可以先发一个测试邮件试试。（每种货币发一封邮件，如果你关注多个货币，就会发多个邮件）</p>
                            <p>开发者QQ：425324438,有问题请联系我，项目还在持续开发中。。。</p>
                            <p>目前实现的功能：
                            <ul>
                                <li>您关注的货币在 <strong>5</strong> 分钟内波动大于 <strong>2%</strong> 发送邮件通知</li>
                            </ul>
                            </p>
                        </div>
                    </div>
                </div>
            </div>

            <#--<p class="text-danger">特此声明：我要感谢我的女朋友/未来的老婆，是她每天用自己的时间来照顾我，-->
                <#--让我有尽量多的时间去做我自己喜欢做的事情。可以说这个项目有她一半的功劳。在这里，我感谢她。老婆我一定会娶你的！</p>-->
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h3 class="panel-title">当前用户</h3>
                </div>
                <div class="panel-body">
                    <button type="button" class="btn btn-primary" data-toggle="modal" data-target=".bs-example-modal-sm">添加用户</button>
                    <ul class="list-group" id="user">
                    </ul>
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
                    <input type="text" class="form-control" id="user_currency" placeholder="格式（多个英文逗号分割）：xrpusdt、eosusdt、btcusdt...">
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

    function testEmail(_this) {
        var li = $(_this).parent();
        var userEmail = $(li).find("#del_user");
        var currency = $(li).find("#del_currency");
        $(_this).button('loading');
        $.ajax({
            type: "get",
            url: "/testEmail",
            data :{
                'user':userEmail[0].innerText,
                'currency':currency[0].innerText,
            },
            success: function(msg) {
                alert('邮件发送：'+msg);
                $(_this).button('reset');
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


