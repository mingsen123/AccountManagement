/**
 * 打开选项卡，进入相应的模块主页
 * @param url
 * @param name
 * @param id
 */
function showTab(url,name,id) {
    //获取选项卡的长度
    let length = $("li[lay-id="+id +"]").length;
    let element = layui.element;
    //判断选项卡是否存在
    if(length == 0){
        //获取URl全路径
        let fullUrl = "/" + url;
        let height = $(window).height() - 185;
        let content = '<iframe style="width: 100%;height: '+height+'px" src="' +fullUrl+'" frameborder="0" scrolling="no">'
        element.tabAdd('menu',{
            title:name,
            content:content,
            id:id

        });
    }
    element.tabChange("menu",id);
}