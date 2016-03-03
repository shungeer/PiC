/**
 * Created by xys on 2015/12/4.
 */

var gMap;
var addressItems = [];      // 同一地址对应包含的项,数组下标即为目标地址数组对应的下标
var feedAddressList = [];	// 地址信息
var addressHtmls = [];      // 这个地址所对应的html
var myGeo;
var PAGE_COUNT = 15;

$.ajax({ async: false });
function _init(_lng, _lat, _show_class) {
    gMap = new BMap.Map("allmap");
    gMap.centerAndZoom(new BMap.Point(_lng, _lat), _show_class);
    gMap.addControl(new BMap.NavigationControl());  // 平移缩放控件
    gMap.addControl(new BMap.ScaleControl());       // 添加比例尺控件
    gMap.addControl(new BMap.OverviewMapControl()); // 添加缩放地图控件
    gMap.addControl(new BMap.MapTypeControl());     // 添加地图类型控件
    gMap.enableScrollWheelZoom();

    myGeo = new BMap.Geocoder();
}

function get_map_list(_cur, is_page) {
	var markers = [];
	if (!is_page) {
		// 第一次加载
		$.ajax({
			type: "GET",
			url: "PiC/loadData",
			dataType: "json",
			timeout:60000,
			success: function(msg) {
				var feedbacks = eval(msg);
				addressItems = feedbacks.feedList;
				feedAddressList = feedbacks.feedAddressList;
				var pageHtml = pageControl(1, feedAddressList.length);
				if (addressItems.length == 0) {
	                // 没有数据
	                $("#page_list").html('<ul class="map_infors_detail clearfix"><li>没有数据</li></ul>');
	                $("#map_page").html('');
	            } else {
	            	gMap.clearOverlays();
	                
	                $.each(addressItems, function(i, value) {
	                    addMarker(addressItems, i, feedAddressList[i].lng, feedAddressList[i].lat);
	                });
	
	                var markerClusterer = new BMapLib.MarkerClusterer(gMap, {markers: markers});
	                var myStyle = [{
	                    url: 'image/m0.png',
	                    size: new BMap.Size(53, 52),
	                    opt_anchor: [10, 0],
	                    textColor: '#ffffff',
	                    opt_textSize: 10
	                }, {
	                    url: 'image/m1.png',
	                    size: new BMap.Size(56, 55),
	                    opt_anchor: [12, 0],
	                    textColor: '#ffffff',
	                    opt_textSize: 10
	                }, {
	                    url: 'image/m2.png',
	                    size: new BMap.Size(66, 65),
	                    opt_anchor: [16, 0],
	                    textColor: '#ffffff',
	                    opt_textSize: 10
	                }, {
	                    url: 'image/m3.png',
	                    size: new BMap.Size(78, 77),
	                    opt_anchor: [40, 35],
	                    textColor: '#ffffff',
	                    opt_textSize: 12
	                }, {
	                    url: 'image/m4.png',
	                    size: new BMap.Size(90, 90),
	                    opt_anchor: [32, 0],
	                    textColor: '#ffffff',
	                    opt_textSize: 14
	                }];
	
	                markerClusterer.setStyles(myStyle);
	                var page_html = '';//'<ul>';
	                // 分页显示，一页只显示15条
	                var items_size = 0;
	                if (addressItems.length > PAGE_COUNT) {
	                    items_size = PAGE_COUNT;
	                } else {
	                    items_size = addressItems.length;
	                }
	                for (var i=0; i<items_size; i++) {
	                    page_html += '<li><img class="u_icon_pic1" src="image/pin_type1.png">'
	                        + '<a href="javascript:;" onclick="showInfoWindow(' + i + ')" class="map_list_item"><i>' + addressItems[i][0].address + '</i></a>'
	                        + '</li>';
	                }
	                //page_html += '</ul>';
	                $("#page_list ul").html(page_html);
	                $("#map_page").html(pageHtml);
	                
	            }
	            
	            function addMarker(addressItem, i, lng, lat) {
				    // 先解析出点
				    var point = new BMap.Point(lng, lat);
				    
				    var myIcon = new BMap.Icon('image/pin_type1.png', new BMap.Size(45, 45));
				    var marker = new BMap.Marker(point, { icon: myIcon });
				    var opts = {
				        enableMessage: false
				    };

				    var html = '<div class="infor_window">'
				        +  '<div class="infor_window_title"><span>' + addressItems[i][0].address + '</span></div>'
				        +  '<div class="infor_window_pic">';
				    $.each(addressItem[i], function(j, value) {
				    	html += '<a data-fancybox-group="address' + i + '" title="'
				    		+ addressItem[i][j].picTitle +'" href="javascript:showPictureInfo('+ i +','+ j +')">'
				    		+ '<img src="' + addressItem[i][j].thumb + '" id="img_'+ addressItem[i][j].id + '"></a>';
				    });
				    html += '</div>';
				    html += '<div id="infor_window_detail">';
				    html += '<p><strong>照片标题: </strong>' + addressItem[i][0].picTitle + '</p>';
				    html += '<p><strong>照片描述: </strong>' + addressItem[i][0].text + '</p>';
				    html += '<p><strong>照片所在地: </strong>' + addressItem[i][0].address + '</p>';
				    html += '<p><strong>照片拍摄时间: </strong>' + addressItem[i][0].photoTime + '</p>';
				    html += '<p><strong>原始照片: </strong><a href="'+ addressItem[i][0].picture +'" target="_blank">详细</a></p>';
				    html += '<p><strong>上传者: </strong>' + addressItem[i][0].username + '</p>';
				    html += '<p><strong>联系方式: </strong>' + addressItem[i][0].phone + '</p>';
				    html += '<p><strong>上传时间: </strong>' + addressItem[i][0].uploadTime + '</p>';
				    html += '</div>';
				    addressHtmls.push(html);
				    var infoWindow = new BMap.InfoWindow(html);
				    marker.addEventListener('click', function() {
				        this.openInfoWindow(infoWindow);
				        //图片加载完毕重绘infowindow
				        $.each(addressItem[i], function(j, value) {
				            if($('#img_'+addressItem[i][j].id).attr('src')){
				                document.getElementById('img_'+addressItem[i][j].id).onload = function (){
				                    infoWindow.redraw();   //防止在网速较慢，图片未加载时，生成的信息框高度比图片的总高度小，导致图片部分被隐藏
				                }
				            }
				        });
				    });
				
				    markers.push(marker);
				}
			},
			error: function() {
				// 没有数据
                $("#page_list").html('<ul class="map_infors_detail clearfix"><li>没有数据</li></ul>');
                $("#map_page").html('');
			}
		});
	} else {
		// 分页显示
		var page_html = ''
		var pageHtml = pageControl(_cur, feedAddressList.length);
		var curPagesSize = _cur*PAGE_COUNT;
		var lastPagesSize = (_cur-1)*PAGE_COUNT;
		var curSize = curPagesSize;
		if ( curPagesSize > feedAddressList.length) {
			curSize = feedAddressList.length;
		}
		for (var i=lastPagesSize; i<curSize; i++) {
			page_html += '<li><img class="u_icon_pic1" src="image/pin_type1.png">'
                + '<a href="javascript:;" onclick="showInfoWindow(' + i + ')" class="map_list_item"><i>' + addressItems[i][0].address + '</i></a>'
                + '</li>';
		}

        $("#page_list ul").html(page_html);
        $("#map_page").html(pageHtml);
	}
}

