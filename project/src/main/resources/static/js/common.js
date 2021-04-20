/**
 * 公共弹出层
 * @param url 弹出层的显示路径
 * @param title 左上角的标签名称
 */
function openLayer(url,title) {
    //这个boolean标签为了实现此方法执行完毕在执行下面的代码
    $.ajaxSettings.async = false;
    //ajax方法请求弹出页面
    $.get(url,function (res) {
        //弹出页的方法
        layer.open({
            //界面层
            type:1
            //弹出层显示的名称
            ,title: title
            //弹出层的宽高
            ,area:['750px','350px']
            //弹出层内容
            ,content: res
        });
    });
    $.ajaxSettings.async = true;
}

/**
 * 公共监听提交事件
 * @param filter <button标签中的lay-filter的值
 * @param type  请求类型
 */
function mySubmit(filter,type,func) {
    //layui官方文档中触发submit提交
    //按钮点击或者表单被执行提交时触发，其中回调函数只有在验证全部通过后才会进入，回调返回三个成员：
    //其中的submit中的方法名称是*List.html文件中的提交的方法名
    layui.form.on('submit('+filter+')', function(data){
        if(typeof (func) != 'undefined'){
            func(data.field);
        }
        //console.log(data.elem) //被执行事件的元素DOM对象，一般为button对象
        //console.log(data.form) //被执行提交的form对象，一般在存在form标签时才会返回
        //console.log(data.field) //当前容器的全部表单字段，名值对形式：{name: value}
        //这里使用ajax方法提交，不适用这里的默认方法提交
        $.ajax({
            //取得url在Add.html中的form中添加th:action="@{/customer}"
            url: data.form.action
            //同步方法，最后响应结果需要把弹出层关闭
            ,async: false
            //提交方式
            ,type: type
            //文本类型json类型
            ,contentType: 'application/json;charset=utf-8'
            //表单参数获取,需要通过JSON方法把参数类型统一
            ,data: JSON.stringify(data.field)
            //成功之后的处理方法，响应
            ,success: function (res) {
                //判断res响应对象的code
                if(res.code == 0){
                    //响应成功，需要把弹出层关闭
                    layer.closeAll();
                    //之后在查询一遍
                    query();
                }else {
                    //返回失败信息
                    layer.alert(res.msg);
                }
            }
        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });
}

/**
 * 公共删除方法
 * @param url
 */
function myDelete(url){
    $.ajax({
        //取得url在Add.html中的form中添加th:action="@{/customer}"
        url: url
        //同步方法，最后响应结果需要把弹出层关闭
        ,async: false
        //提交方式
        ,type: 'DELETE'
        //成功之后的处理方法，响应
        ,success: function (res) {
            //判断res响应对象的code
            if(res.code == 0){
                //之后在查询一遍
                query();
            }else {
                //返回失败信息
                layer.alert(res.msg);
            }
        }
    });
}