<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta content="text/html; charset=utf-8" http-equiv="CONTENT-TYPE">
    <title>图片展示</title>
    <meta content="IE=edge" http-equiv="x-ua-compatible">
    <link href="css/style.css" rel="stylesheet">
    <script type="text/javascript" src="js/jquery-1.11.3.min.js"></script>
    <script type="text/javascript" src="js/jquery.mousewheel-3.0.6.pack.js"></script>
    <link href="css/fancyBox/jquery.fancybox.css" rel="stylesheet">
    <script type="text/javascript" src="js/fancyBox/jquery.fancybox.js"></script>
    <link href="css/fancyBox/jquery.fancybox-buttons.css" rel="stylesheet">
    <script type="text/javascript" src="js/fancyBox/jquery.fancybox-buttons.js"></script>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=M5hW5B104Scn5iL2PBQAGV2w"></script>
    <script type="text/javascript" src="http://api.map.baidu.com/library/TextIconOverlay/1.2/src/TextIconOverlay_min.js"></script>
    <script type="text/javascript" src="http://api.map.baidu.com/library/MarkerClusterer/1.2/src/MarkerClusterer_min.js"></script>
    <script type="text/javascript" src="js/home/map_event.js"></script>
    
    <style type="text/css">
    	.spinner {
		  margin: 100px auto;
		  width: 50px;
		  height: 60px;
		  text-align: center;
		  font-size: 10px;
		}
		 
		.spinner > div {
		  background-color: #67CF22;
		  height: 100%;
		  width: 6px;
		  display: inline-block;
		   
		  -webkit-animation: stretchdelay 1.2s infinite ease-in-out;
		  animation: stretchdelay 1.2s infinite ease-in-out;
		}
		 
		.spinner .rect2 {
		  -webkit-animation-delay: -1.1s;
		  animation-delay: -1.1s;
		}
		 
		.spinner .rect3 {
		  -webkit-animation-delay: -1.0s;
		  animation-delay: -1.0s;
		}
		 
		.spinner .rect4 {
		  -webkit-animation-delay: -0.9s;
		  animation-delay: -0.9s;
		}
		 
		.spinner .rect5 {
		  -webkit-animation-delay: -0.8s;
		  animation-delay: -0.8s;
		}
		 
		@-webkit-keyframes stretchdelay {
		  0%, 40%, 100% { -webkit-transform: scaleY(0.4) } 
		  20% { -webkit-transform: scaleY(1.0) }
		}
		 
		@keyframes stretchdelay {
		  0%, 40%, 100% {
		    transform: scaleY(0.4);
		    -webkit-transform: scaleY(0.4);
		  }  20% {
		    transform: scaleY(1.0);
		    -webkit-transform: scaleY(1.0);
		  }
		}
    </style>
</head>
<body>
    <!-- 顶部导航 -->
    <div class="navbar-wrapper">
        <a href="#">地震现场图片浏览</a>
    </div>
    <!-- 地图 -->
    <div class="map_content">
        <a id="u_expand_btn"  class="u_expand_btn" href="javascript:;"></a>
        <div id="allmap"></div>
        <div id="map_slider" class="map_silder map_silder_left">
            <a id="u_close_btn" class="u_close_btn" title="关闭" href="javascript:;"></a>
            <div id="m_mapdes_box">
                <div class="map_detail_icon">
                    <ul>
                        <li><a href="#" title="地址"><img src="image/pin_type1.png"/></a></li>
                    </ul>
                </div>
                <div class="map_infors" id="map_infors">
                    <div id="page_list">
                    	<ul class="map_infors_detail clearfix">
                    		<!-- 默认样式 -->
							<div class="spinner">  
							  <div class="rect1"></div>
							  <div class="rect2"></div>
							  <div class="rect3"></div>
							  <div class="rect4"></div>
							  <div class="rect5"></div>
							</div>
                    	</ul>
                    </div>
                </div>
                <div class="map_page" id="map_page"></div>
            </div>
        </div>
    </div>
    <script type="text/javascript">

        $(document).ready(function() {
            _init(116.41391319017,39.910592555278, 5);
            get_map_list(1, false);
			setTimeout(function(){
			},2000);
        });
    </script>
    <script type="text/javascript" src="js/home/map_detail.js"></script>
</body>
</html>