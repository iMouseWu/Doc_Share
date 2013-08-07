var search_pageNav=search_pageNav||{};
search_pageNav.fn=null;
search_pageNav.pre="<";
search_pageNav.next=">";
search_pageNav.nav=function(a,b)
{
	var tabsize = 5;  //9;/*分页栏显示的页码的数目*/
	var besidesize = 2;      //4;/*两边的页码数*/
	var whenmove =  4 ;      //7;/*到第几个的时候开始移动*/
	
	/*whenmove - besidesize >= 2
	 * 这个蛋疼的东西可以用数学推导：因为由1处得已经把1这个输出了，再结合2处，所以d>1（这里由d>0可知whenmove > besidesize）;
	 * a-besidesize>1.由3处可得当whenmove<=a的时候 a-besidesize需要大于1；
	 * 因为a是越来越大的，所以a=whenmove的时候需要a-besidesize需要大于1，所以whenmove - besidesize >= 2；
	 * 
	 * 这个后续会继续完善的
	 * */
	if(1>=b)return this.pn=this.p=1,this.pHtml2(1);
	b<a&&(a=b);
	1>=a?a=1:(this.pHtml(a-1,b,search_pageNav.pre),this.pHtml(1,b,"1"));/*1*/
	this.p=a;
	this.pn=b;
	var d=2,e=tabsize>b?b:tabsize;
	whenmove<=a&&($("#pagenumber").append("<li><span>...<span></li>"),d=a-besidesize,e=a+besidesize,e=b<e?b:e);/*3*/
	for(;d<a;d++)this.pHtml(d,b);/*2*/
	this.pHtml2(a);
	for(d=a + 1;d<=e;d++) this.pHtml(d,b);
	e<b&&($("#pagenumber").append("<li><span>...<span></li>"),this.pHtml(b,b));
	a<b&&(this.pHtml(a+1,b,search_pageNav.next));
	};
search_pageNav.pHtml=function(a,b,c)
{    
	$("#pagenumber").append("<li><a href='javascript:Search_paging("+a+");'>"+(c||a)+"</a></li>");
	};
search_pageNav.pHtml2=function(a)
{
	$("#pagenumber").append("<li><span class='cPageNum'>"+a+"</span></li>");
	};
search_pageNav.fn = function(p,pn){
		$("#test").text("Page:"+p+" of "+pn + " pages.");  
	};
search_pageNav.go=function(a,b){
		$('#pagenumber').html("");
		this.nav(a,b);
		null!=this.fn&&this.fn(this.p,this.pn)
		};