function showInfoWindow(index) {
    // 先解析出点
    var point = new BMap.Point(feedAddressList[index].lng, feedAddressList[index].lat);
    gMap.centerAndZoom(point, 18);		// 细节显示18级
    var opts = {
        enableMessage: false
    }
    var infoWindow = new BMap.InfoWindow(addressHtmls[index], opts);
    gMap.openInfoWindow(infoWindow, point);
    $.each(addressItems[index], function(j, value) {
        if($('#img_'+addressItems[index][j].id).attr('src')){
            document.getElementById('img_'+addressItems[index][j].id).onload = function (){
                infoWindow.redraw();   //防止在网速较慢，图片未加载时，生成的信息框高度比图片的总高度小，导致图片部分被隐藏
            }
        }
    });
}

function pageControl( page, count) {
	var maxPage = Math.ceil(count/PAGE_COUNT);
	var strHtml = "";
	
	if(page > 1){
		strHtml += "<div class=\"page_prev\"><a href=\"javascript:;\" onclick=\"get_map_list("
				+ (page-1) + ", true)\">上一页</a></div>";
	}
	
	strHtml += "<div><ul>";
	// 7页以下显示全部页码
	if(count <= 7*PAGE_COUNT){
		if (count <= PAGE_COUNT) {
			strHtml += "<a href=\"javascript:;\" onclick=\"get_map_list(" 
				+ "1" + ", true)\">" + "共1页" + "</a>";
		} else {
			for(var i = 1;i<=maxPage;i++){
				if(page == i){
					strHtml += "<li class=\"current\"><a href=\"javascript:;\" onclick=\"get_map_list(" 
							+ i + ", true)\">" + i + "</a></li>";
				}else{
					strHtml += "<li><a href=\"javascript:;\" onclick=\"get_map_list(" 
							+ i + ", true)\">" + i + "</a></li>";
				}
			}
		}
		
	}else{
		
		if(page <= 4){
			for(var i = page-1;i>0;i--){
				strHtml += "<li><a href=\"javascript:;\" onclick=\"get_map_list(" 
						+ (page-i) + ", true)\">" + (page-i) + "</a></li>";
			}
			strHtml += "<li class=\"current\"><a href=\"javascript:;\" onclick=\"get_map_list(" 
						+ page + ", true)\">" + page + "</a></li>";
			strHtml += "<li><a href=\"javascript:;\" onclick=\"get_map_list(" 
						+ (page+1) + ", true)\">" + (page+1) + "</a></li>";
			strHtml += "<li><a href=\"javascript:;\" onclick=\"get_map_list(" 
					+ (page+2) + ", true)\">" + (page+2) + "</a></li>";
			strHtml += "<li>...</li>";
			strHtml += "<li><a href=\"javascript:;\" onclick=\"get_map_list(" 
					+ maxPage + ", true)\">" + maxPage + "</a></li>";
		}
		if(page>4 && (page<=maxPage-4)){
			strHtml += "<li><a href=\"javascript:;\" onclick=\"get_map_list(" 
					+ 1 + ", true)\">" + 1 + "</a></li>";
			strHtml += "<li>...</li>";
			strHtml += "<li><a href=\"javascript:;\" onclick=\"get_map_list(" 
					+ (page-1) + ", true)\">" + (page-1) + "</a></li>";
			strHtml += "<li class=\"current\"><a href=\"javascript:;\" onclick=\"get_map_list(" 
					+ page + ", true)\">" + page + "</a></li>";
			strHtml += "<li><a href=\"javascript:;\" onclick=\"get_map_list(" 
					+ (page+1) + ", true)\">" + (page+1) + "</a></li>";
			strHtml += "<li>...</li>";
			strHtml += "<li><a href=\"javascript:;\" onclick=\"get_map_list(" 
					+ maxPage + ", true)\">" + maxPage + "</a></li>";
			
		}
		if(page>maxPage-4){
			strHtml += "<li><a href=\"javascript:;\" onclick=\"get_map_list(" 
					+ 1 + ", true)\">" + 1 + "</a></li>";
			strHtml += "<li>...</li>";
			strHtml += "<li><a href=\"javascript:;\" onclick=\"get_map_list(" 
					+ (page-2) + ", true)\">" + (page-2) + "</a></li>";
			strHtml += "<li><a href=\"javascript:;\" onclick=\"get_map_list(" 
					+ (page-1) + ", true)\">" + (page-1) + "</a></li>";
			strHtml += "<li class=\"current\"><a href=\"javascript:;\" onclick=\"get_map_list(" 
					+ page + ", true)\">" + page + "</a></li>";
			for(var i = page+1;i<=maxPage;i++){
				strHtml += "<li><a href=\"javascript:;\" onclick=\"get_map_list(" 
						+ i + ", true)\">" + i + "</a></li>";
			}
		}
		
	}
	strHtml += "</li></ul></div>";
	if(page < maxPage){
		strHtml += "<div class=\"page_next\"><a href=\"javascript:;\" onclick=\"get_map_list("
				+ (page+1) + ", true)\">下一页</a></div>";
	}
	
	return strHtml;
}

