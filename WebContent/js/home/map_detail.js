/**
 * Created by xys on 2015/12/5.
 */
// 判断左侧slider中列表的高度，根据浏览器大小改变高度
function viewSize(){
    if (self.innerHeight){
        // all except Explorer
        this.winWidth = self.innerWidth;
        this.winHeight = self.innerHeight;
    }
    else if (document.documentElement && document.documentElement.clientHeight){
        // Explorer 6 Strict Mode
        this.winWidth = document.documentElement.clientWidth;
        this.winHeight = document.documentElement.clientHeight;
    }
    else if (document.body){
        // other Explorers
        this.winWidth = document.body.clientWidth;
        this.winHeight = document.body.clientHeight;
    }
    return [this.winWidth,this.winHeight];
}

// 显示地图的宽度设定
function reSize(is_full){

    var oW1 = viewSize()[0];
    var oH1 = viewSize()[1];

    var oH2 = oH1 - $(".map_detail_icon").outerHeight() - $(".map_page").outerHeight() - $(".navbar-wrapper").outerHeight();

    $("#allmap").css("height",oH1 - $(".navbar-wrapper").outerHeight());

    $("#map_slider").css("height",oH1);
    $(".map_infors ul").css("height",oH2);
};

reSize();
window.onresize = function(){
	reSize();
};
// 按钮
function ubtnclick() {
    $("#u_expand_btn").click(function() {
        $("#map_slider").show();
        $("#map_slider").addClass("map_silder_left");
        setCookie('stat_list',1);
        reSize();
    });

    $("#u_close_btn").click(function() {
        $("#map_slider").hide();
        setCookie('stat_list',2);
    });
}
ubtnclick();
/*cookie*/
//写cookies

function setCookie(name,value)
{
    var Days = 1;
    var exp = new Date();
    exp.setTime(exp.getTime() + Days*1*60*60*1000);
    document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
}

//读取cookies
function getCookie(name)
{
    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");

    if(arr=document.cookie.match(reg))

        return unescape(arr[2]);
    else
        return null;
}

//删除cookies
function delCookie(name)
{
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval=getCookie(name);
    if(cval!=null)
        document.cookie= name + "="+cval+";expires="+exp.toGMTString();
}

if(getCookie('stat_list') == 2){
    $("#map_slider").hide();
}else{
    $("#map_slider").show();
    $("#map_slider").addClass("map_slider_left");

}

