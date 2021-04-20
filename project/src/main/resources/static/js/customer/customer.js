//获取table对象
var table = layui.table;
//第一个实例
var tableIns = table.render({
    elem: '#customerList'
    ,url: '/customer/list' //数据接口
    ,page: true //开启分页
    /**layui官方文档parseData回调函数将其解析成 table 组件所规定的数据格式
     * 出参数据格式如下
     * {
        "status": 0,
       "message": "",
       "total": 180,
      "data": {
       "item": [{}, {}]
      }
      }
     * @param res
     * @returns {{msg: *, code: *, data: *, count: PaymentItem | number | upload.fileLength}}
     */
    ,parseData: function(res){ //res 即为原始返回的数据
        return {
            "code": res.code, //解析接口状态
            "msg": res.msg, //解析提示文本
            "count": res.data.count, //解析数据长度
            "data": res.data.records //解析数据列表
        };
    }
    ,cols: [[//表头
        {field: 'realName', title: '真实姓名'}
        ,{field: 'sex', title: '性别'}
        ,{field: 'age', title: '年龄'}
        ,{field: 'phone', title: '手机号码'}
        ,{field: 'createTime' , title: '创建时间'}
        ,{title: '操作', toolbar: '#barDemo'}
    ]]
});

/**
 * 按条件查询
 */
//之后query方法在customerList.html文件的查询中加入onclick事件,让前端与后端达成前后一致
function query() {
    //查询
    //以下代码根据layui官方文档的表格重载中的方法进行修改.
    tableIns.reload({
        where: { //异步数据接口的额外查询
            //第一个查询条件
            realName: $("#realName").val()
            //第二个查询条件
            ,phone: $("#phone").val()
            //…
        }
        ,page: {
            curr: 1 //重新从第 1 页开始
        }
    });
}

function toAdd() {
    //调用公共弹出层方法
    openLayer('/customer/toAdd','新增客户');
    //重新渲染
    layui.form.render();
    //调用公共监听提交事件方法
    mySubmit('addSubmit','POST');
}

//工具条事件
table.on('tool(test)', function(obj){ //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
    var data = obj.data; //获得当前行数据
    var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
    var tr = obj.tr; //获得当前行 tr 的 DOM 对象（如果有的话）

    let customerId = data.customerId;

    if(layEvent === 'detail'){ //查看
        //do somehing
        openLayer('/customer/toDetail/'+customerId,'客户详情');
    } else if(layEvent === 'del'){ //删除
        layer.confirm('真的删除吗？', function(index){
            layer.close(index);
            //调用公共删除方法
            myDelete("/customer/"+customerId);
            layer.confirm('删除成功！');
            //向服务端发送删除指令
            layer.close(index);
        });
    } else if(layEvent === 'edit'){ //编辑
        //do something
        //调用公共弹出层方法
        openLayer('/customer/toUpdate/'+customerId,'修改客户');
        //重新渲染
        layui.form.render();
        //调用公共监听提交事件方法
        mySubmit('updateSubmit','PUT');
    }
});