function showPictureInfo(i, j) {
	var html = '';
	html += '<p><strong>照片标题: </strong>' + addressItems[i][j].picTitle + '</p>';
    html += '<p><strong>照片描述: </strong>' + addressItems[i][j].text + '</p>';
    html += '<p><strong>照片所在地: </strong>' + addressItems[i][j].address + '</p>';
    html += '<p><strong>照片拍摄时间: </strong>' + addressItems[i][j].photoTime + '</p>';
    html += '<p><strong>原始照片: </strong><a href="'+ addressItems[i][j].picture +'" target="_blank">详细</a></p>';
    html += '<p><strong>上传者: </strong>' + addressItems[i][j].username + '</p>';
    html += '<p><strong>联系方式: </strong>' + addressItems[i][j].phone + '</p>';
    html += '<p><strong>上传时间: </strong>' + addressItems[i][j].uploadTime + '</p>';
    $("#infor_window_detail").html(html);
    
    var shows = [];
    for (var k=0; k<addressItems[i].length; k++) {
    	var tempShow = {
    		href: addressItems[i][k].picture,
    		title: addressItems[i][k].picTitle
    	};
    	shows.push(tempShow);
    }
    // 打开fancyBox
	/*$.fancybox.open({
		href: addressItems[i][j].picture,
		title: addressItems[i][j].picTitle
	});*/
    $.fancybox.open(shows);
}