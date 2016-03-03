package tk.eqpic.util;

public class PageController {
	public final static int PAGE_COUNT = 15;	// 每页15条记录 

	public static String pageControl(int page,int count){
		int maxPage = (int) Math.ceil((double)count/PAGE_COUNT);
		String strHtml = "";
		
		if(page > 1){
			strHtml += "<div class=\"page_prev\"><a href=\"javascript:;\" onclick=\"get_map_list("
					+ (page-1) + ", true)\">上一页</a></div>";
		}
		
		strHtml += "<div><ul>";
		// 7页以下显示全部页码
		if(count <= 7*PAGE_COUNT){
			for(int i = 1;i<=maxPage;i++){
				if(page == i){
					strHtml += "<li class=\"current\"><a href=\"javascript:;\" onclick=\"get_map_list(" 
							+ i + ", true)\">" + i + "</a></li>";
				}else{
					strHtml += "<li><a href=\"javascript:;\" onclick=\"get_map_list(" 
							+ i + ", true)\">" + i + "</a></li>";
				}
			}
			
		}else{
			
			if(page <= 4){
				for(int i = page-1;i>0;i--){
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
				for(int i = page+1;i<=maxPage;i++){
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
}
